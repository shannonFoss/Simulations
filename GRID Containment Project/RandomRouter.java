/*
 * RandomRouter.java
 * Author: Todd Ebert
 * Date: 11/08/2008
 *
 * 
 */

import java.util.Random;

/**
 * An RandomRouter is an OutRouter that selects the Wire to send a Token to by randomly 
 * choosing a Wire according to a probability distribution.  
 */

public class RandomRouter extends OutRouter
{
	protected double[] q; //cumulative probability
	protected double[] p;
	protected Random gen;
	
	/**
	* The length of the p array is assumed to be equal to the number of output Wires that 
	* emanate from this OutPort.
	*/
	public RandomRouter(String name, double[] p)
	{
		super(name);
		this.p = p;
		gen = new Random();
		q = new double[p.length];
		q[0] = p[0];
		for(int i=1; i < p.length-1;i++)
			q[i] = q[i-1] + p[i];
		q[p.length-1] = 1.0;
	}
	
	/**
	* The length of the p array is assumed to be equal to the number of output Wires that 
	* emanate from this OutPort.
	*/
	public RandomRouter(double[] p)
	{
		super("RandomRouter");
		this.p = p;
		gen = new Random();
		q = new double[p.length];
		q[0] = p[0];
		for(int i=1; i < p.length-1;i++)
			q[i] = q[i-1] + p[i];
		q[p.length-1] = 1.0;
	}
	
	/**
 	 * Returns a randomly selected Wire (according to this Router's probability distribution)
 	 * to where this port relays Token t. 
	 */
	public Wire selectWire(Token t)
	{
		double x = gen.nextDouble();
		int i=0;
		for(outputs.reset();outputs.hasNext();i++,outputs.advance())
			if(x <= q[i])
				return (Wire) outputs.access();
		return null;
	}
	
	/**
 	 * Sends Token t along a selected Wire that interconnects this 
 	 * Module with an outer Module. The Wire is determined
 	 * by the selectWire() method. 
	 */
	public void sendToken(Token t, double time)
	{
		Wire w = selectWire(t);
		w.enterNow(t,time);
	}
}
