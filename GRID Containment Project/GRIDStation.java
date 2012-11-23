/* Auth: Shannon Foss
 * Date: Spring 2011
 * File: GRIDStation
 * Desc: A representation of where the train will be
 * 		 while it is being unloaded by the TUs.
 */
import java.awt.Color;
import java.io.*;

public class GRIDStation extends Module{
	//variables used for final calculations
	public static double totalContainerTime=0;
	public static int totalContainersIn=0;
	public static int totalContainersOut=0;

	public static boolean hasTrain = false;
	
	//train variables and ports
	protected GRIDTrain train;
	protected FIFOPort trainInport;
	protected OutPort trainOutport;
	
	//tu variables and ports
	protected GRIDTransferUnit [] transferUnits = new GRIDTransferUnit [GRIDConstants.NUMTU];
	protected InPort tuInport;
	protected OutPort tuOutport;
	protected InPort tuLoopInport;
	protected OutPort tuLoopOutport;

	public GRIDStation() {
		super("Station", INFINITY, 3);
		//train 
		trainInport = new FIFOPort("StationTrainInport");
		trainOutport = new OutRelay("StationTrainOutport");	
		addInPort(trainInport);
		addOutPort(trainOutport);
		trainOutport.setAssociate(trainInport);
		//tus
		int [] loc = {GRIDConstants.ROWS, 0};
	 	for(int i= 0;i<GRIDConstants.NUMTU;i++){
	  		loc[1] = i*GRIDConstants.NUMTU;
	  		transferUnits[i] = new GRIDTransferUnit(loc);
	 	}
		tuInport = new InPort("StationTUInport");
		tuOutport = new OutRelay("StationTUOutport");
		addInPort(tuInport);
		addOutPort(tuOutport);
		tuOutport.setAssociate(tuInport);
		tuLoopInport = new InPort("LoopInport");
		tuLoopOutport = new OutRelay("LoopOutport");
		addInPort(tuLoopInport);
		addOutPort(tuLoopOutport);
		tuLoopOutport.setAssociate(tuLoopInport);
	}
	//initialize the TUs
	public void initialize(){
		super.initialize();			
		for(int i=0;i<GRIDConstants.NUMTU;i++){
			if(i<GRIDConstants.NUMTUSONTRAIN){
				transferUnits[i].setAreaStart((int)Math.round(i*(GRIDConstants.COLS/(double)GRIDConstants.NUMTUSONTRAIN)));
				transferUnits[i].setRouter(transferUnits[i].getAreaStart()-1);
				transferUnits[i].setAreaEnd((int)Math.round((i+1)*(GRIDConstants.COLS/(double)GRIDConstants.NUMTUSONTRAIN)));
			}else{//work in grid
				transferUnits[i].setAreaStart((int)Math.round((i-GRIDConstants.NUMTUSONTRAIN)*(GRIDConstants.COLS/(double)(GRIDConstants.NUMTU-GRIDConstants.NUMTUSONTRAIN))));	
				transferUnits[i].setRouter(transferUnits[i].getAreaStart());
				transferUnits[i].setAreaEnd((int)Math.round(((i-GRIDConstants.NUMTUSONTRAIN)+1)*(GRIDConstants.COLS/(double)(GRIDConstants.NUMTU-GRIDConstants.NUMTUSONTRAIN))));
				transferUnits[i].setWorkInGrid(true);
			}
			enter(transferUnits[i], 0);
		}
	}
	//Enter method for Trains and TUs
	public void enter(Token t, double time){
		super.enter(t, time);
		//Trains
		if(t instanceof GRIDTrain){
			train = (GRIDTrain)t;
		 	for(int i= 0;i<GRIDConstants.NUMTU;i++){
		 		transferUnits[i].setTrainDest(train.getDestination());
		 		if(i<GRIDConstants.NUMTUSONTRAIN){//work on train
					transferUnits[i].setRouter(transferUnits[i].getAreaStart()-1);
					transferUnits[i].setWorking(true); 
				}
		 	}
			System.out.println("Train Entered: "+ train.getDestination()+" "+train.getSize()+" "+time);
			totalContainersIn += train.getSize();
			hasTrain = true;
			GRIDContainer[] c = train.getContainers();
			for(int i=0;i<c.length;i++){
				c[i].setStartTime(time);
			}
		}
		//Transfer Units
		else if(t instanceof GRIDTransferUnit){
			GRIDTransferUnit tu = (GRIDTransferUnit)t;
			//System.out.println(tu.name()+" Entered Station: "+time);
			//System.out.println(time);
			if(tu.getWorkInGrid()){
				int [] d = {0,tu.getRouter()};
				tu.setDest(d);
				tuOutport.enter(tu, time);
			}else{
				if(hasTrain && tu.getWorking()){
					tu.resetV();
					double timePassed = time;
					int r = 0;
					int[]loc = {GRIDConstants.ROWS, tu.getRouter()};
					tu.setCurLoc(loc);
					if(tu.getHasContainer()){
						tu.incMoves();
						//add container to train
						timePassed += 10+tu.calcBeamMoveTime(1);
						train.addContainer(tu.dropContainer(), tu.getRouter());
						timePassed += 20+tu.calcBeamMoveTime(1);
					}
					r=tu.getRouter();
					tu.changeRouted();
					timePassed += 10+tu.calcStationMoveTime(r);
					while(tu.getRouter()<train.getSize() && train.lookAtContainer(tu.getRouter()).equals(train.getDestination())){
						//skip any containers that are already on the correct train
						r = tu.getRouter();
						tu.changeRouted();
						tu.incMoves();
						train.skipContainer();
						timePassed+=10+tu.calcStationMoveTime(r);
						//System.out.println("Skip");
					}
	
					while (!tu.getFwd()&&tu.getRouter()>=train.getSize()){
						tu.changeRouted();
					}
					while(tu.getRouter()<train.getSize() && train.lookAtContainer(tu.getRouter()).equals(train.getDestination())){
						//skip any containers that are already on the correct train
						r = tu.getRouter();
						tu.changeRouted();
						tu.incMoves();
						train.skipContainer();
						timePassed+=10+tu.calcStationMoveTime(r);
						//System.out.println("Skip");
					}
					//System.out.println(tu.name()+" "+tu.getRouter());
					if(tu.getRouter()<train.getSize()){
						//pick up container from train
						timePassed += 10+tu.calcBeamMoveTime(1);
						tu.pickUpContainer(train.removeContainer(tu.getRouter()));
						tu.incMoves();
						timePassed += 10+tu.calcBeamMoveTime(1);
						tu.findDestination(true);
						//System.out.println(tu.name()+" Exiting Station: "+timePassed);
						tuOutport.enter(tu, timePassed+tu.calcGridMoveTime(true));
					}else{
						//no more containers to unload - train exits and TU waits for new train
						tu.setWorking(false);
						tuLoopOutport.enter(tu, timePassed);
						if(train.isDone(transferUnits)){
							//System.out.println("Train Leaving");
							//train.printTrain();
							System.out.println("Done!");
							trainOutport.enter(train, timePassed);
						}
					}
				}else{ //no train to work on
					if(time<Module.clock.closingTime()){
						tuLoopOutport.enter(t, time+5);//delays the tu since there isn't a train to work on yet
					}
				}
			}
		}
	}
	//exit method for TU tokens
	public void exit(Token t, double time){
		super.exit(t, time);
		//train leaves
		if(t instanceof GRIDTrain){
			GRIDContainer [] c = train.getContainers();
			int s = train.getSize();
			//System.out.println("Train Exiting: "+ train.getDestination()+" "+s);
			for(int i=0;i<s;i++){
				if(c[i]!=null){
					c[i].setEndTime(time);
					totalContainerTime += c[i].calcTimeInGrid();
				}
			}
			hasTrain = false;	
			GRIDContainerGridStack [][] stack = GRIDContainerGrid.getStacks();
			int numContInGrid=0;
			for(int i=0;i<GRIDConstants.ROWS;i++){
				for(int j=0;j<GRIDConstants.COLS;j++){
					numContInGrid += stack[i][j].getSize();
				}
			}		
			train.resizeTrain();
			totalContainersOut += train.getSize();
			//write the information to a file
			try{
				PrintWriter writer = new PrintWriter(new FileWriter("data.txt",true));
				writer.println(train.name() + " - "+ train.getDestination()+" "+s);
				writer.println("Start Time=");
				writer.println("Final Time="+time);
				writer.println("Total Containers In=" +totalContainersIn);
				writer.println("Total Containers Out=" +totalContainersOut);
				writer.println("Total Containers In Grid="+numContInGrid);
				writer.println("Avg Container Time=" +(totalContainerTime/totalContainersOut));
				writer.println("% of grid filled="+(numContInGrid/(double)(GRIDConstants.ROWS*GRIDConstants.COLS*GRIDConstants.HEIGHT)*100)+"%");
				for(int i=0;i<transferUnits.length;i++){
					//writer.println(transferUnits[i].name()+"# of moves:"+transferUnits[i].getMoves());
					writer.print(transferUnits[i].getMoves()+"\t");
				}
				writer.println();
				writer.close();
			}catch(IOException io){}
		}else if(t instanceof GRIDTransferUnit){
			//GRIDTransferUnit trans = (GRIDTransferUnit)t;
			//System.out.println("TU Exiting: "+time );
		}
	}
	//grid block information
	public Color color()
	{
		return Color.GREEN;
	}
	public Object info(int i)
	{
		if(i == 0) 
			return name;
		else if(i == 1) 
		{
			if(loadCount > 0){	
			    return "Dest: "+train.getDestination();
			}else{ 
				return "Idle";
			}
		}
		else
		{
			if(loadCount>0){
				return "Size: "+train.getSize();
			}else{
				return "Idle";
			}
		}
	}	
	public int infoAmount() 
	{
		return 3;
	}
}
