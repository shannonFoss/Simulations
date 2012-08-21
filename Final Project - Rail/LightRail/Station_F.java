import java.awt.Color;
import java.util.Vector;

public class Station_F extends Module
{
	InPort inportFromLower;
	InPort inportFromUpper;
	InPort inportFromLeft;
	OutPort outportToLower;
	OutPort outportToUpper;
	OutPort outportToLeft;
	
	InPort ridersLoadingInport;
	OutPort ridersUnloadingOutport;
	
	Lst[] queues = new Lst[2];
	
	Vector trains = new Vector();
	
	
	public void initialize()
	{
		super.initialize();
		for (int i = 0; i < trains.size(); i++) 
		{
			enter((Train)trains.elementAt(i), 0);//, Utility.gen_normal(5, 2));
		}
	}

	public void addTrainToStation(Train t)
	{
		trains.addElement(t);
	}
	
	public Station_F() 
	{
		super("Station_F", INFINITY, INFINITY);
		
		for (int i = 0; i < queues.length; i++) 
		{
			queues[i] = new Lst();
		}
		
		ridersLoadingInport = new InPort("RidersLoadingInport5");
		ridersUnloadingOutport = new OutRelay("RidersUnloadingOutport5");
		
		inportFromLower = new InPort("LowerEdgeInport_3"); //D
		inportFromUpper = new InPort("UpperEdgeInport_0"); //A
		inportFromLeft = new InPort("LeftEdgeInport_1_2_4"); //B,C,E
		outportToLower = new OutRelay("LowerEdgeOutport_3");
		outportToUpper = new OutRelay("UpperEdgeOutport_0");
		outportToLeft = new OutRelay("LeftEdgeOutport_1_2_4");
		
		addInPort(ridersLoadingInport);
		addOutPort(ridersUnloadingOutport);
		
		addInPort(inportFromLower);
		addInPort(inportFromUpper);
		addInPort(inportFromLeft);
		addOutPort(outportToLower);
		addOutPort(outportToUpper);
		addOutPort(outportToLeft);
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

	public void enter(Token t, double time)
	{
		boolean loaded = false;
		super.enter(t, time);
		// a train enters if there are riders destined to this station
		// unload them
		if (t instanceof Train)
		{
			Train train = (Train)t;
			if (queues[0].size == 0 && queues[1].size == 0 && time > Module.clock.closingTime())
			{
				train.isClosingTime[Station.STATION_F] = true;
			}

			addTrainToStationIfNotExists(train);
			
			double timeToLeaveStation = time;
			/****** UNLOAD *****/
			Rider[] ridersToUnload = train.containsRidersToUnload(Station.STATION_F);
			if (ridersToUnload.length > 0)
			{
				for (int i = 0; i < ridersToUnload.length; i++) 
				{
					ridersUnloadingOutport.enterNow(ridersToUnload
							[i], time);
					train.removeRidersFromTrain(Station.STATION_F, true);
				}
				timeToLeaveStation += Utility.gen_normal(5, 2);
			}

			// if train's destination and station F matches, interchange source and destinations
			if (train.destination == Station.STATION_F)
			{
				train.destination = train.source;
				train.source = Station.STATION_F;
				if (train.direction == Train.BACKWARD)
					train.direction = Train.FORWARD;
				else 
					train.direction = Train.BACKWARD;
			}
			
			//check if there's any other riders except the ones destined for their destinations
			exitRidersToQueue(train);
			
			/***** LOAD *****/
			loaded = loadRidersFromQueue(train);
			
//			if (loaded)
				timeToLeaveStation += Utility.gen_normal(5, 2);
			
			//send cart to the outport, which matches the destination of the train
			if (train.id == 2)//CF
			{
				if (train.isClosingTime[Station.STATION_C] && train.isClosingTime[Station.STATION_F])
				{
					return;
				}
			}
			else if (train.id == 4)//AFD
			{
				if (train.isClosingTime[Station.STATION_A] && train.isClosingTime[Station.STATION_F] && train.isClosingTime[Station.STATION_D])
					return;
			}

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
		Lst q = null;
		if (t.destination == Station.STATION_A || t.destination == Station.STATION_D)
		{
			q = queues[0];
		}
		else
		{
			q = queues[1];
		}
		for (q.reset(); q.hasNext(); q.advance()) 
		{
			if (t.load() < Train.TRAIN_CAPACITY)
			{
				loaded = true;
				t.addRiderToTrain((Rider)q.pop());
			}
			else break;
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
		if (r.destination == Station.STATION_A || r.destination == Station.STATION_D)
			queues[0].enter(r);
		else queues[1].enter(r);
		return remove;
	}
	
	
	public void exit(Token t, double time)
	{
		super.exit(t, time);
		if (t instanceof Train)
		{
			Train tr = (Train)t;
			tr.lastDepartedStation = Station.STATION_F;
			System.out.println("LEAVES WITH "+ tr.riders.size() + " RIDERS FROM STATION F on Train "+tr.id+ " TO "+tr.destination);
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
//				rider = (Rider)train.riders.elementAt(k); 
//    			if(rider.displayRider == true){
					rid = train.getMinRider();
					if (rid != -1)
						s += "(R"+rid+")";;//rider.id()+")";
//    			}
//
				s += ", ";
			}
			return s;

		}
		if(i == 2)
		{
			return "F-D/F-A: " + queues[0].size + ", F-C: "+queues[1].size;
		}
			
		return name;
	}
	
	public int infoAmount() 
	{
		return 3;
	}


}
