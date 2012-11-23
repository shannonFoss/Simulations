/* Auth: Shannon Foss
 * Date: Spring 2011
 * File: GRIDContainerGrid
 * Desc: Representation of the GRID Containment System
 */
public class GRIDContainerGrid  extends Module{
	
	protected static GRIDContainerGridStack [][] stacks = new GRIDContainerGridStack [GRIDConstants.ROWS][GRIDConstants.COLS]; 
	
	//ports
	protected InPort gridInport;
	protected OutPort gridOutport;

	public GRIDContainerGrid(){
		super("ContainerGrid",Module.INFINITY,Module.INFINITY);
		gridInport = new GRIDTransferUnitRouter();
		addInPort(gridInport);
		gridOutport = new OutRelay("GridOutport");
		addOutPort(gridOutport);
		gridOutport.setAssociate(gridInport);

		//create grid stacks
		for(int i=0; i<GRIDConstants.ROWS; i++){
			for(int j=0;j<GRIDConstants.COLS;j++){
				int type = 0;
				if(i==0 && j==0){ //top left corner
					type=1;
					stacks[i][j]= new GRIDContainerGridStack("S["+i+","+j+"]", type, i, j);
				}else if(i==0 && j==GRIDConstants.COLS-1){//top right corner
					type=2;
					stacks[i][j]= new GRIDContainerGridStack("S["+i+","+j+"]", type, i, j);
					stacks[i][j].connectLeft(stacks[i][j-1]);					
				}else if(i==GRIDConstants.ROWS-1 && j==0){//bottom left corner
					type=3;
					stacks[i][j]= new GRIDContainerGridStack("S["+i+","+j+"]", type, i, j);
					stacks[i][j].connectUp(stacks[i-1][j]);		
				}else if(i==GRIDConstants.ROWS-1 && j==GRIDConstants.COLS-1){//bottom right corner
					type=4;
					stacks[i][j]= new GRIDContainerGridStack("S["+i+","+j+"]", type, i, j);
					stacks[i][j].connectLeft(stacks[i][j-1]);
					stacks[i][j].connectUp(stacks[i-1][j]);				
				}else if(i==0){//top edges
					type=5;
					stacks[i][j]= new GRIDContainerGridStack("S["+i+","+j+"]", type, i, j);
					stacks[i][j].connectLeft(stacks[i][j-1]);
				}else if(j==0){//left edges
					type=6;
					stacks[i][j]= new GRIDContainerGridStack("S["+i+","+j+"]", type, i, j);
					stacks[i][j].connectUp(stacks[i-1][j]);
				}else if(j==GRIDConstants.COLS-1){//right edges 
					type=7;
					stacks[i][j]= new GRIDContainerGridStack("S["+i+","+j+"]", type, i, j);
					stacks[i][j].connectLeft(stacks[i][j-1]);
					stacks[i][j].connectUp(stacks[i-1][j]);					
				}else if(i==GRIDConstants.ROWS-1){//bottom edges
					type=8;
					stacks[i][j]= new GRIDContainerGridStack("S["+i+","+j+"]", type, i, j);
					stacks[i][j].connectLeft(stacks[i][j-1]);
					stacks[i][j].connectUp(stacks[i-1][j]);						
				}else{//middles
					type=9;
					stacks[i][j]= new GRIDContainerGridStack("S["+i+","+j+"]", type, i, j);
					stacks[i][j].connectLeft(stacks[i][j-1]);
					stacks[i][j].connectUp(stacks[i-1][j]);
				}
				addSubmodule(stacks[i][j]);
			}
		} 
		//connect grid to grid stacks
		for(int i=0;i<GRIDConstants.COLS;i++){
			Module.connectInToIn(this, stacks[GRIDConstants.ROWS-1][i], "TransferUnitRouter", "ContainerGridStackInport");
			Module.connectOutToOut(stacks[GRIDConstants.ROWS-1][i], this, "ContainerGridStackOutport", "GridOutport");
		}
		//initialize grid with a set of containers
		int contCount = 0;
		while (((double)contCount/(GRIDConstants.ROWS*GRIDConstants.COLS*GRIDConstants.HEIGHT))<= GRIDConstants.PCTGRIDFILLED){
			//create a container
			String dest = "";
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
			GRIDContainer c = new GRIDContainer(dest);
			//pick a location
			int [] empty = {0,0};
			boolean foundEmpty= false;
			boolean foundDest = false;
			for(int i=GRIDConstants.ROWS-1;i>=0;i--){
				for(int j=0;j<GRIDConstants.COLS;j++){
					if(!stacks[i][j].getIsFull()){//any non-full stack
						if(stacks[i][j].getIsEmpty() && !foundEmpty){
							empty[0]=i;
							empty[1]=j;
							foundEmpty = true;
						}else if(stacks[i][j].getDest().equals(c.getDestination())&& !foundDest){
							stacks[i][j].addContainer(c);
							foundDest = true;
						}
					}
				}
			}
			if(!foundDest){
				stacks[empty[0]][empty[1]].addContainer(c);
			}
			contCount++;
		}
	}
	//returns the entire grid
	public static GRIDContainerGridStack [][] getStacks(){
		return stacks;
	}
}
