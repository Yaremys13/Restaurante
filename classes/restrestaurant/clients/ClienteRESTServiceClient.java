package restrestaurant.clients;


import java.io.ByteArrayInputStream;
import java.net.URI;






import java.util.Scanner;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;






import org.glassfish.jersey.client.ClientConfig;

import rest.model.Clientes;
import restrestaurant.res.excepcions.ServicesException;
 

public class ClienteRESTServiceClient {
		
	public static void main(String[] args) {
			Clientes cliente = new Clientes();
			cliente.setApeCliente("Moreno");
			cliente.setCedCliente("7345618");
			cliente.setEmailCliente("pedroperez1@gmail.com");
			cliente.setEstatus("1");
			cliente.setIdCliente(new Long(0));
			cliente.setNomCliente("Pedro");
			cliente.setPassCliente("1234");
			cliente.setTelCliente("04143104619");
			
			String emailCliente = "yaremysluces@gmail.com";
			
			
			try {
				String webServiceURI = "http://localhost:8080/Restaurante/";
				ClientConfig clientConfig = new ClientConfig();
				Client client = ClientBuilder.newClient(clientConfig);
				URI serviceURI = UriBuilder.fromUri(webServiceURI).build();
				WebTarget webTarget = client.target(serviceURI);

				
				 //Ejemplo de Post
				 /*System.out.println(webTarget.path("rest").path("createCliente").request()
						.accept(MediaType.APPLICATION_JSON).post(Entity.entity(cliente, MediaType.APPLICATION_JSON)).bufferEntity());
				*/
				// Ejemplo de get de un registro
				/*Clientes clienteResponse = webTarget.path("rest").path("getCliente/" + emailCliente).request()
						.accept(MediaType.APPLICATION_JSON).get(Clientes.class);
				System.out.println(clienteResponse.getNomCliente());
				*/
				// Ejemplo de get de todos los registros
				Clientes[] clientes = webTarget.path("rest").path("getClientesAll").request()
						.accept(MediaType.APPLICATION_JSON).get(Clientes[].class);
				for(Clientes c : clientes)
				{	System.out.println(c.getNomCliente());
					
				}
				
				 //Ejemplo de actualizar datos
				/*cliente = new Clientes();
				cliente.setApeCliente("Luces Perez");
				cliente.setCedCliente("29751112");
				cliente.setEmailCliente("manuelluces@gmail.com");
				cliente.setEstatus("1");
				cliente.setIdCliente(new Long(0));
				cliente.setNomCliente("Manuel Jose");
				cliente.setPassCliente("1234567");
				cliente.setTelCliente("0414222222");
				
				System.out.println(webTarget.path("rest").path("updateCliente/" + cliente.getEmailCliente()).request()
						.accept(MediaType.APPLICATION_JSON).post(Entity.entity(cliente, MediaType.APPLICATION_JSON)).bufferEntity());
				*/
				//Ejemplo de eliminar un registro
				/*System.out.println(webTarget.path("rest").path("deleteCliente/" + emailCliente).request()
						.accept(MediaType.APPLICATION_JSON).post(Entity.entity(cliente, MediaType.APPLICATION_JSON)).bufferEntity());
				*/
			} catch (Exception e) {
				System.out.println(e.getMessage());
				
			}
		}
	}

