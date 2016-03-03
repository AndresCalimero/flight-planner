package dad.planeador.vuelos.views;

import dad.planeador.vuelos.models.Aeropuerto;
import dad.planeador.vuelos.models.Avion;
import dad.planeador.vuelos.models.PlanDeVuelo;
import dad.planeador.vuelos.services.PlaneadorVuelosService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PlanDeVueloChooserController {

	@FXML
	private TableView<PlanDeVuelo> planesDeVueloTableView;
	@FXML
	private TableColumn<PlanDeVuelo, String> numeroVueloTableColumn;
	@FXML
	private TableColumn<PlanDeVuelo, Avion> avionTableColumn;
	@FXML
	private TableColumn<PlanDeVuelo, Aeropuerto> origenTableColumn;
	@FXML
	private TableColumn<PlanDeVuelo, Aeropuerto> destinoTableColumn;
	@FXML
	private TextField buscarTextField;

	private Stage stage;
	private PlanDeVuelo planDeVueloSeleccionado;

	@FXML
	private void initialize() {
		numeroVueloTableColumn.setCellValueFactory(cellData -> cellData.getValue().numeroVueloProperty());
		avionTableColumn.setCellValueFactory(cellData -> cellData.getValue().avionProperty());
		origenTableColumn.setCellValueFactory(cellData -> cellData.getValue().aeropuertoOrigenProperty());
		destinoTableColumn.setCellValueFactory(cellData -> cellData.getValue().aeropuertoDestinoProperty());

		planesDeVueloTableView.setItems(PlaneadorVuelosService.getListaPlanesDeVuelo());
		planesDeVueloTableView.getSortOrder().add(numeroVueloTableColumn);

		buscarTextField.textProperty().addListener((obs, oldValue, newValue) -> onBuscarTextFieldTextChanged(newValue));
	}

	@FXML
	private void onAbrirButtonActionPerformed() {
		planDeVueloSeleccionado = planesDeVueloTableView.getSelectionModel().getSelectedItem();
		if (planDeVueloSeleccionado != null) {
			stage.close();
		}
	}

	@FXML
	private void onCancelarButtonActionPerformed() {
		stage.close();
	}

	private void onBuscarTextFieldTextChanged(String newValue) {
		if (newValue.trim().length() == 0) {
			planesDeVueloTableView.setItems(PlaneadorVuelosService.getListaPlanesDeVuelo());
		} else {
			newValue = newValue.toUpperCase();
			ObservableList<PlanDeVuelo> planesDeVuelo = FXCollections.observableArrayList();
			for (PlanDeVuelo planDeVuelo : PlaneadorVuelosService.getListaPlanesDeVuelo()) {
				if (planDeVuelo.getNumeroVuelo().toUpperCase().contains(newValue) || (planDeVuelo.getAvion() != null
						&& planDeVuelo.getAvion().getNumeroRegistro().toUpperCase().contains(newValue))) {
					planesDeVuelo.add(planDeVuelo);
				}
			}
			planesDeVueloTableView.setItems(planesDeVuelo);
		}
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public PlanDeVuelo getPlanDeVuelo() {
		return planDeVueloSeleccionado;
	}
}
