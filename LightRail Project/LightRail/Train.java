import java.util.Arrays;
import java.util.Vector;


public class Train implements Token
{
	public static final int TRAIN_1 = 0;
	public static final int TRAIN_2 = 1;
	public static final int TRAIN_3 = 2;
	public static final int TRAIN_4 = 3;
	public static final int TRAIN_5 = 4;
	public static final int TRAIN_6 = 5;
	public static final int TRAIN_7 = 6;
	
	public int id;
	public static final int TRAIN_CAPACITY = 30;
	public Vector riders;
	public boolean isLoading; // keeps track if the train is loading/unloading
	public String name;
	
	public static final int FORWARD = 0;
	public static final int BACKWARD = 1;
	
	private int[] riderDestination;
		
	public int source;
	public int destination;
	public int lastDepartedStation;
	public int direction;// 0=Fwd 1=bkwd
	
	public boolean[] isClosingTime = new boolean[6];
	
//	public int route; //decides which route the train goes in
	
	public Train(/*int p_id, */int route_id, int p_source, int p_destination) 
	{
		name = "T_";//+route_id;//p_id;
		id = route_id;// p_id;//keeps track of the route id
		switch (route_id) 
		{
		case 0: name += "EC";
			break;
		case 1: name += "BC";
			break;
		case 2: name += "CF";
			break;
		case 3: name += "AC";
			break;
		case 4: name += "AFD";
			break;
		case 5: name += "DC";
			break;
		default: name += "BCE";
			break;
		}
		riders = new Vector(); 
		riderDestination = new int[6];
		source = p_source;
		destination = p_destination;
		lastDepartedStation = -1;
		direction = FORWARD;
	}
	
	public int getNumOfRidersInTrain()
	{
		return riders.size();
	}
	
	public void addRiderToTrain(Rider r)
	{
		if (riders.size() < 30)
		{
			riders.addElement(r);
			riderDestination[r.destination]++;
		}
	}
	
	public void removeRidersFromTrain(int destination, boolean removeOnlyOneRider)
	{
		for (int i = 0; i < riders.size(); i++)
		{
			if (((Rider)riders.elementAt(i)).destination == destination)
			{
				riders.remove(i);
				riderDestination[destination] --;
				if (removeOnlyOneRider) break;
			}
		}
		
	}
	
	
	/****** USED BY STATION C ******/
	public Rider[] checkIfRidersNeedToExit()
	{
		Vector ridersNeedToExit = new Vector();
		for (int i = 0; i < riders.size(); i++) 
		{
			if (((Rider)riders.elementAt(i)).destination != destination)
			{
				ridersNeedToExit.add(riders.elementAt(i));
			}
		}
		return (Rider[])ridersNeedToExit.toArray(new Rider[]{});
	}
	/****** ******/

	public int load() 
	{
		return riders.size();
	}

	public int id() 
	{
		return id;
	}

	public String name() 
	{
		return name;
	}
	
	public int[] getRiderDestinations()
	{
		return riderDestination;
	}
	
	public double getUnLoadTime(double time)
	{
		return time + Utility.gen_normal(5, 2); //+ N(5,2)
	}
	
	public double getLoadTime(double time)
	{
		return time + Utility.gen_normal(5, 2); // + N(5,2) 
	}

	
	public Rider[] containsRidersToUnload(int destination)
	{
		Vector ridersToUnload = new Vector();
		int numOfRiderToUnload = riderDestination[destination];
		if (numOfRiderToUnload != 0)
		{
			for (int i = 0; i < riders.size(); i++) 
			{
				if (((Rider)riders.elementAt(i)).destination == destination)
				{
					ridersToUnload.add(riders.elementAt(i));
				}
			}
		}
		if (ridersToUnload.size() == 0)
			return new Rider[0];
		else
			return (Rider[])(ridersToUnload.toArray(new Rider[]{}));
	}
	
	public int getMinRider()
	{
		int[] ids = new int[riders.size()];
		for (int i = 0; i < riders.size(); i++) 
		{
			ids[i] = ((Rider)riders.elementAt(i)).id();
		}
		Arrays.sort(ids);
		if (riders.size() == 0) return -1;
		else return ids[0];
	}
}
