package dad.planeador.vuelos.utils;

import dad.planeador.vuelos.models.Aeropuerto;
import dad.planeador.vuelos.services.PlaneadorVuelosService;

public class AeropuertosUtils {
	
	public static Aeropuerto obtenerAeropuerto(String icao) {
		for (Aeropuerto a : PlaneadorVuelosService.getListaAeropuertos()) {
			if (a.getIcao().equalsIgnoreCase(icao)) {
				return a;
			}
		}
		return null;
	}

}
