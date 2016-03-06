package dad.planeador.vuelos.services;

public class PlaneadorVuelosServiceException extends Exception {
	private static final long serialVersionUID = 2391550552949366393L;
	
	public PlaneadorVuelosServiceException() {
		this("Unknown exception");
	}

	public PlaneadorVuelosServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public PlaneadorVuelosServiceException(String message) {
		super(message);
	}
}
