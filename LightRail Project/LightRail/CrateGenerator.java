/*
 * CrateGenerator.java
 * Author: Todd Ebert
 * Date: 10/22/2008
 * Update: 11/16/08
 * 
 */

import java.util.Random;

/**
 * A CrateGenerator is a Generator that Generates Crates of some type. 
 */
public class CrateGenerator extends Generator
{
	protected Random gen;
	protected int id;
	protected CrateGeneratorRouter router;
	protected String type;
	
	public CrateGenerator(String type)
	{
		super("Crate" + type + "Generator");
		this.type = type;
		gen = new Random();
		id = 0;
		router = new CrateGeneratorRouter();
		addOutPort(router);
	}
	
	/**
 	 * Returns the next Tokens that is to be sent.
     */
	public Token generateToken()
	{
		return new Crate(type,id++);
	}
	
	/**
 	 * Returns the next time that a Token is to be sent. 
     */
	public double nextDepartureTime(double lastDepartureTime)
	{
		double x = gen.nextDouble();
		if(x <= 0.25) return lastDepartureTime+1.0;
		if(x <= 0.65) return lastDepartureTime+2.0;
		if(x <= 0.90) return lastDepartureTime+3.0;
		return lastDepartureTime+4.0;
	}
	
	
	public void initialize()
	{
		id = 0;
		super.initialize();
	}
	
	public void setRoutingPolicy(int policy) {router.setRoutingPolicy(policy);}
}
