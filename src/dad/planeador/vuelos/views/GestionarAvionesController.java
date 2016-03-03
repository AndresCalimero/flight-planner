package dad.planeador.vuelos.views;

import java.util.Optional;

import dad.planeador.vuelos.models.Avion;
import dad.planeador.vuelos.models.TipoAvion;
import dad.planeador.vuelos.services.PlaneadorVuelosService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class GestionarAvionesController extends Controller {
	
	@FXML
	TableView<Avion> avionesTableView;
	@FXML
	TableColumn<Avion, String> numeroRegistroTableColumn;
	@FXML
	TableColumn<Avion, TipoAvion> tipoTableColumn;
	@FXML
	TableColumn<Avion, Number> maxPaxTableColumn;
	@FXML
	TextField buscarTextField;
	@FXML
	Button editarButton;
	@FXML
	Button eliminarButton;
	
	private Avion avionSeleccionado;

	@Override
	protected void loaded() {
		numeroRegistroTableColumn.setCellValueFactory(cellData -> cellData.getValue().numeroRegistroProperty());
		tipoTableColumn.setCellValueFactory(cellData -> cellData.getValue().tipoAvionProperty());
		maxPaxTableColumn.setCellValueFactory(cellData -> cellData.getValue().pasajerosProperty());

		avionesTableView.setItems(PlaneadorVuelosService.getListaAviones());
		avionesTableView.getSortOrder().add(numeroRegistroTableColumn);
		
		buscarTextField.textProperty().addListener((obs, oldValue, newValue) -> onBuscarTextFieldTextChanged(newValue));
		
		avionesTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> onAvionesTableViewSelectionChanged(newValue));
	}
	
	@FXML
	private void onTiposButtonActionPerformed() {
		getMainApp().showGestionTipos();
		avionesTableView.refresh();
	}
	
	@FXML
	private void onNuevoButtonActionPerformed() {
		getMainApp().showNuevoAvion();
	}
	
	@FXML
	private void onEditarButtonActionPerformed() {
		getMainApp().showEditarAvion(avionSeleccionado);
	}
	
	@FXML
	private void onEliminarButtonActionPerformed() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.initOwner(getStage());
		alert.setTitle("Confirmación");
		alert.setHeaderText("¿Esta seguro de que desea eliminar el avión " + avionSeleccionado.getNumeroRegistro() + "?");
		alert.setContentText("Esta operación no se podrá deshacer, se actualizaran todos los planes de vuelo que usen ese avión.");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			PlaneadorVuelosService.eliminarAvion(avionSeleccionado);
		}
	}
	
	private void onAvionesTableViewSelectionChanged(Avion seleccionado) {
		if (avionSeleccionado == null && seleccionado != null) {
			editarButton.setDisable(false);
			eliminarButton.setDisable(false);
		} else if (seleccionado == null) {
			editarButton.setDisable(true);
			eliminarButton.setDisable(true);
		}
		this.avionSeleccionado = seleccionado;
	}

	private void onBuscarTextFieldTextChanged(String newValue) {
		if (newValue.trim().length() == 0) {
			avionesTableView.setItems(PlaneadorVuelosService.getListaAviones());
		} else {
			newValue = newValue.toUpperCase();
			ObservableList<Avion> aviones = FXCollections.observableArrayList();
			for (Avion avion : PlaneadorVuelosService.getListaAviones()) {
				if (avion.getNumeroRegistro().toUpperCase().contains(newValue) || (avion.getTipoAvion() != null
						&& avion.getTipoAvion().getTipo().toUpperCase().contains(newValue))) {
					aviones.add(avion);
				}
			}
			avionesTableView.setItems(aviones);
		}
	}
}
