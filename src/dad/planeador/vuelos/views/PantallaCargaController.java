package dad.planeador.vuelos.views;

import java.util.concurrent.Semaphore;

import dad.planeador.vuelos.components.Mapa;
import dad.planeador.vuelos.services.PlaneadorVuelosService;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class PantallaCargaController extends Controller {

	private Semaphore semaforo = new Semaphore(0);

	@Override
	protected void loaded() {
		Platform.runLater(new Runnable() {
			private Mapa mapa;

			@Override
			public void run() {
				new Thread(new Runnable() {

					@Override
					public void run() {
						PlaneadorVuelosService.cargarDatos();
						semaforo.release();
					}
				}).start();

				mapa = new Mapa();
				mapa.cargarMapa((result) -> {
					if (result) {
						try {
							semaforo.acquire();
							mapa.agregarAeropuertos(PlaneadorVuelosService.getListaAeropuertos());
							mapa.agregarIntersecciones(PlaneadorVuelosService.getListaIntersecciones());
							mapa.agregarNavaids(PlaneadorVuelosService.getListaNavaids());
							getMainApp().setMapa(mapa);
							getStage().close();
							getMainApp().showPlaneadorVuelo();
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						Alert alert = new Alert(AlertType.ERROR);
						alert.initOwner(getStage());
						alert.setTitle("Error");
						alert.setHeaderText("Ocurrió un error al intentar cargar el mapa!");
						alert.setContentText(
								"No se pudo cargar el mapa, asegurese de que tiene conexión a internet e intentelo de nuevo.");
						alert.showAndWait();
					}
					return null;
				});
			}
		});
	}
}