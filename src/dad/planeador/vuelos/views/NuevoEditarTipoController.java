package dad.planeador.vuelos.views;

import dad.planeador.vuelos.dialogs.ExceptionAlert;
import dad.planeador.vuelos.models.TipoAvion;
import dad.planeador.vuelos.services.PlaneadorVuelosService;
import dad.planeador.vuelos.services.PlaneadorVuelosServiceException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class NuevoEditarTipoController extends Controller {
	
	@FXML
	private TextField tipoTextField;
	
	private TipoAvion tipo;

	@Override
	protected void loaded() {
	}
	
	public void setTipo(TipoAvion tipo) {
		tipoTextField.setText(tipo.getTipo());
		this.tipo = tipo;
	}
	
	@FXML
	private void onGuardarButtonActionPerformed() {
		StringBuilder errores = new StringBuilder();
		
		String tipoS = tipoTextField.getText();
		
		if (tipoS.isEmpty()) {
			errores.append("El tipo no puede estar vacio.\n");
		}
		
		if (errores.length() == 0) {
			if (tipo == null) {
				tipo = new TipoAvion();
			}
			tipo.setTipo(tipoS);
			try {
				PlaneadorVuelosService.guardarTipoAvion(tipo);
			} catch (PlaneadorVuelosServiceException e) {
				ExceptionAlert exAlert = new ExceptionAlert(AlertType.ERROR);
				exAlert.setTitle("Error al intentar guardar el tipo de avión");
				exAlert.setHeaderText("Hubo un error al intentar guardar el tipo de avión!");
				exAlert.setContentText(e.getMessage());
				exAlert.setExpandableLabelText("El error fue:");
				exAlert.setException(e);
				exAlert.initOwner(getStage());
				exAlert.showAndWait();
			}
			getStage().close();
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(getStage());
			alert.setTitle("No se ha podido guardar");
			alert.setHeaderText("No se ha podido guardar el tipo de avión.\n\nPor favor, corrija los siguientes errores:");
			alert.setContentText(errores.toString());
			alert.showAndWait();
		}
	}

	@FXML
	private void onCancelarButtonActionPerformed() {
		getStage().close();
	}
}
