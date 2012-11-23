/* Auth: Shannon Foss
 * Date: Spring 2011
 * File: GRIDContainer
 * Desc: Representation of a cargo container that is 
 * 		 brought in to a container terminal, placed
 * 		 into the GRID storage system, and then is
 * 		 later taken out again on a train.
 */
public class GRIDContainer implements Token{
	protected static int containerCtr = 0;
	
	protected int id;
	protected String name;
	protected int load;
	protected String destination;
	protected double startTime;
	protected double endTime;
	protected int [] location = new int [2];
	
	public GRIDContainer(String d){
		id = containerCtr;
		name = ""+id;
		load = 0;
		destination = d;
		startTime = 0;
		endTime = 0;
		location[0]=GRIDConstants.ROWS;
		location[1]=GRIDConstants.COLS;
		containerCtr++;
	}
	//sets or returns information about this container
	public int load(){
		return load;
	}
	public int id(){
		return id;
	}
	public String name(){
		return name;
	}	
	public String getDestination(){
		return destination;
	}
	public double getStartTime(){
		return startTime;
	}
	public void setStartTime(double t){
		startTime = t;
	}
	public void setLoc(int r, int c){
		location[0] = r;
		location[1] = c;
	}
	public int [] getLoc(){
		return location;
	}
	public double getEndTime(){
		return endTime;
	}
	public void setEndTime(double t){
		endTime = t;
	}
	//calculates the amount of time this container spent in the system
	public double calcTimeInGrid(){
		return endTime - startTime;
	}
	//returns the number of containers created
	public static int getContCtr(){
		return containerCtr;
	}
}
