package rest.model;

import java.util.List;

public class PedidoCompleto {
	
	private Pedido pedido;
	private List<DetallePedido> detalles;
	
	public Pedido getPedido() {
		return pedido;
	}
	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}
	public List<DetallePedido> getDetalles() {
		return detalles;
	}
	public void setDetalles(List<DetallePedido> detalles) {
		this.detalles = detalles;
	}
	
	@Override
	public String toString() {
		StringBuffer pedidoC = new StringBuffer("{ idpedido : ").append(this.pedido.getIdpedido())
			.append(", { idcliente : ").append(this.pedido.getCliente().getIdCliente())
			.append(", nomcliente : ").append(this.pedido.getCliente().getNomCliente())
			.append(", emailcliente : ").append(this.pedido.getCliente().getEmailCliente())
			.append("} , fechapedido : ").append(this.pedido.getFechapedido())
			.append(", horapedido : ").append(this.pedido.getHorapedido())
			.append(", subtotal : ").append(this.pedido.getSubtotal())
			.append(", iva : " ).append(this.pedido.getIva());
		for (DetallePedido dp : this.detalles)
		{	pedidoC.append("{ idplato : ").append(dp.getPlato().getIdplato())
			.append(", cant : ").append(dp.getCant())
			.append(" } ");
		}
		pedidoC.append("}");			
		
		
		return pedidoC.toString();
	}
	

}
