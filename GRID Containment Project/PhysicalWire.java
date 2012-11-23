/*
 * PhysicalWire.java
 * Author: Todd Ebert
 * Date: 10/26/2008
 * Updated: 11/02/2008
 * 
 */

/**
 * A PhysicalWire is a Wire that has a constant timed delay for how long it takes an entering 
 * Token to exit the Wire. 
 */

public class PhysicalWire extends Wire
{
	protected double delay;
	
	public PhysicalWire(double delay)
	{
		super();
		this.delay = delay;
	}
	
	/**
 	 * Returns the delay of this Wire.
	 */
	public double delay() {return delay;}
	
	public void enter(Token t, double time)
	{
		EnterPhysicalWireEvent epwe = new EnterPhysicalWireEvent(time,this,t);
		Module.fel().insert(epwe);
	}
	
	public void enterNow(Token t, double time)
	{
		EnterPhysicalWireEvent epwe = new EnterPhysicalWireEvent(time,this,t);
		epwe.setIsNext();
		Module.fel().insert(epwe);
	}
	
	/**
    * Event: Token t has entered this PhysicalWire.
    * Future Effects: If the Wire is connected to an InPort, then t will arrive at that Port after being delayed 
    * by this Wire. If the Wire is connected to an OutPort, then t will enter that Port after being delayed 
    * by this Wire.
    */
	protected class EnterPhysicalWireEvent extends Event
	{
		protected Token token;
		
		public EnterPhysicalWireEvent(double time, EventHost host, Token t)
		{
			super(time,host);
			token=t;
		}
		
		public void process()
		{
			PhysicalWire w = (PhysicalWire) host;
			Port p = w.to();
			if(p.isInPort()) 
			{
				InPort ip = (InPort) p;
				ip.arrive(token,time+w.delay());
			}
			else p.enter(token,time+w.delay());
		}
		
		public String toString()
		{
			Wire w = (Wire) host;
			String s = new String("");
			s += "Time: " + time + "\n";
			s += "Token " + token.name() + " has entered wire " + w.id() + " connecting to module ";
			s +=  w.to().module().name() + "\n\n";
			
			return s;
		}
	}
}
