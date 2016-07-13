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
import rest.model.Menu;
import rest.model.Plato;
import rest.utilities.DataServiceHelper;
import restrestaurant.res.excepcions.ServicesException;
/**
 *  Esta clase define los endpoints/servicios para manipular los datos básicos de los Menués que sirve el Restaurante
 * 	@author Natasha Martinez, Ronny Ayala
 *  @version 1.0
 *    
 **/ 
@Path("menu")
public class MenuRESTService {
	
	/**
	 * Servicio que crea el Menú en la base de datos
	 * @param menu: datos del Menú que se quiere agregar
	 * @return Respuesta HTTP devuelta: 200 Éxito, 400 y 500 Error en el servidor, 404 No encontrado
	 */
	@POST
	@Path("/createMenu")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createMenu(Menu menu) throws ServicesException {
		Connection con = null;
		try {
			con  = DataServiceHelper.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("INSERT INTO menu VALUES (?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setLong(1, new Long(0));
			ps.setString(2, menu.getFecha());
			ps.setString(3, "1");
			Long idMenu = new Long(0);
			if (ps.executeUpdate() != 0)
			{	ResultSet rs = ps.getGeneratedKeys();
				if (rs.next())
				{	idMenu = rs.getLong(1);
				}
				System.out.println("Data Received: " + menu.toString());	
				for(Plato p : menu.getPlatos())
				{	ps = con.prepareStatement("INSERT INTO menu_plato VALUES (?,?,?)");
					ps.setLong(1, idMenu);
					ps.setLong(2, p.getIdplato());
					ps.setString(3, "1");					
					if (ps.executeUpdate() != 0)
					{	System.out.println("Data Received: " + p.toString());				
					}
					else
					{	System.out.println("Error guardando plato");
						break;
					}
				}
				// return HTTP response 200 in case of success
				return Response.status(200).entity(menu.toString()).build();
			}
			
			else
			{	throw new ServicesException("Error creando el menú");
			}
		} catch (ClassNotFoundException e) {
			throw new ServicesException("Error en el objeto Menú pasado como parámetro");
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
	 * Servicio que devuelve un Menú específico
	 * @param idMenu: id único del Menú buscado
	 * @return Menú encontrado
	 */
	@GET
	@Path("/getMenu/{idMenu}")
	@Produces(MediaType.APPLICATION_JSON)
	public Menu getMenu(@PathParam("idMenu") Long idMenu) throws ServicesException {
		
		Menu menu = new Menu();
		Connection con = null;
		try {
			List<Plato> platos = new ArrayList<Plato>();
			List<Ingrediente> ingredientes = new ArrayList<Ingrediente>();
			Clientes cliente = new Clientes();
			System.out.println("Data Received: " + idMenu);
			con  = DataServiceHelper.getInstance().getConnection();
			//Buscando el menu
			PreparedStatement ps = con.prepareStatement("SELECT * FROM menu WHERE idmenu = ? AND estatus = '1'");
			ps.setLong(1, idMenu);
			
			ResultSet rs = ps.executeQuery();
			if (rs.next())
			{	menu.setIdmenu(rs.getLong(1));
				menu.setFecha(rs.getString(2));
				menu.setEstatus(rs.getString(3));
				
				//Buscando los platos del menu
				ps = con.prepareStatement("SELECT * FROM menu_plato WHERE idmenu = ? AND estatus = '1'");
				ps.setLong(1, rs.getLong(1));
				
				ResultSet rs1 = ps.executeQuery();
				while (rs1.next())
				{	ps = con.prepareStatement("SELECT * FROM platos WHERE idplato = ? AND estatus = '1'");
					ps.setLong(1, rs1.getLong(2));
					ResultSet rs2 = ps.executeQuery();
					
					while (rs2.next())
					{	Plato p = new Plato();
						p.setIdplato(rs2.getLong(1));
						p.setNomplato(rs2.getString(2));
						p.setDescplato(rs2.getString(3));
						p.setPrecplato(new BigDecimal(rs2.getDouble(4)));
						p.setFecha(rs2.getString(5));
						p.setImgplato(rs2.getString(6));
						p.setEstatus(rs2.getString(7));
						
						//Buscando los ingredientes de cada plato del menu
						ps = con.prepareStatement("SELECT * FROM ingrediente_plato WHERE idplato = ? AND estatus = '1'");
						ps.setLong(1, rs2.getLong(1));
						
						ResultSet rs3 = ps.executeQuery();
						while (rs3.next())
						{	
							//Buscando los detalles de ingredientes de cada plato del menu
							ps = con.prepareStatement("SELECT * FROM ingredientes WHERE idingrediente = ? AND estatus = '1'");
							ps.setLong(1, rs3.getLong(1));
							
							ResultSet rs4 = ps.executeQuery();
							
							while (rs4.next())
							{	Ingrediente ingrediente = new Ingrediente();
								ingrediente.setIdingrediente(rs4.getLong(1));
								ingrediente.setNomingrediente(rs4.getString(2));
								ingrediente.setDescingrediente(rs4.getString(3));
								ingrediente.setTipoingrediente(rs4.getString(4));
								ingrediente.setPrecioingrediente(new BigDecimal(rs4.getDouble(5)));
								ingrediente.setFecha(rs4.getString(6));
								ingrediente.setCantstock(new BigDecimal(rs4.getDouble(7)));
								ingrediente.setEstatus(rs4.getString(8));
								
								ps = con.prepareStatement("SELECT * FROM clientes WHERE idcliente = ? AND estatus = '1'");
								ps.setLong(1, rs4.getLong(9));
								
								ResultSet rs5 = ps.executeQuery();
								if (rs5.next())
								{	cliente = new Clientes();
									cliente.setIdCliente(rs5.getLong(1));
									cliente.setNomCliente(rs5.getString(2));
									cliente.setApeCliente(rs5.getString(3));
									cliente.setCedCliente(rs5.getString(4));
									cliente.setTelCliente(rs5.getString(5));
									cliente.setEmailCliente(rs5.getString(6));
									cliente.setPassCliente(rs5.getString(7));
									cliente.setTipoCliente(rs5.getString(8));
									cliente.setEstatus(rs5.getString(9));				
									
								}	
								ingrediente.setCliente(cliente);							
								
								ingredientes.add(ingrediente);
							}
							p.setIngredientes(ingredientes);
						}
						platos.add(p);
					}
				}
				menu.setPlatos(platos);			
				
				Response.status(200).entity(menu.toString()).build();
			}
			
			else
			{	throw new ServicesException("Menú no encontrado");
				
			}
		} catch (ClassNotFoundException e) {
			throw new ServicesException("Error en el objeto Menú pasado como parámetro");
			
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
		return menu;
	}
	/**
	 * Servicio que devuelve todos los Menués registrados en la base de datos
	 * @return Lista de Menués encontrados
	 */
	@GET
	@Path("/getMenusAll")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Menu> getMenusAll() throws ServicesException {
		
		List<Menu> menus = new ArrayList<Menu>();
		List<Plato> platos = new ArrayList<Plato>();
		List<Ingrediente> ingredientes = new ArrayList<Ingrediente>();
		Clientes cliente = new Clientes();
		
		Connection con = null;
		try {
			con  = DataServiceHelper.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM menu WHERE estatus = '1'");
			ResultSet rs = ps.executeQuery();
			if (rs.next())
			{	rs.beforeFirst();
				while (rs.next())
				{	Menu menu = new Menu();
					menu.setIdmenu(rs.getLong(1));
					menu.setFecha(rs.getString(2));
					menu.setEstatus(rs.getString(3));	
					ps = con.prepareStatement("SELECT * FROM menu_plato WHERE idmenu = ? AND estatus = '1'");
					ps.setLong(1, rs.getLong(1));
					ResultSet rs1 = ps.executeQuery();
					while (rs1.next())
					{	ps = con.prepareStatement("SELECT * FROM platos WHERE idplato = ? AND estatus = '1'");
						ps.setLong(1, rs1.getLong(1));
						ResultSet rs2 = ps.executeQuery();
						
						while (rs2.next())
						{	Plato p = new Plato();
							p.setIdplato(rs2.getLong(1));
							p.setNomplato(rs2.getString(2));
							p.setDescplato(rs2.getString(3));
							p.setPrecplato(new BigDecimal(rs2.getDouble(4)));
							p.setFecha(rs2.getString(5));
							p.setImgplato(rs2.getString(6));
							p.setEstatus(rs2.getString(7));
							
							//Buscando los ingredientes de cada plato del menu
							ps = con.prepareStatement("SELECT * FROM ingrediente_plato WHERE idplato = ? AND estatus = '1'");
							ps.setLong(1, rs2.getLong(1));
							
							ResultSet rs3 = ps.executeQuery();
							while (rs3.next())
							{	
								//Buscando los detalles de ingredientes de cada plato del menu
								ps = con.prepareStatement("SELECT * FROM ingredientes WHERE idingrediente = ? AND estatus = '1'");
								ps.setLong(1, rs3.getLong(1));
								
								ResultSet rs4 = ps.executeQuery();
								
								while (rs4.next())
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
									ps.setLong(1, rs4.getLong(9));
									
									ResultSet rs5 = ps.executeQuery();
									if (rs5.next())
									{	cliente = new Clientes();
										cliente.setIdCliente(rs5.getLong(1));
										cliente.setNomCliente(rs5.getString(2));
										cliente.setApeCliente(rs5.getString(3));
										cliente.setCedCliente(rs5.getString(4));
										cliente.setTelCliente(rs5.getString(5));
										cliente.setEmailCliente(rs5.getString(6));
										cliente.setPassCliente(rs5.getString(7));
										cliente.setTipoCliente(rs5.getString(8));
										cliente.setEstatus(rs5.getString(9));				
										
									}	
									ingrediente.setCliente(cliente);	
									ingredientes.add(ingrediente);
								}
								p.setIngredientes(ingredientes);
							}
							
							platos.add(p);
						}
					}
					menu.setPlatos(platos);
					menus.add(menu);
				}
				Response.status(200).entity(menus.toString()).build();
			}
			else
			{	throw new ServicesException("Menúes no encontrados");
			
			}
		} catch (ClassNotFoundException e) {
			throw new ServicesException("Error en el objeto Menú pasado como parámetro");
			
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
		return menus;
	}
	/**
	 * Servicio a agrega un Plato al Menú
	 * @param idMenu: id único del Menú al que se agregará el Plato
	 * @param idPlato: id único del Plato que se agregará al Menú
	 * @return Respuesta HTTP devuelta: 200 Éxito, 400 y 500 Error en el servidor, 404 No encontrado
	 */
	@POST
	@Path("/addPlatoMenu/{idMenu}/{idPlato}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addPlatoMenu( @PathParam ("idMenu") Long idMenu, @PathParam ("idPlato") Long idPlato) throws ServicesException {
		Connection con = null;
		try {
			con  = DataServiceHelper.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("INSERT INTO menu_plato VALUES  (?,?,?)");
			
			ps.setLong(1,idMenu);
			ps.setLong(2,idPlato);				
			ps.setString(3,"1");
			
			if (ps.executeUpdate() != 0)
			{	System.out.println("Data Received: " + idMenu.toString());	
				// return HTTP response 200 in case of success
					return Response.status(200).entity(idMenu.toString()).build();
			}
			
			else
			{	throw new ServicesException("Error agregando el plato");
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
	 * Servicio a elimina un Plato del Menú
	 * @param idMenu: id único del Menú del que se eliminará el Plato
	 * @param idPlato: id único del Plato que se eliminará del Menú
	 * @return Respuesta HTTP devuelta: 200 Éxito, 400 y 500 Error en el servidor, 404 No encontrado
	 */
	@POST
	@Path("/deletePlatoMenu/{idMenu}/{idPlato}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deletePlatoMenu(@PathParam("idMenu") Long idMenu, @PathParam("idPlato") Long idPlato) throws ServicesException {
		Connection con = null;
		try {
			con  = DataServiceHelper.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE menu_plato SET  estatus = '0'"
					+ " 	WHERE idmenu = ? AND idplato = ?");
			ps.setLong(1, idMenu);
			ps.setLong(2, idPlato);
			
			if (ps.executeUpdate() != 0)
			{	System.out.println("Data Received: " + idMenu);	
				// return HTTP response 200 in case of success
					return Response.status(200).entity("Eliminado con exito").build();
			}
			
			else
			{	throw new ServicesException("Error eliminando Menú");
			}
		} catch (ClassNotFoundException e) {
			throw new ServicesException("Error en el objeto Menú pasado como parámetro");
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
	 * Servicio que elimina el Menú indicado
	 * @param idMenu: id único del Menú que se eliminará
	 * @return Respuesta HTTP devuelta: 200 Éxito, 400 y 500 Error en el servidor, 404 No encontrado
	 */
	
	@POST
	@Path("/deleteMenu/{idMenu}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteMenu(@PathParam("idMenu") String idMenu) throws ServicesException {
		Connection con = null;
		try {
			con  = DataServiceHelper.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE menu SET  estatus = '0'"
					+ " 	WHERE idmenu = ?");
			ps.setString(1, idMenu);
			
			if (ps.executeUpdate() != 0)
			{	System.out.println("Data Received: " + idMenu);	
				// return HTTP response 200 in case of success
					return Response.status(200).entity("Eliminado con exito").build();
			}
			
			else
			{	throw new ServicesException("Error eliminando el Menú");
			}
		} catch (ClassNotFoundException e) {
			throw new ServicesException("Error en el objeto Menú pasado como parámetro");
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


