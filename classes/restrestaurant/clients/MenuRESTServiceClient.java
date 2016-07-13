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
import rest.model.Menu;
import rest.model.Plato;
 

public class MenuRESTServiceClient {
		
	public static void main(String[] args) {
						
			
			try {
				String webServiceURI = "http://localhost:8080/Restaurante/";
				ClientConfig clientConfig = new ClientConfig();
				Client client = ClientBuilder.newClient(clientConfig);
				URI serviceURI = UriBuilder.fromUri(webServiceURI).build();
				WebTarget webTarget = client.target(serviceURI);

				Menu menu = new Menu();
				menu.setFecha("2015-09-19");
				menu.setEstatus("1");
				menu.setIdmenu(new Long(0));
				
				List<Plato> platos = new ArrayList<Plato>();
				Plato p = webTarget.path("rest").path("plato/getPlato/9" ).request()
						.accept(MediaType.APPLICATION_JSON).get(Plato.class);				
				platos.add(p);
				p = webTarget.path("rest").path("plato/getPlato/10" ).request()
						.accept(MediaType.APPLICATION_JSON).get(Plato.class);				
				platos.add(p);				
				
				menu.setPlatos(platos);
				
				Long idMenu = new Long(3);
				Long idPlato = new Long(10);
				
				/*Long idMenu = new Long(9);
				Long idIngrediente = new Long(3);*/
				//Ejemplo de Post create menu
				/*System.out.println(webTarget.path("rest").path("menu/createMenu").request()
						.accept(MediaType.APPLICATION_JSON).post(Entity.entity(menu, MediaType.APPLICATION_JSON)).bufferEntity());
				*/
				
				// Ejemplo de get de un registro
				/*Menu menuResponse = webTarget.path("rest").path("menu/getMenu/" + idMenu).request()
						.accept(MediaType.APPLICATION_JSON).get(Menu.class);
				System.out.println(menuResponse);
				*/
				// Ejemplo de get de todos los registros
				Menu[] menus = webTarget.path("rest").path("menu/getMenusAll").request()
						.accept(MediaType.APPLICATION_JSON).get(Menu[].class);
				for(Menu c : menus)
				{	System.out.println(c.getIdmenu());
					
				}
				
				//Ejemplo de eliminar un registro
				/*System.out.println(webTarget.path("rest").path("menu/deleteMenu/" + idMenu).request()
						.accept(MediaType.APPLICATION_JSON).post(Entity.entity(menu, MediaType.APPLICATION_JSON)).bufferEntity());
				*/
				//Ejemplo de eliminar un plato al menu
				/*System.out.println(webTarget.path("rest").path("menu/deletePlatoMenu/" + idMenu + "/" + idPlato).request()
						.accept(MediaType.APPLICATION_JSON).post(Entity.entity(menu, MediaType.APPLICATION_JSON)).bufferEntity());
				*/
				//Ejemplo de añadir un plato al menu
				/*System.out.println(webTarget.path("rest").path("menu/addPlatoMenu/" + idMenu + "/" + idPlato).request()
						.accept(MediaType.APPLICATION_JSON).post(Entity.entity(menu, MediaType.APPLICATION_JSON)).bufferEntity());
				*/
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

