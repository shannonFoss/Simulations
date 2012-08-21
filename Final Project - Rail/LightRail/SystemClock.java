/*
 * SystemClock.java
 * Author: Todd Ebert
 * Date: 07/06/2007
 * Modified: 07-16-2007
 * Updated: 11/02/2008
 */

import java.awt.*;
import java.text.DecimalFormat;

/**
 *  The SystemClock keeps track of the current time as well as other time parameters, such as the 
 * simulation "closing time" (deadline to schedule an Event).
 */
public class SystemClock 
{
	//Default: system start time = 0.0
	public static final double START_TIME = 0.0;
	
	//units that may be used to measure time
	public static final int NANOSECONDS=0;
	public static final int MICROSECONDS=1;
	public static final int MILLISECONDS=2;
	public static final int SECONDS=3;
	public static final int MINUTES=4;
	public static final int HOURS=4;
	public static final int DAYS=5;
	public static final int YEARS=6;
	
	private static final int UNDEFINED = -1;
	
	//deadline for Generators to schedule events 
	private double closingTime; 
	//time (after START_TIME and using the defined units) of last event
	private double lastEventTime; 
	private double lastObservedEventTime; 
	
	private double scaleFactor;  
	private boolean isConstantDelay;
	private double delay; //in seconds
	private int units;
	private DecimalFormat fmt;
	private double mcf; //milliseconds conversion factor
	//last time reported by System.currentTimeMillis() when pauseUntilNextEvent() was 
	//last called
	private long timeOfLastPauseRequest; 
	
	//number of milliseconds pause that the user asked for in the last call to 
	//pauseUntilNextEvent()
	private long lastPauseAmount;
	//number of milliseconds over that requested user pauses have encountered.
	//The goal is to keep this value as close to zero as possible, by shortening future delay
	//requests. 
	private long totalOverPause; 
	
	public SystemClock()
	{
		//Default scale factor is 1.0
		scaleFactor = 1.0;
		isConstantDelay = true;
		delay = 0.0;
		
		//Default units is seconds
		units = SECONDS;
		mcf = 1000.0;
		fmt = new DecimalFormat("0.##");
		lastEventTime = START_TIME;
		lastObservedEventTime = START_TIME;
		closingTime = START_TIME;
		timeOfLastPauseRequest = UNDEFINED;
		lastPauseAmount = UNDEFINED;
		totalOverPause = 0;
	}
	
	/**
	* Initialize the clock before running a simulation.
	*/
	public void initialize(double closingTime)
	{
		this.closingTime = closingTime;
		lastEventTime = START_TIME;
		lastObservedEventTime = START_TIME;
		timeOfLastPauseRequest = UNDEFINED;
		lastPauseAmount = UNDEFINED;
		totalOverPause = 0;
	}
	
	/**
	* Sets the time for which the last system event has occurred.
	*/
	public void setLastEventTime(double time)
	{
		lastEventTime = time;
	}
	
	/**
	* Returns the time for which the last event has occurred.
	*/
	public double lastEventTime()
	{
		return lastEventTime;
	}
	
	/**
	* Sets the deadline for which a Generator may schedule an input event.
	*/
	public void setClosingTime(double time)
	{
		closingTime = time;
	}
	
	/**
	* Returns the deadline for which a Generator may schedule an input event.
	*/
	public double closingTime()
	{
		return closingTime;
	}
	
	/**
	* Returns true if simulation events are temporally experienced during .
	*/
	public boolean isScaledSimulation() {return !isConstantDelay;}
	
	/**
	* Sets the clocks scale factor to x >0. The scale factor determines the degree of
	* speed up or slow down that is to occur when viewing a series of real-time events in 
	* a simulation setting. The scale factor is in effect when isScaledSimulation() returns true.
	*/
	public void setScaleFactor(double x)
	{
		scaleFactor = x;
		isConstantDelay = false;
	}
	
	/**
	* Returns the scale factor. 
	*/
	public double scaleFactor()
	{
		return scaleFactor;
	}
	
    /**
	* Sets the clocks delay to s > 0 seconds. The delay determines how long it will take 
	* to process the next simulation event. 
	*/
	public void setDelay(double s)
	{
		delay = 1000*s;
		isConstantDelay = true;
	}
	
	/**
	* Returns true if simulation events are temporally experienced during .
	*/
	public boolean isConstantDelay() {return !isConstantDelay;}
	
	/**
	* Returns the delay. 
	*/
	public double delay()
	{
		return delay;
	}
	
	/**
	* Sets the time units. 
	* Assumption: type is a member of the set
	* {NANOSECONDS,MICROSECONDS,MILLISECONDS,SECONDS,MINUTES,HOURS,DAYS,YEARS}
	*/
	public void setUnits(int type)
	{
		units=type;
		setMCF();
	}
	
	/**
	* Sets the time units. 
	* Assumption: type is a member of the set
	* {"nanoseconds","microseconds","milliseconds","seconds","minutes","hours","days","years"}
	*/
	public void setUnits(String unitName)
	{
		if(unitName.equals("nanoseconds"))
			units = NANOSECONDS;
		else if(unitName.equals("microseconds"))
			units = MICROSECONDS;
		else if(unitName.equals("milliseconds"))
			units = MILLISECONDS;
		else if(unitName.equals("seconds"))
			units = SECONDS;
		else if(unitName.equals("minutes"))
			units = MINUTES;
		else if(unitName.equals("hours"))
			units = HOURS;
		else if(unitName.equals("days"))
			units = DAYS;
		else //if(units == YEARS)
			units = YEARS;
		
		setMCF();
	}
	
	/**
	* Returns the type of units used. The default is SystemClock.SECONDS.
	*/
	public double units()
	{
		return units;
	}
	
	/**
	* Returns the type of units used as a lowercase String. 
	*/
	public String unitsLabel()
	{
		String s = new String("");
		if(units == NANOSECONDS)
			s += "nanoseconds";
		else if(units == MICROSECONDS)
			s += "microseconds";
		else if(units == MILLISECONDS)
			s += "milliseconds";
		else if(units == SECONDS)
			s += "seconds";
		else if(units == MINUTES)
			s += "minutes";
		else if(units == HOURS)
			s += "hours";
		else if(units == DAYS)
			s += "days";
		else //if(units == YEARS)
			s += "years";
		
		return s;
	}
	
	/**
	* Returns an array of Strings with all the different types of supported units.  
	*/
	public String[] namesOfUnits()
	{
		String[] unitName = new String[8];
		
		unitName[0] = new String("nanoseconds");
		unitName[1] = new String("microseconds");
		unitName[2] = new String("milliseconds");
		unitName[3] = new String("seconds");
		unitName[4] = new String("minutes");
		unitName[5] = new String("hours");
		unitName[6] = new String("days");
		unitName[7] = new String("years");
		return unitName;
	}
	
	/**
	* Causes the clock to reset its private pause-request fields.  
	*/
	public void resetPause()
	{
		timeOfLastPauseRequest = UNDEFINED;
		lastPauseAmount = UNDEFINED;
		totalOverPause = 0;
	}
	
	public void pauseUntilNextEvent(double nextEventTime)
	{
		long timeOfCurrentPauseRequest = System.currentTimeMillis();
		if(timeOfLastPauseRequest != UNDEFINED)
		{
			if(timeOfCurrentPauseRequest - timeOfLastPauseRequest > lastPauseAmount)
				totalOverPause += 
					(timeOfCurrentPauseRequest - timeOfLastPauseRequest) - lastPauseAmount;
		}
		timeOfLastPauseRequest = timeOfCurrentPauseRequest;
		
		long currentPauseAmount;
		
		if(isConstantDelay)
		{
			lastPauseAmount = (long) delay;
			currentPauseAmount = (long) delay;
			if(totalOverPause >= currentPauseAmount) 
			{
				currentPauseAmount = 0;
				totalOverPause -= (long) delay;
			}
			else
			{
				currentPauseAmount -= totalOverPause;
				totalOverPause = 0;
			}
			pauseFor(timeOfCurrentPauseRequest,currentPauseAmount);
			lastObservedEventTime = nextEventTime;
			return;
		}
		else //is scaled delay
		{
			lastPauseAmount = (long)(mcf*(nextEventTime-lastObservedEventTime));
			currentPauseAmount = lastPauseAmount;
			
			if(totalOverPause >= currentPauseAmount) 
			{
				currentPauseAmount = 0;
				totalOverPause -= lastPauseAmount;
			}
			else
			{
				currentPauseAmount -= totalOverPause;
				totalOverPause = 0;
			}
			
			pauseFor(timeOfCurrentPauseRequest,currentPauseAmount);
			lastObservedEventTime = nextEventTime;
			return;
		}
	}
	
	
	//The attribute scaleFactor is applied to time to yield time/scaleFactor. This quantity
	//is then converted to milliseconds, and the floor of the value is returned. 
	private void setMCF()
	{
		
		if(units == NANOSECONDS)
			mcf = (1.0/1000000.0);
		else if(units == MICROSECONDS)
			mcf = (1.0/1000.0);
		else if(units == MILLISECONDS)
			mcf = 1.0;
		else if(units == SECONDS)
			mcf = 1000.0;
		else if(units == MINUTES)
			mcf = 60000.0;
		else if(units == HOURS)
			mcf = 3600000.0;
		else if(units == DAYS)
			mcf = 86400000.0;
		else //(units == YEARS)
			mcf = 31536000000.0;
	}
	
	private void pauseFor(long startTime, long duration)
	{
		while((System.currentTimeMillis() - startTime) < duration);
	}
}
