package dad.planeador.vuelos.models;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

public class Aerovia {
	
	private final StringProperty identificador = new SimpleStringProperty();
	private final ListProperty<Punto> puntos = new SimpleListProperty<>(FXCollections.observableArrayList());
	
	public final StringProperty identificadorProperty() {
		return this.identificador;
	}
	
	public final java.lang.String getIdentificador() {
		return this.identificadorProperty().get();
	}
	
	public final void setIdentificador(final java.lang.String identificador) {
		this.identificadorProperty().set(identificador);
	}
	
	public final ListProperty<Punto> puntosProperty() {
		return this.puntos;
	}
	
	public final javafx.collections.ObservableList<dad.planeador.vuelos.models.Punto> getPuntos() {
		return this.puntosProperty().get();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getIdentificador() == null) ? 0 : getIdentificador().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Aerovia))
			return false;
		Aerovia other = (Aerovia) obj;
		if (getIdentificador() == null) {
			if (other.getIdentificador() != null)
				return false;
		} else if (!getIdentificador().equals(other.getIdentificador()))
			return false;
		return true;
	}
}
