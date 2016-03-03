package dad.planeador.vuelos.components;

import java.io.IOException;
import java.util.List;

import dad.planeador.vuelos.MainApp;
import dad.planeador.vuelos.models.Aeropuerto;
import dad.planeador.vuelos.models.Interseccion;
import dad.planeador.vuelos.views.RootController.Puente;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import netscape.javascript.JSObject;

public class Mapa extends BorderPane {

	@FXML
	private ToggleButton aeropuertosToggleButton;
	@FXML
	private ToggleButton interseccionesToggleButton;
	@FXML
	private ToggleButton navaidsToggleButton;
	@FXML
	private ToggleButton aeroviasToggleButton;
	@FXML
	private TextField buscarTextField;
	@FXML
	private WebView webView;

	private Puente puente;
	private WebEngine webEngine;
	private Callback<Boolean, ?> callback;
	private ChangeListener<State> changeListener;

	public Mapa() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Mapa.fxml"));
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
		aeropuertosToggleButton.selectedProperty().addListener((obs, oldValue, newValue) -> {
			onAeropuertosToggleButtonSelectedChanged(newValue);
		});
		interseccionesToggleButton.selectedProperty().addListener((obs, oldValue, newValue) -> {
			onInterseccionesToggleButtonSelectedChanged(newValue);
		});
		changeListener = new ChangeListener<State>() {
			@Override
			public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {
				if (newValue == State.SUCCEEDED) {
					webEngine.getLoadWorker().stateProperty().removeListener(this);
					callback.call(true);
				} else if (newValue == State.CANCELLED || newValue == State.FAILED) {
					webEngine.getLoadWorker().stateProperty().removeListener(this);
					callback.call(false);
				}
			}
		};
	}

	private void onAeropuertosToggleButtonSelectedChanged(boolean newValue) {
		if (webEngine != null) {
			webEngine.executeScript("document.showAirports(" + newValue + ")");
		}
	}
	
	private void onInterseccionesToggleButtonSelectedChanged(boolean newValue) {
		if (webEngine != null) {
			webEngine.executeScript("document.showIntersections(" + newValue + ")");
		}
	}

	@FXML
	private void onAumentarZoomButtonActionPerformed() {
		if (webEngine != null) {
			webEngine.executeScript("document.zoomIn()");
		}
	}

	@FXML
	private void onAlejarZoomButtonActionPerformed() {
		if (webEngine != null) {
			webEngine.executeScript("document.zoomOut()");
		}
	}
	
	public void setPuente(Puente puente) {
		this.puente = puente;
	}

	public void cargarMapa(Callback<Boolean, ?> callback) {
		aeropuertosToggleButton.setSelected(false);
		interseccionesToggleButton.setSelected(false);
		navaidsToggleButton.setSelected(false);
		aeroviasToggleButton.setSelected(false);

		WebEngine we = webView.getEngine();
		we.load(MainApp.class.getResource("web/map.html").toString());
		webEngine = we;
		we.getLoadWorker().stateProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue == State.SUCCEEDED) {
				JSObject jso = (JSObject) we.executeScript("window");
				jso.setMember("java", puente);
			}
		});
		if (callback != null) {
			this.callback = callback;
			we.getLoadWorker().stateProperty().addListener(changeListener);
		}
	}

	public void cargarMapa() {
		cargarMapa(null);
	}

	public void agregarAeropuertos(List<Aeropuerto> aeropuertos) {
		if (webEngine != null) {
			for (Aeropuerto aeropuerto : aeropuertos) {
				String parametros = "'" + aeropuerto.getIcao() + "', " + "'" + aeropuerto.getNombre() + "', "
						+ aeropuerto.getCoordenadas().getGradosNorte() + ", "
						+ aeropuerto.getCoordenadas().getGradosEste();
				webEngine.executeScript("document.addAirport(" + parametros + ")");
			}
		}
	}
	
	public void agregarIntersecciones(List<Interseccion> intersecciones) {
		if (webEngine != null) {
			for (Interseccion interseccion : intersecciones) {
				String parametros = "'" + interseccion.getIdentificador() + "', "
						+ interseccion.getCoordenadas().getGradosNorte() + ", "
						+ interseccion.getCoordenadas().getGradosEste();
				webEngine.executeScript("document.addIntersection(" + parametros + ")");
			}
		}
	}

	public WebEngine getWebEngine() {
		return webView.getEngine();
	}
}
