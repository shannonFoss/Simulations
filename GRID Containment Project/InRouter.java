/*
 * InRouter.java
 * Author: Todd Ebert
 * Date: 10/23/2008
 * Updated: 11/01/2008
 * 
 */

/**
 * An InRouter is an InPort which overrides the sendToken() method by sending the Token
 * along a selected Wire that leads to a submodule of the given Module. 
 */
public abstract class InRouter extends InPort
{
	public InRouter(String name)
	{
		super(name);
	}
	
	/**
 	 * Returns the Wire to where this port relays Tokens. 
	 */
	public abstract Wire selectWire(Token t);
	
	/**
 	 * Sends Token t along a selected Wire that leads to a submodule. The Wire is determined
 	 * by the selectWire() method. 
	 */
	public void sendToken(Token t, double time)
	{
		Wire w = selectWire(t);
		w.enterNow(t,time);
	}
}
