package dad.planeador.vuelos.models;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TipoAvion {
	
	private final LongProperty codigo = new SimpleLongProperty();
	private final StringProperty tipo = new SimpleStringProperty();
	
	public final LongProperty codigoProperty() {
		return this.codigo;
	}
	
	public final long getCodigo() {
		return this.codigoProperty().get();
	}
	
	public final void setCodigo(final long codigo) {
		this.codigoProperty().set(codigo);
	}
	
	public final StringProperty tipoProperty() {
		return this.tipo;
	}
	
	public final java.lang.String getTipo() {
		return this.tipoProperty().get();
	}
	
	public final void setTipo(final java.lang.String tipo) {
		this.tipoProperty().set(tipo);
	}
	
	@Override
	public String toString() {
		return getTipo();
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
		if (!(obj instanceof TipoAvion))
			return false;
		TipoAvion other = (TipoAvion) obj;
		if (other.getCodigo() != getCodigo())
			return false;
		return true;
	}
}
