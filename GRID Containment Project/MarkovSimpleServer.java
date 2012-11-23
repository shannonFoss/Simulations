//MarkovSimpleServer.java
//Author: Todd Ebert
//Date: 11-04-2008

import java.util.Random;
import java.lang.Math;

/**
 * A MarkoveSimpleServer is a SimpleServer whose service-time distribution is exponential with service rate mu.
 */
public class MarkovSimpleServer extends SimpleServer
{  
	protected double mu;
	protected Random gen;
	
	public MarkovSimpleServer(String name, int tokenCapacity, int loadCapacity, double mu)
	{
		super(name,tokenCapacity,loadCapacity);
		this.mu = mu;
		gen = new Random();
	}
	
	/**
	* Returns the service rate that is used for each of the parallel servers.
	*/
	public double mu() {return mu;}
	
    
    protected double getServiceTime(Token t)
    {
    	return (-1.0*Math.log(gen.nextDouble()))/mu;
    }
}
