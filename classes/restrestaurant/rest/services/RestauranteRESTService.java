package restrestaurant.rest.services;


import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import rest.model.Restaurante;
import rest.utilities.DataServiceHelper;
import restrestaurant.res.excepcions.ServicesException;

/**
 *  Esta clase define los endpoints/servicios para manipular los datos básicos del Restaurante
 * 	@author Natasha Martinez, Ronny Ayala
 *  @version 1.0
 *    
 **/

@Path("restaurante")
public class RestauranteRESTService {
	
	/**
	 * Servicio que permite registrar un Restaurante en la base de datos
	 * @param restaurante: datos del Restaurante que se almacenarán
	 * @return Respuesta HTTP devuelta: 200 Éxito, 400 y 500 Error en el servidor, 404 No encontrado
	 */
	@POST
	@Path("/createRestaurante")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createRestaurante(Restaurante restaurante) throws ServicesException {
		Connection con = null;
		try {
			con  = DataServiceHelper.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("INSERT INTO restaurantes VALUES (?,?,?,?,?,?,?,?,?)");
			ps.setLong(1, new Long(0));
			ps.setString(2, restaurante.getNomrestaurante());
			ps.setString(3, restaurante.getDirrestaurante());
			ps.setDouble(4, restaurante.getLatrestaurante().doubleValue());
			ps.setDouble(5, restaurante.getLngrestaurante().doubleValue());
			ps.setString(6, restaurante.getEmailrestaurante());
			ps.setString(7, restaurante.getTwitterrestaurante());
			ps.setString(8, restaurante.getFbrestaurante());
			ps.setString(9, restaurante.getInstrestaurante());
			ps.setString(10, "1");

			if (ps.executeUpdate() != 0)
			{	System.out.println("Data Received: " + restaurante.toString());	
				// return HTTP response 200 in case of success
					return Response.status(200).entity(restaurante.toString()).build();
			}
			
			else
			{	throw new ServicesException("Error creando el Restaurante ");
			}
		} catch (ClassNotFoundException e) {
			throw new ServicesException("Error en el objeto Restaurante pasado como parámetro");
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
	 * Servicio que permite consultar los datos de un Restaurante 
	 * @param idRestaurante: id único del Restaurante a consultar
	 * @return Restaurante encontrado
	 */
	@GET
	@Path("/getRestaurante/{idRestaurante}")
	@Produces(MediaType.APPLICATION_JSON)
	public Restaurante getRestaurante(@PathParam("idRestaurante") Long idRestaurante) throws ServicesException {
		
		Restaurante restaurante = new Restaurante();
		Connection con = null;
		try {
			System.out.println("Data Received: " + idRestaurante);
			con  = DataServiceHelper.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM restaurantes WHERE idrestaurante = ? AND estatus = '1'");
			ps.setLong(1, idRestaurante);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
			{	restaurante.setIdrestaurante(rs.getLong(1));
				restaurante.setNomrestaurante(rs.getString(2));
				restaurante.setDirrestaurante(rs.getString(3));
				restaurante.setLatrestaurante(new BigDecimal(rs.getDouble(4)));
				restaurante.setLngrestaurante(new BigDecimal(rs.getDouble(5)));
				restaurante.setEmailrestaurante(rs.getString(6));
				restaurante.setTwitterrestaurante(rs.getString(7));
				restaurante.setFbrestaurante(rs.getString(8));
				restaurante.setInstrestaurante(rs.getString(9));
				restaurante.setEstatus(rs.getString(10));				
				Response.status(200).entity(restaurante.toString()).build();
			}
			
			else
			{	throw new ServicesException("No se encuentra el Restaurante");
			}
		} catch (ClassNotFoundException e) {
			throw new ServicesException("Error en el objeto Restaurante pasado como parámetro");
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
		return restaurante;
	}
 
	
}


