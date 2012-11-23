/*
 * Wire.java
 * Author: Todd Ebert
 * Date: 10/23/2008
 * Updated: 11/02/2008
 * 
 */

/**
 * An Wire is an Object that connects two Ports, a "from" Port and a "to" Port. The "to"
 * Port is always an InPort.
 */
public class Wire implements EventHost
{
	protected int id;
	protected Port from; //the sending port
	protected Port to; //the receiving port
	
	public Wire()
	{
		id = Module.assignID();
	}
	
	/**
	* Sets the Port that this Wire connects from. 
	*/
	public void connectFrom(Port from) {this.from = from; from.addOutput(this);}
	
	/**
	* Sets the Port that this Wire connects to. 
	*/
	public void connectTo(Port to) {this.to = to; to.addInput(this);}
	
	/**
	* Returns the id of this Wire. 
	*/
	public int id() {return id;}
	
	/**
	* Returns the Port that this Wire connects to. 
	*/
	public Port to() {return to;}
	
	/**
	* Returns the Port that this Wire connects from. 
	*/
	public Port from() {return from;}
	
	/**
	* Creates an EnterWireEvent for Token t that is to occur at the specified time. 
	*/
	public void enter(Token t, double time)
	{
		EnterWireEvent ewe = new EnterWireEvent(time,this,t);
		Module.fel().insert(ewe);
	}
	
	/**
	* Creates an EnterWireEvent for Token t. The Event will be the next Event to occur.
	*/
	public void enterNow(Token t, double time)
	{
		EnterWireEvent ewe = new EnterWireEvent(time,this,t);
		ewe.setIsNext();
		Module.fel().insert(ewe);
	}
	
	/**
	* Process the EnterWireEvent that occurs in this Wire. 
	*/
	public void process(Event e)
	{
		e.process();
	}
	
	/**
    * Event: Token t has entered this PhysicalWire.
    * Immediate Effects: If the Wire is connected to an InPort, then t will arrive at that Port. 
    * If the Wire is connected to an OutPort, then t will enter that Port.
    */
	protected class EnterWireEvent extends Event
	{
		protected Token token;
		
		public EnterWireEvent(double time, EventHost host, Token t)
		{
			super(time,host);
			token=t;
		}
		
		public void process()
		{
			Wire w = (Wire) host;
			Port p = w.to();
			if(p.isInPort()) 
			{
				InPort ip = (InPort) p;
				ip.arriveNow(token,time);
			}
			else p.enterNow(token,time);
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
