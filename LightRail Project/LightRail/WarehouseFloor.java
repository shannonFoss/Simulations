/*
 * WarehouseFloor.java
 * Author: Todd Ebert
 * Date: 11/15/2008
 * 
 */

import java.awt.Color;

/**
 * A WarehouseFloor is a Module that represents part of a floor 
 * (either left or right half) of a Warehouse.
 */
public class WarehouseFloor extends Module
{
	//types of WarehouseFloors
	public static final int GROUND = 1;
	public static final int MIDDLE = 2;
	public static final int TOP = 3;
	
	protected InPort upInPort;
	protected InPort downInPort;
	protected OutPort upOutPort;
	protected OutPort downOutPort;
	protected int type;
	protected Elevator elevator; //elevator that services this WarehouseFloor
	protected int floorNumber;
	
	public WarehouseFloor(String name, int type, int floorNumber, Elevator elevator)
	{
		super(name,Module.INFINITY,Module.INFINITY);
		this.floorNumber = floorNumber;
		this.type = type;
		this.elevator = elevator;
		if(type == GROUND)
		{
			upInPort = null;
			downOutPort = null;
			upOutPort = new OutRelay("UpOutPort");
			downInPort = new InPort("DownInPort");
			addOutPort(upOutPort);
			addInPort(downInPort);
			upOutPort.setAssociate(downInPort); 
		}
		else if(type == MIDDLE)
		{
			upInPort = new InPort("UpInPort");
			downOutPort = new OutRelay("DownOutPort");
			upOutPort = new OutRelay("UpOutPort");
			downInPort = new InPort("DownInPort");
			addOutPort(upOutPort);
			addOutPort(downOutPort);
			addInPort(downInPort);
			addInPort(upInPort);
			upOutPort.setAssociate(upInPort);
			downOutPort.setAssociate(downInPort);
		}
		else //if(type == TOP)
		{
			upInPort = new InPort("UpInPort");
			downOutPort = new OutRelay("DownOutPort");
			upOutPort = null;
			downInPort = null;
			addOutPort(downOutPort);
			addInPort(upInPort);
			downOutPort.setAssociate(upInPort);
		}
	}
	
	public void exitUp(Elevator e, double time)
	{
		upOutPort.enter(e,time);
	}
	
	public void enter(Token t, double time)
	{
		super.enter(t,time);
		double finalUnloadTime;
		if(type == GROUND) elevator.readyForLoading(time);
		else if(type == MIDDLE) 
		{
			elevator.setFloor(floorNumber);
			if(elevator.numB > 0 || elevator.numC > 0) 
			{
				finalUnloadTime = elevator.getUnloadTime(floorNumber,time);
				if(elevator.numA > 0) 
					upOutPort.enter(t,finalUnloadTime+Elevator.INTERFLOOR_TRAVEL_TIME);
				else
					downOutPort.enter(t,finalUnloadTime+Elevator.INTERFLOOR_TRAVEL_TIME);
			}
			else if(elevator.numA > 0) 
				upOutPort.enter(t,time+Elevator.INTERFLOOR_TRAVEL_TIME);
			else  
				downOutPort.enter(t,time+Elevator.INTERFLOOR_TRAVEL_TIME);
		}
		else //(type == TOP) 
		{
			elevator.setFloor(floorNumber);
			if(elevator.numA > 0) 
			{
				finalUnloadTime = elevator.getUnloadTime(floorNumber,time);
				downOutPort.enter(t,finalUnloadTime+Elevator.INTERFLOOR_TRAVEL_TIME);
			}
			else downOutPort.enter(t,time+Elevator.INTERFLOOR_TRAVEL_TIME);
		}
	}
	
	/**
	* Connect this floor to the one directly above it. 
	*/
	public void connect(WarehouseFloor above)
	{
		Module.connect(this,above,"UpOutPort","UpInPort");
		Module.connect(above,this,"DownOutPort","DownInPort");
	}
	
	/**
	* Returns the ith piece of information that describes this object. The default is that, 
	* for any input i, the name of the module is returned. 
	*/
	public Object info(int i)
	{
		if(i == 0) return name;
		if(i == 1 && floorNumber == elevator.getFloor() && 
			elevator.direction() == Elevator.NEUTRAL && floorNumber != GROUND)
			return "elevator unloading";
		if(i == 1 && floorNumber == elevator.getFloor() && 
			elevator.direction() == Elevator.NEUTRAL && floorNumber == GROUND)
			return "elevator loading";
		if(i == 1 && floorNumber == elevator.getFloor() && 
			elevator.direction() == Elevator.UP)
			return "elevator up";
		if(i == 1 && floorNumber == elevator.getFloor() && 
			elevator.direction() == Elevator.DOWN)
			return "elevator down";
		if(i == 1) return "";
		if(i == 2 && elevator.load() == 0 && floorNumber == elevator.getFloor()) 
			return "elevator empty";
		if(i == 2 && floorNumber == elevator.getFloor()) 
			return "payload: " + elevator.loadToString();
		if(i == 2) return "";
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
		if(floorNumber == elevator.getFloor() && elevator.direction() == Elevator.NEUTRAL)
			return Color.RED;
		if(floorNumber == elevator.getFloor()) 
			return Color.GREEN;
		return Color.BLUE;
	}
	
}
