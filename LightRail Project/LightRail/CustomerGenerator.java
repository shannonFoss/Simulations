/*
 * CustomerGenerator.java
 * Author: Todd Ebert
 * Date: 11/04/2008
 * Update: 
 * 
 */

/**
 * A CustomerGenerator is a Generator that Generates Customers.
 */
public abstract class CustomerGenerator extends Generator
{
	protected int id;
	
	public CustomerGenerator(String name)
	{
		super(name);
		id=0;
	}
	
	public void initialize()
	{
		id = 0;
		super.initialize();
	}
	

	public Token generateToken()
	{
		return new Customer(id++);
	}
}
