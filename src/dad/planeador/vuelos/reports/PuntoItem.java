package dad.planeador.vuelos.reports;

import dad.planeador.vuelos.models.Aeropuerto;
import dad.planeador.vuelos.models.Aerovia;
import dad.planeador.vuelos.models.Interseccion;
import dad.planeador.vuelos.models.Navaid;

public class PuntoItem {
	
	private String aerovia;
	private String tipo;
	private String identificador;
	private String nombre;
	private String frecuencia;
	private String coordenadas;
	
	public String getAerovia() {
		return aerovia;
	}
	
	public void setAerovia(String aerovia) {
		this.aerovia = aerovia;
	}
	
	public String getTipo() {
		return tipo;
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public String getIdentificador() {
		return identificador;
	}
	
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getFrecuencia() {
		return frecuencia;
	}
	
	public void setFrecuencia(String frecuencia) {
		this.frecuencia = frecuencia;
	}
	
	public String getCoordenadas() {
		return coordenadas;
	}
	
	public void setCoordenadas(String coordenadas) {
		this.coordenadas = coordenadas;
	}
	
	public void cargarAeropuerto(Aeropuerto punto, boolean destino) {
		if (destino) {
			setAerovia("DCT");
		}
		setTipo("AEROPUERTO");
		setIdentificador(punto.getIcao());
		setNombre(punto.getNombre());
		setCoordenadas(punto.getCoordenadas().toString());
	}
	
	public void cargarInterseccion(Interseccion punto, Aerovia aerovia) {
		setAerovia(aerovia == null ? "DCT" : aerovia.getIdentificador());
		setTipo("INTERSEC.");
		setIdentificador(punto.getIdentificador());
		setCoordenadas(punto.getCoordenadas().toString());
	}
	
	public void cargarNavaid(Navaid punto, Aerovia aerovia) {
		setAerovia(aerovia == null ? "DCT" : aerovia.getIdentificador());
		setTipo(punto.getTipoNavaid().getTipo().toUpperCase());
		setIdentificador(punto.getIdentificador());
		setNombre(punto.getNombre());
		setFrecuencia(punto.getFrecuencia());
		setCoordenadas(punto.getCoordenadas().toString());
	}
}
