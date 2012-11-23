/*
 * MarkovCustomerGenerator.java
 * Author: Todd Ebert
 * Date: 11/04/2008
 * Update: 
 * 
 */
 
import java.util.Random;
import java.lang.Math;

/**
 * A MarkovCustomerGenerator is a CustomerGenerator whose interarrival distribution is exponential.
 */
public class MarkovCustomerGenerator extends CustomerGenerator
{
	protected double lambda;
	protected Random gen;
	
	public MarkovCustomerGenerator(String name, double lambda)
	{
		super(name);
		this.lambda = lambda;
		gen = new Random();
	}
	
	/**
 	 * Returns the interarrival rate lambda. 
     */
	public double lambda() {return lambda;}
	
	/**
 	 * Returns the next time that a customer is sent. The difference between this time and the 
 	 * lastDepartureTime is a random variable that is distributed exponentially with interarrival rate lambda(). 
     */
	public double nextDepartureTime(double lastDepartureTime)
	{
		return lastDepartureTime + (-1.0*Math.log(gen.nextDouble()))/lambda;
	}
	
}
