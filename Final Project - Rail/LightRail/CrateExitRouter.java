/*
 * CrateExitRouter.java
 * Author: Todd Ebert
 * Date: 11/19/2008
 * 
 */

import java.util.Random;

/**
 * A CrateExitRouter is used to route Crates from an Elevator to the Warehouse exit.  
 */
public class CrateExitRouter extends OutRouter
{
	
	public CrateExitRouter(String name)
	{
		super(name);
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
			if(toPortName.equals("CrateOutRelay"))
			{
				if(c.portUsed == Crate.GENERAL_PORT) 
					return w;
			}
			else if(toPortName.equals("LeftOutRelay") || toPortName.equals("RightOutRelay")) 
			{
				if(c.portUsed == Crate.SPECIAL_PORT) 
					return w;
			}
		}
		return null;
	}
}
