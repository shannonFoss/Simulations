import java.awt.Color;
import java.util.Vector;

//import sun.text.normalizer.UProperty;

public class Station_C extends Module
{
	
	InPort inportFromLower;
	InPort inportFromUpper;
	InPort inportFromLeft;
	InPort inportFromRight_3;
	InPort inportFromRight_5;
	OutPort outportToLower;
	OutPort outportToUpper;
	OutPort outportToLeft;
	OutPort outportToRight_3;
	OutPort outportToRight_5;
	
	InPort ridersLoadingInport;
	OutPort ridersUnloadingOutport;
	
	Lst[] queues = new Lst[7];
	
	Vector trains = new Vector();
	
	public Station_C() 
	{
		super("Station_C", INFINITY, INFINITY);
		
		for (int i = 0; i < queues.length; i++) 
		{
			queues[i] = new Lst();
		}
		
		ridersLoadingInport = new InPort("RidersLoadingInport2");
		ridersUnloadingOutport = new OutRelay("RidersUnloadingOutport2");
		
		inportFromLower = new InPort("LowerEdgeInport_4"); //E
		inportFromUpper = new InPort("UpperEdgeInport_0"); //A
		inportFromLeft = new InPort("LeftEdgeInport_1"); //B
		inportFromRight_3 = new InPort("RightEdgeInport_3"); //D & F
		inportFromRight_5 = new InPort("RightEdgeInport_5");
		outportToLower = new OutRelay("LowerEdgeOutport_4");
		outportToUpper = new OutRelay("UpperEdgeOutport_0");
		outportToLeft = new OutRelay("LeftEdgeOutport_1");
		outportToRight_3 = new OutRelay("RightEdgeOutport_3");
		outportToRight_5 = new OutRelay("RightEdgeOutport_5");
		
		addInPort(ridersLoadingInport);
		addOutPort(ridersUnloadingOutport);
		
		addInPort(inportFromLower);
		addInPort(inportFromUpper);
		addInPort(inportFromLeft);
		addInPort(inportFromRight_3);
		addInPort(inportFromRight_5);
		addOutPort(outportToLower);
		addOutPort(outportToUpper);
		addOutPort(outportToLeft);
		addOutPort(outportToRight_3);
		addOutPort(outportToRight_5);
	}
	
	private void addTrainToStationIfNotExists(Train t)
	{
		Train train = null;
		for (int i = 0; i < trains.size(); i++) 
		{
			train = (Train)trains.elementAt(i);
			if (train.id == t.id)
				return;
		}
		trains.addElement(t);
	}
	
	public void initialize()
	{
		super.initialize();
		
		if (trains.size() > 0)
		{
			for (int i = 0; i < trains.size(); i++) 
			{
				enter((Train)trains.elementAt(i), 0);//, Utility.gen_normal(5, 2));
			}
			
		}
	}
	
	public void addTrainToStation(Train t)
	{
		trains.addElement(t);
	}
	
	public void enter(Token t, double time)
	{
		boolean loaded = false;
		super.enter(t, time);
		// a train enters if there are riders destined to this station
		// unload them
		if (t instanceof Train)
		{
			Train train = (Train)t;
			if (queues[0].size == 0 && queues[1].size == 0 && queues[2].size == 0 && queues[3].size == 0 && 
					queues[4].size == 0 && queues[5].size == 0 && time > Module.clock.closingTime())
			{
				train.isClosingTime[Station.STATION_C] = true;
			}

			addTrainToStationIfNotExists(train);
			
			double timeToLeaveStation = time;
			/****** UNLOAD *****/
			Rider[] ridersToUnload = train.containsRidersToUnload(Station.STATION_C);
			if (ridersToUnload.length > 0)
			{
				for (int i = 0; i < ridersToUnload.length; i++) 
				{
					ridersUnloadingOutport.enterNow(ridersToUnload
							[i], time);
					train.removeRidersFromTrain(Station.STATION_C, true);
				}
				timeToLeaveStation += Utility.gen_normal(5, 2);
				
			}
			//check if there's any other riders except the ones destined for their destinations
			exitRidersToQueue(train);
			
//			 if train's destination and station F matches, interchange source and destinations
			if (train.destination == Station.STATION_C)
			{
				train.destination = train.source;
				train.source = Station.STATION_C;
				if (train.direction == Train.BACKWARD)
					train.direction = Train.FORWARD;
				else 
					train.direction = Train.BACKWARD;
			}
			
			/***** LOAD *****/
			loaded = loadRidersFromQueue(train);
			
			timeToLeaveStation += Utility.gen_normal(5, 2);
			
			//send cart to the outport, which matches the destination of the train
			if (train.id == 6)//BCE
			{
				if (train.isClosingTime[Station.STATION_B] && train.isClosingTime[Station.STATION_C] && train.isClosingTime[Station.STATION_E])
				{
					return;
				}
			}
			else if (train.isClosingTime[train.destination] && train.isClosingTime[Station.STATION_C]) return;
			
			sendTrainToOutport(train, timeToLeaveStation);
			
		}
		else if (t instanceof Rider)
		{
			Rider r = (Rider)t;
			//add riders to the specific queues
			addRiderToQueue(r);
			
			//if there's room in a train that is waiting in the station, send the riders to the train
			for (int i = 0; i < trains.size(); i++) 
			{
				loadRidersFromQueue((Train)trains.elementAt(i));
			}
		}
	}
	
	private void sendTrainToOutport(Train t, double time)
	{
		Lst lstOutports = this.outPorts();
		Port p;
		for (lstOutports.reset(); lstOutports.hasNext(); lstOutports.advance())
		{
			p = (Port)lstOutports.access();
			if (p.name().contains(t.destination+""))
			{
				p.enter(t, time);
				break;
			}
		}
	}
	
	private boolean loadRidersFromQueue(Train t)
	{
		boolean loaded = false;
		if (queues[t.destination].size() > 0)
		{
			for (queues[t.destination].reset(); queues[t.destination].hasNext(); queues[t.destination].advance()) 
			{
				if (t.load() < Train.TRAIN_CAPACITY)
				{
					loaded = true;
					t.addRiderToTrain((Rider)queues[t.destination].pop());
				}
				else break;
			}
		}
		return loaded;
	}
	
	private void exitRidersToQueue(Train train)
	{
		Rider[] ridersExit = train.checkIfRidersNeedToExit();
		boolean remove = false;
		if (ridersExit.length > 0)
		{
			//determine the queue they need to enter based on the riders destination
			for (int i = 0; i < ridersExit.length; i++) 
			{
				remove = addRiderToQueue(ridersExit[i]);
				if (remove)
					train.removeRidersFromTrain(ridersExit[i].destination, true);
			}
		}
	}
	
	private boolean addRiderToQueue(Rider r)
	{
		boolean remove = false;
		queues[r.destination].enter(r);
		remove = true;
		return remove;
	}
	
	
	public void exit(Token t, double time)
	{
		super.exit(t, time);
		if (t instanceof Train)
		{
			Train tr = (Train)t;
			tr.lastDepartedStation = Station.STATION_C;
			System.out.println("LEAVES WITH "+ tr.riders.size() + " RIDERS FROM STATION C on Train "+tr.id+ " TO "+tr.destination);
			//remove train from vector
			for (int i = 0; i < trains.size(); i++) 
			{
				if (((Train)trains.elementAt(i)).id == tr.id)
				{
					trains.removeElementAt(i);
					break;
				}
			}
		}
		else if (t instanceof Rider)
		{
			//keep track of the time
			Rider r = (Rider)t;
			r.endTime = time;
			Utility.computeTotalTimeSpentInSystem(r);
		}
	}

	public Color color()
	{
		return Color.GREEN;
	}
	
	public Object info(int i)
	{
		Train train;
		Rider rider;
		int rid = -1;
		String s = "";
		if(i == 0) 
			return name;
		if(i == 1) 
		{
			//give the count of trains
			for (int j = 0; j < trains.size(); j++) 
			{
				train = ((Train)trains.elementAt(j));
				s += ((Train)trains.elementAt(j)).name;
//				for(int k=0;k<train.riders.size();k++){
//					rider = (Rider)train.riders.elementAt(k); 
//	    			if(rider.displayRider == true){
						rid = train.getMinRider();
						if (rid != -1)
							s += "(R"+rid+")";;//rider.id()+")";
//	    			}
//				}
				s += ", ";
			}
			return s;
		}
		if(i == 2)
		{
			return "C-A: " + queues[0].size + ", C-B: "+queues[1].size; 
		}
		if (i == 3)
		{
			return "C-D: "+queues[3].size + ", C-E: "+queues[4].size ;
		}
		if (i == 4)
		{
			return "C-F: "+queues[5].size;
		}
			
		return name;
	}
	
	public int infoAmount() 
	{
		return 5;
	}
}
