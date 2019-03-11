package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnector {
	// Database URL, and database credentials.
	private final String DB_URL = "jdbc:mysql://192.168.178.22:3306/webengdb";
    private final String USER = "webenguser";
    private final String PASS = "webengpass123";
    
    public DatabaseConnector() {}
	
	private ResultSet executeQuery(String query) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet result = null;
		
		try {  
 	        // Open a connection.
		    conn = DriverManager.getConnection(DB_URL, USER, PASS);
		      
		    // Execute query.
		    stmt = conn.createStatement();
		      
		    if (stmt.execute(query)) {
		        result = stmt.getResultSet();
		    }
		} catch (SQLException e) {
	        // Handle errors for JDBC.
	        e.printStackTrace();
	   	} catch (Exception e) {
	        // Handle errors for Class.forName.
	        e.printStackTrace();
	   	} finally{
	        // finally block used to close resources.
	        try {
	        	if (stmt != null) conn.close();
	        } catch (SQLException e) {
	        	e.printStackTrace();
	        }
	        try {
	        	if (conn != null) conn.close();
	        } catch (SQLException e) {
	        	e.printStackTrace();
	        }
	   	}
	   
		return result;
	}
	
	public void createTables() {
	      String query = "CREATE TABLE test (" + 
	      		         "testcol int" + 
	      		         ");";
	      ResultSet result = executeQuery(query);
	      
	      System.out.print(result.toString());
	}
}
