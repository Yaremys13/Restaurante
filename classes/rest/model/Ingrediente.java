package rest.model;

import java.math.BigDecimal;

public class Ingrediente {
	
	private Long idingrediente;
	private String nomingrediente;
	private String descingrediente;
	private String tipoingrediente;
	private BigDecimal precioingrediente;
	private String fecha;
	private BigDecimal cantstock;
	private String estatus;
	private Clientes cliente;
	
	public Long getIdingrediente() {
		return idingrediente;
	}
	public void setIdingrediente(Long idingrediente) {
		this.idingrediente = idingrediente;
	}
	public String getNomingrediente() {
		return nomingrediente;
	}
	public void setNomingrediente(String nomingrediente) {
		this.nomingrediente = nomingrediente;
	}
	public String getDescingrediente() {
		return descingrediente;
	}
	public void setDescingrediente(String descingrediente) {
		this.descingrediente = descingrediente;
	}
	public String getTipoingrediente() {
		return tipoingrediente;
	}
	public void setTipoingrediente(String tipoingrediente) {
		this.tipoingrediente = tipoingrediente;
	}
	public BigDecimal getPrecioingrediente() {
		return precioingrediente;
	}
	public void setPrecioingrediente(BigDecimal precioingrediente) {
		this.precioingrediente = precioingrediente;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public BigDecimal getCantstock() {
		return cantstock;
	}
	public void setCantstock(BigDecimal cantstock) {
		this.cantstock = cantstock;
	}
	public String getEstatus() {
		return estatus;
	}
	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
		
	public Clientes getCliente() {
		return cliente;
	}
	public void setCliente(Clientes cliente) {
		this.cliente = cliente;
	}
	@Override
	public String toString() {
		
		return new StringBuffer(" { nomingrediente : ").append(this.nomingrediente)
				.append(" descingrediente : ").append(this.descingrediente)
				.append(" fecha : ").append(this.fecha)
				.append(" precioingrediente : ").append(this.precioingrediente)
				.append(" cantstock : ").append(this.cantstock)
				.append(" tipoingrediente : ").append(this.tipoingrediente)
				.append(" Cliente : { ").append(" First Name : ").append(this.cliente.getNomCliente())
				.append(" Last Name : ").append(this.cliente.getApeCliente())
				.append(" CI : ").append(this.cliente.getCedCliente())
				.append(" Email : ").append(this.cliente.getEmailCliente())
				.append(" Telefono : ").append(this.cliente.getTelCliente()).append(" } }").toString();
		
		
	}
	
	
	

	

}
