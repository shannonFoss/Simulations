//Event.java
//Author: Todd Ebert
//Date: 10-22-2008
//Modified: 07-16-2007

/**
 * An Event is any instantaneous that changes the state of the system.
 * At a minimum, every Event has a time of occurrence, and a place where 
 * it occurs, called the 
 * EventHost. An Event that is supposed to occur immediately after its parent Event has its
 * isNext flag set to true, which places it at the top of the FutureEventList once it has been 
 * inserted.  
 */
abstract public class Event implements Ordered
{
	protected double time;
	protected EventHost host;
	//true if this Event, when entered, will be the next event. Default is false
	protected boolean isNext; 
	
	/**
 	 * Every Event is processed, which has the effect of changing the states of one or more 
 	 * Objects in the system, including one or more Tokens and/or Modules.  
	 */
	public abstract void process(); 
	
	public Event(double time, EventHost h)
	{
		this.time = time;
		host = h;
		isNext = false;
	}
	
	/**
 	 * Returns true if this Event will be the next Event processed in the FutureEventList.  
	 */
	public boolean isNext() {return isNext;}
	public double time() {return time;}
	public EventHost host() {return host;}
	
	public int compare(Object obj)
	{
		Event e = (Event)obj;
		double time2 = e.time();
		
		if(time < time2) 
			return -1;
		else if(time == time2) 
		{
			if(isNext) return -1;
			else if(e.isNext()) return 1;
			else return 0;
		}
		else return 1;
	}
	
	/**
 	 * Invoked if this Event will be the next Event processed in the FutureEventList.  
	 */
	public void setIsNext() {isNext = true;}
}
