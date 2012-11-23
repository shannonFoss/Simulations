/*
 * RootModule.java
 * Author: Todd Ebert
 * Date: 10/22/2008
 * Updated: 11/01/2008
 * 
 */

/**
 * A RootModule is a Module that serves as the root or (top) module of a SystemModel. 
 * A RootModule has no Ports
 */
public class RootModule extends Module
{
	public RootModule(String name)
	{
		super(name,Module.INFINITY,Module.INFINITY);
	}
	
	/**
 	 *  A RootModule has no Ports.
 	 */
    public void addInPort(InPort p) {}
    
    /**
 	 *  A RootModule has no Ports.
 	 */
    public void addOutPort(OutPort p) {}
	
	/**
 	 *  A RootModule has no Parent.
 	 */
	public void setParent(Module m) {}
	
	/**
	* Returns a text summary of the next event that will take place in this RootModule.
	*/
	public Object info(int i)
	{
		if(i == 0) return name;
		String s = new String("");
		Event e = Module.fel.viewNextEvent();
		s += "Next event: " + e.toString();
		return s;
	}
	
	public int infoAmount() 
	{
		if(Module.fel.viewNextEvent() == null) return 1;
		return 2;
	}
}
