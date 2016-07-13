package rest.model;

import java.math.BigDecimal;



public class Pedido {
	
	
	private Long idpedido;
	private Clientes cliente;
	private String fechapedido;
	private String horapedido;
	private BigDecimal subtotal;
	private BigDecimal iva;
	private String estatus;
	//private List<DetallePedido> detalles;
	
	
	public Long getIdpedido() {
		return idpedido;
	}
	public void setIdpedido(Long idpedido) {
		this.idpedido = idpedido;
	}
	public Clientes getCliente() {
		return cliente;
	}
	public void setCliente(Clientes cliente) {
		this.cliente = cliente;
	}
	public String getFechapedido() {
		return fechapedido;
	}
	public void setFechapedido(String fechapedido) {
		this.fechapedido = fechapedido;
	}
	public String getHorapedido() {
		return horapedido;
	}
	public void setHorapedido(String horapedido) {
		this.horapedido = horapedido;
	}
	public BigDecimal getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}
	public BigDecimal getIva() {
		return iva;
	}
	public void setIva(BigDecimal iva) {
		this.iva = iva;
	}
	public String getEstatus() {
		return estatus;
	}
	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
	/*public List<DetallePedido> getDetalles() {
		return detalles;
	}
	public void setDetalles(List<DetallePedido> detalles) {
		this.detalles = detalles;
	}*/
	
	@Override
	public String toString() {
		StringBuffer pedido = new StringBuffer("{ idpedido : ").append(this.idpedido)
			.append(", { idcliente : ").append(this.cliente.getIdCliente())
			.append(", nomcliente : ").append(this.cliente.getNomCliente())
			.append(", emailcliente : ").append(this.cliente.getEmailCliente())
			.append("} , fechapedido : ").append(this.fechapedido)
			.append(", horapedido : ").append(this.horapedido)
			.append(", subtotal : ").append(this.subtotal)
			.append(", iva : " ).append(this.iva);
		/*for (DetallePedido dp : this.detalles)
		{	pedido.append("{ idplato : ").append(dp.getPlato().getIdplato())
			.append(", cant : ").append(dp.getCant())
			.append(" } ");
		}*/
		pedido.append("}");			
		
		
		return pedido.toString();
	}
	

}
