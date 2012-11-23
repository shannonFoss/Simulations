/*
 * CrateRouter.java
 * Author: Todd Ebert
 * Date: 11/08/2008
 * 
 */

import java.util.Random;

/**
 * A CrateRouter is used in WarehouseModel1, and routes a Crate to an open Elevator. 
 * It randomly chooses the Elevator if both are open.  
 */
public class CrateRouter extends FIFORouter
{
	protected Random gen;
	
	public CrateRouter()
	{
		super("CrateRouter");
		gen = new Random();
	}
	
	public Wire selectWire(Token t)
	{
		Wire w1 = (Wire) outputs.front();
		Wire w2 = (Wire) outputs.back();
		
		Port p1 = w1.to();
		Port p2 = w2.to();
		
		if(p1.isOpen() && !p2.isOpen()) return w1;
		if(p2.isOpen() && !p1.isOpen()) return w2;
		if(gen.nextDouble() <= 0.5) return w1;
		return w2;
	}
}
