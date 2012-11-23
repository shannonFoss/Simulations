/*
 * Customer.java
 * Author: Todd Ebert
 * Date: 11/04/2008
 * 
 */

/**
 * A Customer is an Object that implements the Token interface and visits one or more Modules
 * for the purpose of seeking service. 
 */
public class Customer implements Token
{
	protected int id;
	protected String name;
	protected int load;
	
	public Customer(int id)
	{
		this.id = id;
		name = new String("");
		name += id;
		load = 1;
	}
	
	
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
