import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

public class CallingDatabaseClass {
	public CallingDatabaseClass() {
		
	}
	
	public static ResultSet getClosestPoints(String myLocation, Connection conn, int howMany) throws SQLException {
		   try{ Statement stmt = conn.createStatement();
		      String sql = "SELECT id,projId, ST_AsKML(location), date FROM points ORDER BY location <-> ST_GeomFromText('SRID=4326;POINT(" + myLocation + ")') LIMIT "+Integer.toString(howMany)+";";
		      return stmt.executeQuery(sql);}
		   catch (SQLException se) {
			   throw se;
		   }
	   }
	
	public static Hashtable<String, ProjectObject> updateParameters(Hashtable<String, ProjectObject> uniqueProjects, Connection conn) throws SQLException {
		   try {
		   Statement stmt = conn.createStatement();
		   for (String proj : uniqueProjects.keySet()) {
		    	  System.out.print(proj + " " + uniqueProjects.get(proj).getPointsInR() + "\n");
		      
		      String sql2 = "SELECT id, title, points, users, since_last FROM \"PROJECTS\" WHERE title = '" + proj + "';";
		     // stmt.executeUpdate(sql);
		      ResultSet rs2=stmt.executeQuery(sql2);
		      Integer points = 0;
			  Integer users = 0;
			  Integer sinceLast = 0;
		      while(rs2.next()){
		    	  points = Integer.parseInt(rs2.getString(3));
		    	  users = Integer.parseInt(rs2.getString(4));
		    	  sinceLast = Integer.parseInt(rs2.getString(5));
		      }
		      ProjectObject projObj = uniqueProjects.get(proj);
		      projObj.setPoints(points);
		      projObj.setUsers(users);
		      projObj.setSinceLast(sinceLast);
		      projObj.calculateRelevance();
		       } }
		   catch (SQLException se) {
			   throw se;
		   }
		   return uniqueProjects;
	   }
	
}
