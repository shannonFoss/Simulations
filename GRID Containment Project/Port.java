/*
 * Port.java
 * Author: Todd Ebert
 * Date: 10/23/2008
 * Updated: 11/09/2008
 * 
 */

/**
 * A Port is an Object that is associated with a given Module. If it is an InPort,then Tokens
 * enter it before entering the Module. If it is an OutPort, then Tokens enter it upon exiting
 * the Module.  
 * 
 */
public abstract class Port implements EventHost
{
	public static final boolean IN_PORT = true;
	public static final boolean OUT_PORT = false;
	
	protected String name;
	protected int id;
	protected boolean isIn;
	protected boolean isOpen;
	protected Module module; //the Module associated with this Port
	
	protected Lst inputs; //List of Wires that input to this port
	protected Lst outputs; //List of Wires that output from this port
	
	public Port(String name, boolean isIn)
	{
		this.name = name;
		id = Module.assignID();
		this.isIn = isIn;
		isOpen = true;
		inputs = new Lst();
		outputs = new Lst();
	}
	
	/**
 	 *  Returns the name of this Port.
 	 */
	public String name()
	{
		return name;
	}
	
	/**
	* This method is useful to Router and Relay Ports 
	* that need to send a Token to a submodule of the given Module.
	* For non-routing ports, this method has no effect.
	*/
	public void sendToken(Token t, double time) 
	{
		Wire w;
		if((w=selectWire(t)) != null) w.enterNow(t,time);
	}
	
	/**
 	 * Returns the Wire to where this port relays Tokens. 
 	 * The default is that no Wire is selected, and hence the Token
 	 * is not sent along a Wire. 
	 */
	public Wire selectWire(Token t) {return null;}
	
	/**
 	 * Returns true if the Wire that t will be routed to is connected to a Port that 
 	 * is open. Returns (trivially) true
 	 * in the case that this Port does not route Tokens via Wires. 
	 */
	public boolean canRoute(Token t)
	{
		Wire w = selectWire(t);
		if(w == null) return true;
		return w.to().isOpen();
	}
	
	/**
	* Sets the Module for which this Port is to be associated. 
	*/
	public void setModule(Module m)
	{
		module = m;
	}
	
	/**
	 * Called just before the begin of simulation.
	 */
	public void initialize()
	{
		isOpen=true;
	}
	
	/**
	 * Adds w to the list of Wires that feed into this Port.
	 */
	public void addInput(Wire w) {inputs.enter(w);}
	
	/**
	 * Adds w to the list of Wires that start from this Port.
	 */
	public void addOutput(Wire w) {outputs.enter(w);}
	
	/**
	 * Returns the module associated with this Port.
	 */
	public Module module()
	{
		return module;
	}
	
	/**
	 * Returns true if this is an InPort.
	 */
	public boolean isInPort() {return isIn;}
	
	/**
	 * Returns the id of this Port.
	 */
	public int id() {return id;}
	
	/**
	 * Returns true if this Port is open.
	 */
	public boolean isOpen() {return isOpen;}
	
	/**
	 * Opens this Port.
	 */
	public void open() {isOpen = true;}
	
	/**
	 * Closes this Port.
	 */
	public void close() {isOpen = false;}
	
	/**
	 * Causes the creation of an EnterPortEvent.
	 */
	public void enter(Token t, double time)
	{
		EnterPortEvent epe = new EnterPortEvent(time,this,t);
		Module.fel().insert(epe);
	}
	
	/**
	 * Causes the creation of an EnterPortEvent that will be the next processed Event.
	 */
	public void enterNow(Token t, double time)
	{
		EnterPortEvent epe = new EnterPortEvent(time,this,t);
		epe.setIsNext();
		Module.fel().insert(epe);
	}
	
	public void process(Event e)
	{
		e.process();
	}
	
	/**
    * Event: Token t has entered this Port.
    * Immediate Effects: If this Port is an InRouter or OutRouter, then t will enter one of the Wires that 
    * this Port feeds to. If this Port is an InPort, then t will be registered by the InPort, as well as the associated
    * Module.
    */
	protected class EnterPortEvent extends Event
	{
		protected Token token;
		
		public EnterPortEvent(double time, EventHost host, Token t)
		{
			super(time,host);
			token=t;
		}
		
		public void process()
		{
			Port p = (Port) host;
			if(p.isInPort())
			{
				InPort ip = (InPort) p;
				ip.registerToken(token,time);
				ip.sendToken(token,time);
			}
			else
			{
				OutPort op = (OutPort) p;
				op.registerDeparture(token,time);
				op.sendToken(token,time);
			}
			return;
		}
		
		public String toString()
		{
			Port p = (Port) host;
			String s = new String("");
			s += "Time: " + time + "\n";
			if(p.isInPort())
			{
				s += "Token " + token.name() + " has entered InPort " + p.name();
				s += " of module " + p.module().name() + "\n\n";
			}
			else 
			{
				s += "Token " + token.name() + " has entered OutPort " + p.name();
				s += " of module " + p.module().name() + "\n\n";
			}
				
			return s;
		}
	}
}
