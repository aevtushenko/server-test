
public final class LocationHelper {
	public LocationHelper(){
		
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

}
