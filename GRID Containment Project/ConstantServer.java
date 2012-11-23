//ConstantServer.java
//Author: Todd Ebert
//Date: 11-08-2008
//


/**
 * A ConstantServer is a SimpleServer whose service times are equal to some constant value. 
 */
public class ConstantServer extends SimpleServer
{  
	double serviceTime;
	
	public ConstantServer(String name, int tokenCapacity, int loadCapacity, double serviceTime)
	{
		super(name,tokenCapacity,loadCapacity);
		this.serviceTime = serviceTime;
	}
    
    protected double getServiceTime(Token t) {return serviceTime;}
}
