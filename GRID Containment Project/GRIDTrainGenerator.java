/* Auth: Shannon Foss
 * Date: Spring 2011
 * File: GRIDTrainGenerator
 * Desc: Generates Train tokens that are given a
 * 		 random destination and are preloaded 
 * 		 with a random number of containers.
 * 		 The train generator randomly releases 
 * 		 a new train into the system. 
 */
import java.awt.Color;

public class GRIDTrainGenerator extends Generator{
	//port
	protected OutPort trainGeneratorOutport;
	
	public GRIDTrainGenerator(){
		super("TrainGenerator");
		trainGeneratorOutport = new OutRelay("TrainGeneratorOutport");
		addOutPort(trainGeneratorOutport);
	}	
	public void initialize(){
		super.initialize();
	}
	//creates and initializes each train generated
	public Token generateToken(){
		double rand = Math.random();
		int containers=0;
		if(rand < .15){
			containers = (int)(Math.random()*(175-150+1))+150;
		}else if(rand < .25){
			containers = (int)(Math.random()*(200-175+1))+175;
		}else if(rand < .41){
			containers = (int)(Math.random()*(225-200+1))+200;
		}else if(rand < .63){
			containers = (int)(Math.random()*(250-225+1))+225;
		}else if(rand < .98){
			containers = (int)(Math.random()*(275-250+1))+250;
		}else {
			containers = (int)(Math.random()*(300-275+1))+275;
		}

		String dest = "";
		rand = Math.random();
		if(rand <.05){
			dest = "ATL";
		}else if(rand <.1){
			dest = "BAL";
		}else if(rand <.45){
			dest = "CHI";
		}else if(rand <.50){
			dest = "CLE";
		}else if(rand <.52){
			dest = "CLT";
		}else if(rand <.54){
			dest = "CMH";
		}else if(rand <.59){
			dest = "COR";
		}else if(rand <.64){
			dest = "DET";
		}else if(rand <.74){
			dest = "DFW";
		}else if(rand <.84){
			dest = "HOU";
		}else if(rand <.89){
			dest = "KCK";
		}else if(rand <.90){
			dest = "LMS";
		}else{
			dest = "MEM";				
		}

		//System.out.println("Train Generated: "+dest +" "+ containers);
		return new GRIDTrain(dest, containers);
	}
	//sets the departure time for each train generated
	public double nextDepartureTime(double lastDepartureTime){
		double newTime= lastDepartureTime;
		if(lastDepartureTime == 0){
			newTime += 1;
		}else{
			double r = Math.random();
			int min = 60000;
			int max = 120000;
			int mode = 93000;
			double result;
			//triangular distribution 
			if(r<.5){
				result = min+Math.sqrt(r*(max-min)*(mode-min));
			}else{
				result = max-Math.sqrt((1-r)*(max-min)*(max-mode));
			}
			newTime += result/GRIDConstants.NUMTUSONTRAIN;
		}
		return newTime;
	}

	
	//grid block information
	public Object info(int i)
	{
		if(i == 0){
			return name;
		}
		if(i == 1){
			return "# Gen: " + tokensGenerated;
		}
		if(lastGenerated != null){
			return "Last: " + lastGenerated.name();
		}else{ 
			return "None";
		}
	}
	public int infoAmount() {
		return 3;
	}
	public Color color() {
		return Color.YELLOW;
	}
}
