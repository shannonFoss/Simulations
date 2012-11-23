/*
 * CrateGeneratorRouter.java
 * Author: Todd Ebert
 * Date: 11/08/2008
 * 
 */

import java.util.Random;

/**
 * A CrateGeneratorRouter is used to route Crates from a CrateGenerator.   
 */
public class CrateGeneratorRouter extends OutRouter
{
	protected Random gen;
	protected int routingPolicy;
	
	public static final int RANDOM_ROUTING = 0;
	public static final int SPECIALIZED_ROUTING = 1;
	
	public CrateGeneratorRouter()
	{
		super("CrateGeneratorRouter");
		gen = new Random();
		routingPolicy = RANDOM_ROUTING;
	}
	
	public Wire selectWire(Token t)
	{
		Wire w;
		String toPortName;
		Crate c = (Crate) t;
		
		for(outputs.reset();outputs.hasNext();outputs.advance())
		{
			w = (Wire) outputs.access();
			toPortName = w.to().name();
			if(toPortName.equals("CrateRouter"))
			{
				if(routingPolicy == RANDOM_ROUTING) 
				{
					c.portUsed = Crate.GENERAL_PORT;
					return w;
				}
			}
			else if(routingPolicy == SPECIALIZED_ROUTING) 
			{
				c.portUsed = Crate.SPECIAL_PORT;
				return w;
			}
		}
		return null;
	}
	
	public void setRoutingPolicy(int policy) {routingPolicy = policy;}
}
