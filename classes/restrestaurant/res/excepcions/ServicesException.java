package restrestaurant.res.excepcions;

public class ServicesException extends Exception {

	private static final long serialVersionUID = -8476632712405848362L;

	private String mensaje;
	
	public ServicesException(String mensaje) {		
		this.mensaje = mensaje;
		System.out.println(mensaje);
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	
	

}
