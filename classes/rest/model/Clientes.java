package rest.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Clientes 
{
	private Long idCliente;
	private String nomCliente;
	private String apeCliente;
	private String cedCliente;
	private String telCliente;
	private String emailCliente;
	private String passCliente;
	private String tipoCliente;
	private String estatus;
	

	public Clientes() 
	{	super();
	}


	public Long getIdCliente() {
		return idCliente;
	}


	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}


	public String getNomCliente() {
		return nomCliente;
	}


	public void setNomCliente(String nomCliente) {
		this.nomCliente = nomCliente;
	}


	public String getApeCliente() {
		return apeCliente;
	}


	public void setApeCliente(String apeCliente) {
		this.apeCliente = apeCliente;
	}


	public String getCedCliente() {
		return cedCliente;
	}


	public void setCedCliente(String cedCliente) {
		this.cedCliente = cedCliente;
	}


	public String getTelCliente() {
		return telCliente;
	}


	public void setTelCliente(String telCliente) {
		this.telCliente = telCliente;
	}


	public String getEmailCliente() {
		return emailCliente;
	}


	public void setEmailCliente(String emailCliente) {
		this.emailCliente = emailCliente;
	}


	public String getPassCliente() {
		return passCliente;
	}


	public void setPassCliente(String passCliente) {
		this.passCliente = passCliente;
	}


	public String getTipoCliente() {
		return tipoCliente;
	}


	public void setTipoCliente(String tipoCliente) {
		this.tipoCliente = tipoCliente;
	}


	public String getEstatus() {
		return estatus;
	}


	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
	
	
	@Override
	public String toString() {
		return new StringBuffer("{ First Name : ").append(this.nomCliente)
			.append(" Last Name : ").append(this.apeCliente)
			.append(" CI : ").append(this.cedCliente)
			.append(" Email : ").append(this.emailCliente)
			.append(" Telefono : ").append(this.telCliente)
			.append(" Tipo Cliente : ").append(this.tipoCliente).append(" }").toString();
	}

	 
	

	
}
