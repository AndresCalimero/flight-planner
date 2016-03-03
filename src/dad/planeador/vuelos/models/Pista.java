package dad.planeador.vuelos.models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Pista {
	
	private final StringProperty icao = new SimpleStringProperty();
	private final StringProperty pista = new SimpleStringProperty();
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
	
	public final StringProperty pistaProperty() {
		return this.pista;
	}
	
	public final java.lang.String getPista() {
		return this.pistaProperty().get();
	}
	
	public final void setPista(final java.lang.String pista) {
		this.pistaProperty().set(pista);
	}
	
	public final ObjectProperty<Coordenadas> coordenadasProperty() {
		return this.coordenadas;
	}
	
	public final dad.planeador.vuelos.models.Coordenadas getCoordenadas() {
		return this.coordenadasProperty().get();
	}
	
	@Override
	public String toString() {
		return getPista();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getIcao() == null) ? 0 : getIcao().hashCode());
		result = prime * result + ((getPista() == null) ? 0 : getPista().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Pista))
			return false;
		Pista other = (Pista) obj;
		if (getIcao() == null) {
			if (other.getIcao() != null)
				return false;
		} else if (!getIcao().equals(other.getIcao()))
			return false;
		if (getPista() == null) {
			if (other.getPista() != null)
				return false;
		} else if (!getPista().equals(other.getPista()))
			return false;
		return true;
	}
}
