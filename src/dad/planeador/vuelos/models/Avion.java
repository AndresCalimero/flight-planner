package dad.planeador.vuelos.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Avion {
	
	private final StringProperty numeroRegistro = new SimpleStringProperty();
	private final ObjectProperty<TipoAvion> tipoAvion = new SimpleObjectProperty<>();
	private final LongProperty dow = new SimpleLongProperty();
	private final LongProperty mzfw = new SimpleLongProperty();
	private final LongProperty mtow = new SimpleLongProperty();
	private final LongProperty mlw = new SimpleLongProperty();
	private final IntegerProperty pasajeros = new SimpleIntegerProperty();
	private final LongProperty carga = new SimpleLongProperty();
	private final LongProperty combustible = new SimpleLongProperty();
	private final IntegerProperty consumoAPUHora = new SimpleIntegerProperty();
	
	public final StringProperty numeroRegistroProperty() {
		return this.numeroRegistro;
	}
	
	public final java.lang.String getNumeroRegistro() {
		return this.numeroRegistroProperty().get();
	}
	
	public final void setNumeroRegistro(final java.lang.String numeroRegistro) {
		this.numeroRegistroProperty().set(numeroRegistro);
	}
	
	public final ObjectProperty<TipoAvion> tipoAvionProperty() {
		return this.tipoAvion;
	}
	
	public final dad.planeador.vuelos.models.TipoAvion getTipoAvion() {
		return this.tipoAvionProperty().get();
	}
	
	public final void setTipoAvion(final dad.planeador.vuelos.models.TipoAvion tipoAvion) {
		this.tipoAvionProperty().set(tipoAvion);
	}
	
	public final LongProperty dowProperty() {
		return this.dow;
	}
	
	public final long getDow() {
		return this.dowProperty().get();
	}
	
	public final void setDow(final long dow) {
		this.dowProperty().set(dow);
	}
	
	public final LongProperty mzfwProperty() {
		return this.mzfw;
	}
	
	public final long getMzfw() {
		return this.mzfwProperty().get();
	}
	
	public final void setMzfw(final long mzfw) {
		this.mzfwProperty().set(mzfw);
	}
	
	public final LongProperty mtowProperty() {
		return this.mtow;
	}
	
	public final long getMtow() {
		return this.mtowProperty().get();
	}
	
	public final void setMtow(final long mtow) {
		this.mtowProperty().set(mtow);
	}
	
	public final LongProperty mlwProperty() {
		return this.mlw;
	}
	
	public final long getMlw() {
		return this.mlwProperty().get();
	}
	
	public final void setMlw(final long mlw) {
		this.mlwProperty().set(mlw);
	}
	
	public final IntegerProperty pasajerosProperty() {
		return this.pasajeros;
	}
	
	public final int getPasajeros() {
		return this.pasajerosProperty().get();
	}
	
	public final void setPasajeros(final int pasajeros) {
		this.pasajerosProperty().set(pasajeros);
	}
	
	public final LongProperty cargaProperty() {
		return this.carga;
	}
	
	public final long getCarga() {
		return this.cargaProperty().get();
	}
	
	public final void setCarga(final long carga) {
		this.cargaProperty().set(carga);
	}
	
	public final LongProperty combustibleProperty() {
		return this.combustible;
	}
	
	public final long getCombustible() {
		return this.combustibleProperty().get();
	}
	
	public final void setCombustible(final long combustible) {
		this.combustibleProperty().set(combustible);
	}
	
	public final IntegerProperty consumoAPUHoraProperty() {
		return this.consumoAPUHora;
	}
	
	public final int getConsumoAPUHora() {
		return this.consumoAPUHoraProperty().get();
	}
	
	public final void setConsumoAPUHora(final int consumoAPUHora) {
		this.consumoAPUHoraProperty().set(consumoAPUHora);
	}
	
	@Override
	public String toString() {
		return getNumeroRegistro();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getNumeroRegistro() == null) ? 0 : getNumeroRegistro().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Avion))
			return false;
		Avion other = (Avion) obj;
		if (getNumeroRegistro() == null) {
			if (other.getNumeroRegistro() != null)
				return false;
		} else if (!getNumeroRegistro().equals(other.getNumeroRegistro()))
			return false;
		return true;
	}
}
