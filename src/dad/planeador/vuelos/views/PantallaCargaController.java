package dad.planeador.vuelos.views;

import dad.planeador.vuelos.services.PlaneadorVuelosService;
import javafx.application.Platform;

public class PantallaCargaController extends Controller {

	@Override
	protected void loaded() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				PlaneadorVuelosService.cargarDatos();
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						getStage().close();
						getMainApp().showPlaneadorVuelo();
					}
				});
			}
		}).start();
	}
}
