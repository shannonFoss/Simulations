/*
 * Collector.java
 * Author: Todd Ebert
 * Date: 10/22/2008
 * 
 */

/**
 * A Collector is a SimpleCollector that includes methods for accessing
 * the Tokens that have entered the Collector. 
 */
public class Collector extends SimpleCollector
{
	protected Lst tokens;
	
	public Collector(String name)
	{
		super(name);
		tokens = new Lst();
	}
	
	
	public void initialize()
	{
		super.initialize();
		tokens.clear();
	}
	
	public void enter(Token t,double time)
	{
		super.enter(t,time);
		tokens.enter(t);
	}
	
	/**
 	 * Removes all the Tokens from the Collector. 
	 */
	public void clear() {tokens.clear();}
	
	/**
 	 * Returns all the Tokens that are currently in the Collector.  
	 */
	public Lst getTokens() {return tokens;}
}
