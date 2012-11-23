/*
 * SystemModel.java
 * Author: Todd Ebert
 * Date: 11/12/2008
 * 
 */
 
import java.lang.Exception;
import java.lang.reflect.*;

/**
 * A SystemModel is an Object that represents the model of a system. The SystemModel consists
 * of a toplevel Module Object that serves as the root of the Module hierarchy that forms the
 * system structure.
 */
public abstract class SystemModel
{
	protected RootModule top;
	protected ModelPanel modelPanel;
	protected String systemName;
	protected String[] modelParameters;
	
	public SystemModel()
	{
		setModelParameters();
		makeSystemName();
		top = new RootModule(systemName);
		buildModel();
		if(modelPanel == null) 
		{
			makeModelPanel(1,1);
			assignModule(top,0,0);
		}
	}
	
	
	/**
 	 * Returns the root module of the SystemModel.
     */
	public RootModule top() {return top;}
	
	public ModelPanel modelPanel() {return modelPanel;}
	
	public String[] modelParameters() {return modelParameters;}
	
	public void setParameter(String paramName, String paramValue) {}
	
	public static SystemModel getSystemModel(String modelName)
	{
		Class<?> modelClass=null;
		Constructor<?>[] consArray = null;
		Constructor<?> constructor=null;
		
		try
		{
			modelClass = Class.forName(modelName);
		}
		catch(Exception e)
		{
			return null;	
		}
		if(modelClass == null) return null;
		else
		{	
			try
			{
				consArray = modelClass.getConstructors();
				constructor = consArray[0];
				return (SystemModel) constructor.newInstance((Object[])null);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return null;
			}
		}
	}
	
	
	/**
 	 * Returns the root module of the SystemModel.
     */
	protected abstract void buildModel();
	
	/**
 	 * Makes the ModelPanel that will be used to view the SystemModel.
     */
	protected void makeModelPanel(int rows, int columns)
	{
		modelPanel = new ModelPanel(systemName,rows,columns);
	}
	
	protected void makeSystemName()
	{ 
		String s = this.getClass().toString();
		int index = s.lastIndexOf(' ');
		systemName = s.substring(index+1,s.length());
	}
	
	/**
 	 * Assigns system submodule m the (x,y) location of the ModelPanel grid.
     */
	protected void assignModule(Module m, int x, int y)
	{
		modelPanel.assignModule(m,x,y);
	}
	
	/**
 	 *  Adds m to the list of submodules of the RootModule of this SystemModel.
 	 */
	protected void add(Module m) {top.addSubmodule(m);}
	
	/**
	* Connects the first OutPort of m1 to the first InPort of m2. Has no effect if either Module
	* is missing the needed port. 
	*/
	protected void connect(Module m1, Module m2) 
	{
		Module.connect(m1, m2);
	}
	
	/**
	* Connects the first OutPort of m1 to the first InPort of m2, using a PhysicalWire
	* with the given delay value. Has no effect if either Module
	* is missing the needed port. 
	*/
	protected void connect(Module m1, Module m2, double delay) 
	{
		Module.connect(m1, m2, delay);
	}
	
	/**
	* Connects the OutPort of m1 having name outName with to the InPort of m2
	* having name inName. Has no effect if either Module
	* is missing the needed port. 
	*/
	protected void connect(Module m1, Module m2, String fromName, String toName) 
	{
		Module.connect(m1, m2, fromName, toName);
	}
	
	/**
	* Connects the OutPort of m1 having name outName with to the InPort of m2
	* having name inName, and using a PhysicalWire with the stated delay. 
	* Has no effect if either Module
	* is missing the needed port. 
	*/
	protected void connect(Module m1,Module m2,String fromName, String toName, double delay) 
	{
		Module.connect(m1, m2, fromName, toName, delay);
	}
	
	/**
	* Connects the first InPort of m1 to the first InPort of m2. Has no effect if either Module
	* is missing the needed port. 
	*/
	protected void connectInToIn(Module m1, Module m2) 
	{
		Module.connectInToIn(m1, m2);
	}
	
	/**
	* Connects the first InPort of m1 to the first InPort of m2, using a PhysicalWire
	* with the given delay value. Has no effect if either Module
	* is missing the needed port. 
	*/
	protected void connectInToIn(Module m1, Module m2, double delay) 
	{
		Module.connectInToIn(m1, m2, delay);
	}
	
	/**
	* Connects the InPort of m1 having name fromName with to the InPort of m2
	* having name toName. Has no effect if either Module
	* is missing the needed port. 
	*/
	protected void connectInToIn(Module m1, Module m2, String fromName, String toName) 
	{
		Module.connectInToIn(m1, m2, fromName, toName);
	}
	
	/**
	* Connects the InPort of m1 having name fromName with to the InPort of m2
	* having name toName with a PhysicalWire having the given delay. 
	* Has no effect if either Module
	* is missing the needed port. 
	*/
	protected void connectInToIn(Module m1,Module m2,String fromName, String toName, 
					double delay) 
	{
		Module.connectInToIn(m1, m2, fromName, toName, delay);
	}
	
	/**
	* Connects the first OutPort of m1 to the first OutPort of m2. Has no effect if either Module
	* is missing the needed port. 
	*/
	protected void connectOutToOut(Module m1, Module m2) 
	{
		Module.connectOutToOut(m1, m2);
	}
	
	/**
	* Connects the first OutPort of m1 to the first OutPort of m2, using a PhysicalWire
	* with the given delay value. Has no effect if either Module
	* is missing the needed port. 
	*/
	protected void connectOutToOut(Module m1, Module m2, double delay) 
	{
		Module.connectOutToOut(m1, m2, delay);
	}
	
	/**
	* Connects the OutPort of m1 having name fromName with to the OutPort of m2
	* having name toName. Has no effect if either Module
	* is missing the needed port. 
	*/
	protected void connectOutToOut(Module m1, Module m2, String fromName, String toName) 
	{
		Module.connectOutToOut(m1, m2, fromName, toName);
	}
	
	/**
	* Connects the OutPort of m1 having name fromName with to the OutPort of m2
	* having name toName with a PhysicalWire having the given delay. 
	* Has no effect if either Module
	* is missing the needed port. 
	*/
	protected void connectOutToOut(Module m1,Module m2,String fromName, String toName, 
					double delay) 
	{
		Module.connectOutToOut(m1, m2, fromName, toName, delay);
	}
	
	protected void setModelParameters() {modelParameters = null;}
}
