package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class MysqlDatabaseConnector {
	private ComboPooledDataSource cpds; // Database connection pool.
	
	public MysqlDatabaseConnector(String databaseUrl, String username, String password) {
    	cpds = new ComboPooledDataSource();
    	cpds.setJdbcUrl(databaseUrl);
    	cpds.setUser(username);                                  
    	cpds.setPassword(password);
	}

	public int executeUpdateQuery(String query) throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		
		try {  
 	        // Get connection from pool.
			conn = cpds.getConnection();
			  
		    // Execute query.
		    stmt = conn.createStatement();
		    return stmt.executeUpdate(query);
		} catch (SQLException e) {
	        // Handle errors for JDBC.
	        throw e;
	   	} finally {
	        // finally block used to close resources.
	        try {
	        	if (stmt != null) stmt.close();
	        } catch (SQLException e) {
	        	e.printStackTrace();
	        }
	        
	        try {
	        	if (conn != null) conn.close();
	        } catch (SQLException e) {
	        	e.printStackTrace();
	        }
	   	}
	}
	/*
	public int executeUpdatePreparedQuery(PreparedStatement stmt) {
		try {  
		    // Execute query.
			return stmt.executeUpdate();
		} catch (SQLException e) {
	        // Handle errors for JDBC.
	        throw e;
	   	} finally {
	        // finally block used to close resources.
	        try {
	        	if (stmt != null) stmt.close();
	        } catch (SQLException e) {
	        	e.printStackTrace();
	        }
	        
	        try {
	        	if (conn != null) conn.close();
	        } catch (SQLException e) {
	        	e.printStackTrace();
	        }
	   	}
	}
	*/
	
	
	
	
	
	/*
	String query = "CREATE TABLE IF NOT EXISTS airports ("
		     + "code CHAR(3) NOT NULL"
		     + ");";

try {  
    // Get connection from pool.
	conn = cpds.getConnection();
	  
   // Execute query.
   stmt = conn.createStatement();
   stmt.executeUpdate(query);
	
	*/
	
	
	
	
	public void close() {
		cpds.close();
	}
}
