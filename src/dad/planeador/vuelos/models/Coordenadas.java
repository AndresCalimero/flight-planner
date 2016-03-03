package dad.planeador.vuelos.models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Coordenadas {

	private final DoubleProperty gradosNorte = new SimpleDoubleProperty();
	private final DoubleProperty gradosEste = new SimpleDoubleProperty();

	public final DoubleProperty gradosNorteProperty() {
		return this.gradosNorte;
	}

	public final double getGradosNorte() {
		return this.gradosNorteProperty().get();
	}

	public final void setGradosNorte(final double gradosNorte) {
		this.gradosNorteProperty().set(gradosNorte);
	}

	public final DoubleProperty gradosEsteProperty() {
		return this.gradosEste;
	}

	public final double getGradosEste() {
		return this.gradosEsteProperty().get();
	}

	public final void setGradosEste(final double gradosEste) {
		this.gradosEsteProperty().set(gradosEste);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((int) (getGradosEste() * 1000));
		result = prime * result + ((int) (getGradosNorte() * 1000));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Coordenadas))
			return false;
		Coordenadas other = (Coordenadas) obj;
		if (other.getGradosEste() != getGradosEste() || other.getGradosNorte() != getGradosNorte())
			return false;
		return true;
	}

	@Override
	public String toString() {
		int grados = (int) gradosNorte.get();
		double fraccion = (gradosNorte.get() * 3600) % 3600;
		int minutos = (int) fraccion / 60;
		int segundos = (int) fraccion % 60;
		String norte = (grados < 0 ? "S" : "N") + String.format("%02d", Math.abs(grados)) + "º" + String.format("%02d", Math.abs(minutos))
				+ "'" + String.format("%02d", Math.abs(segundos)) + "''";

		grados = (int) gradosEste.get();
		fraccion = (gradosEste.get() * 3600) % 3600;
		minutos = (int) fraccion / 60;
		segundos = (int) fraccion % 60;
		String este = (grados < 0 ? "W" : "E") + String.format("%02d", Math.abs(grados)) + "º" + String.format("%02d", Math.abs(minutos))
				+ "'" + String.format("%02d", Math.abs(segundos)) + "''";
		return norte + "  " + este;
	}
}
