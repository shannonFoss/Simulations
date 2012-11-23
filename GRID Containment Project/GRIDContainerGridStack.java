/* Auth: Shannon Foss
 * Date: Spring 2011
 * File: GRIDContainerGridStack
 * Desc: Representation of each stack of containers 
 * 		 within the GRID.
 */
import java.awt.Color;
import java.util.Stack;

public class GRIDContainerGridStack extends Module{
	protected int locRow;
	protected int locCol;
	protected Stack<GRIDContainer> stack=new Stack<GRIDContainer>();
	protected boolean isFull;
	protected String dest;
	protected GRIDTransferUnit tu;
	//ports
	protected InPort upInPort;
	protected InPort downInPort;
	protected InPort leftInPort;
	protected InPort rightInPort;
	protected OutPort upOutPort;
	protected OutPort downOutPort;
	protected OutPort leftOutPort;
	protected OutPort rightOutPort;
	
	public GRIDContainerGridStack(String name, int type, int row, int col) {
		super(name, 1, INFINITY);//?
		dest = "none";
		locRow = row;
		locCol = col;
		isFull = false;
		//9 different possible types of stacks in the grid
		if(type==1){ //top left corner
			upInPort = new FIFOPort("UpInPort");
			downInPort = null;
			leftInPort = new FIFOPort("LeftInPort");
			rightInPort = null;
			upOutPort = null;
			downOutPort = new OutRelay("DownOutPort");
			leftOutPort = null;
			rightOutPort = new OutRelay("RightOutPort");
			addInPort(upInPort);
			addInPort(leftInPort);
			addOutPort(downOutPort);
			addOutPort(rightOutPort);
			downOutPort.setAssociate(upInPort);
			rightOutPort.setAssociate(leftInPort);
		}else if(type==2){//top right corner
			upInPort = new FIFOPort("UpInPort");
			downInPort = null;
			leftInPort = null;
			rightInPort = new FIFOPort("RightInPort");
			upOutPort = null;
			downOutPort = new OutRelay("DownOutPort");
			leftOutPort = new OutRelay("LeftOutPort");
			rightOutPort = null;
			addInPort(upInPort);
			addInPort(rightInPort);
			addOutPort(downOutPort);
			addOutPort(leftOutPort);
			downOutPort.setAssociate(upInPort);
			leftOutPort.setAssociate(rightInPort);			
		}else if(type==3){//bottom left corner 
			upInPort = new FIFOPort("ContainerGridStackInport");
			downInPort = new FIFOPort("DownInPort");
			leftInPort = new FIFOPort("LeftInPort");
			rightInPort = null;
			upOutPort = new OutRelay("UpOutPort");
			downOutPort = new OutRelay("ContainerGridStackOutport");
			leftOutPort = null;
			rightOutPort = new OutRelay("RightOutPort");
			addInPort(upInPort);
			addInPort(downInPort);
			addInPort(leftInPort);
			addOutPort(upOutPort);
			addOutPort(downOutPort);
			addOutPort(rightOutPort);
			upOutPort.setAssociate(downInPort);
			downOutPort.setAssociate(upInPort);
			rightOutPort.setAssociate(leftInPort);
		}else if(type==4){//bottom right corner
			upInPort = new FIFOPort("ContainerGridStackInport");
			downInPort = new FIFOPort("DownInPort");
			leftInPort = null;
			rightInPort = new FIFOPort("RightInPort");
			upOutPort = new OutRelay("UpOutPort");
			downOutPort = new OutRelay("ContainerGridStackOutport");
			leftOutPort = new OutRelay("LeftOutPort");
			rightOutPort = null;
			addInPort(upInPort);
			addInPort(downInPort);
			addInPort(rightInPort);
			addOutPort(upOutPort);
			addOutPort(downOutPort);
			addOutPort(leftOutPort);
			upOutPort.setAssociate(downInPort);
			downOutPort.setAssociate(upInPort);
			leftOutPort.setAssociate(rightInPort);
		}else if(type==5){//top edges
			upInPort = new FIFOPort("UpInPort");
			downInPort = null;
			leftInPort = new FIFOPort("LeftInPort");
			rightInPort = new FIFOPort("RightInPort");
			upOutPort = null;
			downOutPort = new OutRelay("DownOutPort");
			leftOutPort = new OutRelay("LeftOutPort");
			rightOutPort = new OutRelay("RightOutPort");
			addInPort(upInPort);
			addInPort(leftInPort);
			addInPort(rightInPort);
			addOutPort(downOutPort);
			addOutPort(leftOutPort);
			addOutPort(rightOutPort);
			downOutPort.setAssociate(upInPort);
			leftOutPort.setAssociate(rightInPort);
			rightOutPort.setAssociate(leftInPort);
		}else if(type==6){//left edges
			upInPort = new FIFOPort("UpInPort");
			downInPort = new FIFOPort("DownInPort");
			leftInPort = new FIFOPort("LeftInPort");
			rightInPort = null;
			upOutPort = new OutRelay("UpOutPort");
			downOutPort = new OutRelay("DownOutPort");
			leftOutPort = null;
			rightOutPort = new OutRelay("RightOutPort");
			addInPort(upInPort);
			addInPort(downInPort);
			addInPort(leftInPort);
			addOutPort(upOutPort);
			addOutPort(downOutPort);
			addOutPort(rightOutPort);
			upOutPort.setAssociate(downInPort);
			downOutPort.setAssociate(upInPort);
			rightOutPort.setAssociate(leftInPort);
		}else if(type==7){//right edges 
			upInPort = new FIFOPort("UpInPort");
			downInPort = new FIFOPort("DownInPort");
			leftInPort = null;
			rightInPort = new FIFOPort("RightInPort");
			upOutPort = new OutRelay("UpOutPort");
			downOutPort = new OutRelay("DownOutPort");
			leftOutPort = new OutRelay("LeftOutPort");
			rightOutPort = null;
			addInPort(upInPort);
			addInPort(downInPort);
			addInPort(rightInPort);
			addOutPort(upOutPort);
			addOutPort(downOutPort);
			addOutPort(leftOutPort);
			upOutPort.setAssociate(downInPort);
			downOutPort.setAssociate(upInPort);
			leftOutPort.setAssociate(rightInPort);
		}else if(type==8){//bottom edges
			upInPort = new FIFOPort("ContainerGridStackInport");
			downInPort = new FIFOPort("DownInPort");
			leftInPort = new FIFOPort("LeftInPort");
			rightInPort = new FIFOPort("RightInPort");
			upOutPort = new OutRelay("UpOutPort");
			downOutPort = new OutRelay("ContainerGridStackOutport");
			leftOutPort = new OutRelay("LeftOutPort");
			rightOutPort = new OutRelay("RightOutPort");
			addInPort(upInPort);
			addInPort(downInPort);
			addInPort(leftInPort);
			addInPort(rightInPort);
			addOutPort(upOutPort);
			addOutPort(downOutPort);
			addOutPort(leftOutPort);
			addOutPort(rightOutPort);
			downOutPort.setAssociate(upInPort);
			upOutPort.setAssociate(downInPort);
			leftOutPort.setAssociate(rightInPort);
			rightOutPort.setAssociate(leftInPort);
		}else if(type==9){//middles
			upInPort = new FIFOPort("UpInPort");
			downInPort = new FIFOPort("DownInPort");
			leftInPort = new FIFOPort("LeftInPort");
			rightInPort = new FIFOPort("RightInPort");
			upOutPort = new OutRelay("UpOutPort");
			downOutPort = new OutRelay("DownOutPort");
			leftOutPort = new OutRelay("LeftOutPort");
			rightOutPort = new OutRelay("RightOutPort");
			addInPort(upInPort);
			addInPort(downInPort);
			addInPort(leftInPort);
			addInPort(rightInPort);
			addOutPort(upOutPort);
			addOutPort(downOutPort);
			addOutPort(leftOutPort);
			addOutPort(rightOutPort);
			downOutPort.setAssociate(upInPort);
			upOutPort.setAssociate(downInPort);
			leftOutPort.setAssociate(rightInPort);
			rightOutPort.setAssociate(leftInPort);
		}
	}
	//connecting the stacks together
	public void connectLeft(GRIDContainerGridStack prev)
	{
		Module.connect(this,prev,"LeftOutPort","LeftInPort");
		Module.connect(prev,this,"RightOutPort","RightInPort");
	}
	public void connectUp(GRIDContainerGridStack above)
	{
		Module.connect(this,above,"UpOutPort","UpInPort");
		Module.connect(above,this,"DownOutPort","DownInPort");
	}
	//method to allow Transfer Unit tokens to enter a stack
	public void enter(Token t, double time){
		super.enter(t, time);
		tu = (GRIDTransferUnit)t;
		tu.incMoves();
		double timePassed=time;
		//System.out.println(tu.name()+" Entered Grid ["+locRow+","+locCol+ "]:"+time);
		int [] loc = {locRow, locCol};
		tu.setCurLoc(loc);
		int [] dest = tu.getDest();
		//if this is the tu's destination
		if(dest[0]==locRow && dest[1]==locCol){
			tu.resetV();
			if(tu.getHasContainer()){
				if(getIsFull()){//double check to make sure that the stack is not full
					if(tu.getWorkInGrid()){
						tu.findDestinationGrid();
					}else{
						tu.findDestination(true);
					}
				}else{
					timePassed += 10+tu.calcBeamMoveTime(getSize()+1);
					addContainer(tu.dropContainer());
					timePassed += 10+tu.calcBeamMoveTime(getSize()+1);
					//System.out.println("Added to Grid");
					if(tu.getWorkInGrid()){
						tu.findDestinationGrid();
					}else{
						tu.findDestination(true);
					}
				}
			}else{//double check to make sure that a container is still available
				if(getIsEmpty()){
					if(tu.getWorkInGrid()){
						tu.findDestinationGrid();
					}else{
						tu.findDestination(true);
					}
				}else{
					timePassed += 10+tu.calcBeamMoveTime(getSize());
					tu.pickUpContainer(removeContainer());
					timePassed += 10+tu.calcBeamMoveTime(getSize());
					//System.out.println("Removed from Grid");
					if(tu.getWorkInGrid()){
						tu.findDestinationGrid();
					}else{
						tu.findDestination(false);
					}
				}
			}
			dest = tu.getDest();
		}
		//set outport based on tu's destination
		if(dest[0] == locRow && dest[1] < locCol){
			//go left
			if(tu.getDir().equals("UD")){
				timePassed += 20;
				tu.setDir("LR");
			}
			leftOutPort.enter(tu, timePassed+tu.calcGridMoveTime(false));
		}else if(dest[0]==locRow && dest[1] > locCol){
			//go right
			if(tu.getDir().equals("UD")){
				timePassed += 20;
				tu.setDir("LR");
			}
			rightOutPort.enter(tu, timePassed+tu.calcGridMoveTime(false));
		}else if(dest[1]==locCol && dest[0] < locRow){
			//go up
			if(tu.getDir().equals("LR")){
				timePassed += 20;
				tu.setDir("UD");
			}
			upOutPort.enter(tu, timePassed+tu.calcGridMoveTime(true));
		}else if(dest[1]==locCol && dest[0] > locRow){
			//go down
			if(tu.getDir().equals("LR")){
				timePassed += 20;
				tu.setDir("UD");
			}
			downOutPort.enter(tu, timePassed+tu.calcGridMoveTime(true));
		}else if(dest[0]<locRow){
			//go up
			if(tu.getDir().equals("LR")){
				timePassed += 20;
				tu.setDir("UD");
			}
			upOutPort.enter(tu, timePassed+tu.calcGridMoveTime(true));
		}else if(dest[0]>locRow){
			if (locRow == GRIDConstants.ROWS-1){//at the bottom row 
				if(dest[1]< locCol){
					//send left 
					if(tu.getDir().equals("UD")){
						timePassed += 20;
						tu.setDir("LR");
					}
					leftOutPort.enter(tu, timePassed+tu.calcGridMoveTime(false));
				}else{
					//send right
					if(tu.getDir().equals("UD")){
						timePassed += 20;
						tu.setDir("LR");
					}
					rightOutPort.enter(tu, timePassed+tu.calcGridMoveTime(false));
				}
			}else{
				//go down
				if(tu.getDir().equals("LR")){
					timePassed += 20;
					tu.setDir("UD");
				}
				downOutPort.enter(tu, timePassed+tu.calcGridMoveTime(true));
			}
		}
	}
	//exit method for TU tokens
	public void exit(Token t, double time){
		super.exit(t, time);
		tu = null;
	}
	//changes whether this stack is full
	public void changeIsFull(){
		isFull = !isFull;
	}
	//tests to see if this stack is full
	public boolean getIsFull(){
		return isFull;
	}
	//tests to see if this stack is empty
	public boolean getIsEmpty(){
		return stack.isEmpty();
	}
	//adds a container to the top of this stack
	public void addContainer(GRIDContainer c){
		if(dest.equals("none")){
			dest = c.getDestination();
		}
		stack.push(c);
		if(stack.size() == GRIDConstants.HEIGHT){
			isFull = true;
		}
	}
	//removes the top container of this stack
	public GRIDContainer removeContainer(){
		GRIDContainer c = (GRIDContainer)stack.pop();		
		if(stack.size()<GRIDConstants.HEIGHT){
			isFull = false;
		}
		if(stack.isEmpty()){
			dest = "none";
		}
		return c;
	}
	//returns the destination of the containers of this stack
	public String getDest(){
		return dest;
	}
	//returns the location of a particular container in this stack
	public int findContainer(GRIDContainer c){
		int i = stack.search(c);
		return i;
	}
	//method to look at the top container in this stack
	public GRIDContainer topContainerIs(){
		GRIDContainer c = (GRIDContainer)stack.peek();
		return c;
	}
	//method to return the number of containers in this stack
	public int getSize(){
		return stack.size();
	}	
	//grid block information
	public Color color()
	{
		if(tu == null){
			if(getIsEmpty()){
				return Color.WHITE;
			}else{
				if(getIsFull()){
					return Color.RED;
				}else{
					return Color.PINK;
				}
			}
		}else{
			return Color.BLUE;
		}
	}
	public Object info(int i)
	{
		if(i == 0){
			return name;
		}else{
			return dest+":"+stack.size();
		}
	}
	public int infoAmount() {
		return 2;
	}
}

