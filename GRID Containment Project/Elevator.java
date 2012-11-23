/*
 * Elevator.java
 * Author: Todd Ebert
 * Date: 11/09/2008
 * Update: 11/15/2008
 */

/**
 * An Elevator is a Module that moves Crates from the first floor of a Warehouse up to the 
 * second and third floors. The Elevator leaves the first floor after loading one Crate. 
 */
public class Elevator extends Module implements Token
{
	public static final int LOAD_POLICY_IMPATIENT = 0;
	public static final int LOAD_POLICY_PATIENT = 1;
	public static final int LOAD_POLICY_PATIENCE_LIMIT = 4000;
	public static final int UP = 0;
	public static final int DOWN = 1;
	public static final int NEUTRAL = 2;
	
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int CRATE_CAPACITY = 10;
	public static final int LOAD_CAPACITY = 5000;
	public static final double INTERFLOOR_TRAVEL_TIME = 0.5;
	
	public int numA;
	public int numB;
	public int numC;
	public int numCrates;
	protected int curFloor;
	protected int loadingPolicy;
	protected int direction;
	protected WarehouseFloor groundFloor;
	
	protected Crate[] crate;
	
	public Elevator(int location)
	{
		super("",CRATE_CAPACITY,LOAD_CAPACITY);
		Warehouse w = (Warehouse) parent;
		
		if(location == LEFT) 
			name = new String("Left");
		else 
			name = new String("Right");
		
		FIFOPort eip = new FIFOPort("ElevatorInPort");
		addInPort(eip);
		CrateExitRouter eop = new CrateExitRouter("ElevatorOutPort");
		addOutPort(eop);
		eop.setAssociate(eip);
		crate = new Crate[CRATE_CAPACITY];
		
		direction = NEUTRAL;
		curFloor = WarehouseFloor.GROUND;
		numA = numB = numC = numCrates = 0;
		for(int i=0; i < CRATE_CAPACITY; i++) crate[i] = null;
	}
	
	public void initialize()
	{
		super.initialize();
		
		direction = NEUTRAL;
		curFloor = WarehouseFloor.GROUND;
		numA = numB = numC = numCrates = 0;
		for(int i=0; i < CRATE_CAPACITY; i++) crate[i] = null;
		
	}
	
	/**
	Returns the current floor that this elevator is on. 
	*/
	public int getFloor() {return curFloor;}
	
	public void setFloor(int floor) {curFloor = floor;}
	
	public int crateLoad() 
	{
		return numA*2000 + numB*1000 + numC*500;
	}
	
	public int load() 
	{
		return 1;
	}
	
	/**
	* Elevator is ready to load Crates.
	*/
	public void readyForLoading(double time)
	{
		openPortsNow(time);
		direction = NEUTRAL;
		curFloor = WarehouseFloor.GROUND;
		numA = numB = numC = numCrates = 0;
		for(int i=0; i < CRATE_CAPACITY; i++) crate[i] = null;
	}
	
	/**
	* Returns the direction that the Elevator is moving: UP, DOWN, or NEUTRAL.
	*/
	public int direction() {return direction;}
	
	/**
	* Sets the loading policy which determines the rules for which an Elevator may leave 
	* the ground floor to deliver the Crates to other floors. 
	*/
	public void setLoadingPolicy(int policy) {loadingPolicy = policy;}
	
	public void setGroundFloor(WarehouseFloor wf) {groundFloor = wf;}
	
	public void enter(Token t, double time)
	{ 
		super.enter(t,time);
		Crate c = (Crate) t;
		if(c.type().equals("A")) numA++;
		else if(c.type().equals("B")) numB++;
		else numC++;
		crate[numCrates++] = c;
		applyLoadingPolicy(time);
	}
	
	/**
 	 *  Updates this Elevators direction when the last Crate has exited.
 	 */
	public void exit(Token t, double time)
	{ 
		int i;
		super.exit(t,time);
		Crate c = (Crate) t;
		for(i=0; i < CRATE_CAPACITY; i++)
		{
			if(crate[i] != null && crate[i].name().equals(c.name()))
			{
				crate[i] = null;
				numCrates--;
				break;
			}
		}
		
		if(c.type().equals("A")) 
		{
			numA--;
			if(numA == 0) direction = DOWN;
		}
		else if(c.type().equals("B")) 
		{
			numB--;
			if(numB == 0 && numC == 0 && numA > 0) direction = UP;
			else if(numB == 0 && numC == 0 && numA == 0) direction = DOWN;
		}
		else 
		{
			numC--;
			if(numB == 0 && numC == 0 && numA > 0) direction = UP;
			else if(numB == 0 && numC == 0 && numA == 0) direction = DOWN;
		}
	}
	
	/**Returns the time in which the last crate will be unloaded.
	*/
	public double getUnloadTime(int floor, double time)
	{
		direction = NEUTRAL;
		int i;
		Port p = (Port) (outPorts.front());
		Crate c;
		double lastUnloadTime=time;
		
		if(floor == WarehouseFloor.MIDDLE) //unload type B and C 
		{
			lastUnloadTime = time + numA*Crate.A_UNLOAD_TIME;
			for(i=0; i < CRATE_CAPACITY; i++)
			{
				if((c=crate[i]) != null && c.type().equals("B"))
				{
					lastUnloadTime += Crate.B_UNLOAD_TIME;
					p.enter(c, lastUnloadTime);
				}
				else if((c=crate[i]) != null && c.type().equals("C"))
				{
					lastUnloadTime += Crate.C_UNLOAD_TIME;
					p.enter(c, lastUnloadTime);
				}
			}
		}
		else if(floor == WarehouseFloor.TOP) //unload type A
		{
			for(i=0; i < CRATE_CAPACITY; i++)
			{
				if((c=crate[i]) != null && c.type().equals("A"))
				{
					lastUnloadTime += Crate.A_UNLOAD_TIME;
					p.enter(c, lastUnloadTime);
				}
			}
		}
		
		return lastUnloadTime;
	}
	
	public String loadToString()
	{
		String s = new String("");
		int count = 0;
		Crate c;
		
		for(int i=0; i < CRATE_CAPACITY; i++)
		{
			if((c=crate[i]) != null)
			{
				count++;
				s += c.name();
				if(count < numCrates)
					s += ",";
			}
		}
		
		return s;
	}
	
	/**
	* This is called whenever a Token enters an OutPort, and causes the Module to be
	* under utilized. Whenever a Module is fully utilized, its InPorts are automatically closed.
	* This method allows the Module to selectively open InPorts given that it is now under
	* utilized. The default is to open all InPorts.
	*/
	public void selectPortOpenings(double time)
	{
	
	}
	
	protected void applyLoadingPolicy(double time)
	{
		if(loadingPolicy == LOAD_POLICY_IMPATIENT)
		{
			closePortsNow(time);
			direction = UP;
			groundFloor.exitUp(this,time+INTERFLOOR_TRAVEL_TIME);
		}
		else if(loadingPolicy == LOAD_POLICY_PATIENT && crateLoad() >= LOAD_POLICY_PATIENCE_LIMIT)
		{
			closePortsNow(time);
			direction = UP;
			groundFloor.exitUp(this,time+INTERFLOOR_TRAVEL_TIME);
		}
	}
}
