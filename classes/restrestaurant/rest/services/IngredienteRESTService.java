package restrestaurant.rest.services;

import java.math.BigDecimal;
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
import rest.model.Ingrediente;
import rest.utilities.DataServiceHelper;
import restrestaurant.res.excepcions.ServicesException;

/**
 *  Esta clase define los endpoints/servicios para manipular los Ingredientes de los 
 *  platos que se sirven en el Restaurante
 * 	@author Natasha Martinez, Ronny Ayala
 *  @version 1.0
 * 
 *  
 **/
@Path("ingrediente")
public class IngredienteRESTService {
	
	/**
	 * Servicio que permite insertar un Ingrediente en la base de datos
	 * @param ingrediente: datos del ingrediente que se quiere insertar
	 * @return Respuesta HTTP devuelta: 200 Éxito, 400 y 500 Error en el servidor, 404 No encontrado
	 */
	
	@POST
	@Path("/createIngrediente")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createIngrediente(Ingrediente ingrediente) throws ServicesException {
		if (ingrediente.getCliente() != null && ingrediente.getCliente().getTipoCliente() != null 
				&& ingrediente.getCliente().getTipoCliente().toUpperCase().equals("ADMINISTRADOR"))
		{
			Connection con = null;
			try {
				con  = DataServiceHelper.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement("INSERT INTO ingredientes VALUES (?,?,?,?,?,?,?,?,?)");
				ps.setLong(1, new Long(0));
				ps.setString(2, ingrediente.getNomingrediente());
				ps.setString(3, ingrediente.getDescingrediente());
				ps.setString(4, ingrediente.getTipoingrediente());
				ps.setDouble(5, ingrediente.getPrecioingrediente().doubleValue());
				ps.setString(6, ingrediente.getFecha());
				ps.setDouble(7, ingrediente.getCantstock().doubleValue());
				ps.setString(8, "1");
				ps.setLong(9, ingrediente.getCliente().getIdCliente());
				if (ps.executeUpdate() != 0)
				{	System.out.println("Data Received: " + ingrediente.toString());	
					// return HTTP response 200 in case of success
						return Response.status(200).entity(ingrediente.toString()).build();
				}
				
				else
				{	throw new ServicesException("Error creando el ingrediente");
				}
			} catch (ClassNotFoundException e) {
				throw new ServicesException("Error en el objeto Ingrediente pasado como parámetro");
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
		else
		{
			throw new ServicesException("El usuario no tiene permiso para crear el Ingrediente");
		}
	}
	
	/**
	 * Servicio que devuelve el Ingrediente buscado segun un id único
	 * @param idIngrediente: id único del Ingrediente
	 * @return
	 */
 
	@GET
	@Path("/getIngrediente/{idIngrediente}")
	@Produces(MediaType.APPLICATION_JSON)
	public Ingrediente getIngrediente(@PathParam("idIngrediente") Long idIngrediente) throws ServicesException{
		
		Ingrediente ingrediente = new Ingrediente();
		Clientes cliente = new Clientes();
		Connection con = null;
		try {
			System.out.println("Data Received: " + idIngrediente);
			con  = DataServiceHelper.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM ingredientes WHERE idingrediente = ? AND estatus = '1'");
			ps.setLong(1, idIngrediente);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
			{	ingrediente.setIdingrediente(rs.getLong(1));
				ingrediente.setNomingrediente(rs.getString(2));
				ingrediente.setDescingrediente(rs.getString(3));
				ingrediente.setTipoingrediente(rs.getString(4));
				ingrediente.setPrecioingrediente(new BigDecimal(rs.getDouble(5)));
				ingrediente.setFecha(rs.getString(6));
				ingrediente.setCantstock(new BigDecimal(rs.getDouble(7)));
				ingrediente.setEstatus(rs.getString(8));	
				
				ps = con.prepareStatement("SELECT * FROM clientes WHERE idcliente = ? AND estatus = '1'");
				ps.setLong(1, rs.getLong(9));
				
				ResultSet rs1 = ps.executeQuery();
				if (rs1.next())
				{	
					cliente.setIdCliente(rs1.getLong(1));
					cliente.setNomCliente(rs1.getString(2));
					cliente.setApeCliente(rs1.getString(3));
					cliente.setCedCliente(rs1.getString(4));
					cliente.setTelCliente(rs1.getString(5));
					cliente.setEmailCliente(rs1.getString(6));
					cliente.setPassCliente(rs1.getString(7));
					cliente.setTipoCliente(rs1.getString(8));
					cliente.setEstatus(rs1.getString(9));
				}
				ingrediente.setCliente(cliente);
				
				Response.status(200).entity(ingrediente.toString()).build();
			}
			
			else
			{	throw new ServicesException("Ingrediente no encontrado");
			}
		} catch (ClassNotFoundException e) {
			throw new ServicesException("Error en el objeto Ingrediente pasado como parámetro");
			
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
		return ingrediente;
	}
	/**
	 * Servicio que devuelve la lista de todos los Ingredientes registrados
	 * @return Lista de los Ingredientes encontrados
	 */
	@GET
	@Path("/getIngredientesAll")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Ingrediente> getIngredientesAll() throws ServicesException {
		
		List<Ingrediente> ingredientes = new ArrayList<Ingrediente>();
		Clientes cliente = new Clientes();
		Connection con = null;
		try {
			con  = DataServiceHelper.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM ingredientes WHERE estatus = '1'");
			ResultSet rs = ps.executeQuery();
			if (rs.next())
			{	rs.beforeFirst();
				while (rs.next())
				{	Ingrediente ingrediente = new Ingrediente();
					ingrediente.setIdingrediente(rs.getLong(1));
					ingrediente.setNomingrediente(rs.getString(2));
					ingrediente.setDescingrediente(rs.getString(3));
					ingrediente.setTipoingrediente(rs.getString(4));
					ingrediente.setPrecioingrediente(new BigDecimal(rs.getDouble(5)));
					ingrediente.setFecha(rs.getString(6));
					ingrediente.setCantstock(new BigDecimal(rs.getDouble(7)));
					ingrediente.setEstatus(rs.getString(8));							
					
					
					ps = con.prepareStatement("SELECT * FROM clientes WHERE idcliente = ? AND estatus = '1'");
					ps.setLong(1, rs.getLong(9));
					
					ResultSet rs1 = ps.executeQuery();
					if (rs1.next())
					{	cliente = new Clientes();
						cliente.setIdCliente(rs1.getLong(1));
						cliente.setNomCliente(rs1.getString(2));
						cliente.setApeCliente(rs1.getString(3));
						cliente.setCedCliente(rs1.getString(4));
						cliente.setTelCliente(rs1.getString(5));
						cliente.setEmailCliente(rs1.getString(6));
						cliente.setPassCliente(rs1.getString(7));
						cliente.setTipoCliente(rs1.getString(8));
						cliente.setEstatus(rs1.getString(9));
					}
					ingrediente.setCliente(cliente);
					
					ingredientes.add(ingrediente);
				}
				Response.status(200).entity(ingredientes.toString()).build();
			}
			else
			{	throw new ServicesException("No se consiguieron Ingredientes");
				
			}
		} catch (ClassNotFoundException e) {
			throw new ServicesException("Error en el objeto Ingrediente pasado como parámetro");
			
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
		return ingredientes;
	}
	
	/**
	 * Servicio que actualiza los datos del Ingrediente
	 * @param idIngrediente: id único del Ingrediente a modificar
	 * @param ingrediente: datos que se modificarán del Ingrediente
	 * @return Respuesta HTTP devuelta: 200 Éxito, 400 y 500 Error en el servidor, 404 No encontrado
	 */
	@POST
	@Path("/updateIngrediente/{idIngrediente}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateIngrediente(@PathParam("idIngrediente") Long idIngrediente, Ingrediente ingrediente) throws ServicesException {
		Connection con = null;
		try {
			con  = DataServiceHelper.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE ingredientes SET  nomingrediente = ?, "
					+ " descingrediente = ?, tipoingrediente = ?, precioingrediente = ?, fecha = ?, cantstock = ?, estatus = ?"
					+ " idcliente = ?	WHERE idingrediente = ?");
					
			ps.setString(1,ingrediente.getNomingrediente());
			ps.setString(2,ingrediente.getDescingrediente());
			ps.setString(3,ingrediente.getTipoingrediente());
			ps.setDouble(4,ingrediente.getPrecioingrediente().doubleValue());
			ps.setString(5,ingrediente.getFecha());
			ps.setDouble(6,ingrediente.getCantstock().doubleValue());
			ps.setString(7,ingrediente.getEstatus());				
			ps.setLong(8,ingrediente.getCliente().getIdCliente());
			ps.setLong(9,idIngrediente);
			
			if (ps.executeUpdate() != 0)
			{	System.out.println("Data Received: " + ingrediente.toString());	
				// return HTTP response 200 in case of success
					return Response.status(200).entity(ingrediente.toString()).build();
			}
			
			else
			{	throw new ServicesException("Error actualizando ingrediente");
			}
		} catch (ClassNotFoundException e) {
			throw new ServicesException("Error en el objeto pasado como parámetro");
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
	 * Servicio que elimina un Ingrediente
	 * @param idIngrediente: id único del Ingrediente que se quiere eliminar
	 * @return Respuesta HTTP devuelta: 200 Éxito, 400 y 500 Error en el servidor, 404 No encontrado
	 */
	
	@POST
	@Path("/deleteIngrediente/{idIngrediente}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteIngrediente(@PathParam("idIngrediente") String idIngrediente) throws ServicesException {
		Connection con = null;
		try {
			con  = DataServiceHelper.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE ingredientes SET  estatus = '0'"
					+ " 	WHERE idingrediente = ?");
			ps.setString(1, idIngrediente);
			
			if (ps.executeUpdate() != 0)
			{	System.out.println("Data Received: " + idIngrediente);	
				// return HTTP response 200 in case of success
					return Response.status(200).entity("Eliminado con exito").build();
			}
			
			else
			{	throw new ServicesException("Error eliminando el Ingrediente");
			}
		} catch (ClassNotFoundException e) {
			throw new ServicesException("Error en el objeto pasado como parámetro");
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
	 * Servicio que permite descontar una cantidad utilizada de un ingrediente
	 * @param idIngrediente: id único del Ingrediente que se quiere modificar
	 * @param cant: cantidad del ingrediente a descontar
	 * @return Respuesta HTTP devuelta: 200 Éxito, 400 y 500 Error en el servidor, 404 No encontrado
	 */
	
	@POST
	@Path("/descontarIngrediente/{idIngrediente}/{cant}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response descontarIngrediente(@PathParam("idIngrediente") String idIngrediente, @PathParam("cant") Double cant) throws ServicesException {
		Connection con = null;
		try {
			con  = DataServiceHelper.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE ingredientes SET cantstock = (cantstock - " + cant 
					+ ") 	WHERE idingrediente = " + idIngrediente);
			
			
			if (ps.executeUpdate() != 0)
			{	System.out.println("Data Received: " + idIngrediente);	
				// return HTTP response 200 in case of success
					return Response.status(200).entity("Descontado con exito").build();
			}
			
			else
			{	throw new ServicesException("Error descontando el Ingrediente");
			}
		} catch (ClassNotFoundException e) {
			throw new ServicesException("Error en el objeto pasado como parámetro");
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


