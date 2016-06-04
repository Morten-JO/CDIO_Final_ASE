package database_connector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** @author Gruppe_24 */
public class Connector {
	private static Connector conn = null;
    private static final String HOST     = "localhost";
    private static final int    PORT     = 3306;
    private static final String DATABASE = "cdio_db";
    private static final String USERNAME = "root"; 
    private static final String PASSWORD = "";
    private static Connection connection;
    
    private Connector() {
    	if(connection==null){
			try {
				Class.forName("com.mysql.jdbc.Driver");
				String url = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE;
				connection = DriverManager.getConnection(url, USERNAME, PASSWORD);
				
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
    }
    
    public static Connector getInstance(){  
    	if (conn == null){
    		conn = new Connector();
    	}
    	return conn;
    }
    
    public ResultSet doQuery(String query) throws SQLException{
        Statement stmt = connection.createStatement();
        ResultSet res = stmt.executeQuery(query);
        return res;
    }
    
    public void doUpdate(String query) throws SQLException{
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(query);
    }

	public Connection getConnection() {
		return connection;
	}
}