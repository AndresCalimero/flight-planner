package dad.planeador.vuelos.views;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import dad.planeador.vuelos.MainApp;
import dad.planeador.vuelos.components.Mapa;
import dad.planeador.vuelos.components.NumberTextField;
import dad.planeador.vuelos.components.StatusBar;
import dad.planeador.vuelos.dialogs.ExceptionAlert;
import dad.planeador.vuelos.models.Aeropuerto;
import dad.planeador.vuelos.models.Aerovia;
import dad.planeador.vuelos.models.Avion;
import dad.planeador.vuelos.models.Coordenadas;
import dad.planeador.vuelos.models.Interseccion;
import dad.planeador.vuelos.models.Navaid;
import dad.planeador.vuelos.models.Pista;
import dad.planeador.vuelos.models.PlanDeVuelo;
import dad.planeador.vuelos.models.Punto;
import dad.planeador.vuelos.reports.PlanDeVueloItem;
import dad.planeador.vuelos.reports.PuntoItem;
import dad.planeador.vuelos.services.PlaneadorVuelosService;
import dad.planeador.vuelos.services.PlaneadorVuelosServiceException;
import dad.planeador.vuelos.utils.AeropuertosUtils;
import dad.planeador.vuelos.utils.RutaUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.web.WebEngine;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

public class RootController extends Controller {

	private static final int PESO_ADULTO = 62;
	private static final int PESO_NINOS = 35;
	private static final int PESO_BEBES = 10;

	@FXML
	private BorderPane mapaBorderPane;
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

	private Mapa mapa;
	private PlanDeVuelo planDeVuelo;

	private Aeropuerto aeropuertoOrigen;
	private Aeropuerto aeropuertoDestino;

	private Avion avion;

	@Override
	protected void loaded() {
		mapa = getMainApp().getMapa();
		mapaBorderPane.setCenter(mapa);
		mapa.setPuente(new Puente());
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Coordenadas coorEspaña = new Coordenadas();
				coorEspaña.setGradosNorte(40.138739);
				coorEspaña.setGradosEste(-4.646889);
				mapa.moverMapa(coorEspaña, 6);
			}
		});

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
			mapa.agregarIntersecciones(PlaneadorVuelosService.getListaIntersecciones());
			mapa.agregarNavaids(PlaneadorVuelosService.getListaNavaids());
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
			if (validarRuta(rutaTextArea.getText(), false)) {
				mapa.establecerRuta(aeropuertoOrigen, obtenerRuta().keySet(), aeropuertoDestino);
			}
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
			if (validarRuta(rutaTextArea.getText(), false)) {
				mapa.establecerRuta(aeropuertoOrigen, obtenerRuta().keySet(), aeropuertoDestino);
			}
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
			
			if (validarRuta(plan.getRutaATC(), false)) {
				mapa.establecerRuta(aeropuertoOrigen, obtenerRuta().keySet(), aeropuertoDestino);
			}
		} else {
			mapa.limpiarRuta();
			eliminarPlanMenuItem.setDisable(true);
			numeroVueloTextField.setDisable(false);
		}

		actualizarTitulo();
	}

	@FXML
	private void onGuardarPlanMenuItemActionPerformed() {
		guardarPlan();
	}

	private void guardarPlan() {
		if (validarPlan()) {
			String numeroVuelo = numeroVueloTextField.getText();
			int altitudCrucero, carga = 0, equipaje = 0, numeroAdultos = 0, numeroBebes = 0, numeroChildren = 0;

			if (!cargaTextField.getText().isEmpty()) {
				carga = Integer.parseInt(cargaTextField.getText());
			}

			if (!equipajeTextField.getText().isEmpty()) {
				equipaje = Integer.parseInt(equipajeTextField.getText());
			}

			if (!adultosTextField.getText().isEmpty()) {
				numeroAdultos = Integer.parseInt(adultosTextField.getText());
			}

			if (!bebesTextField.getText().isEmpty()) {
				numeroBebes = Integer.parseInt(bebesTextField.getText());
			}

			if (!ninosTextField.getText().isEmpty()) {
				numeroChildren = Integer.parseInt(ninosTextField.getText());
			}

			altitudCrucero = Integer.parseInt(altitudComboBox.getSelectionModel().getSelectedItem().substring(2)) * 100;

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
			if (validarRuta(rutaTextArea.getText(), false)) {
				mapa.establecerRuta(aeropuertoOrigen, obtenerRuta().keySet(), aeropuertoDestino);
			}

			actualizarTitulo();

			try {
				PlaneadorVuelosService.guardarPlanDeVuelo(planDeVuelo);
			} catch (PlaneadorVuelosServiceException e) {
				ExceptionAlert alert = new ExceptionAlert(AlertType.ERROR);
				alert.setTitle("Error al intentar guardar el plan de vuelo");
				alert.setHeaderText("Hubo un error al intentar guardar el plan de vuelo!");
				alert.setContentText(e.getMessage());
				alert.setExpandableLabelText("El error fue:");
				alert.setException(e);
				alert.initOwner(getStage());
				alert.showAndWait();
			}
			eliminarPlanMenuItem.setDisable(false);
		}
	}

	private boolean validarPlan() {
		StringBuilder mensaje = new StringBuilder();
		String numeroVuelo = numeroVueloTextField.getText();

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
				Integer.parseInt(cargaTextField.getText());
			} catch (NumberFormatException e) {
				mensaje.append("La carga no es valida.\n");
			}
		}

		if (!equipajeTextField.getText().isEmpty()) {
			try {
				Integer.parseInt(equipajeTextField.getText());
			} catch (NumberFormatException e) {
				mensaje.append("La equipaje no es valido.\n");
			}
		}

		if (!adultosTextField.getText().isEmpty()) {
			try {
				Integer.parseInt(adultosTextField.getText());
			} catch (NumberFormatException e) {
				mensaje.append("El numero de adultos no es valido.\n");
			}
		}

		if (!bebesTextField.getText().isEmpty()) {
			try {
				Integer.parseInt(bebesTextField.getText());
			} catch (NumberFormatException e) {
				mensaje.append("El numero de bebes no es valido.\n");
			}
		}

		if (!ninosTextField.getText().isEmpty()) {
			try {
				Integer.parseInt(ninosTextField.getText());
			} catch (NumberFormatException e) {
				mensaje.append("El numero de niños no es valido.\n");
			}
		}

		if (mensaje.length() != 0) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(getStage());
			alert.setTitle("El plan de vuelo no es valido");
			alert.setHeaderText(
					"No se ha podido guardar/exportar el plan de vuelo.\n\nPor favor, corrija los siguientes errores:");
			alert.setContentText(mensaje.toString());
			alert.showAndWait();
			return false;
		}

		return true;
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
				try {
					PlaneadorVuelosService.eliminarPlanDeVuelo(planDeVuelo);
					establecerPlan(null);
				} catch (PlaneadorVuelosServiceException e) {
					ExceptionAlert exAlert = new ExceptionAlert(AlertType.ERROR);
					exAlert.setTitle("Error al intentar eliminar el plan de vuelo");
					exAlert.setHeaderText("Hubo un error al intentar eliminar el plan de vuelo!");
					exAlert.setContentText(e.getMessage());
					exAlert.setExpandableLabelText("El error fue:");
					exAlert.setException(e);
					exAlert.initOwner(getStage());
					exAlert.showAndWait();
				}
			}
		}
	}

	@FXML
	private void onExportarPlanMenuItemActionPerformed() {
		String ruta = rutaTextArea.getText().replace("\n", "");
		if (!ruta.isEmpty()) {
			if (validarPlan() && validarRuta(ruta, true)) {
				guardarPlan();
				Map<Punto, Aerovia> puntos = obtenerRuta();
				List<PlanDeVueloItem> planDeVuelo = new ArrayList<>();
				List<PuntoItem> puntosItem = new ArrayList<>();

				PuntoItem aeropuerto = new PuntoItem();
				aeropuerto.cargarAeropuerto(aeropuertoOrigen, false);
				puntosItem.add(aeropuerto);
				
				for (Entry<Punto, Aerovia> p : puntos.entrySet()) {
					PuntoItem pi = new PuntoItem();
					if (p.getKey() instanceof Interseccion) {
						pi.cargarInterseccion((Interseccion) p.getKey(), p.getValue());
					} else {
						pi.cargarNavaid((Navaid) p.getKey(), p.getValue());
					}
					puntosItem.add(pi);
				}

				aeropuerto = new PuntoItem();
				aeropuerto.cargarAeropuerto(aeropuertoDestino, true);
				puntosItem.add(aeropuerto);
				
				PlanDeVueloItem pdvi = new PlanDeVueloItem();
				pdvi.cargarPlanDeVuelo(this.planDeVuelo);
				pdvi.setPuntos(puntosItem);
				planDeVuelo.add(pdvi);

				try {
					JasperPrint jasperPrint;

					InputStream is = getClass()
							.getResourceAsStream("/dad/planeador/vuelos/reports/PlanDeVuelo.jasper");
					JRDataSource data = new JRBeanCollectionDataSource(planDeVuelo);
					Map<String, Object> parametros = new HashMap<>();
					parametros.put("PESO_BEBE", PESO_BEBES);
					parametros.put("PESO_NINO", PESO_NINOS);
					parametros.put("PESO_ADULTO", PESO_ADULTO);
					jasperPrint = JasperFillManager.fillReport(is, parametros, data);

					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					JasperViewer viewer = new JasperViewer(jasperPrint, false);
					viewer.setTitle("Vista preeliminar");
					viewer.setVisible(true);
				} catch (JRException | ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException e1) {
					e1.printStackTrace();
					ExceptionAlert alert = new ExceptionAlert(AlertType.ERROR);
					alert.setTitle("Error al intentar generar el reporte");
					alert.setHeaderText("Hubo un error al intentar exportar el plan de vuelo!");
					alert.setContentText(e1.getMessage());
					alert.setExpandableLabelText("El error fue:");
					alert.setException(e1);
					alert.initOwner(getStage());
					alert.showAndWait();
				}
			}
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(getStage());
			alert.setTitle("La ruta esta vacia");
			alert.setHeaderText("La ruta ATC esta vacia!");
			alert.setContentText("No se puede exportar el plan de vuelo sin una ruta ATC.");
			alert.showAndWait();
		}
	}
	
	private Map<Punto, Aerovia> obtenerRuta() {
		Map<Punto, Aerovia> puntos = new LinkedHashMap<>();
		
		String ruta = rutaTextArea.getText().replace("\n", "");
		String[] partesRuta = ruta.split(" ");

		// 0 = se espera aerovia o DCT
		// 1 = se espera punto
		// 2 = hay pendiente un punto
		int estado = 1;
		String puntoAnterior = null;
		Aerovia aeroviaActual = null;
		for (String parte : partesRuta) {
			if (estado == 0) {
				aeroviaActual = parte.equalsIgnoreCase("DCT") ? null : RutaUtils.obtenerAerovia(parte);
				estado = 1;
			} else if (estado == 1) {
				if (aeroviaActual == null) {
					puntoAnterior = parte;
					estado = 2;
				} else {
					Punto punto = RutaUtils.obtenerPunto(parte, aeroviaActual);
					puntos.put(punto, aeroviaActual);
					estado = 0;
				}
			} else if (estado == 2) {
				aeroviaActual = RutaUtils.obtenerAerovia(parte);
				Punto punto = RutaUtils.obtenerPunto(puntoAnterior, aeroviaActual);
				puntos.put(punto, null);
				estado = 1;
			}
		}
		return puntos;
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
			if (validarRuta(ruta, true)) {
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

	private boolean validarRuta(String ruta, boolean mostrarErrores) {
		ruta = ruta.replace("\n", "");
		if (ruta.isEmpty()) return false;
		
		Pattern p = Pattern.compile("\\sDCT .+ DCT\\s");
	    Matcher m = p.matcher(ruta);
		
		if (m.find()) {
			if (mostrarErrores) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.initOwner(getStage());
				alert.setTitle("La ruta no es valida");
				alert.setHeaderText("La ruta ATC no es valida!");
				alert.setContentText("Un punto de la ruta es ambiguo, intente evitar el uso de DCT.");
				alert.showAndWait();
			}
			return false;
		}
		
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
						if (mostrarErrores) {
							Alert alert = new Alert(AlertType.WARNING);
							alert.initOwner(getStage());
							alert.setTitle("La ruta no es valida");
							alert.setHeaderText("La ruta ATC no es valida!");
							alert.setContentText("La aerovía " + parte + " no existe.");
							alert.showAndWait();
						}
						return false;
					} else if (puntoAnterior != null && !RutaUtils.aeroviaContienePunto(aeroviaActual, puntoAnterior)) {
						if (mostrarErrores) {
							Alert alert = new Alert(AlertType.WARNING);
							alert.initOwner(getStage());
							alert.setTitle("La ruta no es valida");
							alert.setHeaderText("La ruta ATC no es valida!");
							alert.setContentText("La aerovía " + aeroviaActual.getIdentificador()
									+ " no contiene el punto " + puntoAnterior.getIdentificador() + ".");
							alert.showAndWait();
						}
						return false;
					}
				}
				estado = 1;
			} else if (estado == 1) {
				Punto punto = RutaUtils.obtenerPunto(parte);
				if (punto == null) {
					if (mostrarErrores) {
						Alert alert = new Alert(AlertType.WARNING);
						alert.initOwner(getStage());
						alert.setTitle("La ruta no es valida");
						alert.setHeaderText("La ruta ATC no es valida!");
						alert.setContentText("El punto " + parte + " no existe.");
						alert.showAndWait();
					}
					return false;
				} else if (aeroviaActual != null) {
					if (!RutaUtils.aeroviaContienePunto(aeroviaActual, punto)) {
						if (mostrarErrores) {
							Alert alert = new Alert(AlertType.WARNING);
							alert.initOwner(getStage());
							alert.setTitle("La ruta no es valida");
							alert.setHeaderText("La ruta ATC no es valida!");
							alert.setContentText("La aerovía " + aeroviaActual.getIdentificador()
									+ " no contiene el punto " + punto.getIdentificador() + ".");
							alert.showAndWait();
						}
						return false;
					}
				}
				puntoAnterior = punto;
				estado = 0;
			}
		}

		mapa.establecerRuta(aeropuertoOrigen, obtenerRuta().keySet(), aeropuertoDestino);
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
