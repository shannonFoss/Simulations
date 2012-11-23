/*
 * Crate.java
 * Author: Todd Ebert
 * Date: 11/04/2008
 * 
 */

/**
 * A Crate is an Object that implements the Token interface and visits one or more Modules
 * for the purpose of seeking service. 
 */
public class Crate implements Token
{
	public static final double A_UNLOAD_TIME = 1.0;
	public static final double B_UNLOAD_TIME = 0.5;
	public static final double C_UNLOAD_TIME = 0.5;
	
	public static final int A_WEIGHT = 2000;
	public static final int B_WEIGHT = 1000;
	public static final int C_WEIGHT = 500;
	
	public static final int SPECIAL_PORT = 1;
	public static final int GENERAL_PORT = 2;
	
	protected int id;
	protected String name;
	protected int load;
	protected String type;
	public int portUsed;
	
	public Crate(String type,int id)
	{
		this.type = type;
		this.id = id;
		name = new String("");
		name += this.type + this.id;
		if(type.equals("A")) load = A_WEIGHT;
		else if(type.equals("B")) load = B_WEIGHT;
		else load = C_WEIGHT;
	}
	
	public String type() {return type;}
	
	
	/**
 	 * Returns the name of the Token.
 	 */
	public String name() {return name;}
	
	/**
 	 * Returns the id of the Token.
 	 */
	public int id() {return id;}
	
	
	/**
 	 * Returns the load of this Token. For example, if the Token represents cargo that is to enter
 	 * an Elevator Module, then the load() will represent the weight of the cargo. 
 	 */
	public int load() {return load;}
}
