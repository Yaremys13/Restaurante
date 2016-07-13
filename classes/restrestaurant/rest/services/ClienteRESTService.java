package restrestaurant.rest.services;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import rest.model.Clientes;
import rest.model.requests.PasswordChangeRequest;
import rest.utilities.DataServiceHelper;
import restrestaurant.res.excepcions.ServicesException;
/**
 * 
 * Esta clase define los endpoints/servicios para manipular Clientes del Restaurante que se 
 * logean de manera que puedan operar y usar las funcionalidades 
 * @author Natasha Martinez, Ronny Ayala
 *
 */
@Path("/")
public class ClienteRESTService {
	
	/**
	 * Servicio que inserta un cliente en la base de datos 
	 * @param cliente: cliente que será insertado en la base de datos
	 * @return Respuesta HTTP devuelta: 200 Éxito, 400 y 500 Error en el servidor, 404 No encontrado 
	 */
	
	@POST
	@Path("/createCliente")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createCliente(Clientes cliente) throws ServicesException {
		Connection con = null;
		try {
			con  = DataServiceHelper.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("INSERT INTO clientes VALUES (?,?,?,?,?,?,?,?,?)");
			ps.setLong(1, new Long(0));
			ps.setString(2, cliente.getNomCliente());
			ps.setString(3, cliente.getApeCliente());
			ps.setString(4, cliente.getCedCliente());
			ps.setString(5, cliente.getTelCliente());
			ps.setString(6, cliente.getEmailCliente());
			ps.setString(7, cliente.getPassCliente());
			ps.setString(8, cliente.getTipoCliente());
			ps.setString(9, "1");

			if (ps.executeUpdate() != 0)
			{	System.out.println("Data Received: " + cliente.toString());	
				return Response.status(200).entity(cliente.toString()).build();
			}
			
			else
			{	throw new ServicesException("Error creando Cliente");
			}
		} catch (ClassNotFoundException e) {
			throw new ServicesException("Error en el objeto Cliente pasado como parámetro");
		} catch (SQLException e) {
			throw new ServicesException("Error en la sentencia SQL");
		} finally
		{	try {
				if (con != null)
				{	con.close();
				
				}
			} catch (SQLException e) {
				throw new ServicesException("Error cerrando la conexión");
			}
		}
		
	}
	
	/**
	 * Servicio que retorna el cliente según el email
	 * @param emailCliente: email del cliente que se desea buscar
	 * @return Cliente encontrado
	 * 
	 */
	
	@GET
	@Path("/getCliente/{emailCliente}")
	@Produces(MediaType.APPLICATION_JSON)
	public Clientes getCliente(@PathParam("emailCliente") String emailCliente) throws ServicesException  {
		
		Clientes cliente = new Clientes();
		Connection con = null;
		try {
			System.out.println("Data Received: " + emailCliente);
			con  = DataServiceHelper.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM clientes WHERE emailcliente = ? AND estatus = '1'");
			ps.setString(1, emailCliente);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
			{	cliente.setIdCliente(rs.getLong(1));
				cliente.setNomCliente(rs.getString(2));
				cliente.setApeCliente(rs.getString(3));
				cliente.setCedCliente(rs.getString(4));
				cliente.setTelCliente(rs.getString(5));
				cliente.setEmailCliente(rs.getString(6));
				cliente.setPassCliente(rs.getString(7));
				cliente.setTipoCliente(rs.getString(8));
				cliente.setEstatus(rs.getString(9));				
				Response.status(200).entity(cliente.toString()).build();
			}
			
			else
			{	throw new ServicesException("Usuario no encontrado");
			}
		} catch (ClassNotFoundException e) {
			throw new ServicesException("Error en el objeto Cliente pasado como parámetro");
		} catch (SQLException e) {			
			throw new ServicesException("Error en la sentencia SQL");
		} finally
		{	try {
				if (con != null)
				{	con.close();				
				}
			} catch (SQLException e) {
				throw new ServicesException("Error cerrando la conexión");
			}
		}
			
		// return HTTP response 200 in case of success
		return cliente;
	}
	/**
	 * Servicio que devuelve todos los clientes registrados en la base de datos
	 * @return Lista de Clientes encontrados
	 */
	@GET
	@Path("/getClientesAll")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Clientes> getClientesAll() throws ServicesException {
		
		List<Clientes> clientes = new ArrayList<Clientes>();
		Connection con = null;
		try {
			con  = DataServiceHelper.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM clientes WHERE estatus = '1'");
			ResultSet rs = ps.executeQuery();
			if (rs.next())
			{	rs.beforeFirst();		
				while (rs.next())
				{	Clientes cliente = new Clientes();
					cliente.setIdCliente(rs.getLong(1));
					cliente.setNomCliente(rs.getString(2));
					cliente.setApeCliente(rs.getString(3));
					cliente.setCedCliente(rs.getString(4));
					cliente.setTelCliente(rs.getString(5));
					cliente.setEmailCliente(rs.getString(6));
					cliente.setPassCliente(rs.getString(7));
					cliente.setTipoCliente(rs.getString(8));
					cliente.setEstatus(rs.getString(9));					
					clientes.add(cliente);
				}
				Response.status(200).entity(clientes.toString()).build();
			}
			else
			{	throw new ServicesException("No se consiguieron clientes");							
			}
			
		} catch (ClassNotFoundException e) {
			throw new ServicesException("Error en el objeto Cliente pasado como parámetro");
			
		} catch (SQLException e) {
			throw new ServicesException("Error la sentencia SQL");

		} finally
		{	try {
				if (con != null)
				{	con.close();				
				}
			} catch (SQLException e) {
				throw new ServicesException("Error cerrando la conexión");

			}
		}
			
		// return HTTP response 200 in case of success
		return clientes;
	}
	/**
	 * Servicio que actualiza lod datos del Cliente
	 * @param emailCliente: email del cliente al modificar
	 * @param cliente: datos que se modificarán en el registro del cliente
	 * @return Respuesta HTTP devuelta: 200 Éxito, 400 y 500 Error en el servidor, 404 No encontrado
	 */
	@POST
	@Path("/updateCliente/{emailCliente}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateCliente(@PathParam("emailCliente") String emailCliente, Clientes cliente) throws ServicesException {
		Connection con = null;
		try {
			con  = DataServiceHelper.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE clientes SET  nomcliente = ?, "
					+ " apecliente = ?, cedcliente = ?, telcliente = ?, passcliente = ?, estatus = ?, "
					+ " tipocliente = ?	WHERE emailcliente = ?");
			
			ps.setString(1, cliente.getNomCliente());
			ps.setString(2, cliente.getApeCliente());
			ps.setString(3, cliente.getCedCliente());
			ps.setString(4, cliente.getTelCliente());			
			ps.setString(5, cliente.getPassCliente());			
			ps.setString(6, "1");
			ps.setString(7, cliente.getTipoCliente());
			ps.setString(8, emailCliente);

			if (ps.executeUpdate() != 0)
			{	System.out.println("Data Received: " + cliente.toString());	
				// return HTTP response 200 in case of success
					return Response.status(200).entity(cliente.toString()).build();
			}
			
			else
			{	throw new ServicesException("Error modificando el cliente");
			}
		} catch (ClassNotFoundException e) {
			throw new ServicesException("Error en el objeto Cliente pasado como parámetro");
		} catch (SQLException e) {
			throw new ServicesException("Error en la sentencia SQL");
			
		} finally
		{	try {
				if (con != null)
				{	con.close();
				
				}
			} catch (SQLException e) {
				throw new ServicesException("Error cerrando la conexión");
			}
		}
		
	}
	/**
	 * Servicio que elimina el Cliente
	 * @param emailCliente: email del cliente a eliminar
	 * @return Respuesta HTTP devuelta: 200 Éxito, 400 y 500 Error en el servidor, 404 No encontrado
	 */
	@POST
	@Path("/deleteCliente/{emailCliente}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteCliente(@PathParam("emailCliente") String emailCliente) throws ServicesException {
		Connection con = null;
		try {
			con  = DataServiceHelper.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE clientes SET  estatus = '0'"
					+ " 	WHERE emailcliente = ?");
			ps.setString(1, emailCliente);
			
			if (ps.executeUpdate() != 0)
			{	System.out.println("Data Received: " + emailCliente);	
				// return HTTP response 200 in case of success
					return Response.status(200).entity("Eliminado con exito").build();
			}
			
			else
			{	throw new ServicesException("Error eliminando el cliente");
			}
		} catch (ClassNotFoundException e) {
			throw new ServicesException("Error en el objeto Cliente pasado como parámetro");
		} catch (SQLException e) {
			throw new ServicesException("Error en la sentencia SQL");
		} finally
		{	try {
				if (con != null)
				{	con.close();
				
				}
			} catch (SQLException e) {
				throw new ServicesException("Error cerrando la conexión");
			}
		}
		
	}
	
	
	/**
	 * Servicio que retorna el cliente según el email y el password, si existen
	 * @param cliente: email del cliente que se desea buscar
	 * @return Cliente encontrado
	 * 
	 */
	
	@GET
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	public Clientes login(Clientes clienteLogin) throws ServicesException  {
		
		Clientes cliente = new Clientes();
		Connection con = null;
		try {
			System.out.println("Data Received: " + cliente.getEmailCliente());
			con  = DataServiceHelper.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM clientes WHERE emailcliente = ? AND passcliente = ? AND estatus = '1'");
			ps.setString(1, clienteLogin.getEmailCliente());
			ps.setString(2, clienteLogin.getPassCliente());
			ResultSet rs = ps.executeQuery();
			if (rs.next())
			{	cliente.setIdCliente(rs.getLong(1));
				cliente.setNomCliente(rs.getString(2));
				cliente.setApeCliente(rs.getString(3));
				cliente.setCedCliente(rs.getString(4));
				cliente.setTelCliente(rs.getString(5));
				cliente.setEmailCliente(rs.getString(6));
				cliente.setPassCliente(rs.getString(7));
				cliente.setTipoCliente(rs.getString(8));
				cliente.setEstatus(rs.getString(9));						
				Response.status(200).entity(cliente.toString()).build();
			}
			
			else
			{	throw new ServicesException("Usuario no encontrado");
			}
		} catch (ClassNotFoundException e) {
			throw new ServicesException("Error en el objeto Cliente pasado como parámetro");
		} catch (SQLException e) {
			throw new ServicesException("Error en la sentencia SQL");
			
		} finally
		{	try {
				if (con != null)
				{	con.close();
				
				}
			} catch (SQLException e) {
				throw new ServicesException("Error cerrando la conexión");
				
			}
		}
		
		// return HTTP response 200 in case of success
		return cliente;
	}
	
	/**
	 * Servicio que cambia el password del cliente
	 * @param cliente: datos que se modificarán en el registro del cliente
	 * @param nuevo: nueva contraseña	 
	 * @return Respuesta HTTP devuelta: 200 Éxito, 400 y 500 Error en el servidor, 404 No encontrado
	 */
	@POST
	@Path("/cambiarPassword")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response cambiarPassword(PasswordChangeRequest data) throws ServicesException {
		Connection con = null;
		try {
			con  = DataServiceHelper.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE clientes SET  passcliente = ?"
					+ " 	WHERE emailcliente = ?");
			
			ps.setString(1, data.getNuevoPassword());
			ps.setString(2, data.getCliente().getEmailCliente());

			if (ps.executeUpdate() != 0)
			{	System.out.println("Data Received: " + data.toString());	
				// return HTTP response 200 in case of success
					return Response.status(200).entity(data.toString()).build();
			}
			
			else
			{	throw new ServicesException("Error cambiando el password");
			}
		} catch (ClassNotFoundException e) {
			throw new ServicesException("Error en el objeto Cliente pasado como parámetro");
			
		} catch (SQLException e) {
			throw new ServicesException("Error en la sentencia SQL");
		} finally
		{	try {
				if (con != null)
				{	con.close();
				
				}
			} catch (SQLException e) {
				throw new ServicesException("Error cerrando la conexión");
			}
		}
		
	}
	
	
	
}


