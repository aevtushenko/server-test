//STEP 1. Import required packages
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.Map;
import java.util.Comparator;
import java.util.Collections;
import java.util.Hashtable;
import java.text.DecimalFormat;
import java.text.ParseException;

public class sample {
   // JDBC driver name and database URL
   static final String JDBC_DRIVER = "org.postgresql.Driver";  
   static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";

   //  Database credentials
   static final String USER = "postgres";
   static final String PASS = "password";
   
   public static void main(String[] args) {
   Connection conn = null;
   Statement stmt = null;
   try{
      //STEP 2: Register JDBC driver
      Class.forName("org.postgresql.Driver");

      //STEP 3: Open a connection
      System.out.println("Connecting to database...");
      conn = DriverManager.getConnection(DB_URL, USER, PASS);

      //STEP 4: Execute a query
      System.out.println("Creating database...");
      //stmt = conn.createStatement();
      
      //String sql = "CREATE DATABASE STUDENTS_TEST";
      //stmt.executeUpdate(sql);
      //System.out.println("Database created successfully...");
      //stmt.close();
      
      /*stmt = conn.createStatement();
      String sql2 = "CREATE INDEX name ON POINTS USING rtree (location);";
      stmt.executeUpdate(sql2);
      stmt.close();*/
      String myLocation = "0 0 0";
      ArrayList<String> projects = new ArrayList<>();
      ArrayList<String> locations = new ArrayList<>();
      Hashtable<String, ProjectObject> uniqueProjects = new Hashtable<String, ProjectObject>();
      stmt = conn.createStatement();
      String sql = "SELECT id,projId, ST_AsKML(location), date FROM points ORDER BY location <-> ST_GeomFromText('SRID=4326;POINT(" + myLocation + ")') LIMIT 10;";
     // stmt.executeUpdate(sql);
      ResultSet rs=stmt.executeQuery(sql);
      while(rs.next()){
    	  /*System.out.println(rs.getString(1));
    	  System.out.println(rs.getString("projid"));
    	  System.out.println(rs.getString(4));
    	  System.out.println(rs.getString(3));*/
    	  projects.add(rs.getString("projid"));
    	  locations.add(rs.getString(3));
      }
      
      for (int i = 0; i<=projects.size()-1; i++) {
    	  String proj = projects.get(i);
    	  String location = locations.get(i);
    	  if (uniqueProjects.containsKey(proj)) {
    		  int currentPoints = uniqueProjects.get(proj).getPointsInR();
    		  uniqueProjects.get(proj).setPointsInR(currentPoints + 1);
    	  }
    	  else {
    		  String[] pointLocation = parseLatLngAltFromKML(location);
    		  double distance = distance(pointLocation, myLocation);
    		  uniqueProjects.put(proj, new ProjectObject(proj, 1, distance));
    	  }
      }
      //sortValue(uniqueProjects);

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
       }
      stmt.close();
      Hashtable<String,Double> relevanceTable = new Hashtable<>();
      
      for (String proj : uniqueProjects.keySet()){
    	  double relevance = uniqueProjects.get(proj).getRelevance();
    	  relevanceTable.put(proj, relevance);
    	  
    	  
      }
      sortValue(relevanceTable);

      
      conn.close();
   }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }finally{
      //finally block used to close resources
      try{
         if(stmt!=null)
            stmt.close();
      }catch(SQLException se2){
      }// nothing we can do
      try{
         if(conn!=null)
            conn.close();
      }catch(SQLException se){
         se.printStackTrace();
      }//end finally try
   }//end try
   //System.out.println("Goodbye!");
   System.out.println(Double.toString(1000*(1*5000*500/(10000*24645.16239360158))));
}//end main
   
   public final void addData (int s, int e) throws SQLException, ParseException {
	   Connection conn = null;
	   conn = DriverManager.getConnection(DB_URL, USER, PASS);
	   Statement stmt = null;
	   	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
	    Date startDate = (Date) format.parse("2016-05-28T11:15:33UTC");
	    DecimalFormat df = new DecimalFormat("#.0#####");
	      for (int i = s; i<= e; i++){
	    	  int random = (int) (Math.random() * 600000 + 1);
	          Calendar calendar = Calendar.getInstance();
	          calendar.setTime(startDate);
	          long t = calendar.getTimeInMillis();
	          startDate =new Date(t + (random));
	          int project = (int) (Math.random() * 10);
	          String reportDate = format.format(startDate);
	          double lng = -180 + (Math.random() * 360);
	          String lngStr = df.format(lng);
	          double lat = -90 + (Math.random() * 180);
	          String latStr = df.format(lat);
	          String reportLocation = lngStr + " " + latStr + " 0";
	          
	    	  stmt = conn.createStatement();
	          String sql = "INSERT INTO POINTS (ID,PROJID,LOCATION,DATE) "
	                + "VALUES ("+ Integer.toString(i) + ", 'Project" + Integer.toString(project) +"', ST_GeomFromText('SRID=4326;POINT(" + reportLocation+ ")'),'" + reportDate +"');";
	          stmt.executeUpdate(sql);
	          stmt.close(); 
	      } }
   
   public static void deleteData() throws SQLException {
	   Connection conn = null;
	   conn = DriverManager.getConnection(DB_URL, USER, PASS);
	   Statement stmt = null;
	   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
	   String laterTimeStamp = format.format(Calendar.getInstance().getTime());
	   Calendar cal = Calendar.getInstance();
	   cal.add(Calendar.DATE, -7);
	   String earlierTimeStamp = format.format(cal.getTime());
	   stmt = conn.createStatement();
	   String sql = "DELETE FROM POINTS WHERE date < '" + earlierTimeStamp +"';";
         stmt.executeUpdate(sql);
         stmt.close();
	   
   }
   
   public static ArrayList<Map.Entry<?, Double>> sortValue(Hashtable<?, Double> t){

       //Transfer as List and sort it
       ArrayList<Map.Entry<?, Double>> l = new ArrayList<Map.Entry<?, Double>>(t.entrySet());
       Collections.sort(l, new Comparator<Map.Entry<?, Double>>(){

         public int compare(Map.Entry<?, Double> o1, Map.Entry<?, Double> o2) {
            return o2.getValue().compareTo(o1.getValue());
        }});
       
       System.out.println(l);
       return l;
    }
   
   public static String[] parseLatLngAltFromKML(String kml) {
       kml = kml.substring(("<Point><coordinates>").length(), kml.length());
       kml = kml.substring(0, (kml.length() - "</coordinates></Point>".length()));
       String[] coordArray = kml.split(","); // Long, Lat, Alt
       return coordArray;
   }
   
   public static double distance(String[] pointloc, String myLocation) {
	   String[] myloc = myLocation.split(" ");
	   Double lat1 = Double.parseDouble(pointloc[0]);
	   Double lat2 = Double.parseDouble(myloc[0]);
	   Double lon1 = Double.parseDouble(pointloc[1]);
	   Double lon2 = Double.parseDouble(myloc[1]);
	   Double el1 = Double.parseDouble(pointloc[2]);
	   Double el2 = Double.parseDouble(myloc[2]);
	   return distance(lat1, lat2, lon1, lon2, el1, el2);
   }
   
   public static double distance(double lat1, double lat2, double lon1,
	        double lon2, double el1, double el2) {

	    final int R = 6371; // Radius of the earth

	    Double latDistance = Math.toRadians(lat2 - lat1);
	    Double lonDistance = Math.toRadians(lon2 - lon1);
	    Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
	            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
	            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
	    Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    double distance = R * c * 1000; // convert to meters

	    double height = el1 - el2;

	    distance = Math.pow(distance, 2) + Math.pow(height, 2);

	    return Math.sqrt(distance);
	}
   
   
}//end JDBCExample