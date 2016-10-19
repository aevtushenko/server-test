import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

public final class DataManager {
	public DataManager() {
		
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
	   
	   public static Hashtable<String, ProjectObject> getUniqueProjects(ArrayList<String> projects, ArrayList<String> locations, String myLocation) {
		   Hashtable<String, ProjectObject> uniqueProjects = new Hashtable<String, ProjectObject>();
		      for (int i = 0; i<=projects.size()-1; i++) {
		    	  String proj = projects.get(i);
		    	  String location = locations.get(i);
		    	  if (uniqueProjects.containsKey(proj)) {
		    		  int currentPoints = uniqueProjects.get(proj).getPointsInR();
		    		  uniqueProjects.get(proj).setPointsInR(currentPoints + 1);
		    	  }
		    	  else {
		    		  String[] pointLocation = LocationHelper.parseLatLngAltFromKML(location);
		    		  double distance = LocationHelper.distance(pointLocation, myLocation);
		    		  uniqueProjects.put(proj, new ProjectObject(proj, 1, distance));
		    	  }
		      }
		  return uniqueProjects;
	   }

	   public static ArrayList<Map.Entry<?, Double>> getRelevanceTable(Hashtable<String, ProjectObject> uniqueProjects) {
		   Hashtable<String,Double> relevanceTable = new Hashtable<>();
		      
		      for (String proj : uniqueProjects.keySet()){
		    	  double relevance = uniqueProjects.get(proj).getRelevance();
		    	  relevanceTable.put(proj, relevance);
		      }
		      return sortValue(relevanceTable);
	   }
	   
	   public int getSinceLastFromLastDate(String lastDate) throws ParseException  {
		   	try { SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		    Date then = (Date) format.parse(lastDate);
		    Date now = Calendar.getInstance().getTime();
		    int seconds = (int) (now.getTime()-then.getTime())/1000;
		    return seconds; }
		   	catch (ParseException pe) {
		   		throw pe;
		   	}
	   }
	
}
