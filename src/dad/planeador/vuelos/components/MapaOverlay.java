package dad.planeador.vuelos.components;

import java.awt.Point;
import java.io.IOException;

import dad.planeador.vuelos.models.Aeropuerto;
import dad.planeador.vuelos.models.Coordenadas;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MapaOverlay extends Pane {

	@FXML
	private Group rootGroup;

	private Bounds mapBounds;
	private boolean aeropuertosVisibles = false;
	private ObservableList<Aeropuerto> aeropuertos = FXCollections.observableArrayList();

	public MapaOverlay() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MapaOverlay.fxml"));
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

	}

	private void reenderizarOverlay() {
		System.out.println("--------------------");
		rootGroup.getChildren().clear();
		reenderizarAeropuertos();
	}

	private void reenderizarAeropuertos() {
		if (aeropuertosVisibles && aeropuertos.size() > 0) {
			for (Aeropuerto aeropuerto : aeropuertos) {
//				if (aeropuerto.getIcao().equals("GCXO")) {
//					mapBounds.contiene(aeropuerto.getCoordenadas());
//				}
				if (mapBounds.contiene(aeropuerto.getCoordenadas())) {
					System.out.println("Aeropuerto a la vista: " + aeropuerto.getIcao());
					Point p = transformarCoordenadasAPixeles(aeropuerto.getCoordenadas());
					Rectangle r = new Rectangle(p.getX(), p.getY(), 5, 5);
					r.setFill(Color.RED);
					rootGroup.getChildren().add(r);
				}
			}
		}
	}
	
	public Point transformarCoordenadasAPixeles(Coordenadas coordenadas) {
		double latDiff = mapBounds.getNorthEastLat() - mapBounds.getSouthWestLat();
		double lngDiff = mapBounds.getNorthEastLng() - mapBounds.getSouthWestLng();
		
		System.out.println("latDiff: " + latDiff + " lngDiff: " + lngDiff);
		
		//System.out.println("gradosNorte: " + coordenadas.getGradosNorte() + " southWestLat: " + mapBounds.getSouthWestLat());
		double latRelativa = mapBounds.getNorthEastLat() - coordenadas.getGradosNorte();
		
		//System.out.println("gradosEste: " + coordenadas.getGradosEste() + " northEastLng: " + mapBounds.getNorthEastLng());
		double lngRelativa = coordenadas.getGradosEste() - mapBounds.getSouthWestLng();
		
		System.out.println("latRelativa: " + latRelativa + " lngRelativa: " + lngRelativa);
		
		double porcentajeX = (lngRelativa * 100) / lngDiff;
		double porcentajeY = (latRelativa * 100) / latDiff;
		
//		if (porcentajeX < 0) {
//			porcentajeX = porcentajeX * -1;
//		}
//		
//		if (porcentajeY < 0 ) {
//			porcentajeY = porcentajeY * -1;
//		}
		
		System.out.println("porcentajeX: " + porcentajeX + " porcentajeY: " + porcentajeY);
		
		double x = (porcentajeX * getWidth()) / 100;
		double y = (porcentajeY * getHeight()) / 100;
		
		System.out.println("x: " + x + " y: " + y);
		System.out.println("width: " + getWidth() + " height: " + getHeight());
		
		Point point = new Point();
		point.setLocation(x, y);
		return point;
	}

	public void establecerMapBounds(Bounds mapBounds) {
		this.mapBounds = mapBounds;
		reenderizarOverlay();

	}

	public void establecerAeropuertos(ObservableList<Aeropuerto> aeropuertos) {
		this.aeropuertos.clear();
		this.aeropuertos.addAll(aeropuertos);
	}

	public void setAeropuertosVisibles(boolean visibles) {
		if (aeropuertosVisibles != visibles) {
			aeropuertosVisibles = visibles;
			reenderizarOverlay();
		}
	}

	public static class Bounds {
		private double northEastLat;
		private double northEastLng;
		private double southWestLat;
		private double southWestLng;

		public Bounds(double northEastLat, double northEastLng, double southWestLat, double southWestLng) {
			this.northEastLat = northEastLat;
			this.northEastLng = northEastLng;
			this.southWestLat = southWestLat;
			this.southWestLng = southWestLng;
		}

		public double getNorthEastLat() {
			return northEastLat;
		}

		public void setNorthEastLat(double northEastLat) {
			this.northEastLat = northEastLat;
		}

		public double getNorthEastLng() {
			return northEastLng;
		}

		public void setNorthEastLng(double northEastLng) {
			this.northEastLng = northEastLng;
		}

		public double getSouthWestLat() {
			return southWestLat;
		}

		public void setSouthWestLat(double southWestLat) {
			this.southWestLat = southWestLat;
		}

		public double getSouthWestLng() {
			return southWestLng;
		}

		public void setSouthWestLng(double southWestLng) {
			this.southWestLng = southWestLng;
		}

		public boolean contiene(Coordenadas coordenadas) {
//			System.out.println("Coor. Norte: " + coordenadas.getGradosNorte());
//			System.out.println("Coor. Este: " + coordenadas.getGradosEste());
//			System.out.println("Bounds northEastLat: " + northEastLat + " northEastLng: " + northEastLng);
//			System.out.println("Bounds southWestLat: " + southWestLat + " southWestLng: " + southWestLng);
			return coordenadas.getGradosEste() < northEastLng && coordenadas.getGradosEste() > southWestLng
					&& coordenadas.getGradosNorte() < northEastLat && coordenadas.getGradosNorte() > southWestLat;
		}
	}
}
