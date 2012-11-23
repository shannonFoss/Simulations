/*
 * Module.java
 * Author: Todd Ebert
 * Date: 10/22/2008
 * Updated: 11/01/2008
 * 
 */

import java.awt.Color;

/**
 * A Module is a general term used for an Object that, among other things, 
 * processes and/or holds Tokens for some given duration, before exiting them via one of
 * its OutPort.  
 */
public class Module implements EventHost
{
	/**
 	 *  Used when one desires to make either the Token capacity or load capacity infinite.
 	 */
	public final static int INFINITY = 2147483647; //2^31-1
	public final static int UNDEFINED = -1;
	
	/**
 	 *  The FutureEventList that holds events that pertain to the discrete-event simulation
 	 *  of a Module and its submodules. 
 	 */
	public static final FutureEventList fel = new FutureEventList();
	
	public static final FutureEventList fel() {return Module.fel;};
	
	/**
 	 *  The keeps track of the current time as well as other time parameters, such as the 
 	 * simulation "closing time" (deadline to schedule an Event).
 	 */
	public static final SystemClock clock = new SystemClock();
	
	private static int idCount=0;
	
	protected String name;
	protected int id;
	protected Lst inPorts;
	protected Lst outPorts;
	protected Module parent; //parent module that this module is embedded in
	protected Lst submodules; //list of all top-level submodules of this system
	protected int tokenCapacity;
	protected int loadCapacity;
	protected int tokenCount;
	protected int loadCount;
	
	/**
 	 *  Every Module is to be provided a name, token capacity, and load capacity. 
 	 * The token capacity indicates the maximum number of Tokens that can be in the Module
 	 * at any given time. Similarly, the load capacity represents the maximum Token load
 	 * (each Token has an associated load() value that is assigned to it) at any given time.
 	 * Should one desire to make either of the two infinite, then one should use the value 
 	 * Module.INFINITY for this purpose. 
 	 */
	public Module(String name, int tokenCapacity, int loadCapacity)
	{
		this.name = name;
		id = Module.idCount++;
		
		submodules = new Lst();
		inPorts = new Lst();
		outPorts = new Lst();
		this.tokenCapacity = tokenCapacity;
		this.loadCapacity = loadCapacity;
		tokenCount = 0;
		loadCount = 0;
	}
	
	/**
 	 *  Returns the name of this Module.
 	 */
	public String name()
	{
		return name;
	}
	
	/**
 	 *  Returns the id of this Module.
 	 */
	public int id()
	{
		return id;
	}
	
	/**
 	 *  Returns a List of all InPorts of this module.
 	 */
	public Lst inPorts() {return inPorts;}
	
	/**
 	 *  Returns a List of all OutPorts of this module.
 	 */
	public Lst outPorts() {return outPorts;}
	
	public int tokenCapacity() {return tokenCapacity;}
	public int loadCapacity() {return loadCapacity;}
	
	/**
 	 *  Returns the number of tokens currently in the module.
 	 */
	public int tokenCount() {return tokenCount;}
	
	/**
 	 *  Returns the current Token load.
 	 */
	public int loadCount() {return loadCount;}
	

	
	/**
 	 *  Called before the beginning of a simulation. Initializes all submodules, and 
 	 * affiliated Ports. Schedules any initial root Events that should be hosted by this
 	 * Module. 
 	 */
	public void initialize()
	{
		tokenCount = 0;
		loadCount = 0;
		Port p;
		Module m;
		
		//initialize all in ports
		for(inPorts.reset();inPorts.hasNext();inPorts.advance())
		{
			p = (Port) inPorts.access();
			p.initialize();
		}
		
        //initialize all out ports
		for(outPorts.reset();outPorts.hasNext();outPorts.advance())
		{
			p = (Port) outPorts.access();
			p.initialize();
		}
		
		//initialize all submodules
		for(submodules.reset(); submodules.hasNext(); submodules.advance())
		{
			m = (Module) submodules.access();
			m.initialize();
		}
		
		scheduleInitialEvents();
	}
	
	/**
 	 *  Called after the end of a simulation. Finalizes all submodules, and 
 	 * affiliated Ports. 
 	 */
	public void finalize(double finalTime)
	{
		InPort p;
		Module m;
		
		//finalize all in ports
		for(inPorts.reset();inPorts.hasNext();inPorts.advance())
		{
			p = (InPort) inPorts.access();
			p.finalize(finalTime);
		}
		
		//finalize all submodules
		for(submodules.reset(); submodules.hasNext(); submodules.advance())
		{
			m = (Module) submodules.access();
			m.finalize(finalTime);
		}
	}
	
	/**
 	 *  Sets the time of every PortStats object, so that it may update its statistics
 	 *  regarding port usage. 
 	 */
	public void setTime(double time)
	{
		InPort p;
		Module m;
		
		for(inPorts.reset();inPorts.hasNext();inPorts.advance())
		{
			p = (InPort) inPorts.access();
			p.setTime(time);
		}
		
		for(submodules.reset(); submodules.hasNext(); submodules.advance())
		{
			m = (Module) submodules.access();
			m.setTime(time);
		}
	}
	
	
	//Schedule any initial events that are known to occur in this Module.
	protected void scheduleInitialEvents()
	{
		
	}
	
	/**
 	 *  Adds p to the list of InPorts associated with this Module.
 	 */
    public void addInPort(InPort p)
    {
    	p.setModule(this);
    	inPorts.enter(p);
    }
    
    /**
 	 *  Adds p to the list of OutPorts associated with this Module.
 	 */
    public void addOutPort(OutPort p)
    {
    	p.setModule(this);
    	outPorts.enter(p);
    }
	
	/**
 	 *  Sets m to be the parent of this Module.
 	 */
	public void setParent(Module m)
	{
		parent = m;
	}
	
	/**
 	 *  Returns the parent of this Module. Returns null if the parent does not exist;
 	 * meaning that this module is the top Module of the Module design hierarchy. 
 	 */
	public Module parent()
	{
		return parent;
	}
	
	/**
 	 *  Adds m to the list of submodules of this Module. Sets this Module to be
 	 * the parent of m. 
 	 */
	public void addSubmodule(Module m)
	{
		submodules.enter(m);
		m.setParent(this);
	}
	
	/**
 	 *  Returns the list of submodules associated with this Module. 
 	 */
	public Lst submodules()
	{
		return submodules;
	}
	
	/**
 	 *  Returns the submodule of this module that has the given name, if it exists. 
 	 * Returns null otherwise.
 	 */
	public Module getModule(String name)
	{
		if(this.name.equals(name)) return this;
		Module m;
		Module m2;
		
		for(submodules.reset(); submodules.hasNext(); submodules.advance())
		{
			m = (Module) submodules.access();
			m2 = m.getModule(name);
			if(m2 != null) return m2;
		}
		return null;
	}
	
	/**
 	 *  Returns the InPort of this module that has the given name, if it exists. 
 	 * Returns null otherwise.
 	 */
	public InPort getInPort(String name)
	{
		InPort p;
		
		for(inPorts.reset(); inPorts.hasNext(); inPorts.advance())
		{
			p = (InPort) inPorts.access();
			if(p.name().equals(name)) return p;
		}
		return null;
	}
	
	/**
 	 *  Returns the OutPort of this module that has the given name, if it exists. 
 	 * Returns null otherwise.
 	 */
	public OutPort getOutPort(String name)
	{
		OutPort p;
		
		for(outPorts.reset(); outPorts.hasNext(); outPorts.advance())
		{
			p = (OutPort) outPorts.access();
			if(p.name().equals(name)) return p;
		}
		return null;
	}
	
	/**
 	 *  Returns all descendant Modules of this module. 
 	 */
	public Lst descendants()
	{
		Lst l = new Lst();
		l.enter(submodules);
		Module m;
		
		//finalize all submodules
		for(submodules.reset(); submodules.hasNext(); submodules.advance())
		{
			m = (Module) submodules.access();
			l.enter(m.descendants());
		}
		return l;
	}
	
	/**
	* Returns the ith piece of information that describes this object. The default is that, 
	* for any input i, the name of the module is returned. 
	*/
	public Object info(int i)
	{
		return name;
	}
	
	/**
	* Returns the number of pieces of information that are used to display information about 
	* this Module. The default is 1, where the only piece of information is the Module name. 
	*/
	public int infoAmount() {return 1;}
	
	/**
 	 *  Color that is used for the background of the JPanel that is used to represent the 
 	 * graphical view of this Module.  
 	 */
	public Color color()
	{
		return Color.WHITE;
	}
	
	/**
 	 *  An EventHost processes an Event by simply calling the process() method of the Event. 
 	 */
	public void process(Event e)
	{
		e.process();
	}
	
	/**
 	 *  Returns true if obj is a Module whose name is equal to the name of this Module. 
 	 */
	public boolean equals(Object obj)
	{
		Module m = (Module) obj;
		return equals(m);
	}
	
	/**
 	 *  Returns true if m's name equals the name of this Module.
 	 */
	public boolean equals(Module m)
	{
		return name.equals(m.name());
	}
	
	/**
 	 *  This method is called by the InPort p of this Module for which t has entered. 
 	 * Any Module that overrides this method should do so using super.enter(t,time) as the first
 	 * line, so that the Token bookeeping will not be lost. 
 	 */
	public void enter(Token t, double time)
	{
		tokenCount++;
		loadCount += t.load();
		if(tokenCount == tokenCapacity || loadCount == loadCapacity) 
			closePortsNow(time);
	}
	
	/**
 	 *  This method is called by the OutPort p of this Module for which t has exited. 
 	 * Any Module that overrides this method should do so using super.exit(t,time) as the first
 	 * line, so that the Token bookeeping will not be lost. 
 	 */
	public void exit(Token t, double time)
	{
		int oldTokenCount = tokenCount;
		int oldLoadCount = loadCount;
		tokenCount--;
		loadCount -= t.load();
		
		if(oldTokenCount == tokenCapacity || oldLoadCount == loadCapacity) 
			selectPortOpenings(time);
		else 
			selectDeQueueAttempts(time);
	}
	
	/**
 	 *  Returns true this module's token and load counts are small enough so as to 
 	 * allow t to enter the module. 
 	 */
	public boolean hasCapacity(Token t)
	{
		return tokenCount < tokenCapacity && loadCount + t.load() <= loadCapacity; 
	}
	
	/**
 	 *  Invoking this method will cause this Module to trigger OpenPortEvents for all 
 	 * of its InPorts. These Events will be the next to occur.
 	 */
	public void openPortsNow(double time)
	{
		InPort p;
		for(inPorts.reset(); inPorts.hasNext(); inPorts.advance())
		{
			p = (InPort) inPorts.access();
			p.openNow(time);
		}
	}
	
	/**
 	 *  Invoking this method will cause this Module to trigger ClosePortEvents for all 
 	 * of its InPorts.
 	 */
	public void closePorts(double time)
	{
		InPort p;
		
		for(inPorts.reset(); inPorts.hasNext(); inPorts.advance())
		{
			p = (InPort) inPorts.access();
			p.close(time);
		}
	}
	
	/**
 	 *  Invoking this method will cause this Module to trigger ClosePortEvents for all 
 	 * of its InPorts. These Events will be the next to occur.
 	 */
	public void closePortsNow(double time)
	{
		InPort p;
		for(inPorts.reset(); inPorts.hasNext(); inPorts.advance())
		{
			p = (InPort) inPorts.access();
			p.closeNow(time);
		}
	}
	
	/**
	* This is called whenever a Token enters an OutPort, and causes the Module to be
	* under utilized. Whenever a Module is fully utilized, its InPorts are automatically closed.
	* This method allows the Module to selectively open InPorts given that it is now under
	* utilized. The default is to open all InPorts.
	*/
	public void selectPortOpenings(double time)
	{
		InPort p;
		//Default: open all InPorts.
		for(inPorts.reset(); inPorts.hasNext(); inPorts.advance())
		{
			p = (InPort) inPorts.access();
			p.open(time);
		}
	}
	
	/**
	* This is called whenever a Token enters an OutPort, and causes the Module to be
	* under utilized. It allows for all of its InPorts to attempt to dequeue Tokens.
	*/
	public void selectDeQueueAttempts(double time)
	{
		InPort p;
		//Default: attempt to dequeue from all inports
		for(inPorts.reset(); inPorts.hasNext(); inPorts.advance())
		{
			p = (InPort) inPorts.access();
			p.attemptDeQueue(time);
		}
	}
	
	/**
	* A method for performing a (non-graphical) discrete-event simulation.
	* @param top the top module of the module system that is to be simulated
	* @param closingTime the deadline for scheduling Events for this simulation
	*/ 
	public static void simulate(Module top, double closingTime)
	{
		Module.fel.initialize();
		Module.clock.initialize(closingTime);
		top.initialize();
		
		Event event;
		while(Module.fel.hasMoreEvents())
		{
			event =  Module.fel.nextEvent();
			Module.clock.setLastEventTime(event.time());
			EventHost host = event.host();
			host.process(event);
		}
		
		top.finalize(Module.clock.lastEventTime());
	}
	
	/**
	* A method for performing a verbose discrete-event simulation.
	* @param top the top module of the module system that is to be simulated
	* @param closingTime the deadline for scheduling Events for this simulation
	*/ 
	public static void stepSimulate(Module top, double closingTime)
	{
		Module.fel.initialize();
		Module.clock.initialize(closingTime);
		top.initialize();
	
		System.out.println("***Beginning of Simulation***\n");
		System.out.println("System Events\n");
		
		Event event;
		while(Module.fel.hasMoreEvents())
		{
			event =  Module.fel.nextEvent();
			Module.clock.setLastEventTime(event.time());
			System.out.print("Event " + Module.fel.numberOfEvents() + ". " + event.toString());
			
			System.out.print("(press enter)");
			Keyboard.readAnyKey();
			System.out.println();
			EventHost host = event.host();
			host.process(event);
		}
			
		System.out.println("***End of Simulation***");
		top.finalize(Module.clock.lastEventTime());
	}
	
	/**
	* Returns the time of occurrence of the Event that is processed.
	*/
	public static double processNextEvent()
	{
		Event event =  Module.fel.nextEvent();
		EventHost host = event.host();
		host.process(event);
		return event.time();
	}
	
	/**
	* Connects the first OutPort of m1 to the first InPort of m2. Has no effect if either Module
	* is missing the needed port. 
	*/
	public static void connect(Module m1, Module m2)
	{
		if(m1.outPorts().size() == 0 || m2.inPorts().size() == 0) return;
		OutPort op = (OutPort) m1.outPorts().front();
		InPort ip = (InPort) m2.inPorts().front();
		Wire w = new Wire();
		w.connectFrom(op);
		w.connectTo(ip);
	}
	
	/**
	* Connects the first OutPort of m1 to the first InPort of m2, using a PhysicalWire
	* with the given delay value. Has no effect if either Module
	* is missing the needed port. 
	*/
	public static void connect(Module m1, Module m2, double delay)
	{
		if(m1.outPorts().size() == 0 || m2.inPorts().size() == 0) return;
		OutPort op = (OutPort) m1.outPorts().front();
		InPort ip = (InPort) m2.inPorts().front();
		Wire w = new PhysicalWire(delay);
		w.connectFrom(op);
		w.connectTo(ip);
	}
	
	/**
	* Connects the OutPort of m1 having name outName with to the InPort of m2
	* having name inName. Has no effect if either Module
	* is missing the needed port. 
	*/
	public static void connect(Module m1, Module m2,String outName, String inName)
	{
		Lst ports = m1.outPorts();
		Port p;
		OutPort op = null;
		
		for(ports.reset(); ports.hasNext(); ports.advance())
		{
			p = (OutPort) ports.access();
			if(p.name().equals(outName))
			{
				op = (OutPort) p;
				break;
			}
		}
		
		if(op == null) return;
		
		InPort ip = null;
		ports = m2.inPorts();
		
		for(ports.reset(); ports.hasNext(); ports.advance())
		{
			p = (InPort) ports.access();
			if(p.name().equals(inName))
			{
				ip = (InPort) p;
				break;
			}
		}
		
		if(ip == null) return;
		
		Wire w = new Wire();
		w.connectFrom(op);
		w.connectTo(ip);
	}
	
	/**
	* Connects the OutPort of m1 having name outName with to the InPort of m2
	* having name inName, and using a PhysicalWire with the stated delay. 
	* Has no effect if either Module
	* is missing the needed port. 
	*/
	public static void connect(Module m1, Module m2,String outName, String inName, double delay)
	{
		Lst ports = m1.outPorts();
		Port p;
		OutPort op = null;
		
		for(ports.reset(); ports.hasNext(); ports.advance())
		{
			p = (OutPort) ports.access();
			if(p.name().equals(outName))
			{
				op = (OutPort) p;
				break;
			}
		}
		
		if(op == null) return;
		
		InPort ip = null;
		ports = m2.inPorts();
		
		for(ports.reset(); ports.hasNext(); ports.advance())
		{
			p = (InPort) ports.access();
			if(p.name().equals(inName))
			{
				ip = (InPort) p;
				break;
			}
		}
		
		if(ip == null) return;
		
		Wire w = new PhysicalWire(delay);
		w.connectFrom(op);
		w.connectTo(ip);
	}
	
	/**
	* Connects the first InPort of m1 to the first InPort of m2. Has no effect if either Module
	* is missing the needed port. 
	*/
	public static void connectInToIn(Module m1, Module m2)
	{
		if(m1.inPorts().size() == 0 || m2.inPorts().size() == 0) return;
		InPort ip1 = (InPort) m1.inPorts().front();
		InPort ip2 = (InPort) m2.inPorts().front();
		Wire w = new Wire();
		w.connectFrom(ip1);
		w.connectTo(ip2);
	}
	
	/**
	* Connects the first InPort of m1 to the first InPort of m2, using a PhysicalWire
	* with the given delay value. Has no effect if either Module
	* is missing the needed port. 
	*/
	public static void connectInToIn(Module m1, Module m2, double delay)
	{
		if(m1.inPorts().size() == 0 || m2.inPorts().size() == 0) return;
		InPort ip1 = (InPort) m1.inPorts().front();
		InPort ip2 = (InPort) m2.inPorts().front();
		Wire w = new PhysicalWire(delay);
		w.connectFrom(ip1);
		w.connectTo(ip2);
	}
	
	/**
	* Connects the InPort of m1 having name fromName with to the InPort of m2
	* having name toName. Has no effect if either Module
	* is missing the needed port. 
	*/
	public static void connectInToIn(Module m1, Module m2,String fromName, String toName)
	{
		Lst ports = m1.inPorts();
		InPort p;
		InPort ip1 = null;
		
		for(ports.reset(); ports.hasNext(); ports.advance())
		{
			p = (InPort) ports.access();
			if(p.name().equals(fromName))
			{
				ip1 = p;
				break;
			}
		}
		
		if(ip1 == null) return;
		
		InPort ip2 = null;
		ports = m2.inPorts();
		
		for(ports.reset(); ports.hasNext(); ports.advance())
		{
			p = (InPort) ports.access();
			if(p.name().equals(toName))
			{
				ip2 = p;
				break;
			}
		}
		
		if(ip2 == null) return;
		
		Wire w = new Wire();
		w.connectFrom(ip1);
		w.connectTo(ip2);
	}
	
	/**
	* Connects the InPort of m1 having name fromName with to the InPort of m2
	* having name toName with a PhysicalWire having the given delay. 
	* Has no effect if either Module
	* is missing the needed port. 
	*/
	public static void connectInToIn(Module m1, Module m2,String fromName, String toName, 
		double delay)
	{
		Lst ports = m1.inPorts();
		InPort p;
		InPort ip1 = null;
		
		for(ports.reset(); ports.hasNext(); ports.advance())
		{
			p = (InPort) ports.access();
			if(p.name().equals(fromName))
			{
				ip1 = p;
				break;
			}
		}
		
		if(ip1 == null) return;
		
		InPort ip2 = null;
		ports = m2.inPorts();
		
		for(ports.reset(); ports.hasNext(); ports.advance())
		{
			p = (InPort) ports.access();
			if(p.name().equals(toName))
			{
				ip2 = p;
				break;
			}
		}
		
		if(ip2 == null) return;
		
		Wire w = new PhysicalWire(delay);
		w.connectFrom(ip1);
		w.connectTo(ip2);
	}
	
	/**
	* Connects the first OutPort of m1 to the first OutPort of m2. Has no effect if either Module
	* is missing the needed port. 
	*/
	public static void connectOutToOut(Module m1, Module m2)
	{
		if(m1.outPorts().size() == 0 || m2.outPorts().size() == 0) return;
		OutPort op1 = (OutPort) m1.outPorts().front();
		OutPort op2 = (OutPort) m2.outPorts().front();
		Wire w = new Wire();
		w.connectFrom(op1);
		w.connectTo(op2);
	}
	
	/**
	* Connects the first OutPort of m1 to the first OutPort of m2, using a PhysicalWire
	* with the given delay value. Has no effect if either Module
	* is missing the needed port. 
	*/
	public static void connectOutToOut(Module m1, Module m2, double delay)
	{
		if(m1.outPorts().size() == 0 || m2.outPorts().size() == 0) return;
		OutPort op1 = (OutPort) m1.outPorts().front();
		OutPort op2 = (OutPort) m2.outPorts().front();
		Wire w = new PhysicalWire(delay);
		w.connectFrom(op1);
		w.connectTo(op2);
	}
	
	/**
	* Connects the OutPort of m1 having name fromName with to the OutPort of m2
	* having name toName. Has no effect if either Module
	* is missing the needed port. 
	*/
	public static void connectOutToOut(Module m1, Module m2,String fromName, String toName)
	{
		Lst ports = m1.outPorts();
		OutPort p;
		OutPort op1 = null;
		
		for(ports.reset(); ports.hasNext(); ports.advance())
		{
			p = (OutPort) ports.access();
			if(p.name().equals(fromName))
			{
				op1 = p;
				break;
			}
		}
		
		if(op1 == null) return;
		
		OutPort op2 = null;
		ports = m2.outPorts();
		
		for(ports.reset(); ports.hasNext(); ports.advance())
		{
			p = (OutPort) ports.access();
			if(p.name().equals(toName))
			{
				op2 = p;
				break;
			}
		}
		
		if(op2 == null) return;
		
		Wire w = new Wire();
		w.connectFrom(op1);
		w.connectTo(op2);
	}
	
	/**
	* Connects the OutPort of m1 having name fromName with to the OutPort of m2
	* having name toName with a PhysicalWire having the given delay. 
	* Has no effect if either Module
	* is missing the needed port. 
	*/
	public static void connectOutToOut(Module m1, Module m2,String fromName, String toName, 
		double delay)
	{
		Lst ports = m1.outPorts();
		OutPort p;
		OutPort op1 = null;
		
		for(ports.reset(); ports.hasNext(); ports.advance())
		{
			p = (OutPort) ports.access();
			if(p.name().equals(fromName))
			{
				op1 = p;
				break;
			}
		}
		
		if(op1 == null) return;
		
		OutPort op2 = null;
		ports = m2.outPorts();
		
		for(ports.reset(); ports.hasNext(); ports.advance())
		{
			p = (OutPort) ports.access();
			if(p.name().equals(toName))
			{
				op2 = p;
				break;
			}
		}
		
		if(op2 == null) return;
		
		Wire w = new PhysicalWire(delay);
		w.connectFrom(op1);
		w.connectTo(op2);
	}
	
	public static int assignID() {return Module.idCount++;}
}
