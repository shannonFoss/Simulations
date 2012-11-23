/*
 * FutureEventList.java
 * Author: Todd Ebert
 * Date: 07/06/2007
 * Modified: 11-01-2008
 */

/**
 * A FutureEventList is a Heap data structure that orders the Events that occur in 
 * a discrete-event simulation.
 */
public class FutureEventList extends Heap
{
	private int eventNum;

	public FutureEventList() 
	{
		super();
	}
	
	/**
 	 * The FEL is cleared before the begin of a new simulation. 
 	 */
	public void initialize()
	{
		clear();
	}
	
	/**
 	 * Returns the next Event that is to occur. Returns null if there are no events. 
 	 */
	public Event viewNextEvent()
	{
		return (Event)(super.top());
	}
	
	/**
 	 * Removes and returns the most current event from the event list 
 	 */
	public Event nextEvent()
	{
		eventNum++;
		return (Event)(super.pop());
	}
	
	/**
	* Returns true if there are more Events stored in this FutureEventList.
	*/
	public boolean hasMoreEvents()
	{
		if(Module.fel.size() > 0) return true;
		return false;
	}
	
	/**
 	 * Inserts e into the event list. 
 	 */
	public void insert(Event e)
	{
		super.insert(e);
	}
	
	/**
 	 * Removes e into the event list. 
 	 */
	public Event cancel(Event e)
	{
		return (Event)(super.remove(e));
	}
	
	/**
 	 * Number of events that have been processed by way of calling nextEvent()
 	 * (A good indicator of how many events have occurred in the system). 
 	 */
	public int numberOfEvents()
	{
		return eventNum;
	}
	
	public void clear()
	{
		eventNum=0;
		super.clear();
	}
}
