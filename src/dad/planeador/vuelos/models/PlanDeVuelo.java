package dad.planeador.vuelos.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PlanDeVuelo {
	
	private final LongProperty codigo = new SimpleLongProperty();
	private final ObjectProperty<Avion> avion = new SimpleObjectProperty<>();
	private final ObjectProperty<Aeropuerto> aeropuertoOrigen = new SimpleObjectProperty<>();
	private final ObjectProperty<Aeropuerto> aeropuertoDestino = new SimpleObjectProperty<>();
	private final IntegerProperty altitudCrucero = new SimpleIntegerProperty();
	private final StringProperty rutaATC = new SimpleStringProperty();
	private final StringProperty numeroVuelo = new SimpleStringProperty();
	private final ObjectProperty<Pista> pistaDespegue = new SimpleObjectProperty<>();
	private final ObjectProperty<Pista> pistaAterrizaje = new SimpleObjectProperty<>();
	private final StringProperty observacionesAvion = new SimpleStringProperty();
	private final IntegerProperty numeroAdultos = new SimpleIntegerProperty();
	private final IntegerProperty numeroChildren = new SimpleIntegerProperty();
	private final IntegerProperty numeroBebes = new SimpleIntegerProperty();
	private final IntegerProperty equipaje = new SimpleIntegerProperty();
	private final IntegerProperty carga = new SimpleIntegerProperty();
	
	public final LongProperty codigoProperty() {
		return this.codigo;
	}
	
	public final long getCodigo() {
		return this.codigoProperty().get();
	}
	
	public final void setCodigo(final long codigo) {
		this.codigoProperty().set(codigo);
	}
	
	public final ObjectProperty<Avion> avionProperty() {
		return this.avion;
	}
	
	public final dad.planeador.vuelos.models.Avion getAvion() {
		return this.avionProperty().get();
	}
	
	public final void setAvion(final dad.planeador.vuelos.models.Avion avion) {
		this.avionProperty().set(avion);
	}
	
	public final ObjectProperty<Aeropuerto> aeropuertoOrigenProperty() {
		return this.aeropuertoOrigen;
	}
	
	public final dad.planeador.vuelos.models.Aeropuerto getAeropuertoOrigen() {
		return this.aeropuertoOrigenProperty().get();
	}
	
	public final void setAeropuertoOrigen(final dad.planeador.vuelos.models.Aeropuerto aeropuertoOrigen) {
		this.aeropuertoOrigenProperty().set(aeropuertoOrigen);
	}
	
	public final ObjectProperty<Aeropuerto> aeropuertoDestinoProperty() {
		return this.aeropuertoDestino;
	}
	
	public final dad.planeador.vuelos.models.Aeropuerto getAeropuertoDestino() {
		return this.aeropuertoDestinoProperty().get();
	}
	
	public final void setAeropuertoDestino(final dad.planeador.vuelos.models.Aeropuerto aeropuertoDestino) {
		this.aeropuertoDestinoProperty().set(aeropuertoDestino);
	}
	
	public final IntegerProperty altitudCruceroProperty() {
		return this.altitudCrucero;
	}
	
	public final int getAltitudCrucero() {
		return this.altitudCruceroProperty().get();
	}
	
	public final void setAltitudCrucero(final int altitudCrucero) {
		this.altitudCruceroProperty().set(altitudCrucero);
	}
	
	public final StringProperty rutaATCProperty() {
		return this.rutaATC;
	}
	
	public final java.lang.String getRutaATC() {
		return this.rutaATCProperty().get();
	}
	
	public final void setRutaATC(final java.lang.String rutaATC) {
		this.rutaATCProperty().set(rutaATC);
	}

	public final StringProperty numeroVueloProperty() {
		return this.numeroVuelo;
	}
	

	public final java.lang.String getNumeroVuelo() {
		return this.numeroVueloProperty().get();
	}
	

	public final void setNumeroVuelo(final java.lang.String numeroVuelo) {
		this.numeroVueloProperty().set(numeroVuelo);
	}
	

	public final ObjectProperty<Pista> pistaDespegueProperty() {
		return this.pistaDespegue;
	}
	

	public final dad.planeador.vuelos.models.Pista getPistaDespegue() {
		return this.pistaDespegueProperty().get();
	}
	

	public final void setPistaDespegue(final dad.planeador.vuelos.models.Pista pistaDespegue) {
		this.pistaDespegueProperty().set(pistaDespegue);
	}
	

	public final ObjectProperty<Pista> pistaAterrizajeProperty() {
		return this.pistaAterrizaje;
	}
	

	public final dad.planeador.vuelos.models.Pista getPistaAterrizaje() {
		return this.pistaAterrizajeProperty().get();
	}
	

	public final void setPistaAterrizaje(final dad.planeador.vuelos.models.Pista pistaAterrizaje) {
		this.pistaAterrizajeProperty().set(pistaAterrizaje);
	}
	

	public final StringProperty observacionesAvionProperty() {
		return this.observacionesAvion;
	}
	

	public final java.lang.String getObservacionesAvion() {
		return this.observacionesAvionProperty().get();
	}
	

	public final void setObservacionesAvion(final java.lang.String observacionesAvion) {
		this.observacionesAvionProperty().set(observacionesAvion);
	}
	

	public final IntegerProperty numeroAdultosProperty() {
		return this.numeroAdultos;
	}
	

	public final int getNumeroAdultos() {
		return this.numeroAdultosProperty().get();
	}
	

	public final void setNumeroAdultos(final int numeroAdultos) {
		this.numeroAdultosProperty().set(numeroAdultos);
	}
	

	public final IntegerProperty numeroChildrenProperty() {
		return this.numeroChildren;
	}
	

	public final int getNumeroChildren() {
		return this.numeroChildrenProperty().get();
	}
	

	public final void setNumeroChildren(final int numeroChildren) {
		this.numeroChildrenProperty().set(numeroChildren);
	}
	

	public final IntegerProperty numeroBebesProperty() {
		return this.numeroBebes;
	}
	

	public final int getNumeroBebes() {
		return this.numeroBebesProperty().get();
	}
	

	public final void setNumeroBebes(final int numeroBebes) {
		this.numeroBebesProperty().set(numeroBebes);
	}
	

	public final IntegerProperty equipajeProperty() {
		return this.equipaje;
	}
	

	public final int getEquipaje() {
		return this.equipajeProperty().get();
	}
	

	public final void setEquipaje(final int equipaje) {
		this.equipajeProperty().set(equipaje);
	}
	

	public final IntegerProperty cargaProperty() {
		return this.carga;
	}
	

	public final int getCarga() {
		return this.cargaProperty().get();
	}
	

	public final void setCarga(final int carga) {
		this.cargaProperty().set(carga);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (int)(prime * result + getCodigo());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof PlanDeVuelo))
			return false;
		PlanDeVuelo other = (PlanDeVuelo) obj;
		if (other.getCodigo() != getCodigo())
			return false;
		return true;
	}
}