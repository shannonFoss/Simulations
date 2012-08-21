//SimpleServer.java
//Author: Todd Ebert
//Date: 10-26-2008
//Updated: 11/02/2008

import java.awt.Color;

/**
 * A SimpleServer is a Module that, upon accepting a Token t for entry, proceeds to schedule for t an
 * exit based on the method getServiceTime(t). The token capacity of a SimpleServer indicates the number of
 * parallel servers that it represents. In other words, one SimpleServer could represent several parallel servers.
 * Thus it is assumed that a SimpleServer has a finite token and load capacity. Also, the InPort of a SimpleServer is 
 * assumed to be a FIFOPort. 
 */
public abstract class SimpleServer extends Module
{  
	protected FIFOPort inPort;
	protected OutPort outPort;
	protected Token[] token;  //token that is currently in service
	protected int n; //number of servers
	
	public SimpleServer(String name, int tokenCapacity, int loadCapacity)
	{
		super(name,tokenCapacity,loadCapacity);
		inPort = new FIFOPort(name + "InPort");
		super.addInPort(inPort);
		n = tokenCapacity;
		token = new Token[n];
		for(int i=0; i < n; i++)
			token[i] = null;
	}
	
	public void initialize()
	{
		super.initialize();
		for(int i=0; i < n; i++)
			token[i] = null;
	}
	
	public Object info(int i)
	{
		if(i == 0) return name;
		if(i == 1)
		{
			String s = new String("");
			if(tokenCount > 0)
			{
				int count = 0;
				s += "Status: serving ";
				for(int j=0; j < n; j++)
				{
					if(token[j] != null)
			   	 	{
			    		s += token[j].name();
			    		count++;
			    		if(count < tokenCount)
			    			s += ",";
			    	}
			    }
			    
			    return s;
			}	
			else return "Status: idle";
		}
		
		return "Queue length: " + inPort.qLength();
	}
	
	public int infoAmount() {return 3;}
	
	public Color color() 
	{
		if(tokenCount < tokenCapacity && loadCount < loadCapacity)
			return Color.GREEN;
		return Color.RED;
	}
	
	/**
 	 * When a Token enters a SimpleServer, an ExitPortEvent is immediately schedule for the 
 	 * Token using the getServiceTime() method. 
     */
	public void enter(Token t, double time)
	{
		super.enter(t,time);
		outPort.enter(t,time+getServiceTime(t));
		for(int i=0; i < n; i++)
		{
			if(token[i] == null) 
			{
				token[i] = t;
				break;
			}
		}
	}
	
	public void exit(Token t, double time)
	{
		super.exit(t,time);
		//Remove token from array of tokens currently in service.
		for(int i=0; i < n; i++)
		{
			if(token[i] != null && token[i].id() == t.id()) 
			{
				token[i] = null;
				break;
			}
		}
	}
	
	/**
 	 * SimpleServers only have one InPort which was added during construction. 
 	 * Thus, this method has no effect.
     */
	public void addInPort(InPort p)
    {
 
    }
    
 	/**
 	 * SimpleServers only have one OutPort. Thus, adding an OutPort using this method
 	 * will replace any existing OutPorts.
     */
    public void addOutPort(OutPort p)
    {
    	super.addOutPort(p);
    	outPort = p;
    	outPort.setAssociate(inPort);
    }
    
    protected abstract double getServiceTime(Token t);
}
