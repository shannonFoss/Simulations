/* Auth: Shannon Foss
 * Date: Spring 2011
 * File: GRIDTrain
 * Desc: Representation of a cargo train that comes 
 * 		 in to a container terminal loaded with 
 * 		 containers, then is reloaded with any containers 
 * 		 that have the same destination as the train. 
 */

public class GRIDTrain implements Token{
	protected static int trainCtr = 0;
	protected int id;
	protected String name;
	protected int load;

	protected String destination; //final destination of train
	protected int size;//length of train in # of containers
	protected GRIDContainer [] containers; //set of containers on train
	protected int remContCtr;//don't need?
	protected int addContCtr;
	protected boolean unloaded; //true if the train has been fully unloaded
	protected boolean done;
	
	public GRIDTrain(String d, int z){
		id = trainCtr;
		name = "T:"+id;
		load = 2;
		destination = d;
		unloaded = false;
		size = z;
		remContCtr=0;
		addContCtr=0;
		//tuCtr = 0;
		done= false;
		containers = new GRIDContainer[size];
		setContainers();
		trainCtr++;
	}
	//initialize all of the containers on the train
	private void setContainers(){
		String dest = "";
		for(int i=0;i<size;i++){
			double rand = Math.random();
			if(rand <.06){
				dest = "ATL";
			}else if(rand <.09){
				dest = "BAL";
			}else if(rand <.46){
				dest = "CHI";
			}else if(rand <.49){
				dest = "CLE";
			}else if(rand <.505){
				dest = "CLT";
			}else if(rand <.525){
				dest = "CMH";
			}else if(rand <.555){
				dest = "COR";
			}else if(rand <.625){
				dest = "DET";
			}else if(rand <.705){
				dest = "DFW";
			}else if(rand <.825){
				dest = "HOU";
			}else if(rand <.875){
				dest = "KCK";
			}else if(rand <.88){
				dest = "LMS";
			}else{
				dest = "MEM";				
			}
			containers[i] = new GRIDContainer(dest);
		}
	}
	//set and return values of the train
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
	public int getSize(){
		return size;
	}	
	public GRIDContainer [] getContainers(){
		return containers;
	}
	public String lookAtContainer(int x){
		return containers[x].getDestination();
	}
	public static int getTrainCtr(){
		return trainCtr;
	}	
	public boolean isUnloaded(){
		return unloaded;
	}
	//method to add a container to a particular location on the train
	public void addContainer(GRIDContainer c, int x){
		containers[x] = c;
		incAdd();
	}
	//method to remove a container from the train from the specified location
	public GRIDContainer removeContainer(int x){
		GRIDContainer c = containers[x];
		containers[x] = null;
		incRem();
		return c;
	}
	//method to increment the number of containers added
	public void incAdd(){
		addContCtr++;
	}
	//method to increment the number of containers removed
	//and test to see if the train is now unloaded
	public void incRem(){
		remContCtr++;
		if(remContCtr == size){
			unloaded=true;
		}
	}
	//method to skip a container and keep track of containers
	public void skipContainer(){
		incRem();
		incAdd();
	}
	//resets the size of the train after it is finished
	public void resizeTrain(){
		size = addContCtr;
	}
	//prints out the contents of the train
	public void printTrain(){
		for(int i=0;i<containers.length;i++){			
			if(i%10==0){
				System.out.println();
			}
			if(containers[i]!=null){
				System.out.print(i+":"+containers[i].getDestination());
			}else{
				System.out.print("Empty");
			}
		}
		System.out.println();
	}
	//test to see if the train is finished being unloaded/loaded
	public boolean isDone(GRIDTransferUnit[] tus){
		if(unloaded){
			int count = 0;
			for(int i=0;i<tus.length;i++){
				if(tus[i].getWorking()){
					count++;
				}
			}
			if(count==0){
				done = true;
			}
		}
		return done;
	}
}
