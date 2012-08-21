/*
 * Generator.java
 * Author: Todd Ebert
 * Date: 10/22/2008
 * Update: 11/15/08
 * 
 */

import java.awt.Color;

/**
 * A Generator is a Module that creates Tokens at time intervals that follow 
 * some probability distribution, and then sends them to other Modules in the system.
 */
public abstract class Generator extends Module
{
	protected int tokensGenerated;
	protected Token lastGenerated;
	protected OutPort outPort;
	protected double lastDepartureTime; 
	
	public Generator(String name)
	{
		super(name,0,0);
		tokensGenerated = 0;
		lastGenerated = null;
		lastDepartureTime = 0.0;
	}
	
	/**
 	 * Returns the next Tokens that is to be sent.
     */
	public abstract Token generateToken();
	
	/**
 	 * Returns the next time that a Token is to be sent. 
     */
	public abstract double nextDepartureTime(double lastDepartureTime);
	
	/**
 	 * Returns the only OutPort that is associated with this Generator.
     */
	public OutPort outPort() {return outPort;}
	
	public void initialize()
	{
		super.initialize();
		tokensGenerated = 0;
		lastGenerated = null;
	}
	
	public Object info(int i)
	{
		if(i == 0) return name;
		if(i == 1) return "Tokens generated: " + tokensGenerated;
		if(lastGenerated != null) 
			return "Last token generated: " + lastGenerated.name();
		else return "No tokens generated";
	}
	
	public int infoAmount() {return 3;}
	
	public Color color() {return Color.YELLOW;}
	
	/**
 	 *  Keeps track of how many tokens have exited.
 	 */
	public void exit(Token t, double time)
	{
		tokensGenerated++;
	}
	
	public int tokensGenerated() {return tokensGenerated;}
	
	/**
 	 * Generators do not have InPorts. Therefore this method has no effect.
     */
	public void addInPort(InPort p)
    {
 
    }
    
 	/**
 	 * Generators only have one OutPort. Thus, adding an OutPort using this method
 	 * will replace any existing OutPorts.
     */
    public void addOutPort(OutPort p)
    {
    	super.addOutPort(p);
    	outPort = p;
    }
    
    /**
    * Schedules the first Token departure from this Generator.
    *
    */
	protected void scheduleInitialEvents()
	{
		double time = nextDepartureTime(0.0);
		if(time <= Module.clock.closingTime())
		{
			Token t = generateToken();
			//REMOVE
			if (t instanceof Rider)
			{
				((Rider)t).startTime = time;
			}
			//*****
			TokenDepartureEvent tde = new TokenDepartureEvent(time,this,t);
			Module.fel().insert(tde);
		}
	}
	
	/**
    * Event: Token t has departed this Generator.
    * Immediate Effects: t will enter this Generator's OutPort.
    * Future Effects: another Token will be scheduled for departure if its departure time
    * does not exceed the closing time specified in the simulation.
    */
	protected class TokenDepartureEvent extends Event
	{
		Token token;
		
		public TokenDepartureEvent(double time, EventHost host, Token t)
		{
			super(time,host);
			token=t;
		}
		
		public void process()
		{
			Generator g = (Generator) host;
			OutPort p = g.outPort();
			lastGenerated = token;
			p.enterNow(token,time);
			lastDepartureTime = time;
			double nextTime = nextDepartureTime(time);
			if(nextTime <= Module.clock.closingTime())
			{
				Token t = generateToken();
				//REMOVE
				if (t instanceof Rider)
				{
					((Rider)t).startTime = time;
				}
				//*****
				TokenDepartureEvent tde = new TokenDepartureEvent(nextTime,host,t);
				Module.fel().insert(tde);
			}
			return;
		}
		
		public String toString()
		{
			Generator g = (Generator) host;
			String s = new String("");
			s += "Time: " + time + "\n";
			s += g.name() + " has generated token " + token.name() + ".\n\n";
			return s;
		}
	}
}
