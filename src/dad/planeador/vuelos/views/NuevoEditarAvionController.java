package dad.planeador.vuelos.views;

import java.util.List;

import dad.planeador.vuelos.components.NumberTextField;
import dad.planeador.vuelos.dialogs.ExceptionAlert;
import dad.planeador.vuelos.models.Avion;
import dad.planeador.vuelos.models.TipoAvion;
import dad.planeador.vuelos.services.PlaneadorVuelosService;
import dad.planeador.vuelos.services.PlaneadorVuelosServiceException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class NuevoEditarAvionController extends Controller {
	
	@FXML
	private TextField numeroRegistroTextField;
	@FXML
	private ComboBox<TipoAvion> tipoAvionComboBox;
	@FXML
	private NumberTextField pasajerosTextField;
	@FXML
	private NumberTextField cargaTextField;
	@FXML
	private NumberTextField combustibleTextField;
	@FXML
	private NumberTextField consumoAPUTextField;
	@FXML
	private NumberTextField DOWTextField;
	@FXML
	private NumberTextField MZFWTextField;
	@FXML
	private NumberTextField MTOWTextField;
	@FXML
	private NumberTextField MLWTextField;
	
	private Avion avion;

	@Override
	protected void loaded() {
		tipoAvionComboBox.setItems(PlaneadorVuelosService.getListaTiposAvion());
	}
	
	public void setAvion(Avion avion) {
		numeroRegistroTextField.setDisable(true);
		numeroRegistroTextField.setText(avion.getNumeroRegistro());
		tipoAvionComboBox.getSelectionModel().select(avion.getTipoAvion());
		pasajerosTextField.setText("" + avion.getPasajeros());
		cargaTextField.setText("" + avion.getCarga());
		combustibleTextField.setText("" + avion.getCombustible());
		consumoAPUTextField.setText("" + avion.getConsumoAPUHora());
		DOWTextField.setText("" + avion.getDow());
		MZFWTextField.setText("" + avion.getMzfw());
		MTOWTextField.setText("" + avion.getMtow());
		MLWTextField.setText("" + avion.getMlw());
		this.avion = avion;
	}
	
	private boolean existeNumeroRegistro(String numeroRegistro) {
		List<Avion> aviones = PlaneadorVuelosService.getListaAviones();
		for (Avion avion : aviones) {
			if (avion.getNumeroRegistro().equals(numeroRegistro)) {
				return true;
			}
		}
		
		return false;
	}
	
	@FXML
	private void onGuardarButtonActionPerformed() {
		StringBuilder errores = new StringBuilder();
		
		String numeroRegistro = numeroRegistroTextField.getText();
		TipoAvion tipo = tipoAvionComboBox.getSelectionModel().getSelectedItem();
		int pasajeros = 0, consumoAPU = 0;
		long dow = 0, mzfw = 0, mtow = 0, mlw = 0, carga = 0, combustible = 0;
		
		if (avion == null) {
			if (numeroRegistro.isEmpty()) {
				errores.append("El número de registro no puede estar vacio.\n");
			}
			if (existeNumeroRegistro(numeroRegistro)) {
				errores.append("Ya existe un avión con el numero de registro " + numeroRegistro + ".\n");
			}
		}
		
		if (tipo == null) {
			errores.append("El tipo de avión no puede estar vacio.\n");
		}
		
		try {
			pasajeros = Integer.parseInt(pasajerosTextField.getText());
		} catch(NumberFormatException e) {
			errores.append("La capacidad maxima de pasajeros no es valida.\n");
		}
		try {
			consumoAPU = Integer.parseInt(consumoAPUTextField.getText());
		} catch(NumberFormatException e) {
			errores.append("El consumo de combustible del APU por hora no es valido.\n");
		}
		try {
			dow = Long.parseLong(DOWTextField.getText());
		} catch(NumberFormatException e) {
			errores.append("El DOW no es valido.\n");
		}
		try {
			mzfw = Long.parseLong(MZFWTextField.getText());
		} catch(NumberFormatException e) {
			errores.append("El MZFW no es valido.\n");
		}
		try {
			mtow = Long.parseLong(MTOWTextField.getText());
		} catch(NumberFormatException e) {
			errores.append("El MTOW no es valido.\n");
		}
		try {
			mlw = Long.parseLong(MLWTextField.getText());
		} catch(NumberFormatException e) {
			errores.append("El MLW no es valido.\n");
		}
		try {
			carga = Long.parseLong(cargaTextField.getText());
		} catch(NumberFormatException e) {
			errores.append("La carga maxima no es valida.\n");
		}
		try {
			combustible = Long.parseLong(combustibleTextField.getText());
		} catch(NumberFormatException e) {
			errores.append("El combustible maximo no es valido.\n");
		}
		
		if (errores.length() == 0) {
			if (avion == null) {
				avion = new Avion();
				avion.setNumeroRegistro(numeroRegistro);
			}
			avion.setTipoAvion(tipo);
			avion.setCarga(carga);
			avion.setCombustible(combustible);
			avion.setConsumoAPUHora(consumoAPU);
			avion.setDow(dow);
			avion.setMlw(mlw);
			avion.setMtow(mtow);
			avion.setMzfw(mzfw);
			avion.setPasajeros(pasajeros);
			try {
				PlaneadorVuelosService.guardarAvion(avion);
			} catch (PlaneadorVuelosServiceException e) {
				ExceptionAlert alert = new ExceptionAlert(AlertType.ERROR);
				alert.setTitle("Error al intentar guardar el avión");
				alert.setHeaderText("Hubo un error al intentar guardar el avión!");
				alert.setContentText(e.getMessage());
				alert.setExpandableLabelText("El error fue:");
				alert.setException(e);
				alert.initOwner(getStage());
				alert.showAndWait();
			}
			getStage().close();
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(getStage());
			alert.setTitle("No se ha podido guardar");
			alert.setHeaderText("No se ha podido guardar el avión.\n\nPor favor, corrija los siguientes errores:");
			alert.setContentText(errores.toString());
			alert.showAndWait();
		}
		
	}

	@FXML
	private void onCancelarButtonActionPerformed() {
		getStage().close();
	}
}
