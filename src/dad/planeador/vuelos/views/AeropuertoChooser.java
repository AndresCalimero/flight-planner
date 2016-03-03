package dad.planeador.vuelos.views;

import java.io.IOException;

import dad.planeador.vuelos.images.Imagenes;
import dad.planeador.vuelos.models.Aeropuerto;
import dad.planeador.vuelos.utils.WindowsUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AeropuertoChooser {
	
	public static Aeropuerto showAeropuertoChooser(Stage parentStage) {
		return showAeropuertoChooser(parentStage, null);
	}
	
	public static Aeropuerto showAeropuertoChooser(Stage parentStage, Aeropuerto valorInicial) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(AeropuertoChooser.class.getResource("AeropuertoChooser.fxml"));
			BorderPane rootLayout = (BorderPane) loader.load();
			
			Stage aeropuertoChooser = new Stage();
			aeropuertoChooser.initOwner(parentStage);
			aeropuertoChooser.getIcons().add(Imagenes.ICON);
			aeropuertoChooser.setTitle("Selecciona un aeropuerto");
			aeropuertoChooser.initModality(Modality.WINDOW_MODAL);
			aeropuertoChooser.setOnShown((event) -> WindowsUtils.centerOnParent((Stage) event.getSource(), parentStage));
			
			AeropuertoChooserController controller = loader.getController();
			controller.setStage(aeropuertoChooser);
			if (valorInicial != null) {
				controller.seleccionarAeropuerto(valorInicial);
			}

			Scene scene = new Scene(rootLayout);
			aeropuertoChooser.setScene(scene);
			aeropuertoChooser.showAndWait();
			return controller.getAeropuertoSeleccionado();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
