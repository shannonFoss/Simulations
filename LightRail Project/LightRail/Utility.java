import java.util.Random;


public class Utility 
{
	public static int numOfRider = 0;
	public static double totalTime = 0;
	
	public static double gen_expo(double lambda)
	{
		Random gen = new Random();
		return -1*(Math.log(gen.nextDouble()))/lambda;
	}

	public static double gen_normal(double mean, double std)
	{
		double rv = 0.0;
		Random gen = new Random();
		double rand1 = gen.nextDouble();
		double rand2 = gen.nextDouble();
		rv = mean + std* Math.sqrt(-2*Math.log(rand1))* Math.cos(2*rand2*Math.PI);
		if (rv < 0)
			rv = -1 * rv;
		return rv;
	}
	
	public static void computeTotalTimeSpentInSystem(Rider r)
	{
		double timeSpentInSystem = r.endTime - r.startTime;
		totalTime += timeSpentInSystem;
		numOfRider++;
	}
	
	public static double avgTimeInSystem()
	{
		return totalTime/numOfRider;
	}
}
