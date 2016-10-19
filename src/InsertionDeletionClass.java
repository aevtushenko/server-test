import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class InsertionDeletionClass {
	public InsertionDeletionClass(){
		
	}
	
	public final void addRandomData (int s, int e, Connection conn) throws SQLException, ParseException {
		   try{
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
		   catch (SQLException se) {
		    	  throw se;
		      }
		   catch (ParseException pe) {
			   throw pe;
		   }
		      
	   
	   }
	   
	   public static void deleteData(Connection conn) throws SQLException {
		   try {
		   Statement stmt = null;
		   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		   String laterTimeStamp = format.format(Calendar.getInstance().getTime());
		   Calendar cal = Calendar.getInstance();
		   cal.add(Calendar.DATE, -7);
		   String earlierTimeStamp = format.format(cal.getTime());
		   stmt = conn.createStatement();
		   String sql = "DELETE FROM POINTS WHERE date < '" + earlierTimeStamp +"';";
	         stmt.executeUpdate(sql);
	         stmt.close(); }
		   catch (SQLException se) {
		    	  throw se;
		      }
	   }
	   
}
