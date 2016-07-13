package rest.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import rest.model.Clientes;




public class DataServiceHelper 
{

	public static DataServiceHelper dataServiceHelper = null;
	private Connection con = null;
	DataSource dataSource = null;
	InitialContext initialContext = null;

	public static final String DB_URL = "jdbc:mysql://localhost:3306/restaurante";

	public static final String DRIVER_NAME = "com.mysql.jdbc.Driver";


 

	public Connection getConnection() throws ClassNotFoundException,SQLException 
	{	Class.forName(DRIVER_NAME);
		con = DriverManager.getConnection(DB_URL, "root", "1234");
		return con;

	}
 

	public void closeConnection() throws SQLException 
	{	if (isConnectionOpen()) 
		{	con.close();	
			con = null;	
		}

	}

	public boolean isConnectionOpen() 
	{	return (con != null);	
	}

 

	public static DataServiceHelper getInstance() 
	{	if (dataServiceHelper == null) 
		{	dataServiceHelper = new DataServiceHelper();	
		}
	
		return dataServiceHelper;
	
	}

 

	public void executeUpdateQuery(String query) throws SQLException,ClassNotFoundException 
	{	
		Connection con = getConnection();	
		Statement stmt = con.createStatement();		
		stmt.execute(query);		
		closeConnection();
	
	}

 

	

 

}
