/* Auth: Shannon Foss
 * Date: Spring 2011
 * File: GRIDSystemModel
 * Desc: This is the location where all of the 
 * 		 pieces of the system are put together.
 */

public class GRIDSystemModel extends SystemModel {
	public GRIDSystemModel(){
		super();
	}
	
	protected void buildModel(){
		//generators
		GRIDTrainGenerator trainGen = new GRIDTrainGenerator();
		
		//modules
		GRIDStation station = new GRIDStation();
		GRIDContainerGrid grid = new GRIDContainerGrid();
		
		//collector
		SimpleCollector collector = new SimpleCollector("TrainCollector");
		
		//connections
		connect(trainGen, station, "TrainGeneratorOutport","StationTrainInport");
		connect(station, grid, "StationTUOutport", "TransferUnitRouter");
		connect(grid, station, "GridOutport", "StationTUInport");
		connect(station, station, "LoopOutport", "LoopInport");
		connect(station, collector, "StationTrainOutport","TrainCollectorInPort");
		
		//add to system model
		add(trainGen);
		add(station);
		add(grid);
		add(collector);

		//draw model panel
		makeModelPanel(GRIDConstants.ROWS+2,GRIDConstants.COLS+2);//0,0 = top left y,0 = bottom left
		GRIDContainerGridStack [][] stacks = GRIDContainerGrid.getStacks();
		for(int i=0;i<stacks.length;i++){
			for(int j=0;j<stacks[0].length;j++){
				assignModule(stacks[i][j], i, j+1);
			}
		}
		assignModule(trainGen,GRIDConstants.ROWS+1,0);
		assignModule(station, GRIDConstants.ROWS+1,(GRIDConstants.COLS+2)/2);
		assignModule(collector,GRIDConstants.ROWS+1,GRIDConstants.COLS+1);
	}
}
