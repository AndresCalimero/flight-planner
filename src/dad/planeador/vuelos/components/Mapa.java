package dad.planeador.vuelos.components;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import dad.planeador.vuelos.MainApp;
import dad.planeador.vuelos.models.Aeropuerto;
import dad.planeador.vuelos.models.Coordenadas;
import dad.planeador.vuelos.models.Interseccion;
import dad.planeador.vuelos.models.Localizable;
import dad.planeador.vuelos.models.Navaid;
import dad.planeador.vuelos.models.Punto;
import dad.planeador.vuelos.views.RootController.Puente;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import netscape.javascript.JSObject;

public class Mapa extends BorderPane {

	@FXML
	private ToggleButton aeropuertosToggleButton;
	@FXML
	private ToggleButton interseccionesToggleButton;
	@FXML
	private ToggleButton navaidsToggleButton;
	@FXML
	private WebView webView;

	private WebEngine webEngine;
	private Callback<Boolean, ?> callback;
	private ChangeListener<State> changeListener;

	public Mapa() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Mapa.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
			inicializar();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void inicializar() {
		aeropuertosToggleButton.selectedProperty().addListener((obs, oldValue, newValue) -> {
			onAeropuertosToggleButtonSelectedChanged(newValue);
		});
		interseccionesToggleButton.selectedProperty().addListener((obs, oldValue, newValue) -> {
			onInterseccionesToggleButtonSelectedChanged(newValue);
		});
		navaidsToggleButton.selectedProperty().addListener((obs, oldValue, newValue) -> {
			onNavaidsToggleButtonSelectedChanged(newValue);
		});
		changeListener = new ChangeListener<State>() {
			@Override
			public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {
				if (newValue == State.SUCCEEDED) {
					webView.getEngine().getLoadWorker().stateProperty().removeListener(this);
					callback.call(true);
				} else if (newValue == State.CANCELLED || newValue == State.FAILED) {
					webView.getEngine().getLoadWorker().stateProperty().removeListener(this);
					callback.call(false);
				}
			}
		};

		// DEBUG
		webView.getEngine().setOnAlert(new EventHandler<WebEvent<String>>() {
			@Override
			public void handle(WebEvent<String> event) {
				System.out.println(event.getData());
			}
		});
	}

	private void onAeropuertosToggleButtonSelectedChanged(boolean newValue) {
		if (webEngine != null) {
			webEngine.executeScript("document.showAirports(" + newValue + ")");
		}
	}

	private void onInterseccionesToggleButtonSelectedChanged(boolean newValue) {
		if (webEngine != null) {
			webEngine.executeScript("document.showIntersections(" + newValue + ")");
		}
	}

	private void onNavaidsToggleButtonSelectedChanged(boolean newValue) {
		if (webEngine != null) {
			webEngine.executeScript("document.showNavaids(" + newValue + ")");
		}
	}

	@FXML
	private void onAumentarZoomButtonActionPerformed() {
		if (webEngine != null) {
			webEngine.executeScript("document.zoomIn()");
		}
	}

	@FXML
	private void onAlejarZoomButtonActionPerformed() {
		if (webEngine != null) {
			webEngine.executeScript("document.zoomOut()");
		}
	}

	public void setPuente(Puente puente) {
		if (webEngine != null) {
			JSObject jso = (JSObject) webEngine.executeScript("window");
			jso.setMember("java", puente);
		}
	}

	public void cargarMapa(Callback<Boolean, ?> callback) {
		aeropuertosToggleButton.setSelected(false);
		interseccionesToggleButton.setSelected(false);
		navaidsToggleButton.setSelected(false);

		WebEngine we = webView.getEngine();
		we.getLoadWorker().stateProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue == State.SUCCEEDED) {
				webEngine = we;
			}
		});
		if (callback != null) {
			this.callback = callback;
			we.getLoadWorker().stateProperty().addListener(changeListener);
		}
		we.load(MainApp.class.getResource("web/map.html").toString());
	}

	public void cargarMapa() {
		cargarMapa(null);
	}

	public void establecerRuta(Aeropuerto origen, Collection<Punto> puntos, Aeropuerto destino) {
		if (webEngine != null) {
			StringBuilder ruta = new StringBuilder();
			if (origen != null) {
				ruta.append(obtenerLatLng(origen) + ",");
			}

			for (Punto p : puntos) {
				ruta.append(obtenerLatLng(p) + ",");
			}

			if (destino != null) {
				ruta.append(obtenerLatLng(destino));
			} else {
				ruta.delete(ruta.length() - 1, ruta.length());
			}

			webEngine.executeScript("document.setRoute([" + ruta.toString() + "])");
		}
	}

	public void limpiarRuta() {
		if (webEngine != null) {
			webEngine.executeScript("document.setRoute(0)");
		}
	}

	private String obtenerLatLng(Localizable localizable) {
		Coordenadas c = localizable.getCoordenadas();
		return "{lat: " + c.getGradosNorte() + ", lng: " + c.getGradosEste() + "}";
	}

	public void agregarAeropuertos(List<Aeropuerto> aeropuertos) {
		if (webEngine != null) {
			for (Aeropuerto aeropuerto : aeropuertos) {
				String parametros = "'" + aeropuerto.getIcao() + "', " + "'" + aeropuerto.getNombre() + "', "
						+ aeropuerto.getCoordenadas().getGradosNorte() + ", "
						+ aeropuerto.getCoordenadas().getGradosEste();
				webEngine.executeScript("document.addAirport(" + parametros + ")");
			}
		}
	}

	public void agregarIntersecciones(List<Interseccion> intersecciones) {
		if (webEngine != null) {
			for (Interseccion interseccion : intersecciones) {
				String parametros = "'" + interseccion.getIdentificador() + "', "
						+ interseccion.getCoordenadas().getGradosNorte() + ", "
						+ interseccion.getCoordenadas().getGradosEste();
				webEngine.executeScript("document.addIntersection(" + parametros + ")");
			}
		}
	}

	public void agregarNavaids(List<Navaid> navaids) {
		if (webEngine != null) {
			for (Navaid navaid : navaids) {
				String parametros = "'" + navaid.getIdentificador() + "', '" + navaid.getNombre() + "', '"
						+ navaid.getTipoNavaid().getTipo() + "', " + navaid.getCoordenadas().getGradosNorte() + ", "
						+ navaid.getCoordenadas().getGradosEste();
				webEngine.executeScript("document.addNavaid(" + parametros + ")");
			}
		}
	}
	
	public void moverMapa(Coordenadas coor, int zoom) {
		if (webEngine != null) {
			webEngine.executeScript("document.goToLocation({lat: " + coor.getGradosNorte() + ", lng: " + coor.getGradosEste() + "}, " + zoom + ")");
		}
	}

	public WebEngine getWebEngine() {
		return webView.getEngine();
	}
}
