package rest.model.requests;

import rest.model.Clientes;

public class PasswordChangeRequest {
	
	private Clientes cliente;
	private String nuevoPassword;
	
	public Clientes getCliente() {
		return cliente;
	}
	public void setCliente(Clientes cliente) {
		this.cliente = cliente;
	}
	public String getNuevoPassword() {
		return nuevoPassword;
	}
	public void setNuevoPassword(String nuevoPassword) {
		this.nuevoPassword = nuevoPassword;
	}
	
	

}
