/*
 * Token.java
 * Author: Todd Ebert
 * Date: 10/26/2008
 * Updated: 11/12/2008
 * 
 */

/**
 * A Token is an Object that moves from Module to Module via Module InPorts, OutPorts, and
 * Wires that connect Ports.
 */
public interface Token
{
	/**
 	 * Returns the name of the Token.
 	 */
	String name();
	
	/**
 	 * Returns the id of the Token.
 	 */
	int id();
	
	/**
 	 * Returns the load of this Token. For example, if the Token represents cargo that is to enter
 	 * an Elevator Module, then the load() will represent the weight of the cargo. 
 	 */
	int load();
}
