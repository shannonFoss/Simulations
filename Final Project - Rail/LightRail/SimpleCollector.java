/*
 * SimpleCollector.java
 * Author: Todd Ebert
 * Date: 10/22/2008
 * 
 */

import java.awt.Color;

/**
 * A SimpleCollector is a Module that collects retired Tokens. A SimpleCollector 
 * does not store the Tokens, but only counts how many has arrived. This can be useful
 * for bookkeeping purposes. One should use a Collector Module if one wishes to keep the 
 * Tokens stored in a List for further processing. 
 */
public class SimpleCollector extends Module
{
	protected int tokensCollected;
	
	public SimpleCollector(String name)
	{
		super(name,Module.INFINITY,Module.INFINITY);
		super.addInPort(new InPort(name + "InPort"));
		tokensCollected = 0;
	}
	
	/**
 	 * Called just before the beginning of simulation.
	 */
	public void initialize()
	{
		super.initialize();
		tokensCollected = 0;
	}
	
	public Object info(int i)
	{
		if(i == 0) return name;
		String s = new String("");
		s += "Tokens collected: " + tokensCollected;
		return s;
	}
	
	public int infoAmount() {return 2;}
	
	public Color color() {return Color.MAGENTA;}
	
	/**
 	 * SimpleCollectors have exactly one InPort that is added during construction. 
 	 * Therefore this method has no effect.
	 */
	public void addInPort(InPort p)
    {
 
    }
    
    /**
 	 * SimpleCollectors do not have OutPorts. 
 	 * Therefore this method has no effect.
	 */
    public void addOutPort(OutPort p)
    {
    
    }
	
	
	public void enter(Token t, double time)
	{
		super.enter(t,time);
		tokensCollected++;
	}
	
	/**
 	 * Returns the number of Tokens that are currently in the Collector.  
	 */
	public int numTokens() {return tokensCollected;}
}
