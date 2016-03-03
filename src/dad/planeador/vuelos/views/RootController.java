package dad.planeador.vuelos.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dad.planeador.vuelos.MainApp;
import dad.planeador.vuelos.components.Mapa;
import dad.planeador.vuelos.components.NumberTextField;
import dad.planeador.vuelos.components.StatusBar;
import dad.planeador.vuelos.models.Aeropuerto;
import dad.planeador.vuelos.models.Aerovia;
import dad.planeador.vuelos.models.Avion;
import dad.planeador.vuelos.models.Pista;
import dad.planeador.vuelos.models.PlanDeVuelo;
import dad.planeador.vuelos.models.Punto;
import dad.planeador.vuelos.services.PlaneadorVuelosService;
import dad.planeador.vuelos.utils.AeropuertosUtils;
import dad.planeador.vuelos.utils.RutaUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.web.WebEngine;

public class RootController extends Controller {

	private static final int PESO_ADULTO = 62;
	private static final int PESO_NINOS = 35;
	private static final int PESO_BEBES = 10;

	@FXML
	private Mapa mapa;
	@FXML
	private StatusBar statusBar;

	@FXML
	private MenuItem eliminarPlanMenuItem;
	@FXML
	private TextField numeroVueloTextField;
	@FXML
	private TextField origenTextField;
	@FXML
	private TextField destinoTextField;
	@FXML
	private ComboBox<Pista> pistaDespegueComboBox;
	@FXML
	private ComboBox<Pista> pistaAterrizajeComboBox;

	@FXML
	private ComboBox<Avion> avionComboBox;
	@FXML
	private TextField tipoAvionTextField;
	@FXML
	private TextField dowTextField;
	@FXML
	private TextField mzfwTextField;
	@FXML
	private TextField observacionesTextField;
	@FXML
	private TextField mtowTextField;
	@FXML
	private TextField mlwTextField;

	@FXML
	private NumberTextField adultosTextField;
	@FXML
	private NumberTextField ninosTextField;
	@FXML
	private NumberTextField bebesTextField;
	@FXML
	private TextField maxPaxTextField;
	@FXML
	private NumberTextField equipajeTextField;
	@FXML
	private NumberTextField cargaTextField;
	@FXML
	private TextField zfTextField;

	@FXML
	private ComboBox<String> altitudComboBox;
	@FXML
	private TextArea rutaTextArea;

	private PlanDeVuelo planDeVuelo;

	private Aeropuerto aeropuertoOrigen;
	private Aeropuerto aeropuertoDestino;

	private Avion avion;

	@Override
	protected void loaded() {
		mapa.setPuente(new Puente());
		WebEngine webEngine = mapa.getWebEngine();
		statusBar.setWorker("Cargando mapa...", webEngine.getLoadWorker());
		mapa.cargarMapa((result) -> onMapaLoaded(result));

		avionComboBox.setItems(PlaneadorVuelosService.getListaAviones());
		avionComboBox.getSelectionModel().selectedItemProperty()
				.addListener((obs, oldValue, newValue) -> establecerAvion(newValue));

		adultosTextField.setMaximumDigits(4);
		ninosTextField.setMaximumDigits(4);
		bebesTextField.setMaximumDigits(4);
		equipajeTextField.setMaximumDigits(9);
		cargaTextField.setMaximumDigits(9);
		adultosTextField.textProperty().addListener((obs, oldValue, newValue) -> actualizarCargas());
		ninosTextField.textProperty().addListener((obs, oldValue, newValue) -> actualizarCargas());
		bebesTextField.textProperty().addListener((obs, oldValue, newValue) -> actualizarCargas());
		equipajeTextField.textProperty().addListener((obs, oldValue, newValue) -> actualizarCargas());
		cargaTextField.textProperty().addListener((obs, oldValue, newValue) -> actualizarCargas());

		List<String> altitudes = new ArrayList<>();
		for (int i = 15; i < 41; i++) {
			altitudes.add("FL" + i + "0");
		}
		altitudComboBox.getItems().addAll(altitudes);
		altitudComboBox.getSelectionModel().select(0);

		establecerPlan(null);
	}

	private Void onMapaLoaded(Boolean resultado) {
		if (resultado) {
			mapa.agregarAeropuertos(PlaneadorVuelosService.getListaAeropuertos());
			//mapa.agregarIntersecciones(PlaneadorVuelosService.getListaIntersecciones());
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(getStage());
			alert.setTitle("Error");
			alert.setHeaderText("Ocurrió un error al intentar cargar el mapa!");
			alert.setContentText(
					"No se pudo cargar el mapa, asegurese de que tiene conexión a internet e intentelo de nuevo.");
			alert.showAndWait();
		}
		return null;
	}

	private void setAeropuertoOrigen(Aeropuerto aeropuertoOrigen) {
		if (aeropuertoOrigen != null) {
			origenTextField.setText(aeropuertoOrigen.getIcao());
			pistaDespegueComboBox.setItems(aeropuertoOrigen.getPistas());
			pistaDespegueComboBox.getSelectionModel().select(0);
		} else {
			origenTextField.setText("");
			pistaDespegueComboBox.setItems(FXCollections.emptyObservableList());
		}

		this.aeropuertoOrigen = aeropuertoOrigen;
	}

	private void setAeropuertoDestino(Aeropuerto aeropuertoDestino) {
		if (aeropuertoDestino != null) {
			destinoTextField.setText(aeropuertoDestino.getIcao());
			pistaAterrizajeComboBox.setItems(aeropuertoDestino.getPistas());
			pistaAterrizajeComboBox.getSelectionModel().select(0);
		} else {
			destinoTextField.setText("");
			pistaAterrizajeComboBox.setItems(FXCollections.emptyObservableList());
		}

		this.aeropuertoDestino = aeropuertoDestino;
	}

	private void actualizarTitulo() {
		if (planDeVuelo != null) {
			getStage().setTitle(MainApp.TITULO_VENTANA + " - " + planDeVuelo.getNumeroVuelo() + " ["
					+ planDeVuelo.getAeropuertoOrigen().getIcao() + " -> "
					+ planDeVuelo.getAeropuertoDestino().getIcao() + "]");
		} else {
			getStage().setTitle(MainApp.TITULO_VENTANA + " - Nuevo plan*");
		}
	}

	@FXML
	private void onNuevoPlanMenuItemActionPerformed() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.initOwner(getStage());
		alert.setTitle("Confirmación");
		alert.setHeaderText("¿Esta seguro de que desea crear un nuevo plan?");
		alert.setContentText(
				"Todos los cambios realizados en el plan actual, que no hayan sido guardados, se perderan.");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			establecerPlan(null);
		}
	}

	@FXML
	private void onAbrirPlanMenuItemActionPerformed() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.initOwner(getStage());
		alert.setTitle("Confirmación");
		alert.setHeaderText("¿Esta seguro de que desea abrir un plan?");
		alert.setContentText(
				"Todos los cambios realizados en el plan actual, que no hayan sido guardados, se perderan.");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			PlanDeVuelo plan = PlanDeVueloChooser.showAeropuertoChooser(getStage());
			if (plan != null) {
				establecerPlan(plan);
			}
		}
	}

	private void establecerPlan(PlanDeVuelo plan) {
		planDeVuelo = plan;
		numeroVueloTextField.setText("");
		setAeropuertoOrigen(null);
		setAeropuertoDestino(null);
		avionComboBox.getSelectionModel().clearSelection();
		observacionesTextField.setText("");
		adultosTextField.setText("");
		ninosTextField.setText("");
		bebesTextField.setText("");
		cargaTextField.setText("");
		equipajeTextField.setText("");
		altitudComboBox.getSelectionModel().select(0);
		rutaTextArea.setText("");

		if (plan != null) {
			eliminarPlanMenuItem.setDisable(false);
			numeroVueloTextField.setText(plan.getNumeroVuelo());
			numeroVueloTextField.setDisable(true);
			setAeropuertoOrigen(plan.getAeropuertoOrigen());
			setAeropuertoDestino(plan.getAeropuertoDestino());
			if (plan.getAvion() != null) {
				avionComboBox.getSelectionModel().select(plan.getAvion());
			}

			if (plan.getPistaDespegue() != null) {
				pistaDespegueComboBox.getSelectionModel().select(plan.getPistaDespegue());
			}
			if (plan.getPistaAterrizaje() != null) {
				pistaAterrizajeComboBox.getSelectionModel().select(plan.getPistaAterrizaje());
			}
			if (plan.getObservacionesAvion() != null) {
				observacionesTextField.setText(plan.getObservacionesAvion());
			}
			if (plan.getNumeroAdultos() != 0) {
				adultosTextField.setText("" + plan.getNumeroAdultos());
			}
			if (plan.getNumeroChildren() != 0) {
				ninosTextField.setText("" + plan.getNumeroChildren());
			}
			if (plan.getNumeroBebes() != 0) {
				bebesTextField.setText("" + plan.getNumeroBebes());
			}
			if (plan.getCarga() != 0) {
				cargaTextField.setText("" + plan.getCarga());
			}
			if (plan.getEquipaje() != 0) {
				equipajeTextField.setText("" + plan.getEquipaje());
			}
			if (plan.getAltitudCrucero() != 0) {
				altitudComboBox.getSelectionModel().select("FL" + plan.getAltitudCrucero() / 100);
			}
			if (plan.getRutaATC() != null) {
				rutaTextArea.setText(plan.getRutaATC());
			}
		} else {
			eliminarPlanMenuItem.setDisable(true);
			numeroVueloTextField.setDisable(false);
		}

		actualizarTitulo();
	}

	@FXML
	private void onGuardarPlanMenuItemActionPerformed() {
		StringBuilder mensaje = new StringBuilder();
		String numeroVuelo = numeroVueloTextField.getText();
		int altitudCrucero, carga = 0, equipaje = 0, numeroAdultos = 0, numeroBebes = 0, numeroChildren = 0;

		if (numeroVuelo.isEmpty()) {
			mensaje.append("El numero de vuelo esta vacio.\n");
		}
		if (aeropuertoOrigen == null) {
			mensaje.append("Debe seleccionar el aeropuerto de origen.\n");
		}
		if (aeropuertoDestino == null) {
			mensaje.append("Debe seleccionar el aeropuerto de destino.\n");
		}
		if (avion == null) {
			mensaje.append("Debe seleccionar un avión.\n");
		}

		if (!cargaTextField.getText().isEmpty()) {
			try {
				carga = Integer.parseInt(cargaTextField.getText());
			} catch (NumberFormatException e) {
				mensaje.append("La carga no es valida.\n");
			}
		}

		if (!equipajeTextField.getText().isEmpty()) {
			try {
				equipaje = Integer.parseInt(equipajeTextField.getText());
			} catch (NumberFormatException e) {
				mensaje.append("La equipaje no es valido.\n");
			}
		}

		if (!adultosTextField.getText().isEmpty()) {
			try {
				numeroAdultos = Integer.parseInt(adultosTextField.getText());
			} catch (NumberFormatException e) {
				mensaje.append("El numero de adultos no es valido.\n");
			}
		}

		if (!bebesTextField.getText().isEmpty()) {
			try {
				numeroBebes = Integer.parseInt(bebesTextField.getText());
			} catch (NumberFormatException e) {
				mensaje.append("El numero de bebes no es valido.\n");
			}
		}

		if (!ninosTextField.getText().isEmpty()) {
			try {
				numeroChildren = Integer.parseInt(ninosTextField.getText());
			} catch (NumberFormatException e) {
				mensaje.append("El numero de niños no es valido.\n");
			}
		}

		altitudCrucero = Integer.parseInt(altitudComboBox.getSelectionModel().getSelectedItem().substring(2)) * 100;

		if (mensaje.length() == 0) {
			if (planDeVuelo == null) {
				planDeVuelo = new PlanDeVuelo();
				planDeVuelo.setNumeroVuelo(numeroVuelo);
			}

			planDeVuelo.setAeropuertoDestino(aeropuertoDestino);
			planDeVuelo.setAeropuertoOrigen(aeropuertoOrigen);

			planDeVuelo.setAltitudCrucero(altitudCrucero);

			planDeVuelo.setAvion(avion);
			planDeVuelo.setCarga(carga);
			planDeVuelo.setEquipaje(equipaje);
			planDeVuelo.setNumeroAdultos(numeroAdultos);
			planDeVuelo.setNumeroBebes(numeroBebes);
			planDeVuelo.setNumeroChildren(numeroChildren);
			planDeVuelo.setObservacionesAvion(observacionesTextField.getText());
			planDeVuelo.setPistaAterrizaje(pistaAterrizajeComboBox.getValue());
			planDeVuelo.setPistaDespegue(pistaDespegueComboBox.getValue());
			planDeVuelo.setRutaATC(rutaTextArea.getText());

			actualizarTitulo();

			PlaneadorVuelosService.guardarPlanDeVuelo(planDeVuelo);
			eliminarPlanMenuItem.setDisable(false);
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(getStage());
			alert.setTitle("No se ha podido guardar");
			alert.setHeaderText(
					"No se ha podido guardar el plan de vuelo.\n\nPor favor, corrija los siguientes errores:");
			alert.setContentText(mensaje.toString());
			alert.showAndWait();
		}
	}

	@FXML
	private void onEliminarPlanMenuItemActionPerformed() {
		if (planDeVuelo != null) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.initOwner(getStage());
			alert.setTitle("Confirmación");
			alert.setHeaderText("¿Esta seguro de que desea eliminar el plan?");
			alert.setContentText("Esta operación no se podrá deshacer.");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				PlaneadorVuelosService.eliminarPlanDeVuelo(planDeVuelo);
				establecerPlan(null);
			}
		}
	}

	@FXML
	private void onGestionarAvionesMenuItemActionPerformed() {
		getMainApp().showGestionAviones();
		establecerAvion(avion);
	}

	@FXML
	private void onRefrescarMapaMenuItemActionPerformed() {
		WebEngine webEngine = mapa.getWebEngine();
		statusBar.setWorker("Cargando mapa...", webEngine.getLoadWorker());
		mapa.cargarMapa((result) -> onMapaLoaded(result));
	}

	@FXML
	private void onSeleccionarAeropuertoOrigenButtonActionPerformed() {
		Aeropuerto aeropuerto = AeropuertoChooser.showAeropuertoChooser(getStage(), aeropuertoOrigen);
		if (aeropuerto != null) {
			setAeropuertoOrigen(aeropuerto);
		}
	}

	@FXML
	private void onSeleccionarAeropuertoDestinoButtonActionPerformed() {
		Aeropuerto aeropuerto = AeropuertoChooser.showAeropuertoChooser(getStage(), aeropuertoDestino);
		if (aeropuerto != null) {
			setAeropuertoDestino(aeropuerto);
		}
	}

	@FXML
	private void onValidarRutaActionPerformed() {
		String ruta = rutaTextArea.getText().replace("\n", "");

		if (!ruta.isEmpty()) {
			if (validarRuta(ruta)) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.initOwner(getStage());
				alert.setTitle("La ruta es valida");
				alert.setHeaderText("La ruta ATC es correcta.");
				alert.setContentText((aeropuertoOrigen != null ? aeropuertoOrigen.getIcao() + " DCT " : "") + ruta
						+ (aeropuertoDestino != null ? " DCT " + aeropuertoDestino.getIcao() : ""));
				alert.showAndWait();
			}
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(getStage());
			alert.setTitle("La ruta esta vacia");
			alert.setHeaderText("La ruta ATC esta vacia y por tanto no se puede validar.");
			alert.showAndWait();
		}
	}

	private boolean validarRuta(String ruta) {
		// ruta = ruta.replace("\n", "");
		String[] partesRuta = ruta.split(" ");

		// 0 = se espera aerovia o DCT
		// 1 = se espera punto
		int estado = 1;
		Punto puntoAnterior = null;
		Aerovia aeroviaActual = null;
		for (String parte : partesRuta) {
			if (estado == 0) {
				aeroviaActual = RutaUtils.obtenerAerovia(parte);
				if (!parte.equalsIgnoreCase("DCT")) {
					if (aeroviaActual == null) {
						Alert alert = new Alert(AlertType.WARNING);
						alert.initOwner(getStage());
						alert.setTitle("La ruta no es valida");
						alert.setHeaderText("La ruta ATC no es valida!");
						alert.setContentText("La aerovía " + parte + " no existe.");
						alert.showAndWait();
						return false;
					} else if (puntoAnterior != null && !RutaUtils.aeroviaContienePunto(aeroviaActual, puntoAnterior)) {
						Alert alert = new Alert(AlertType.WARNING);
						alert.initOwner(getStage());
						alert.setTitle("La ruta no es valida");
						alert.setHeaderText("La ruta ATC no es valida!");
						alert.setContentText("La aerovía " + aeroviaActual.getIdentificador() + " no contiene el punto "
								+ puntoAnterior.getIdentificador() + ".");
						alert.showAndWait();
						return false;
					}
				}
				estado = 1;
			} else if (estado == 1) {
				Punto punto = RutaUtils.obtenerPunto(parte);
				if (punto == null) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.initOwner(getStage());
					alert.setTitle("La ruta no es valida");
					alert.setHeaderText("La ruta ATC no es valida!");
					alert.setContentText("El punto " + parte + " no existe.");
					alert.showAndWait();
					return false;
				} else if (aeroviaActual != null) {
					if (!RutaUtils.aeroviaContienePunto(aeroviaActual, punto)) {
						Alert alert = new Alert(AlertType.WARNING);
						alert.initOwner(getStage());
						alert.setTitle("La ruta no es valida");
						alert.setHeaderText("La ruta ATC no es valida!");
						alert.setContentText("La aerovía " + aeroviaActual.getIdentificador() + " no contiene el punto "
								+ punto.getIdentificador() + ".");
						alert.showAndWait();
						return false;
					}
				}
				puntoAnterior = punto;
				estado = 0;
			}
		}

		return true;
	}

	private void establecerAvion(Avion avion) {
		this.avion = avion;
		if (avion != null) {
			tipoAvionTextField.setText(avion.getTipoAvion().getTipo());
			dowTextField.setText(String.format("%,d", avion.getDow()) + " kg");
			mzfwTextField.setText(String.format("%,d", avion.getMzfw()) + " kg");
			mtowTextField.setText(String.format("%,d", avion.getMtow()) + " kg");
			mlwTextField.setText(String.format("%,d", avion.getMlw()) + " kg");
			maxPaxTextField.setText("" + avion.getPasajeros());
		} else {
			tipoAvionTextField.setText("");
			dowTextField.setText("");
			mzfwTextField.setText("");
			mtowTextField.setText("");
			mlwTextField.setText("");
			maxPaxTextField.setText("");
		}
		actualizarCargas();
	}

	private void actualizarCargas() {
		maxPaxTextField.getStyleClass().remove("invalido");
		zfTextField.getStyleClass().remove("invalido");
		try {
			int numeroAdultos = adultosTextField.getText().isEmpty() ? 0 : Integer.parseInt(adultosTextField.getText());
			int numeroNinos = ninosTextField.getText().isEmpty() ? 0 : Integer.parseInt(ninosTextField.getText());
			int numeroBebes = bebesTextField.getText().isEmpty() ? 0 : Integer.parseInt(bebesTextField.getText());

			long pesoCeroCombustible = numeroAdultos * PESO_ADULTO;
			pesoCeroCombustible += numeroNinos * PESO_NINOS;
			pesoCeroCombustible += numeroBebes * PESO_BEBES;
			pesoCeroCombustible += equipajeTextField.getText().isEmpty() ? 0
					: Integer.parseInt(equipajeTextField.getText());
			pesoCeroCombustible += cargaTextField.getText().isEmpty() ? 0 : Integer.parseInt(cargaTextField.getText());

			if (avion != null) {
				if (numeroAdultos + numeroBebes + numeroNinos > avion.getPasajeros()) {
					maxPaxTextField.getStyleClass().add("invalido");
				}
				if (pesoCeroCombustible > avion.getMzfw()) {
					zfTextField.getStyleClass().add("invalido");
				}
			}

			zfTextField.setText(String.format("%,d", pesoCeroCombustible) + " kg");
		} catch (NumberFormatException e) {
			zfTextField.getStyleClass().add("invalido");
			zfTextField.setText("Error");
		}
	}

	public class Puente {
		public void establecerAeropuertoOrigen(String icao) {
			setAeropuertoOrigen(AeropuertosUtils.obtenerAeropuerto(icao));
		}

		public void establecerAeropuertoDestino(String icao) {
			setAeropuertoDestino(AeropuertosUtils.obtenerAeropuerto(icao));
		}
	}
}
