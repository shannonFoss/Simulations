/*
 * OutRelay.java
 * Author: Todd Ebert
 * Date: 10/24/2008
 * Updated: 11/02/2008
 * 
 */

/**
 * An OutRelay is an OutRouter which overrides the selectWire() method by having a single
 * designated relay Wire that Tokens are sent to.
 */

public class OutRelay extends OutRouter
{
	protected Wire relayWire;
	
	public OutRelay(String name)
	{
		super(name);
	}
	
	public OutRelay()
	{
		super("OutRelay");
	}
	
	/**
 	 * Returns the Wire to where this port relays Tokens. 
	 * 
	 */
	public Wire selectWire(Token t) {return relayWire;}
	
	/**
 	 * Tokens are sent to the relay Wire that connects to an InPort of an outer Module. 
	 * 
	 */
	public void sendToken(Token t, double time)
	{
		relayWire.enterNow(t,time);
	}
	
	/**
 	 * Relays only have one output Wire. Thus, invoking this method will redefine Wire.
	 */
	public void addOutput(Wire w) 
	{
		outputs.enter(w);
		relayWire = w;
	}
}
