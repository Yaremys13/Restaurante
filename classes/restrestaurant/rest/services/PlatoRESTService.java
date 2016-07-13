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
import rest.model.Plato;
import rest.utilities.DataServiceHelper;
import restrestaurant.res.excepcions.ServicesException;

/**
 *  Esta clase define los endpoints/servicios para manipular los datos básicos de los Platos servidors en el Restaurante
 * 	@author Natasha Martinez, Ronny Ayala
 *  @version 1.0
 *    
 **/

@Path("plato")
public class PlatoRESTService {

	/**
	 * Servicio que permite registrar un Plato en la base de datos
	 * @param plato: datos del plato a registrar
	 * @return Respuesta HTTP devuelta: 200 Éxito, 400 y 500 Error en el servidor, 404 No encontrado
	 */	
	@POST
	@Path("/createPlato")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createPlato(Plato plato) throws ServicesException  {
		Connection con = null;
		try {
			con  = DataServiceHelper.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("INSERT INTO platos VALUES (?,?,?,?,?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setLong(1, new Long(0));
			ps.setString(2, plato.getNomplato());
			ps.setString(3, plato.getDescplato());
			ps.setDouble(4, plato.getPrecplato().doubleValue());
			ps.setString(5, plato.getFecha());
			ps.setString(6, plato.getImgplato());
			ps.setString(7, "1");

			if (ps.executeUpdate() != 0)
			{	ResultSet rs = ps.getGeneratedKeys();
				Integer idPlato;
				if (rs.next());
				{	idPlato = rs.getInt(1);
				}
				System.out.println("Data Received: " + plato.toString());
				for(Ingrediente i : plato.getIngredientes())
				{	ps = con.prepareStatement("INSERT INTO ingrediente_plato VALUES (?,?,?,?,?)");
					ps.setLong(1, i.getIdingrediente());
					ps.setLong(2, idPlato);
					ps.setInt(3, 0);
					ps.setString(4, "");
					ps.setString(5, "1");					
					if (ps.executeUpdate() != 0)
					{	System.out.println("Data Received: " + i.toString());				
					}
					else
					{	System.out.println("Error guardando ingrediente");
						break;
					}
				}
				// return HTTP response 200 in case of success
				return Response.status(200).entity(plato.toString()).build();
			}
			
			else
			{	throw new ServicesException("Error guardando Plato");
			}
		} catch (ClassNotFoundException e) {
			throw new ServicesException("Error en el objeto Plato pasado como parámetro");
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
	 * Servicio que devuelve el Plato buscado 
	 * @param idPlato: id único del plato que se busca
	 * @return Plato encontrado
	 */
	@GET
	@Path("/getPlato/{idPlato}")
	@Produces(MediaType.APPLICATION_JSON)
	public Plato getPlato(@PathParam("idPlato") String idPlato) throws ServicesException  {
		
		Plato plato = new Plato();
		Connection con = null;
		try {
			System.out.println("Data Received: " + idPlato);
			List<Ingrediente> ingredientes = new ArrayList<Ingrediente>();
			Clientes cliente = new Clientes();
			con  = DataServiceHelper.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM platos WHERE idplato = ? AND estatus = '1'");
			ps.setString(1, idPlato);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
			{	plato.setIdplato(rs.getLong(1));
				plato.setNomplato(rs.getString(2));
				plato.setDescplato(rs.getString(3));
				plato.setPrecplato(new BigDecimal(rs.getDouble(4)));
				plato.setFecha(rs.getString(5));
				plato.setImgplato(rs.getString(6));
				plato.setEstatus(rs.getString(7));	
				
				ResultSet rs1 = ps.executeQuery();
				while (rs1.next())
				{	ps = con.prepareStatement("SELECT * FROM ingrediente_plato WHERE idplato = ? AND estatus = '1'");
					ps.setLong(1, rs1.getLong(1));
					ResultSet rs2 = ps.executeQuery();
					
					while (rs2.next())
					{	ps = con.prepareStatement("SELECT * FROM ingredientes WHERE idingrediente = ? AND estatus = '1'");
						ps.setLong(1, rs2.getLong(1));
					
						ResultSet rs3 = ps.executeQuery();
						while (rs3.next())
						{	Ingrediente ingrediente = new Ingrediente();
							ingrediente.setIdingrediente(rs3.getLong(1));
							ingrediente.setNomingrediente(rs3.getString(2));
							ingrediente.setDescingrediente(rs3.getString(3));
							ingrediente.setTipoingrediente(rs3.getString(4));
							ingrediente.setPrecioingrediente(new BigDecimal(rs3.getDouble(5)));
							ingrediente.setFecha(rs3.getString(6));
							ingrediente.setCantstock(new BigDecimal(rs3.getDouble(7)));
							ingrediente.setEstatus(rs3.getString(8));					
													
							ps = con.prepareStatement("SELECT * FROM clientes WHERE idcliente = ? AND estatus = '1'");
							ps.setLong(1, rs3.getLong(9));
							
							ResultSet rs4 = ps.executeQuery();
							if (rs4.next())
							{	cliente = new Clientes();
								cliente.setIdCliente(rs4.getLong(1));
								cliente.setNomCliente(rs4.getString(2));
								cliente.setApeCliente(rs4.getString(3));
								cliente.setCedCliente(rs4.getString(4));
								cliente.setTelCliente(rs4.getString(5));
								cliente.setEmailCliente(rs4.getString(6));
								cliente.setPassCliente(rs4.getString(7));
								cliente.setTipoCliente(rs4.getString(8));
								cliente.setEstatus(rs4.getString(9));				
								
							}	
							ingrediente.setCliente(cliente);
							ingredientes.add(ingrediente);
							
						}
						
						plato.setIngredientes(ingredientes);
				
					}
				}
				Response.status(200).entity(plato.toString()).build();
			}
			
			else
			{	throw new ServicesException("Plato no encontrado");
			}
		} catch (ClassNotFoundException e) {
			throw new ServicesException("Error en el objeto Plato pasado como parámetro");
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
		return plato;
	}
 
	/**
	 * Servicio que devuelve todos los Platos registrados
	 * @return Lista de Platos encontrados
	 */
	
	@GET
	@Path("/getPlatosAll")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Plato> getPlatosAll() throws ServicesException {
		
		List<Plato> platos = new ArrayList<Plato>();
		List<Ingrediente> ingredientes = new ArrayList<Ingrediente>();
		Clientes cliente = new Clientes();
		Connection con = null;
		try {
			con  = DataServiceHelper.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM platos WHERE estatus = '1'");
			ResultSet rs = ps.executeQuery();
			if (rs.next())
			{	rs.beforeFirst();
				while (rs.next())
				{	Plato plato = new Plato();
					plato.setIdplato(rs.getLong(1));
					plato.setNomplato(rs.getString(2));
					plato.setDescplato(rs.getString(3));
					plato.setPrecplato(new BigDecimal(rs.getDouble(4)));
					plato.setFecha(rs.getString(5));
					plato.setImgplato(rs.getString(6));
					plato.setEstatus(rs.getString(7));		
					
					
					ps = con.prepareStatement("SELECT * FROM ingrediente_plato WHERE idplato = ? AND estatus = '1'");
					ps.setLong(1, rs.getLong(1));
					ResultSet rs2 = ps.executeQuery();
					
					while (rs2.next())
					{	ps = con.prepareStatement("SELECT * FROM ingredientes WHERE idingrediente = ? AND estatus = '1'");
						ps.setLong(1, rs2.getLong(1));
					
						ResultSet rs3 = ps.executeQuery();
						while (rs3.next())
						{	Ingrediente ingrediente = new Ingrediente();
							ingrediente.setIdingrediente(rs3.getLong(1));
							ingrediente.setNomingrediente(rs3.getString(2));
							ingrediente.setDescingrediente(rs3.getString(3));
							ingrediente.setTipoingrediente(rs3.getString(4));
							ingrediente.setPrecioingrediente(new BigDecimal(rs3.getDouble(5)));
							ingrediente.setFecha(rs3.getString(6));
							ingrediente.setCantstock(new BigDecimal(rs3.getDouble(7)));
							ingrediente.setEstatus(rs3.getString(8));
							
							ps = con.prepareStatement("SELECT * FROM clientes WHERE idcliente = ? AND estatus = '1'");
							ps.setLong(1, rs3.getLong(9));
							
							ResultSet rs4 = ps.executeQuery();
							if (rs4.next())
							{	cliente = new Clientes();
								cliente.setIdCliente(rs4.getLong(1));
								cliente.setNomCliente(rs4.getString(2));
								cliente.setApeCliente(rs4.getString(3));
								cliente.setCedCliente(rs4.getString(4));
								cliente.setTelCliente(rs4.getString(5));
								cliente.setEmailCliente(rs4.getString(6));
								cliente.setPassCliente(rs4.getString(7));
								cliente.setTipoCliente(rs4.getString(8));
								cliente.setEstatus(rs4.getString(9));				
								
							}	
							ingrediente.setCliente(cliente);
							ingredientes.add(ingrediente);
						
						}
							
						plato.setIngredientes(ingredientes);
					}
					
					platos.add(plato);
				}
				Response.status(200).entity(platos.toString()).build();
			}
			else
			{	throw new ServicesException("No se encuentran platos");
			
			}
		} catch (ClassNotFoundException e) {
			throw new ServicesException("Error en el objeto Plato pasado como parámetro");
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
		return platos;
	}
	
	/**
	 * Servicio que actualiza un Plato específico
	 * @param idPlato: id único del Plato que se quiere modificar
	 * @param plato: datos a modificar del Plato
	 * @return Respuesta HTTP devuelta: 200 Éxito, 400 y 500 Error en el servidor, 404 No encontrado
	 */
	
	@POST
	@Path("/updatePlato/{idPlato}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updatePlato(@PathParam("idPlato") Long idPlato, Plato plato) throws ServicesException {
		Connection con = null;
		try {
			con  = DataServiceHelper.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE platos SET  nomplato = ?, "
					+ " descplato = ?, precplato = ?, fecha = ?, imgplato = ?, estatus = ?"
					+ " 	WHERE idplato = ?");
						
			ps.setString(1, plato.getNomplato());
			ps.setString(2, plato.getDescplato());
			ps.setDouble(3, plato.getPrecplato().doubleValue());
			ps.setString(4, plato.getFecha());
			ps.setString(5, plato.getImgplato());
			ps.setString(6, plato.getEstatus());
			ps.setLong(7, idPlato);

			if (ps.executeUpdate() != 0)
			{	System.out.println("Data Received: " + plato.toString());	
				// return HTTP response 200 in case of success
					return Response.status(200).entity(plato.toString()).build();
			}
			
			else
			{	throw new ServicesException("Error actualizando Plato");
			}
		} catch (ClassNotFoundException e) {
			throw new ServicesException("Error en el objeto Plato pasado como parámetro");
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
	 * Servicio que agrega un Ingrediente a un Plato específico
	 * @param idIngrediente: id único del Ingrediente que se quiere agregar
	 * @param idPlato: id único del Plato al que se agregará el Ingrediente
	 * @return Respuesta HTTP devuelta: 200 Éxito, 400 y 500 Error en el servidor, 404 No encontrado
	 */
	@POST
	@Path("/addIngredientePlato/{idIngrediente}/{idPlato}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addIngredientePlato(@PathParam ("idIngrediente") Long idIngrediente, @PathParam ("idPlato") Long idPlato) throws ServicesException {
		Connection con = null;
		try {
			con  = DataServiceHelper.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("INSERT INTO ingrediente_plato VALUES  (?,?,?,?,?)");
			
			ps.setLong(1,idIngrediente);
			ps.setLong(2,idPlato);	
			ps.setInt(3, 0);
			ps.setString(4, "");
			ps.setString(5,"1");
			
			if (ps.executeUpdate() != 0)
			{	System.out.println("Data Received: " + idPlato.toString());	
				// return HTTP response 200 in case of success
					return Response.status(200).entity(idPlato.toString()).build();
			}
			
			else
			{	throw new ServicesException("Error agregando el Ingrediente al Plato");
			}
		} catch (ClassNotFoundException e) {
			throw new ServicesException("Error en el objeto Plato pasado como parámetro");
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
	 * Servicio que elimina un Ingrediente a un Plato específico
	 * @param idIngrediente: id único del Ingrediente que se quiere eliminar
	 * @param idPlato: id único del Plato al que se eliminará el Ingrediente
	 * @return Respuesta HTTP devuelta: 200 Éxito, 400 y 500 Error en el servidor, 404 No encontrado
	 */
	@POST
	@Path("/deleteIngredientePlato/{idPlato}/{idIngrediente}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteIngredientePlato(@PathParam("idPlato") Long idPlato, @PathParam("idIngrediente") Long idIngrediente) throws ServicesException {
		Connection con = null;
		try {
			con  = DataServiceHelper.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE ingrediente_plato SET  estatus = '0'"
					+ " 	WHERE idplato = ? AND idingrediente = ?");
			ps.setLong(1, idPlato);
			ps.setLong(2, idIngrediente);
			
			if (ps.executeUpdate() != 0)
			{	System.out.println("Data Received: " + idIngrediente);	
				// return HTTP response 200 in case of success
					return Response.status(200).entity("Eliminado con exito").build();
			}
			
			else
			{	throw new ServicesException("Error eliminando el Ingrediente del Plato");
			}
		} catch (ClassNotFoundException e) {
			throw new ServicesException("Error en el objeto Plato pasado como parámetro");
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
	 * Servicio que elimina un Plato registrado
	 * @param idPlato: id único del Plato que se quiere eliminar
	 * @return Respuesta HTTP devuelta: 200 Éxito, 400 y 500 Error en el servidor, 404 No encontrado
	 */
	@POST
	@Path("/deletePlato/{idPlato}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deletePlato(@PathParam("idPlato") Long idPlato) throws ServicesException {
		Connection con = null;
		try {
			con  = DataServiceHelper.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE platos SET  estatus = '0'"
					+ " 	WHERE idplato = ? ");
			ps.setLong(1, idPlato);
			
			if (ps.executeUpdate() != 0)
			{	System.out.println("Data Received: " + idPlato);	
				// return HTTP response 200 in case of success
					return Response.status(200).entity("Eliminado con exito").build();
			}
			
			else
			{	throw new ServicesException("Error eliminando el Plato");
			}
		} catch (ClassNotFoundException e) {
			throw new ServicesException("Error en el objeto Plato pasado como parámetro");
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


