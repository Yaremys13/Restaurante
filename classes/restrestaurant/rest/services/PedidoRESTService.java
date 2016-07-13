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
import rest.model.DetallePedido;
import rest.model.Pedido;
import rest.model.PedidoCompleto;
import rest.model.Plato;
import rest.utilities.DataServiceHelper;
import restrestaurant.res.excepcions.ServicesException;
/**
 *  Esta clase define los endpoints/servicios para manipular Pedidos del Restaurante
 * 	@author Natasha Martinez, Ronny Ayala
 *  @version 1.0
 * 
 *  
 **/
@Path("pedido")
public class PedidoRESTService {
	
	/** 
	 * Servicio que inserta un pedido en la base de datos 
	 * @param Pedido pedido: contiene los valores del pedido que se quiere crear 
	 **/
	@POST
	@Path("/createPedido")	
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createPedido(PedidoCompleto pedidoCompleto) throws ServicesException {
		Connection con = null;
		try {
			Pedido pedido = pedidoCompleto.getPedido();
			List<DetallePedido> detalles = pedidoCompleto.getDetalles();
			con  = DataServiceHelper.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("INSERT INTO pedidos VALUES (?,?,?,?,?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setLong(1, new Long(0));
			ps.setLong(2, pedido.getCliente().getIdCliente());
			ps.setString(3, pedido.getFechapedido());
			ps.setString(4, pedido.getHorapedido());
			ps.setDouble(5, pedido.getSubtotal().doubleValue());
			ps.setDouble(6, pedido.getIva().doubleValue());
			ps.setString(7, "1");
			Long idDp = new Long(0);
			if (ps.executeUpdate() != 0)
			{	ResultSet rs = ps.getGeneratedKeys();
				if (rs.next())
				{	idDp = rs.getLong(1);				
					System.out.println("Data Received: " + pedido.toString());	
					for(DetallePedido dp : detalles)
					{	ps = con.prepareStatement("INSERT INTO detallespedido VALUES (?,?,?,?)");
						ps.setLong(1, idDp);
						ps.setLong(2, dp.getPlato().getIdplato());
						ps.setDouble(3, dp.getCant());
						ps.setString(4, "1");					
						if (ps.executeUpdate() != 0)
						{	System.out.println("Data Received: " + dp.toString());				
						}
						else
						{	System.out.println("Error guardando detalle pedido");
							break;
						}
					}
					// return HTTP response 200 in case of success
					return Response.status(200).entity(pedido.toString()).build();
				}
				else
				{	throw new ServicesException("Error creando el Detalle del Pedido");				
				}
			}
			
			else
			{	throw new ServicesException("Error creando el Pedido");
			}
		} catch (ClassNotFoundException e) {
			throw new ServicesException("Error en el objeto Pedido pasado como parámetro");
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
	 * Servicio que obtiene un pedido específico de la base de datos 
	 * @param Long idPedido: pedido que se busca
	 * @return Pedido encontrado
	 * */
	@GET
	@Path("/getPedido/{idPedido}")
	
	@Produces(MediaType.APPLICATION_JSON)
	public PedidoCompleto getPedido(@PathParam("idPedido") Long idPedido) throws ServicesException {
		
		PedidoCompleto pedidoC = new PedidoCompleto();
		Pedido pedido = new Pedido();
		Connection con = null;
		try {
			List<DetallePedido> detalles = new ArrayList<DetallePedido>();
			Clientes cliente = new Clientes();
			System.out.println("Data Received: " + idPedido);
			
			
			con  = DataServiceHelper.getInstance().getConnection();
			//Buscando el pedido
			PreparedStatement ps = con.prepareStatement("SELECT * FROM pedidos WHERE idpedido = ? AND estatus = '1'");
			ps.setLong(1, idPedido);
			
			ResultSet rs = ps.executeQuery();
			if (rs.next())
			{	pedido.setIdpedido(rs.getLong(1));
				
				ps = con.prepareStatement("SELECT * FROM clientes WHERE idcliente= ? AND estatus = '1'");
				ps.setLong(1, rs.getLong(2));
			
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
					cliente.setEstatus(rs1.getString(8));
					
				}
				
				pedido.setCliente(cliente);
				pedido.setFechapedido(rs.getString(3));
				pedido.setHorapedido(rs.getString(4));
				pedido.setSubtotal(new BigDecimal(rs.getDouble(5)));
				pedido.setIva(new BigDecimal(rs.getDouble(6)));
				pedido.setEstatus(rs.getString(7));
				
				//Buscando los detalles del pedido
				ps = con.prepareStatement("SELECT * FROM detallespedido WHERE idpedido = ? AND estatus = '1'");
				ps.setLong(1, rs.getLong(1));
				
				ResultSet rs2 = ps.executeQuery();
				while (rs2.next())
				{	DetallePedido dp = new DetallePedido();
					dp.setPedido(pedido);
					
					ps = con.prepareStatement("SELECT * FROM platos WHERE idplato = ? AND estatus = '1'");
					ps.setLong(1, rs2.getLong(2));
					ResultSet rs3 = ps.executeQuery();
					
					if (rs3.next())
					{	Plato p = new Plato();
						p.setIdplato(rs3.getLong(1));
						p.setNomplato(rs3.getString(2));
						p.setDescplato(rs3.getString(3));
						p.setPrecplato(new BigDecimal(rs3.getDouble(4)));
						p.setFecha(rs3.getString(5));
						p.setImgplato(rs3.getString(6));
						p.setEstatus(rs3.getString(7));
						
						dp.setPlato(p);
						
					}
					
					dp.setCant(rs2.getDouble(3));
					dp.setEstatus(rs2.getString(4));
					
					detalles.add(dp);
				}
				pedidoC.setPedido(pedido);
				pedidoC.setDetalles(detalles);			
				
				Response.status(200).entity(pedido.toString()).build();
			}
			
			else
			{	throw new ServicesException("Pedido no encontrado");
			}
		} catch (ClassNotFoundException e) {
			throw new ServicesException("Error en el objeto Pedido pasado como parámetro");
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
		return pedidoC;
	}
 
	/**
	 * Servicio que obtiene los pedidos registrados en la base de datos 
	 * @return Lista de pedidos encontrados
	 */
	@GET
	@Path("/getPedidosAll")
	@Produces(MediaType.APPLICATION_JSON)
	public List<PedidoCompleto> getPedidosAll() throws ServicesException {
		
		List<PedidoCompleto> pedidosCompletos = new ArrayList<PedidoCompleto>();
		
		Clientes cliente = new Clientes();
		
		Connection con = null;
		try {
			con  = DataServiceHelper.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM pedidos WHERE estatus = '1'");
			ResultSet rs = ps.executeQuery();
			if (rs.next())
			{
				rs.beforeFirst();
				while (rs.next())
				{	PedidoCompleto pedidoCompleto = new PedidoCompleto();
					Pedido pedido = new Pedido();
					List<DetallePedido> detalles = new ArrayList<DetallePedido>();
					
					pedido.setIdpedido(rs.getLong(1));
					
					ps = con.prepareStatement("SELECT * FROM clientes WHERE idcliente= ? AND estatus = '1'");
					ps.setLong(1, rs.getLong(2));
				
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
						cliente.setEstatus(rs1.getString(8));
						
					}
					
					pedido.setCliente(cliente);
					pedido.setFechapedido(rs.getString(3));
					pedido.setHorapedido(rs.getString(4));
					pedido.setSubtotal(new BigDecimal(rs.getDouble(5)));
					pedido.setIva(new BigDecimal(rs.getDouble(6)));
					pedido.setEstatus(rs.getString(7));
					
					//Buscando los detalles del pedido
					ps = con.prepareStatement("SELECT * FROM detallespedido WHERE idpedido = ? AND estatus = '1'");
					ps.setLong(1, rs.getLong(1));
					
					ResultSet rs2 = ps.executeQuery();
					while (rs2.next())
					{	DetallePedido dp = new DetallePedido();
						dp.setPedido(pedido);
						
						ps = con.prepareStatement("SELECT * FROM platos WHERE idplato = ? AND estatus = '1'");
						ps.setLong(1, rs2.getLong(2));
						ResultSet rs3 = ps.executeQuery();
						
						if (rs3.next())
						{	Plato p = new Plato();
							p.setIdplato(rs3.getLong(1));
							p.setNomplato(rs3.getString(2));
							p.setDescplato(rs3.getString(3));
							p.setPrecplato(new BigDecimal(rs3.getDouble(4)));
							p.setFecha(rs3.getString(5));
							p.setImgplato(rs3.getString(6));
							p.setEstatus(rs3.getString(7));
							
							dp.setPlato(p);
							
						}
						
						dp.setCant(rs2.getDouble(3));
						dp.setEstatus(rs2.getString(4));
						
						detalles.add(dp);
					}
					
					pedidoCompleto.setPedido(pedido);
					pedidoCompleto.setDetalles(detalles);
					
					pedidosCompletos.add(pedidoCompleto);
				}
				Response.status(200).entity(pedidosCompletos.toString()).build();
			}
			else
			{	throw new ServicesException("No se consiguen Pedidos");
			}
		} catch (ClassNotFoundException e) {
			throw new ServicesException("Error en el objeto Pedido pasado como parámetro");
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
		return pedidosCompletos;
	}
	
	/**
	 * Servicio que inserta un detalle de pedido en la base de datos 
	 * @param idPedido: pedido al que se le agregará el detalle
	 * @param detallePedido: detalle del pedido que será agregado
	 * @return Respuesta HTTP devuelta: 200 Éxito, 400 y 500 Error en el servidor, 404 No encontrado
	 */
	@POST
	@Path("/addDetallePedido/{idPedido}")
	
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addPlatoPedido(@PathParam("idPedido") Long idPedido, DetallePedido detallePedido ) throws ServicesException {
		Connection con = null;
		try {
			con  = DataServiceHelper.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("INSERT INTO detallespedido VALUES  (?,?,?,?)");
			
			ps.setLong(1,idPedido);
			ps.setLong(2,detallePedido.getPlato().getIdplato());
			ps.setDouble(3, detallePedido.getCant());
			ps.setString(4,"1");
			
			if (ps.executeUpdate() != 0)
			{	System.out.println("Data Received DP: " + detallePedido.toString());	
				// return HTTP response 200 in case of success
					return Response.status(200).entity(detallePedido.toString()).build();
			}
			
			else
			{	throw new ServicesException("Error agregando el plato al pedido");
			}
		} catch (ClassNotFoundException e) {
			throw new ServicesException("Error en el objeto Pedido pasado como parámetro");
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
	 * 
	 * @param idPedido: pedido al que se le eliminará el detalle
	 * @param idDetalle: detalle del pedido que será agregado
	 * @return Respuesta HTTP devuelta: 200 Éxito, 400 y 500 Error en el servidor, 404 No encontrado
	 */
	@POST
	@Path("/deleteDetallePedido/{idPedido}/{idDetalle}")
	/* Servicio que elimina un detalle de pedido específico en la base de datos */
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deletePlatoPedido(@PathParam("idPedido") Long idPedido, @PathParam("idDetalle") Long idDetalle) throws ServicesException {
		Connection con = null;
		try {
			con  = DataServiceHelper.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE detallespedido SET  estatus = '0'"
					+ " 	WHERE idpedido = ? AND idplato = ?");
			ps.setLong(1, idPedido);
			ps.setLong(2, idDetalle);
			
			if (ps.executeUpdate() != 0)
			{	System.out.println("Data Received: " + idPedido + " " + idDetalle);	
				// return HTTP response 200 in case of success
					return Response.status(200).entity("Eliminado con exito").build();
			}
			
			else
			{	throw new ServicesException("Error eliminando el plato del pedido");
			}
		} catch (ClassNotFoundException e) {
			throw new ServicesException("Error en el objeto Pedido pasado como parámetro");
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
	 * Servicio que elimina un pedido específico de la base de datos
	 * @param idPedido: pedido que será eliminado
	 * @return Respuesta HTTP devuelta: 200 Éxito, 400 y 500 Error en el servidor, 404 No encontrado
	 */
	
	@POST
	@Path("/deletePedido/{idPedido}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deletePedido(@PathParam("idPedido") Long idPedido) throws ServicesException {
		Connection con = null;
		try {
			con  = DataServiceHelper.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE pedidos SET  estatus = '0'"
					+ " 	WHERE idpedido = ?");
			ps.setLong(1, idPedido);
			
			if (ps.executeUpdate() != 0)
			{	System.out.println("Data Received: " + idPedido);	
				// return HTTP response 200 in case of success
					return Response.status(200).entity("Eliminado con exito").build();
			}
			
			else
			{	throw new ServicesException("Error eliminando el Pedido");
			}
		} catch (ClassNotFoundException e) {
			throw new ServicesException("Error en el objeto Pedido pasado como parámetro");
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
	 * Servicio que actualiza el estatus del pedido en la base de datos
	 * @param idPedido: id del pedido que será actualizado
	 * @param estatus: estatus que se le actualizará
	 * @return
	 */
	@POST
	@Path("/updateEstatusPedido/{idPedido}/{estatus}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateEstatusPedido(@PathParam("idPedido") Long idPedido, @PathParam("estatus") String estatus) throws ServicesException {
		Connection con = null;
		try {
			con  = DataServiceHelper.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE pedidos SET  estatus = ? "
					+ " 	WHERE idpedido = ?");
			ps.setLong(2, idPedido);
			ps.setString(1, estatus);
			
			if (ps.executeUpdate() != 0)
			{	System.out.println("Data Received: " + idPedido + " " + estatus );	
				// return HTTP response 200 in case of success
					return Response.status(200).entity("Actualizado con exito").build();
			}
			
			else
			{	throw new ServicesException("Error actualizando el estatus del pedido");
			}
		} catch (ClassNotFoundException e) {
			throw new ServicesException("Error en el objeto Pedido pasado como parámetro");
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


