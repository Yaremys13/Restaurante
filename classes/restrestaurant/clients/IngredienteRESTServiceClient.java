package restrestaurant.clients;

import java.math.BigDecimal;
import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;

import rest.model.Ingrediente;

public class IngredienteRESTServiceClient {
	
	
	public static void main(String args[])
	{	
		String webServiceURI = "http://localhost:8080/Restaurante/";
		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		URI serviceURI = UriBuilder.fromUri(webServiceURI).build();
		WebTarget webTarget = client.target(serviceURI);
		
		//Ejemplo de crear ingrediente
		Ingrediente ingrediente = new Ingrediente();
		ingrediente.setIdingrediente(new Long(0));
		ingrediente.setNomingrediente("espaguetis");
		ingrediente.setDescingrediente("capri");
		ingrediente.setFecha("2015-09-19");
		ingrediente.setPrecioingrediente(new BigDecimal(300));
		ingrediente.setCantstock(new BigDecimal(10));
		ingrediente.setTipoingrediente("secos");
		ingrediente.setEstatus("1");
		
		String idIngrediente = "1";
		
		//Ejemplo de CREATE
		/*System.out.println(webTarget.path("rest").path("ingrediente/createIngrediente").request()
			.accept(MediaType.APPLICATION_JSON).post(Entity.entity(ingrediente, MediaType.APPLICATION_JSON)).bufferEntity());
		*/
		
		//Ejemplo de GET un ingrediente
		/*Ingrediente ingredienteResponse = webTarget.path("rest").path("ingrediente/getIngrediente/" + idIngrediente).request()
				.accept(MediaType.APPLICATION_JSON).get(Ingrediente.class);
		System.out.println(ingredienteResponse);*/
		
		//Ejemplo de GET todos los ingredientes
		Ingrediente[] ingredienteResponse = webTarget.path("rest").path("ingrediente/getIngredientesAll").request()
						.accept(MediaType.APPLICATION_JSON).get(Ingrediente[].class);
				
		for(Ingrediente i : ingredienteResponse)
		{	System.out.println(i.getNomingrediente());
					
		}
		
		//Ejemplo de actualizar un cliente
		/*ingrediente = new Ingrediente();
		ingrediente.setNomingrediente("espaguetis");
		//ingrediente.setDescingrediente("capri");
		ingrediente.setFecha("2015-09-19");
		ingrediente.setPrecioingrediente(new BigDecimal(300));
		ingrediente.setCantstock(new BigDecimal(10));
		ingrediente.setTipoingrediente("secos");
		ingrediente.setEstatus("1");	
		
		System.out.println(webTarget.path("rest").path("ingrediente/updateIngrediente/" + idIngrediente).request()
				.accept(MediaType.APPLICATION_JSON).post(Entity.entity(ingrediente, MediaType.APPLICATION_JSON)).bufferEntity());
		*/
		
		//Ejemplo de Eliminar un Ingrediente
		/*System.out.println(webTarget.path("rest").path("ingrediente/deleteIngrediente/" + idIngrediente).request()
				.accept(MediaType.APPLICATION_JSON).post(Entity.entity(ingrediente, MediaType.APPLICATION_JSON)).bufferEntity());
		*/
		/*Double cant = new Double(2);
		System.out.println(webTarget.path("rest").path("ingrediente/descontarIngrediente/" + idIngrediente + "/" + cant).request()
				.accept(MediaType.APPLICATION_JSON).post(Entity.entity(ingrediente, MediaType.APPLICATION_JSON)).bufferEntity());
		*/
		
	}
	

}
