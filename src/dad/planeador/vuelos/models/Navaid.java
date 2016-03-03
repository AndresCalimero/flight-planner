package dad.planeador.vuelos.models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Navaid extends Punto {
	
	private final ObjectProperty<TipoNavaid> tipoNavaid = new SimpleObjectProperty<>();
	private final StringProperty nombre = new SimpleStringProperty();
	private final StringProperty frecuencia = new SimpleStringProperty();
	
	public final ObjectProperty<TipoNavaid> tipoNavaidProperty() {
		return this.tipoNavaid;
	}
	
	public final dad.planeador.vuelos.models.TipoNavaid getTipoNavaid() {
		return this.tipoNavaidProperty().get();
	}
	
	public final void setTipoNavaid(final dad.planeador.vuelos.models.TipoNavaid tipoNavaid) {
		this.tipoNavaidProperty().set(tipoNavaid);
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
	
	public final StringProperty frecuenciaProperty() {
		return this.frecuencia;
	}
	
	public final java.lang.String getFrecuencia() {
		return this.frecuenciaProperty().get();
	}
	
	public final void setFrecuencia(final java.lang.String frecuencia) {
		this.frecuenciaProperty().set(frecuencia);
	}
}
