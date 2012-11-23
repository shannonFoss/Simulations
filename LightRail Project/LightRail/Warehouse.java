/*
 * Warehouse.java
 * Author: Todd Ebert
 * Date: 11/09/2008
 * 
 */

import java.awt.Color;

/**
 * A Warehouse is a Module that houses the two Elevators.
 */
public class Warehouse extends Module
{
	public static final int NUMBER_OF_FLOORS = 3;
	
	public static final int RANDOM_ROUTING = 0;
	public static final int SPECIALIZED_ROUTING = 1;
	
	protected WarehouseFloor[] leftFloor;
	protected WarehouseFloor[] rightFloor;
	protected Elevator eLeft;
	protected Elevator eRight;
	protected int routingPolicy;
	protected InPort leftRouter;
	protected InPort rightRouter;
	protected InPort crateRouter;
	
	public Warehouse()
	{
		super("Warehouse",Module.INFINITY,Module.INFINITY);
		routingPolicy = RANDOM_ROUTING;
		routingPolicy = RANDOM_ROUTING;
		eLeft = new Elevator(Elevator.LEFT);
		eRight = new Elevator(Elevator.RIGHT);
		addSubmodule(eLeft);
		addSubmodule(eRight);
		
		leftRouter = new FIFORelay("LeftRelay");
		rightRouter = new FIFORelay("RightRelay");
		crateRouter = new CrateRouter();
		addInPort(crateRouter);
		addInPort(leftRouter);
		addInPort(rightRouter);
		OutRelay leftOutRelay = new OutRelay("LeftOutRelay");
		OutRelay rightOutRelay = new OutRelay("RightOutRelay");
		OutRelay crateOutRelay = new OutRelay("CrateOutRelay");
		addOutPort(crateOutRelay);
		addOutPort(leftOutRelay);
		addOutPort(rightOutRelay);
		crateOutRelay.setAssociate(crateRouter);
		leftOutRelay.setAssociate(leftRouter);
		rightOutRelay.setAssociate(rightRouter);
		Module.connectInToIn(this,eLeft,"CrateRouter","ElevatorInPort");
		Module.connectInToIn(this,eRight,"CrateRouter","ElevatorInPort");
		Module.connectInToIn(this,eLeft,"LeftRelay","ElevatorInPort");
		Module.connectInToIn(this,eRight,"RightRelay","ElevatorInPort");
		
		//Connect Elevator OutPorts with Warehouse OutPorts
		Module.connectOutToOut(eLeft,this,"ElevatorOutPort","LeftOutRelay");
		Module.connectOutToOut(eLeft,this,"ElevatorOutPort","CrateOutRelay");
		Module.connectOutToOut(eRight,this,"ElevatorOutPort","RightOutRelay");
		Module.connectOutToOut(eRight,this,"ElevatorOutPort","CrateOutRelay");
		
		leftFloor = new WarehouseFloor[NUMBER_OF_FLOORS];
		rightFloor = new WarehouseFloor[NUMBER_OF_FLOORS];
		
		//Define and connect WarehouseFloors
		leftFloor[0] = new WarehouseFloor("LeftFloor " + 1,WarehouseFloor.GROUND,1,eLeft);
		rightFloor[0] = new WarehouseFloor("RightFloor " + 1,WarehouseFloor.GROUND,1,eRight);
		addSubmodule(leftFloor[0]);
		addSubmodule(rightFloor[0]);
		eLeft.setGroundFloor(leftFloor[0]);
		eRight.setGroundFloor(rightFloor[0]);
		int fnum = 1;
		
		int i;
		for(i=1; i <  NUMBER_OF_FLOORS-1; i++)
		{
			fnum = i+1;
			leftFloor[i] = 
				new WarehouseFloor("LeftFloor " + fnum,WarehouseFloor.MIDDLE,fnum,eLeft);
			rightFloor[i] = 
				new WarehouseFloor("RightFloor " + fnum,WarehouseFloor.MIDDLE,fnum, eRight);
			leftFloor[i-1].connect(leftFloor[i]);
			rightFloor[i-1].connect(rightFloor[i]);
			addSubmodule(leftFloor[i]);
			addSubmodule(rightFloor[i]);
		}
		
		fnum++;
		leftFloor[NUMBER_OF_FLOORS-1] = 
			new WarehouseFloor("LeftFloor " + fnum,WarehouseFloor.TOP,fnum,eLeft);
		rightFloor[NUMBER_OF_FLOORS-1] = 
			new WarehouseFloor("RightFloor " + fnum,WarehouseFloor.TOP,fnum,eRight);
		addSubmodule(leftFloor[i]);
		addSubmodule(rightFloor[i]);
			
		leftFloor[i-1].connect(leftFloor[i]);
		rightFloor[i-1].connect(rightFloor[i]);
			
		//Connect Modules
		Module.connectInToIn(this,eLeft,"LeftRelay","ElevatorInPort");
		Module.connectInToIn(this,eRight,"RightRelay","ElevatorInPort");
		Module.connectInToIn(this,eLeft,"CrateRouter","ElevatorInPort");
		Module.connectInToIn(this,eRight,"CrateRouter","ElevatorInPort");
	}
	
	public WarehouseFloor leftFloor(int i) {return leftFloor[i];}
	public WarehouseFloor rightFloor(int i) {return rightFloor[i];}
	
	public void setRoutingPolicy(int policy) 
	{
		routingPolicy = policy;
	}
	
	public void setLoadingPolicy(int policy) 
	{
		eLeft.setLoadingPolicy(policy);
		eRight.setLoadingPolicy(policy);
	}
	
	/**
	* Returns the ith piece of information that describes this object. The default is that, 
	* for any input i, the name of the module is returned. 
	*/
	public Object info(int i)
	{
		if(i == 0 && routingPolicy == RANDOM_ROUTING) 
			return "Elevator Queue";
		if(i == 0 && routingPolicy == SPECIALIZED_ROUTING) 
			return "Elevator Queues";
		if(i == 1 && routingPolicy == RANDOM_ROUTING)
			return "Queue length: " + crateRouter.qLength(); 
		if(i == 1 && routingPolicy == SPECIALIZED_ROUTING)
			return "A-Queue length: " + leftRouter.qLength(); 
		if(i == 2 && routingPolicy == RANDOM_ROUTING)
			return ""; 
		if(i == 2 && routingPolicy == SPECIALIZED_ROUTING)
			return "BC-Queue length: " + rightRouter.qLength(); 
		return null;
	}
	
	/**
	* Returns the number of pieces of information that are used to display information about 
	* this Module. The default is 1, where the only piece of information is the Module name. 
	*/
	public int infoAmount() {return 3;}
	
	/**
 	 *  Color that is used for the background of the JPanel that is used to represent the 
 	 * graphical view of this Module.  
 	 */
	public Color color()
	{
		return Color.YELLOW;
	}
}
