package dad.planeador.vuelos.views;

import java.io.IOException;

import dad.planeador.vuelos.images.Imagenes;
import dad.planeador.vuelos.models.PlanDeVuelo;
import dad.planeador.vuelos.utils.WindowsUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PlanDeVueloChooser {
	
	public static PlanDeVuelo showAeropuertoChooser(Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(PlanDeVueloChooser.class.getResource("PlanDeVueloChooser.fxml"));
			BorderPane rootLayout = (BorderPane) loader.load();
			
			Stage planDeVueloChooser = new Stage();
			planDeVueloChooser.initOwner(parentStage);
			planDeVueloChooser.getIcons().add(Imagenes.ICON);
			planDeVueloChooser.setTitle("Selecciona un plan de vuelo");
			planDeVueloChooser.initModality(Modality.WINDOW_MODAL);
			planDeVueloChooser.setOnShown((event) -> WindowsUtils.centerOnParent((Stage) event.getSource(), parentStage));
			
			PlanDeVueloChooserController controller = loader.getController();
			controller.setStage(planDeVueloChooser);

			Scene scene = new Scene(rootLayout);
			planDeVueloChooser.setScene(scene);
			planDeVueloChooser.showAndWait();
			return controller.getPlanDeVuelo();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
