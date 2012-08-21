/*
 * PortStats.java
 * Author: Todd Ebert
 * Date: 10/26/2008
 * 
 */

public class PortStats
{
	protected FiniteDistribution tokenDistribution;
	protected FiniteDistribution loadDistribution;
	protected FiniteDistribution queueDistribution;
	
	public int totalArrivals;
	public int totalLoad;   //total amount of load to enter this module
	protected double lastRecordedTime;
	protected double lastQueueTime; //last time a Token entered or exited the Queue, or 0.0 if not
	public int curTokens;  //current number of tokens in module that entered from this port
	public int curLoad;  //total load in module that entered from this port
	public int queuedTokens;
	protected int maxCapacity;
	
	private static final int UNDEFINED = -1;
	
	
	public PortStats(int maxCapacity)
	{
		this.maxCapacity = maxCapacity;
		tokenDistribution = new FiniteDistribution(maxCapacity);
		loadDistribution = new FiniteDistribution(maxCapacity);
		queueDistribution = new FiniteDistribution();
		totalArrivals = 0;
		totalLoad = 0;
		lastRecordedTime = 0.0;
		lastQueueTime = 0.0;
		curTokens = 0;
		queuedTokens = 0;
		curLoad = 0;
	}
	
	public PortStats()
	{
		this.maxCapacity = UNDEFINED;
		tokenDistribution = new FiniteDistribution();
		loadDistribution = new FiniteDistribution();
		queueDistribution = new FiniteDistribution();
		totalArrivals = 0;
		totalLoad = 0;
		lastQueueTime = 0.0;
		lastRecordedTime = 0.0;
		curTokens = 0;
		queuedTokens = 0;
		curLoad = 0;
	}
	
	public void setTime(double time) 
	{
		loadDistribution.addMass(time-lastRecordedTime,curLoad);
		tokenDistribution.addMass(time-lastRecordedTime,curTokens);
		queueDistribution.addMass(time-lastQueueTime,queuedTokens);
		lastRecordedTime = time;
		lastQueueTime = time;
	}
	
	public void initialize()
	{
		if(this.maxCapacity == UNDEFINED)
		{
			tokenDistribution = new FiniteDistribution();
			loadDistribution = new FiniteDistribution();
		}
		else
		{
			tokenDistribution = new FiniteDistribution(maxCapacity);
			loadDistribution = new FiniteDistribution(maxCapacity);
		}
		queueDistribution = new FiniteDistribution();
		totalArrivals = 0;
		totalLoad = 0;
		lastRecordedTime = 0.0;
		lastQueueTime = 0.0;
		curTokens = 0;
		queuedTokens = 0;
		curLoad = 0;
	}
	
	public void enter(Token t, double time)
	{
		loadDistribution.addMass(time-lastRecordedTime,curLoad);
		tokenDistribution.addMass(time-lastRecordedTime,curTokens);
		curTokens++;
		curLoad += t.load();
		lastRecordedTime = time;
	}
	
	public void exit(Token t, double time)
	{
		if(curTokens == 0 || curLoad - t.load() < 0) return;
		loadDistribution.addMass(time-lastRecordedTime,curLoad);
		tokenDistribution.addMass(time-lastRecordedTime,curTokens);
		curTokens--;
		curLoad -= t.load();
		lastRecordedTime = time;
	}
	
	public void enterQueue(double time)
	{
		queueDistribution.addMass(time-lastQueueTime,queuedTokens);
		queuedTokens++;
		lastQueueTime = time;
	}
	
	public void exitQueue(double time)
	{
		queueDistribution.addMass(time-lastQueueTime,queuedTokens);
		queuedTokens--;
		lastQueueTime = time;
	}
	
	
	public double queueArrivalRate()
	{
		if(lastQueueTime == 0.0) return 0.0;
		return totalArrivals/lastQueueTime;
	}
	
	public double moduleArrivalRate()
	{
		if(lastRecordedTime == 0.0) return 0.0;
		return (totalArrivals-queuedTokens)/lastRecordedTime;
	}
	
	public double loadArrivalRate()
	{
		if(lastRecordedTime == 0.0) return 0.0;
		return totalLoad/lastRecordedTime;
	}
	
	public double averageTimeSpentInModule()
	{
		double rate = moduleArrivalRate();
		if(rate == 0.0) return 0.0;
		return tokenDistribution.mean()/rate;
	}
	
	public double averageTimeSpentInQueue()
	{
		double rate = queueArrivalRate();
		if(rate == 0.0) return 0.0;
		return queueDistribution.mean()/rate;
	}
	
	public double averageNumberOfTokensInModule()
	{
		return tokenDistribution.mean();
	}
	
	public double averageNumberOfTokensInQueue()
	{
		return queueDistribution.mean();
	}
	
	public double varianceOfTokensInModule()
	{
		return tokenDistribution.variance();
	}
	
	public double varianceOfTokensInQueue()
	{
		return queueDistribution.variance();
	}
	
	public double stdOfTokensInModule()
	{
		return tokenDistribution.std();
	}
	
	public double stdOfTokensInQueue()
	{
		return queueDistribution.std();
	}
	
	public double averageModuleLoad()
	{
		return loadDistribution.mean();
	}
	
	public double varianceOfModuleLoad()
	{
		return loadDistribution.variance();
	}
	
	public double stdOfModuleLoad()
	{
		return loadDistribution.std();
	}
	
	public int maxNumberOfTokensInModule()
	{
		return tokenDistribution.size()-1;
	}
	
	public int maxNumberOfTokensInQueue()
	{
		return queueDistribution.size()-1;
	}
	
	public int maxModuleLoad()
	{
		return loadDistribution.size()-1;
	}
	
	public int mostFrequentNumberOfTokensInModule()
	{
		return tokenDistribution.mode();
	}
	
	public int mostFrequentNumberOfTokensInQueue()
	{
		return queueDistribution.mode();
	}
	
	public int mostFrequentModuleLoad()
	{
		return loadDistribution.mode();
	}
}
