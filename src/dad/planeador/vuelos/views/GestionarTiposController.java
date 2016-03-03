package dad.planeador.vuelos.views;

import java.util.Optional;

import dad.planeador.vuelos.models.TipoAvion;
import dad.planeador.vuelos.services.PlaneadorVuelosService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class GestionarTiposController extends Controller {
	
	@FXML
	ListView<TipoAvion> tiposListView;

	@FXML
	TextField buscarTextField;
	@FXML
	Button editarButton;
	@FXML
	Button eliminarButton;
	
	private TipoAvion tipoSeleccionado;

	@Override
	protected void loaded() {
		tiposListView.setItems(PlaneadorVuelosService.getListaTiposAvion());
		buscarTextField.textProperty().addListener((obs, oldValue, newValue) -> onBuscarTextFieldTextChanged(newValue));
		tiposListView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> onTiposListViewSelectionChanged(newValue));
	}
	
	@FXML
	private void onNuevoButtonActionPerformed() {
		getMainApp().showNuevoTipo();
	}
	
	@FXML
	private void onEditarButtonActionPerformed() {
		getMainApp().showEditarTipo(tipoSeleccionado);
		tiposListView.refresh();
	}
	
	@FXML
	private void onEliminarButtonActionPerformed() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.initOwner(getStage());
		alert.setTitle("Confirmación");
		alert.setHeaderText("¿Esta seguro de que desea eliminar el tipo de avión " + tipoSeleccionado.getTipo() + "?");
		alert.setContentText("Esta operación no se podrá deshacer, se eliminaran todos los aviones de ese tipo.");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			PlaneadorVuelosService.eliminarTipoAvion(tipoSeleccionado);
		}
	}
	
	private void onTiposListViewSelectionChanged(TipoAvion seleccionado) {
		if (tipoSeleccionado == null && seleccionado != null) {
			editarButton.setDisable(false);
			eliminarButton.setDisable(false);
		} else if (seleccionado == null) {
			editarButton.setDisable(true);
			eliminarButton.setDisable(true);
		}
		this.tipoSeleccionado = seleccionado;
	}

	private void onBuscarTextFieldTextChanged(String newValue) {
		filtrarTipos(newValue);
	}

	private void filtrarTipos(String newValue) {
		if (newValue.trim().length() == 0) {
			tiposListView.setItems(PlaneadorVuelosService.getListaTiposAvion());
		} else {
			newValue = newValue.toUpperCase();
			ObservableList<TipoAvion> tipos = FXCollections.observableArrayList();
			for (TipoAvion tipo : PlaneadorVuelosService.getListaTiposAvion()) {
				if (tipo.getTipo().toUpperCase().contains(newValue)) {
					tipos.add(tipo);
				}
			}
			tiposListView.setItems(tipos);
		}
	}
}
