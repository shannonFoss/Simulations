/*
 * InPort.java
 * Author: Todd Ebert
 * Date: 10/23/2008
 * Updated: 11/09/2008
 * 
 */

/**
 * An InPort is a Port that monitors Tokens that are attempting to enter a Module.
 * When a Token arrives to an InPort (by way of a PortArrivalEvent), if the port is open then
 * it schedules an EnterPortEvent. If the port is closed or if there is not enough capacity for
 * the Token to enter, then the Token is entered into a queue, and attempts to dequeue it at 
 * at appropriate times (whenever a Token exits a Module, or when the port re-opens). It is 
 * important to note that InPorts implement the QPort abstract interface which provides them with
 * the functionality to queue and dequeue Tokens. However, the default implementation of this 
 * interface does not use a queue structure, meaning that Tokens that are unable to enter will 
 * be lost. If this is not the desired outcome, then JavaDEVS has the FIFOPort class which 
 * implements the QPort interface with a FIFO queue. 
 * 
 */
public class InPort extends Port implements QPort
{
	public final static int INFINITY = 2147483647; //2^31-1
	
	protected PortStats portStats;
	
	public InPort(String name)
	{
		super(name,true);
		this.name = name;
	}
	
	/**
	 * Returns the name of this port. Port names represent the easiest way to identify
	 * a port.
	 */
	public String name() {return name;}
	
	public boolean isInPort() {return true;}
	
	/**
	 * Returns the PortStats object associated with this object. 
	 */
	public PortStats portStats() {return portStats;}
	
	/**
	 * Sets the module that is to be associated with this Port.  
	 */
	public void setModule(Module m)
	{
		module = m;
		if(module.loadCapacity() < INFINITY) portStats = new PortStats(module.loadCapacity());
		else portStats = new PortStats();
	}
	
	/**
	 * Initially a port is open with an empty queue.  
	 */
	public void initialize()
	{
		isOpen=true;
		clearQueue();
		portStats.initialize();
	}
	
	/**
	 * Reports the final simulation time to the PortStats object.
	 */
	public void finalize(double finalTime)
	{
		portStats.setTime(finalTime);
	}
	
	/**
	 * Sets the time of this InPorts PortStats Object.
	 */
	public void setTime(double time)
	{
		portStats.setTime(time);
	}
	
	/**
	 * Invoking this method triggers the creation of a PortArrivalEvent for Token t at the
	 * indicated time. 
	 */
	public void arrive(Token t, double time)
	{
		PortArrivalEvent pae = new PortArrivalEvent(time,this,t);
		Module.fel().insert(pae);
	}
	
	/**
	 * Invoking this method triggers the creation of a PortArrivalEvent for Token t at the
	 * indicated time. This event will be the next processed event.  
	 */
	public void arriveNow(Token t, double time)
	{
		PortArrivalEvent pae = new PortArrivalEvent(time,this,t);
		pae.setIsNext();
		Module.fel().insert(pae);
	}
	
	/**
	 * Invoking this method triggers the creation of an OpenPortEvent at the
	 * indicated time. 
	 */
	public void open(double time)
	{
		OpenPortEvent ope = new OpenPortEvent(time,this);
		Module.fel().insert(ope);
	}
	
	/**
	 * Invoking this method triggers the creation of an OpenPortEvent at the
	 * indicated time. This event will be the next processed event.
	 */
	public void openNow(double time)
	{
		OpenPortEvent ope = new OpenPortEvent(time,this);
		ope.setIsNext();
		Module.fel().insert(ope);
	}
	
	/**
	 * Invoking this method triggers the creation of a ClosePortEvent at the
	 * indicated time. 
	 */
	public void close(double time)
	{
		ClosePortEvent cpe = new ClosePortEvent(time,this);
		Module.fel().insert(cpe);
	}
	
	/**
	 * Invoking this method triggers the creation of a ClosePortEvent at the
	 * indicated time. This event will be the next processed event.
	 */
	public void closeNow(double time)
	{
		ClosePortEvent cpe = new ClosePortEvent(time,this);
		cpe.setIsNext();
		Module.fel().insert(cpe);
	}
	
	/**
	 * Invoking this method triggers the creation of an AttemptDeQueueEvent at the
	 * indicated time. 
	 */
	public void attemptDeQueue(double time)
	{
		AttemptDeQueueEvent adqe = new AttemptDeQueueEvent(time,this);
		Module.fel().insert(adqe);
	}
	
	/**
	* Returns a string that formats the statistics regarding the Tokens that have entered 
	* this Port. 
	*/
	public String portStatsReport()
	{
		String s = new String("");
		s += "InPort name: " + name + "\n";
		s += "Tokens entered: " + portStats.totalArrivals + "\n";
		s += "Load entered: " + portStats.totalLoad + "\n";
		s += "Tokens remaining in queue: " + portStats.queuedTokens + "\n";
		s += "Tokens remaining in module: " + portStats.curTokens + "\n";
		s += "Load remaining in module: " + portStats.curLoad + "\n";
		s += "Load remaining in module: " + portStats.curLoad + "\n";
		s += "Queue arrival rate: " + portStats.queueArrivalRate() + "\n";
		s += "Module arrival rate: " + portStats.moduleArrivalRate() + "\n";
		s += "Load arrival rate: " + portStats.loadArrivalRate() + "\n\n";
		
		s += "Average time spent in queue: " + portStats.averageTimeSpentInQueue() + "\n";
		s += "Average number of tokens in queue: ";
		s +=  portStats.averageNumberOfTokensInQueue() + "\n";
		s += "Variance of tokens in queue: ";
		s +=  portStats.varianceOfTokensInQueue() + "\n";
		s += "std of tokens in queue: ";
		s +=  portStats.stdOfTokensInQueue() + "\n";
		s += "Max tokens in queue: " + portStats.maxNumberOfTokensInQueue() + "\n";
		s += "Most frequent number of tokens in queue: ";
		s +=  portStats.mostFrequentNumberOfTokensInQueue() + "\n\n";
		
		
		s += "Average time spent in module: " + portStats.averageTimeSpentInModule() + "\n";
		s += "Average number of tokens in module: ";
		s +=  portStats.averageNumberOfTokensInModule() + "\n";
		s += "Variance of tokens in module: ";
		s +=  portStats.varianceOfTokensInModule() + "\n";
		s += "std of tokens in module: ";
		s +=  portStats.stdOfTokensInModule() + "\n";
		s += "Max tokens in module: " + portStats.maxNumberOfTokensInModule() + "\n";
		s += "Most frequent number of tokens in module: ";
		s +=  portStats.mostFrequentNumberOfTokensInModule() + "\n\n";
		
		s += "Average module load: ";
		s +=  portStats.averageModuleLoad() + "\n";
		s += "Variance module load: ";
		s +=  portStats.varianceOfModuleLoad() + "\n";
		s += "std of module load: ";
		s +=  portStats.stdOfModuleLoad() + "\n";
		s += "Max module load: " + portStats.maxModuleLoad() + "\n";
		s += "Most frequent module load: ";
		s +=  portStats.mostFrequentModuleLoad() + "\n";
		return s;
	}
	
	/**
    * Event: Token t has arrived to this Port.
    * Immediate Effects: t will enter this Port if there is capacity within the module and,
    * if t is to be routed to another InPort, then that InPort is currently open. Otherwise 
    * t is placed in the port queue.
    */
	protected class PortArrivalEvent extends Event
	{
		Token token;
		
		public PortArrivalEvent(double time, EventHost host, Token t)
		{
			super(time,host);
			token = t;
		}
		
		public void process()
		{
			portStats.totalArrivals++;
			portStats.totalLoad += token.load();
			InPort p = (InPort) host;
			if(!p.isOpen())
			{
				p.enterQueue(token);
				portStats.enterQueue(time);
			}
			else if(p.module().hasCapacity(token) && p.canRoute(token))
				p.enterNow(token,time);
			else 
			{
				p.enterQueue(token);
				portStats.enterQueue(time);
			}
		}
		
		public String toString()
		{
			InPort p = (InPort) host;
			String s = new String("");
			s += "Time: " + time + "\n";
			s += "Token " + token.name() + " has arrived at port ";
			s += p.name() + " of module " + p.module().name() + ".\n\n";
			return s;
		}
	}
	
	/**
    * Event: this Port has opened.
    * Immediate Effects: this Port will attempt to dequeue a Token from its queue if one exists.
    * Any InPorts that connect to this Port will also attempt to dequeue Tokens.
    */
	protected class OpenPortEvent extends Event
	{
		public OpenPortEvent(double time, EventHost host)
		{
			super(time,host);
		}
		
		public void process()
		{
			InPort p = (InPort) host;
			p.open();
			p.attemptDeQueue(time);
			p.announceOpening(time);
		}
		
		public String toString()
		{
			InPort p = (InPort) host;
			String s = new String("");
			s += "Time: " + time + "\n";
			s += "Port " + p.name() + " of module " + p.module().name() + " has opened.\n\n";
			return s;
		}
	}
	
	/**
    * Event: this Port has closed.
    * 
    */
	protected class ClosePortEvent extends Event
	{
		public ClosePortEvent(double time, EventHost host)
		{
			super(time,host);
		}
		
		public void process()
		{
			InPort p = (InPort) host;
			p.close();
		}
		
		public String toString()
		{
			InPort p = (InPort) host;
			String s = new String("");
			s += "Time: " + time + "\n";
			s += "Port " + p.name() + " of module " + p.module().name() + " has closed.\n\n";
			return s;
		}
	}
	
	/**
    * Event: this Port is attempting to dequeue a Token from its queue, if one exists.
    * Immediate Effects: if it is successful in dequeueing a Token t, then t will enter 
    * this Port, and this Port will attempt to dequeue another Token.
    */
	protected class AttemptDeQueueEvent extends Event
	{
		public AttemptDeQueueEvent(double time, EventHost host)
		{
			super(time,host);
		}
		
		public void process()
		{
			InPort p = (InPort) host;
			if(!isOpen || p.qLength() == 0) return;
			Token t = p.viewNext();
			
			if(!p.module().hasCapacity(t) || !p.canRoute(t)) return;
			
			t = p.exitQueue();
			portStats.exitQueue(time);
			p.enterNow(t,time);
			if(p.qLength() > 0) 
			{
				AttemptDeQueueEvent dqte = new AttemptDeQueueEvent(time,host);
				Module.fel().insert(dqte);
			}
		}
		
		public String toString()
		{
			InPort p = (InPort) host;
			String s = new String("");
			s += "Time: " + time + "\n";
			s += "Port " + p.name() + " of module " + p.module().name();
			s += " is attempting to dequeue a token\n\n";
			return s;
		}
	}
	
	//Implementing QPort: the default is for a Port to not use a Queue. 
	//Override these methods in the case that an actual Queue will be used.
	public Token viewNext() {return null;}
	public int qLength() {return 0;}
	public void enterQueue(Token t) {}
	public Token exitQueue() {return null;}
	public void clearQueue() {}
	
	protected void registerToken(Token t, double time)
	{
		portStats.enter(t,time);
		module.enter(t,time);
	}
	
	protected void recordDepartingToken(Token t, double time)
	{
		portStats.exit(t,time);
	}
	
	//Attempt dequeueing for any InPorts that feed into this InPort.
	public void announceOpening(double time)
	{
		Port p;
		Wire w;
		InPort ip;
		
		for(inputs.reset();inputs.hasNext();inputs.advance())
		{
			w = (Wire) inputs.access();
			p = w.from();
			if(p.isInPort())
			{
				ip = (InPort) p;
				ip.attemptDeQueue(time);
			}
		}
	}
}
