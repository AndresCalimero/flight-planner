package dad.planeador.vuelos.views;

import dad.planeador.vuelos.MainApp;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public abstract class Controller {

	private MainApp mainApp;
	private Stage stage;
	private boolean initialized = false;

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		if (stage != null && initialized) {
			loaded();
		}
	}

	public void setStage(Stage stage) {
		this.stage = stage;
		if (mainApp != null && initialized) {
			loaded();
		}
	}

	@FXML
	private void initialize() {
		initialized = true;
		if (mainApp != null && stage != null) {
			loaded();
		}
	}

	protected abstract void loaded();

	protected MainApp getMainApp() {
		return mainApp;
	}

	protected Stage getStage() {
		return stage;
	}
}
