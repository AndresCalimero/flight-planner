package dad.planeador.vuelos.views;

import dad.planeador.vuelos.models.Aeropuerto;
import dad.planeador.vuelos.services.PlaneadorVuelosService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AeropuertoChooserController {

	@FXML
	private TableView<Aeropuerto> aeropuertosTableView;
	@FXML
	private TableColumn<Aeropuerto, String> icaoTableColumn;
	@FXML
	private TableColumn<Aeropuerto, String> nombreTableColumn;
	@FXML
	private TextField buscarTextField;

	private Stage stage;
	private Aeropuerto aeropuertoSeleccionado;

	@FXML
	private void initialize() {
		icaoTableColumn.setCellValueFactory(cellData -> cellData.getValue().icaoProperty());
		nombreTableColumn.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());

		aeropuertosTableView.setItems(PlaneadorVuelosService.getListaAeropuertos());
		aeropuertosTableView.getSortOrder().add(icaoTableColumn);

		buscarTextField.textProperty().addListener((obs, oldValue, newValue) -> onBuscarTextFieldTextChanged(newValue));
	}

	@FXML
	private void onSeleccionarAeropuertoButtonActionPerformed() {
		aeropuertoSeleccionado = aeropuertosTableView.getSelectionModel().getSelectedItem();
		if (aeropuertoSeleccionado != null) {
			stage.close();
		}
	}

	@FXML
	private void onCancelarButtonActionPerformed() {
		stage.close();
	}

	private void onBuscarTextFieldTextChanged(String newValue) {
		if (newValue.trim().length() == 0) {
			aeropuertosTableView.setItems(PlaneadorVuelosService.getListaAeropuertos());
		} else {
			newValue = newValue.toUpperCase();
			ObservableList<Aeropuerto> aeropuertos = FXCollections.observableArrayList();
			for (Aeropuerto aeropuerto : PlaneadorVuelosService.getListaAeropuertos()) {
				if (aeropuerto.getIcao().toUpperCase().contains(newValue) || (aeropuerto.getNombre() != null
						&& aeropuerto.getNombre().toUpperCase().contains(newValue))) {
					aeropuertos.add(aeropuerto);
				}
			}
			aeropuertosTableView.setItems(aeropuertos);
		}
	}

	public void seleccionarAeropuerto(Aeropuerto aeropuerto) {
		aeropuertosTableView.getSelectionModel().select(aeropuerto);
		Platform.runLater(() -> {
			aeropuertosTableView.scrollTo(aeropuerto);
		});
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Aeropuerto getAeropuertoSeleccionado() {
		return aeropuertoSeleccionado;
	}
}
