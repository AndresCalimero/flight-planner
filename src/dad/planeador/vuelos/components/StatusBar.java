package dad.planeador.vuelos.components;

import java.io.IOException;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class StatusBar extends HBox {
	
	@FXML
	private Label estadoLabel;
	@FXML
	private Label porcentajeLabel;
	@FXML
	private ProgressBar progressBar;
	@FXML
	private StackPane stackPane;
	
	private Worker<?> worker;
	private ChangeListener<Number> changeListener;

	public StatusBar() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("StatusBar.fxml"));
		loader.setRoot(this);
		loader.setController(this);

		try {
			loader.load();
			inicializar();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void inicializar() {
		estadoLabel.setText("Listo");
		stackPane.setOpacity(0);
		progressBar.progressProperty().addListener((obs, oldValue, newValue) -> onProgressValueChanged(newValue));
		changeListener = new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				setPorcentaje(newValue.doubleValue() / 100);
			}
		};
	}
	
	private void onProgressValueChanged(Number newValue) {
		porcentajeLabel.setText(((int)(newValue.doubleValue() * 100)) + "%");
		if (newValue.intValue() == 1) {
			estadoLabel.setText("Listo");
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(2000);
						aplicarFadeOut(300, stackPane);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
		} else {
			stackPane.setOpacity(1);
		}
	}
	
	private void aplicarFadeOut(long millis, Node node) {
		FadeTransition fadeTransition = new FadeTransition(Duration.millis(millis), node);
		fadeTransition.setFromValue(1.0);
		fadeTransition.setToValue(0.0);
		fadeTransition.play();
	}
	
	public void setWorker(String estado, Worker<?> worker) {
		if (this.worker != null) {
			this.worker.workDoneProperty().removeListener(changeListener);
		}
		this.worker = worker;
		setEstado(estado);
		this.worker.workDoneProperty().addListener(changeListener);
	}
	
	public void setPorcentaje(double porcentaje) {
		if (porcentaje == 1 && worker != null) {
			worker.workDoneProperty().removeListener(changeListener);
			worker = null;
		}
		progressBar.setProgress(porcentaje);
	}
	
	public void setEstado(String estado) {
		estadoLabel.setText(estado);
	}
}
