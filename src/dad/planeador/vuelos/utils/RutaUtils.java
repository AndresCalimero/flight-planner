package dad.planeador.vuelos.utils;

import dad.planeador.vuelos.models.Aerovia;
import dad.planeador.vuelos.models.Interseccion;
import dad.planeador.vuelos.models.Navaid;
import dad.planeador.vuelos.models.Punto;
import dad.planeador.vuelos.services.PlaneadorVuelosService;

public class RutaUtils {

	public static Aerovia obtenerAerovia(String aerovia) {
		for (Aerovia a : PlaneadorVuelosService.getListaAerovias()) {
			if (a.getIdentificador().equalsIgnoreCase(aerovia)) {
				return a;
			}
		}
		return null;
	}
	
	public static Punto obtenerPunto(String punto) {
		for (Interseccion i : PlaneadorVuelosService.getListaIntersecciones()) {
			if (i.getIdentificador().equalsIgnoreCase(punto)) {
				return i;
			}
		}
		
		for (Navaid n : PlaneadorVuelosService.getListaNavaids()) {
			if (n.getIdentificador().equalsIgnoreCase(punto)) {
				return n;
			}
		}
		
		return null;
	}
	
	public static boolean aeroviaContienePunto(Aerovia aerovia, Punto punto) {
		for (Punto p : aerovia.getPuntos()) {
			if (p.getIdentificador().equalsIgnoreCase(punto.getIdentificador())) {
				return true;
			}
		}
		return false;
	}
}
