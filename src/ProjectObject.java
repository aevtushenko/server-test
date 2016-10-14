
public class ProjectObject {
	public ProjectObject(){
	}
	
	public ProjectObject(String title, int pointsInR, double distance){
		this.title = title;
		this.pointsInR = pointsInR;
		this.closestPointDist = distance;
	}
	
	private String title;
	private int pointsInR;
	private int points;
	private int users;
	private int sinceLast;
	private double closestPointDist;
	private double relevance;
	
	public double calculateRelevance() {
		double relevance = 1000*(pointsInR*points*users/(sinceLast*closestPointDist));
		this.relevance = relevance;
		System.out.println(Integer.toString(pointsInR) + " " + Integer.toString(points) + " " + Integer.toString(users) + " " + Integer.toString(sinceLast) + " " + Double.toString(closestPointDist) + " " + Double.toString(relevance));
		return relevance;
	}
	
	public void setClosestPointDist (double dist) {
		this.closestPointDist = dist;
	}
	
	public double getRelevance() {
		return this.relevance;
	}
	
	public void setTitle (String title) {
		this.title = title;
	}
	
	public void setPointsInR (Integer pointsInR) {
		this.pointsInR = pointsInR;
	}
	
	public void setPoints (Integer points) {
		this.points = points;
	}
	
	public void setUsers (Integer users) {
		this.users = users;
	}
	
	public void setSinceLast (Integer sinceLast) {
		this.sinceLast = sinceLast;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public int getPointsInR() {
		return this.pointsInR;
	}
	
	public int getPoints() {
		return this.points;
	}
	
	public int getUsers() {
		return this.users;
	}
	
	public int getSinceLast() {
		return this.sinceLast;
	}
	
}
