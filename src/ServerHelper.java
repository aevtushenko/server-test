import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class ServerHelper {
	
	public ServerHelper(){
		
	}
	static final String JDBC_DRIVER = "org.postgresql.Driver";  
	static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
	static final String USER = "postgres";
	static final String PASS = "password";
	
	String createDB = "CREATE DATABASE STUDENTS_TEST";
	String createTable = "CREATE TABLE points (id integer NOT NULL PRIMARY KEY, projid char NOT NULL, location geography(POINtZ,4326), date timestamptz);";
	String createIndex = "CREATE INDEX name ON POINTS USING rtree (location);";
	String addExtension1 = "CREATE EXTENSION IF NOT EXISTS postgis;";
	String addExtension2 = "CREATE EXTENSION IF NOT EXISTS postgis_topology;";
	String addExtension3 = "CREATE EXTENSION IF NOT EXISTS adminpack;";
	
	public void openDB() throws SQLException, ClassNotFoundException {
		Statement stmt = null;
		Connection conn = null;
		try{
		      Class.forName("org.postgresql.Driver");
		      System.out.println("Connecting to database...");
		      conn = DriverManager.getConnection(DB_URL, USER, PASS);
		      System.out.println("Creating database...");
		      stmt = conn.createStatement();
		} catch (SQLException se) {
			throw se;
		} catch (ClassNotFoundException e) {
			throw e;
		}
	}
	
	public void executeNoReturnStatement(String statement, Connection conn) throws SQLException {
		try {Statement stmt = conn.createStatement();
		stmt.executeQuery(statement);
		stmt.close();}
		catch (SQLException se) {
			throw se;
		}
	}
	
}
