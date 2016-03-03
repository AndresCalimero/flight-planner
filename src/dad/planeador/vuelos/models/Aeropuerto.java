package dad.planeador.vuelos.models;

import java.util.Comparator;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

public class Aeropuerto {
	
	private final StringProperty icao = new SimpleStringProperty();
	private final StringProperty nombre = new SimpleStringProperty();
	private final ListProperty<Pista> pistas = new SimpleListProperty<>(FXCollections.observableArrayList());
	private final ObjectProperty<Coordenadas> coordenadas = new SimpleObjectProperty<>(new Coordenadas());
	

	public final StringProperty icaoProperty() {
		return this.icao;
	}

	public final java.lang.String getIcao() {
		return this.icaoProperty().get();
	}

	public final void setIcao(final java.lang.String icao) {
		this.icaoProperty().set(icao);
	}
	
	public final StringProperty nombreProperty() {
		return this.nombre;
	}
	
	public final java.lang.String getNombre() {
		return this.nombreProperty().get();
	}
	
	public final void setNombre(final java.lang.String nombre) {
		this.nombreProperty().set(nombre);
	}
	
	public final ListProperty<Pista> pistasProperty() {
		return this.pistas;
	}
	
	public final javafx.collections.ObservableList<dad.planeador.vuelos.models.Pista> getPistas() {
		return this.pistasProperty().get();
	}
	
	public final ObjectProperty<Coordenadas> coordenadasProperty() {
		return this.coordenadas;
	}
	
	public final dad.planeador.vuelos.models.Coordenadas getCoordenadas() {
		return this.coordenadasProperty().get();
	}
	
	@Override
	public String toString() {
		return getIcao();
	}
	
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getIcao() == null) ? 0 : getIcao().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Aeropuerto))
			return false;
		Aeropuerto other = (Aeropuerto) obj;
		if (getIcao() == null) {
			if (other.getIcao() != null)
				return false;
		} else if (!getIcao().equals(other.getIcao()))
			return false;
		return true;
	}

	public static class AeropuertoComparator implements Comparator<Aeropuerto> {
		@Override
		public int compare(Aeropuerto a1, Aeropuerto a2) {
			return a1.getIcao().compareTo(a2.getIcao());
		}
	}
}
