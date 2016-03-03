package dad.planeador.vuelos.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class Punto {
	
	public static final int TIPO_INTERSECCION = 0;
	public static final int TIPO_NAVAID = 1;
	
	private final LongProperty codigo = new SimpleLongProperty();
	private final StringProperty identificador = new SimpleStringProperty();
	private final ObjectProperty<Coordenadas> coordenadas = new SimpleObjectProperty<>(new Coordenadas());
	private final IntegerProperty tipo = new SimpleIntegerProperty();
	
	public final LongProperty codigoProperty() {
		return this.codigo;
	}
	
	public final long getCodigo() {
		return this.codigoProperty().get();
	}
	
	public final void setCodigo(final long codigo) {
		this.codigoProperty().set(codigo);
	}
	
	public final StringProperty identificadorProperty() {
		return this.identificador;
	}
	
	public final java.lang.String getIdentificador() {
		return this.identificadorProperty().get();
	}
	
	public final void setIdentificador(final java.lang.String identificador) {
		this.identificadorProperty().set(identificador);
	}
	
	public final ObjectProperty<Coordenadas> coordenadasProperty() {
		return this.coordenadas;
	}
	
	public final dad.planeador.vuelos.models.Coordenadas getCoordenadas() {
		return this.coordenadasProperty().get();
	}
	
	public final IntegerProperty tipoProperty() {
		return this.tipo;
	}
	

	public final int getTipo() {
		return this.tipoProperty().get();
	}
	

	public final void setTipo(final int tipo) {
		this.tipoProperty().set(tipo);
	}
	
	public static int generateHashCode(long codigo, int tipo) {
		if (tipo == TIPO_INTERSECCION) {
			return (int) codigo;
		} else {
			return -1 * (int) codigo;
		}
	}

	@Override
	public int hashCode() {
		return Punto.generateHashCode(getCodigo(), getTipo());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Punto))
			return false;
		Punto other = (Punto) obj;
		if (getCoordenadas() == null) {
			if (other.getCoordenadas() != null)
				return false;
		} else if (!getCoordenadas().equals(other.getCoordenadas()))
			return false;
		if (getIdentificador() == null) {
			if (other.getIdentificador() != null)
				return false;
		} else if (!getIdentificador().equals(other.getIdentificador()))
			return false;
		return true;
	}
}
