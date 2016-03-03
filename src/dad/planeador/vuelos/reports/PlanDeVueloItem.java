package dad.planeador.vuelos.reports;

import java.util.List;

import dad.planeador.vuelos.models.Aeropuerto;
import dad.planeador.vuelos.models.Avion;
import dad.planeador.vuelos.models.Pista;
import dad.planeador.vuelos.models.PlanDeVuelo;

public class PlanDeVueloItem {

	private Avion avion;
	private Aeropuerto aeropuertoOrigen;
	private Aeropuerto aeropuertoDestino;
	private int altitudCrucero;
	private String rutaATC;
	private List<PuntoItem> puntos;
	private String numeroVuelo;
	private Pista pistaDespegue;
	private Pista pistaAterrizaje;
	private String observacionesAvion;
	private int numeroAdultos;
	private int numeroChildren;
	private int numeroBebes;
	private int equipaje;
	private int carga;

	public Avion getAvion() {
		return avion;
	}

	public void setAvion(Avion avion) {
		this.avion = avion;
	}

	public Aeropuerto getAeropuertoOrigen() {
		return aeropuertoOrigen;
	}

	public void setAeropuertoOrigen(Aeropuerto aeropuertoOrigen) {
		this.aeropuertoOrigen = aeropuertoOrigen;
	}

	public Aeropuerto getAeropuertoDestino() {
		return aeropuertoDestino;
	}

	public void setAeropuertoDestino(Aeropuerto aeropuertoDestino) {
		this.aeropuertoDestino = aeropuertoDestino;
	}

	public int getAltitudCrucero() {
		return altitudCrucero;
	}

	public void setAltitudCrucero(int altitudCrucero) {
		this.altitudCrucero = altitudCrucero;
	}

	public String getRutaATC() {
		return rutaATC;
	}

	public void setRutaATC(String rutaATC) {
		this.rutaATC = rutaATC;
	}

	public String getNumeroVuelo() {
		return numeroVuelo;
	}

	public void setNumeroVuelo(String numeroVuelo) {
		this.numeroVuelo = numeroVuelo;
	}

	public Pista getPistaDespegue() {
		return pistaDespegue;
	}

	public void setPistaDespegue(Pista pistaDespegue) {
		this.pistaDespegue = pistaDespegue;
	}

	public Pista getPistaAterrizaje() {
		return pistaAterrizaje;
	}

	public void setPistaAterrizaje(Pista pistaAterrizaje) {
		this.pistaAterrizaje = pistaAterrizaje;
	}

	public String getObservacionesAvion() {
		return observacionesAvion;
	}

	public void setObservacionesAvion(String observacionesAvion) {
		this.observacionesAvion = observacionesAvion;
	}

	public int getNumeroAdultos() {
		return numeroAdultos;
	}

	public void setNumeroAdultos(int numeroAdultos) {
		this.numeroAdultos = numeroAdultos;
	}

	public int getNumeroChildren() {
		return numeroChildren;
	}

	public void setNumeroChildren(int numeroChildren) {
		this.numeroChildren = numeroChildren;
	}

	public int getNumeroBebes() {
		return numeroBebes;
	}

	public void setNumeroBebes(int numeroBebes) {
		this.numeroBebes = numeroBebes;
	}

	public int getEquipaje() {
		return equipaje;
	}

	public void setEquipaje(int equipaje) {
		this.equipaje = equipaje;
	}

	public int getCarga() {
		return carga;
	}

	public void setCarga(int carga) {
		this.carga = carga;
	}

	public List<PuntoItem> getPuntos() {
		return puntos;
	}

	public void setPuntos(List<PuntoItem> puntos) {
		this.puntos = puntos;
	}
	
	public void cargarPlanDeVuelo(PlanDeVuelo plan) {
		setAeropuertoDestino(plan.getAeropuertoDestino());
		setAeropuertoOrigen(plan.getAeropuertoOrigen());
		setAltitudCrucero(plan.getAltitudCrucero());
		setAvion(plan.getAvion());
		setCarga(plan.getCarga());
		setEquipaje(plan.getEquipaje());
		setNumeroAdultos(plan.getNumeroAdultos());
		setNumeroBebes(plan.getNumeroBebes());
		setNumeroChildren(plan.getNumeroChildren());
		setNumeroVuelo(plan.getNumeroVuelo());
		setObservacionesAvion(plan.getObservacionesAvion());
		setPistaAterrizaje(plan.getPistaAterrizaje());
		setPistaDespegue(plan.getPistaDespegue());
		setRutaATC(plan.getRutaATC());
	}
}
