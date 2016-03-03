package dad.planeador.vuelos;

import java.io.IOException;

import dad.planeador.vuelos.components.Mapa;
import dad.planeador.vuelos.images.Imagenes;
import dad.planeador.vuelos.models.Avion;
import dad.planeador.vuelos.models.TipoAvion;
import dad.planeador.vuelos.utils.WindowsUtils;
import dad.planeador.vuelos.views.Controller;
import dad.planeador.vuelos.views.NuevoEditarAvionController;
import dad.planeador.vuelos.views.NuevoEditarTipoController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainApp extends Application {
	
	public static final String TITULO_VENTANA = "Planeador de Vuelos";
	private Mapa mapa;
	private Stage primaryStage;
	private Stage planeadorVuelo;
	private Stage gestionAviones;
	private Stage gestionTipos;

	public static void main(String[] args) {
		//System.setProperty("sun.java2d.opengl", "true");
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		primaryStage.getIcons().add(Imagenes.ICON);
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("views/PantallaCarga.fxml"));
			AnchorPane rootLayout = (AnchorPane) loader.load();
			
			Controller controller = loader.getController();
			controller.setMainApp(this);
			controller.setStage(primaryStage);

			Scene scene = new Scene(rootLayout, 700, 345);
			scene.setFill(Color.TRANSPARENT);
			primaryStage.setScene(scene);
			primaryStage.show();
			
			Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
	        primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
	        primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showPlaneadorVuelo() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("views/Root.fxml"));
			BorderPane rootLayout = (BorderPane) loader.load();
			
			planeadorVuelo = new Stage();
			planeadorVuelo.initOwner(primaryStage);
			planeadorVuelo.getIcons().add(Imagenes.ICON);
			planeadorVuelo.setTitle(TITULO_VENTANA);
			
			Controller controller = loader.getController();
			controller.setMainApp(this);
			controller.setStage(planeadorVuelo);

			Scene scene = new Scene(rootLayout);
			planeadorVuelo.setScene(scene);
			planeadorVuelo.setMaximized(true);
			planeadorVuelo.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showGestionAviones() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("views/GestionarAviones.fxml"));
			BorderPane rootLayout = (BorderPane) loader.load();
			
			gestionAviones = new Stage();
			gestionAviones.initModality(Modality.WINDOW_MODAL);
			gestionAviones.initOwner(planeadorVuelo);
			gestionAviones.getIcons().add(Imagenes.ICON);
			gestionAviones.setTitle("Gestión de aviones");
			gestionAviones.setOnShown((event) -> WindowsUtils.centerOnParent((Stage) event.getSource(), planeadorVuelo));
			
			Controller controller = loader.getController();
			controller.setMainApp(this);
			controller.setStage(gestionAviones);

			Scene scene = new Scene(rootLayout);
			gestionAviones.setScene(scene);
			
			gestionAviones.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showEditarAvion(Avion avion) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("views/NuevoEditarAvion.fxml"));
			AnchorPane rootLayout = (AnchorPane) loader.load();
			
			Stage editarAvion = new Stage();
			editarAvion.initModality(Modality.WINDOW_MODAL);
			editarAvion.initOwner(gestionAviones);
			editarAvion.getIcons().add(Imagenes.ICON);
			if (avion == null) {
				editarAvion.setTitle("Nuevo avión");
			} else {
				editarAvion.setTitle("Editar avión");
			}
			editarAvion.setOnShown((event) -> WindowsUtils.centerOnParent((Stage) event.getSource(), gestionAviones));
			
			NuevoEditarAvionController controller = loader.getController();
			controller.setMainApp(this);
			controller.setStage(editarAvion);
			if (avion != null) {
				controller.setAvion(avion);
			}

			Scene scene = new Scene(rootLayout);
			editarAvion.setScene(scene);
			
			editarAvion.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showNuevoAvion() {
		showEditarAvion(null);
	}
	
	public void showGestionTipos() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("views/GestionarTipos.fxml"));
			BorderPane rootLayout = (BorderPane) loader.load();
			
			gestionTipos = new Stage();
			gestionTipos.initModality(Modality.WINDOW_MODAL);
			gestionTipos.initOwner(gestionAviones);
			gestionTipos.getIcons().add(Imagenes.ICON);
			gestionTipos.setTitle("Gestión de tipos de aviones");
			gestionTipos.setOnShown((event) -> WindowsUtils.centerOnParent((Stage) event.getSource(), gestionAviones));
			
			Controller controller = loader.getController();
			controller.setMainApp(this);
			controller.setStage(gestionTipos);

			Scene scene = new Scene(rootLayout);
			gestionTipos.setScene(scene);
			
			gestionTipos.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showEditarTipo(TipoAvion tipo) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("views/NuevoEditarTipo.fxml"));
			AnchorPane rootLayout = (AnchorPane) loader.load();
			
			Stage editarTipo = new Stage();
			editarTipo.initModality(Modality.WINDOW_MODAL);
			editarTipo.initOwner(gestionTipos);
			editarTipo.getIcons().add(Imagenes.ICON);
			if (tipo == null) {
				editarTipo.setTitle("Nuevo tipo");
			} else {
				editarTipo.setTitle("Editar tipo");
			}
			editarTipo.setOnShown((event) -> WindowsUtils.centerOnParent((Stage) event.getSource(), gestionTipos));
			
			NuevoEditarTipoController controller = loader.getController();
			controller.setMainApp(this);
			controller.setStage(editarTipo);
			if (tipo != null) {
				controller.setTipo(tipo);
			}

			Scene scene = new Scene(rootLayout);
			editarTipo.setScene(scene);
			
			editarTipo.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showNuevoTipo() {
		showEditarTipo(null);
	}

	public Mapa getMapa() {
		return mapa;
	}

	public void setMapa(Mapa mapa) {
		this.mapa = mapa;
	}
}
