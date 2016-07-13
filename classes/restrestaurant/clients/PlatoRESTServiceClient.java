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
import rest.model.Ingrediente;
import rest.model.Plato;
 

public class PlatoRESTServiceClient {
		
	public static void main(String[] args) {
						
			
			try {
				String webServiceURI = "http://localhost:8080/Restaurante/";
				ClientConfig clientConfig = new ClientConfig();
				Client client = ClientBuilder.newClient(clientConfig);
				URI serviceURI = UriBuilder.fromUri(webServiceURI).build();
				WebTarget webTarget = client.target(serviceURI);

				Plato plato = new Plato();
				plato.setNomplato("Parrilla");
				plato.setPrecplato(new BigDecimal(769));
				plato.setDescplato("Parrilla");
				plato.setEstatus("1");
				plato.setIdplato(new Long(0));
				plato.setFecha("2015-09-19");
				plato.setImgplato("");
				
				List<Ingrediente> ingredientes = new ArrayList<Ingrediente>();
				Ingrediente i = webTarget.path("rest").path("ingrediente/getIngrediente/1" ).request()
						.accept(MediaType.APPLICATION_JSON).get(Ingrediente.class);				
				ingredientes.add(i);
				i = webTarget.path("rest").path("ingrediente/getIngrediente/2" ).request()
						.accept(MediaType.APPLICATION_JSON).get(Ingrediente.class);				
				ingredientes.add(i);				
				System.out.println(ingredientes);
				plato.setIngredientes(ingredientes);
				
				Long idPlato = new Long(9);
				Long idIngrediente = new Long(3);
				//Ejemplo de Post create plato
				 System.out.println(webTarget.path("rest").path("plato/createPlato").request()
						.accept(MediaType.APPLICATION_JSON).post(Entity.entity(plato, MediaType.APPLICATION_JSON)).bufferEntity());
				
				
				// Ejemplo de get de un registro
				/*Plato platoResponse = webTarget.path("rest").path("plato/getPlato/" + idPlato).request()
						.accept(MediaType.APPLICATION_JSON).get(Plato.class);
				System.out.println(platoResponse);*/
				
				// Ejemplo de get de todos los registros
				/*Plato[] platos = webTarget.path("rest").path("plato/getPlatosAll").request()
						.accept(MediaType.APPLICATION_JSON).get(Plato[].class);
				for(Plato c : platos)
				{	System.out.println(c.getNomplato());
					
				}
				
				 //Ejemplo de actualizar datos
								
				/*System.out.println(webTarget.path("rest").path("plato/updatePlato/" + idPlato).request()
						.accept(MediaType.APPLICATION_JSON).post(Entity.entity(plato, MediaType.APPLICATION_JSON)).bufferEntity());
				*/
				//Ejemplo de eliminar un registro
				/*System.out.println(webTarget.path("rest").path("plato/deletePlato/" + idPlato).request()
						.accept(MediaType.APPLICATION_JSON).post(Entity.entity(plato, MediaType.APPLICATION_JSON)).bufferEntity());
				*/
				//Ejemplo de eliminar un ingrediente al plato
				/*System.out.println(webTarget.path("rest").path("plato/deleteIngredientePlato/" + idPlato + "/" + idIngrediente).request()
						.accept(MediaType.APPLICATION_JSON).post(Entity.entity(plato, MediaType.APPLICATION_JSON)).bufferEntity());
				*/
				//Ejemplo de añadir un ingrediente al plato
				/*System.out.println(webTarget.path("rest").path("plato/addIngredientePlato/" + idIngrediente + "/" + idPlato).request()
						.accept(MediaType.APPLICATION_JSON).post(Entity.entity(plato, MediaType.APPLICATION_JSON)).bufferEntity());
				*/
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

