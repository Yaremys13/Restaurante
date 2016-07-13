package restrestaurant.clients;


import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;

import rest.model.Clientes;
import rest.model.DetallePedido;
import rest.model.Pedido;
import rest.model.PedidoCompleto;
import rest.model.Plato;
 

public class PedidoRESTServiceClient {
		
	public static void main(String[] args) {
						
			
			try {
				String webServiceURI = "http://localhost:8080/Restaurante/";
				ClientConfig clientConfig = new ClientConfig();
				Client client = ClientBuilder.newClient(clientConfig);
				URI serviceURI = UriBuilder.fromUri(webServiceURI).build();
				WebTarget webTarget = client.target(serviceURI);

				Clientes cliente = webTarget.path("rest").path("getCliente/yaremysluces@gmail.com").request()
						.accept(MediaType.APPLICATION_JSON).get(Clientes.class);
				
				PedidoCompleto pedidoCompleto = new PedidoCompleto();
				
				Pedido pedido = new Pedido();
				pedido.setCliente(cliente);
				pedido.setHorapedido(null);
				pedido.setEstatus("1");
				pedido.setIdpedido(new Long(0));
				pedido.setFechapedido("2015-09-19");
				pedido.setIva(new BigDecimal(15));
				pedido.setSubtotal(new BigDecimal(2000));
				
				List<DetallePedido> detalles = new ArrayList<DetallePedido>();
				DetallePedido dp = new DetallePedido();
				dp.setEstatus("1");
				dp.setPedido(pedido);
				dp.setCant(new Double(2));				
				Plato p = webTarget.path("rest").path("plato/getPlato/9" ).request()
						.accept(MediaType.APPLICATION_JSON).get(Plato.class);	
				dp.setPlato(p);
				detalles.add(dp);
				
				dp = new DetallePedido();
				dp.setEstatus("1");
				dp.setPedido(pedido);
				dp.setCant(new Double(3));				
				p = webTarget.path("rest").path("plato/getPlato/10" ).request()
						.accept(MediaType.APPLICATION_JSON).get(Plato.class);	
				dp.setPlato(p);
				detalles.add(dp);
				
				pedidoCompleto.setPedido(pedido);
				pedidoCompleto.setDetalles(detalles);
				
				//System.out.println(pedidoCompleto);
				//Ejemplo de Post create pedido
				 /*System.out.println(webTarget.path("rest").path("pedido/createPedido").request()
						.accept(MediaType.APPLICATION_JSON).post(Entity.entity(pedidoCompleto, MediaType.APPLICATION_JSON)).bufferEntity());
				*/
				 String idPedido = "3";
				
				// Ejemplo de get de un registro
				/*PedidoCompleto pedidoResponse = webTarget.path("rest").path("pedido/getPedido/" + idPedido).request()
						.accept(MediaType.APPLICATION_JSON).get(PedidoCompleto.class);
				System.out.println(pedidoResponse);*/
				
				// Ejemplo de get de todos los registros
				PedidoCompleto[] pedidos = webTarget.path("rest").path("pedido/getPedidosAll").request()
						.accept(MediaType.APPLICATION_JSON).get(PedidoCompleto[].class);
				for(PedidoCompleto c : pedidos)
				{	System.out.println(c);
					
				}
				
				 
				 //Ejemplo de actualizar datos agregar detalle de pedido
				/*pedido = webTarget.path("rest").path("pedido/getPedido/" + idPedido).request()
						.accept(MediaType.APPLICATION_JSON).get(PedidoCompleto.class).getPedido();
				dp = new DetallePedido();
				dp.setEstatus("1");
				dp.setPedido(pedido);
				dp.setCant(new Double(2));				
				p = webTarget.path("rest").path("plato/getPlato/9" ).request()
						.accept(MediaType.APPLICATION_JSON).get(Plato.class);	
				dp.setPlato(p);				
				System.out.println(webTarget.path("rest").path("pedido/addDetallePedido/" + idPedido).request()
						.accept(MediaType.APPLICATION_JSON).post(Entity.entity(dp, MediaType.APPLICATION_JSON)).bufferEntity());
				*/
				//Ejemplo de eliminar un detalle de pedido registro
				/*String idDetalle = "9";
				System.out.println(webTarget.path("rest").path("pedido/deleteDetallePedido/" + idPedido + "/" + idDetalle).request()
						.accept(MediaType.APPLICATION_JSON).post(Entity.entity(pedido, MediaType.APPLICATION_JSON)).bufferEntity());
				*/
				//Ejemplo de eliminar un pedido
				/*System.out.println(webTarget.path("rest").path("pedido/deletePedido/" + idPedido).request()
						.accept(MediaType.APPLICATION_JSON).post(Entity.entity(pedido, MediaType.APPLICATION_JSON)).bufferEntity());
				*/
				//Ejemplo de cambiar estatus a pedido
				/*String estatus = "6";
				System.out.println(webTarget.path("rest").path("pedido/updateEstatusPedido/" + idPedido + "/" + estatus).request()
						.accept(MediaType.APPLICATION_JSON).post(Entity.entity(pedido, MediaType.APPLICATION_JSON)).bufferEntity());
				*/
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

