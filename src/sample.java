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

   Statement stmt = null;
   Connection conn = null;
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
      stmt = conn.createStatement();
      /*stmt = conn.createStatement();
      String sql2 = "CREATE INDEX name ON POINTS USING rtree (location);";
      stmt.executeUpdate(sql2);
      stmt.close();*/
      String myLocation = "0 0 0";
      ArrayList<String> projects = new ArrayList<>();
      ArrayList<String> locations = new ArrayList<>();
      
      ResultSet rs = CallingDatabaseClass.getClosestPoints(myLocation, conn, 10);
      while(rs.next()){
    	  /*System.out.println(rs.getString(1));
    	  System.out.println(rs.getString("projid"));
    	  System.out.println(rs.getString(4));
    	  System.out.println(rs.getString(3));*/
    	  projects.add(rs.getString("projid"));
    	  locations.add(rs.getString(3));
      }
      Hashtable<String, ProjectObject> uniqueProjects = DataManager.getUniqueProjects(projects, locations, myLocation);

      uniqueProjects = CallingDatabaseClass.updateParameters(uniqueProjects, conn);
      
      stmt.close();
      ArrayList<Map.Entry<?, Double>> relevanceTable = DataManager.getRelevanceTable(uniqueProjects);
      
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
   
   
   
   
}//end JDBCExample