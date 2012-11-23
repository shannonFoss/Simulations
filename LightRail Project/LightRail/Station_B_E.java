import java.awt.Color;
import java.util.Vector;


public class Station_B_E extends Module
{
	InPort inport;
	OutPort outport;

	InPort ridersLoadingInport;
	OutPort ridersUnloadingOutport;

	Lst queue = new Lst();
	
	public int stationId;
	
	Vector trains = new Vector();
	

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

	public Station_B_E(int id) 
	{
		super("Station_"+id, INFINITY, INFINITY);
		stationId = id;
		
		ridersLoadingInport = new InPort("RidersLoadingInport"+id);
		ridersUnloadingOutport = new OutRelay("RidersUnloadingOutport"+id);

		inport = new InPort("InportFromTrack"); 
		outport = new OutRelay("OutportToTrack");

		addInPort(ridersLoadingInport);
		addOutPort(ridersUnloadingOutport);

		addInPort(inport);
		addOutPort(outport);
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
			
			addTrainToStationIfNotExists(train);
			
			if (queue.size == 0 && time > Module.clock.closingTime())
			{
				train.isClosingTime[stationId] = true;
			}

			double timeToLeaveStation = time;
			/****** UNLOAD *****/
			Rider[] ridersToUnload = train.containsRidersToUnload(stationId);
			if (ridersToUnload.length > 0)
			{
				for (int i = 0; i < ridersToUnload.length; i++) 
				{
					ridersUnloadingOutport.enterNow(ridersToUnload
							[i], time);
					train.removeRidersFromTrain(stationId, true);
				}
				timeToLeaveStation += Utility.gen_normal(5, 2);
			}

			// if train's destination and station F matches, interchange source and destinations
			if (train.destination == stationId)
			{
				train.destination = train.source;
				train.source = stationId;
				if (train.direction == Train.BACKWARD)
					train.direction = Train.FORWARD;
				else 
					train.direction = Train.BACKWARD;
			}


			/***** LOAD *****/
			loaded = loadRidersFromQueue(train);

			timeToLeaveStation += Utility.gen_normal(5, 2);

			//send cart to the outport
			if (train.id == 1)//BC
			{
				if (train.isClosingTime[Station.STATION_B] && train.isClosingTime[Station.STATION_C])
				{
					return;
				}
			}
			else if (train.id == 6)//BCE
			{
				if (train.isClosingTime[Station.STATION_B] && train.isClosingTime[Station.STATION_C] && train.isClosingTime[Station.STATION_E])
					return;
			}
			else if (train.id == 0)//EC
			{
				if (train.isClosingTime[Station.STATION_E] && train.isClosingTime[Station.STATION_C])
					return;
			}
				
			outport.enter(train, timeToLeaveStation);

		}
		else if (t instanceof Rider)
		{
			Rider r = (Rider)t;
			//add riders to the specific queues
			queue.enter(r);
			//if there's room in a train that is waiting in the station, send the riders to the train
			for (int i = 0; i < trains.size(); i++) 
			{
				loadRidersFromQueue((Train)trains.elementAt(i));
			}
			
		}
	}


	private boolean loadRidersFromQueue(Train t)
	{
		boolean loaded = false;
		for (queue.reset(); queue.hasNext(); queue.advance()) 
		{
			if (t.load() < Train.TRAIN_CAPACITY)
			{
				loaded = true;
				t.addRiderToTrain((Rider)queue.pop());
			}
			else break;
		}

		return loaded;
	}


	public void exit(Token t, double time)
	{
		super.exit(t, time);
		if (t instanceof Train)
		{
			Train tr = (Train) t;
			tr.lastDepartedStation = this.stationId;
			System.out.println("LEAVES WITH "+ tr.riders.size() + " RIDERS FROM STATION "+stationId+" on Train "+tr.id+ " TO "+tr.destination);
			//remove train from list of trains
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
		if(i == 0 && stationId == Station.STATION_B) 
			return "Station_B";
		if(i == 0 && stationId == Station.STATION_E) 
			return "Station_E";
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
			return "Queue Size: " + queue.size();//"A-D: " + queues[0].size + ", A-C: "+queues[1].size;
		}
			
		return name;
	}
	
	
	public int infoAmount() 
	{
		return 3;
	}

}