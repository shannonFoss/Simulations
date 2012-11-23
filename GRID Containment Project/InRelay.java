/*
 * InRelay.java
 * Author: Todd Ebert
 * Date: 10/24/2008
 * Updated: 11/01/2008
 * 
 */

/**
 * An InRelay is an InRouter which overrides the selectWire() method by having a single
 * designated relay Wire that Tokens are sent to.
 */
public class InRelay extends InRouter
{
	protected Wire relayWire;
	
	public InRelay(String name)
	{
		super(name);
	}
	
	/**
 	 * Returns the Wire to where this port relays Tokens. 
	 * 
	 */
	public Wire selectWire(Token t) {return relayWire;}
	
	/**
 	 * Tokens are sent to the relay Wire that connects to a submodule of this Module. 
	 * 
	 */
	public void sendToken(Token t, double time)
	{
		relayWire.enterNow(t,time);
	}
	
	/**
 	 * Relays only have one output Wire. Thus, invoking this method will redefine this Wire.
	 */
	public void addOutput(Wire w) 
	{
		outputs.enter(w);
		relayWire = w;
	}
}
