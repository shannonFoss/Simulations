/*
 * OutPort.java
 * Author: Todd Ebert
 * Date: 10/23/2008
 * Updated: 11/01/2008
 * 
 */

/**
 * An OutPort is a Port that sends Tokens away from a Module to another Module. 
 */

public abstract class OutPort extends Port
{
	InPort associate; //When a token enters this OutPort, the portStats of the associate InPort are updated.
	
	public OutPort(String name)
	{
		super(name,false);
		associate = null;
	}
	
	/**
	* Sets the associate InPort of this OutPort. When a token enters this OutPort, the portStats of the associate 
	* InPort are updated.
	*/
	public void setAssociate(InPort p) {associate = p;}
	
	/**
	* Always returns false.
	*/
	public boolean isInPort() {return false;}
	
	/**
	* OutPorts are always open. Hence, always returns true.
	*/
	public boolean isOpen() {return true;}
	
	
	public void registerDeparture(Token t, double time)
	{
		if(associate != null)
			associate.recordDepartingToken(t,time);
		//DEBUG
		//if(module == null) System.out.println("port " + name + " has no associated module.");
		module.exit(t,time);
	}
}
