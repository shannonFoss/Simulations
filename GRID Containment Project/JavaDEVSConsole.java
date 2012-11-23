import java.lang.Math;

public class JavaDEVSConsole 
{
	public static void main(String[] args)
	{
		int choice=1; 
		final int QUIT=0;
		RootModule top=null;
		double closingTime = 0.0;
		String moduleName;
		Module m;
		Lst ports;
		InPort ip;
		double arrivalRate = 0.0;
		double serviceRate = 0.0;
		int c = 1;
		String modelName=null;
		SystemModel sm;
		
		while(choice != QUIT)
		{
			System.out.println("Choose operation:");
			System.out.println("0=quit");
			System.out.println("1=load system model");
			System.out.println("2=simulate");
			System.out.println("3=step simulate");
			System.out.println("4=M/M/c simulation");
			System.out.println("5=M/M/c step simulation");
			System.out.println("6=display port statistics");
			choice = Keyboard.readInt();
			
			if(choice == 0) break;
			else if(choice == 1)
			{
				System.out.print("Enter model name: ");
				modelName = Keyboard.readString();
				sm = SystemModel.getSystemModel(modelName);
				if(sm != null)
				{
					top = sm.top();
					System.out.println();
					System.out.println("Model " + modelName + " was successfully built.");
					System.out.println();
				}
				else
				{
					System.out.println();
					System.out.println("Unable to build model " + modelName + ".");
					System.out.println();
				}
			}
			else if(choice == 2 && top != null)
			{
				System.out.print("Enter closing time: ");
				closingTime = Keyboard.readDouble();
				Module.simulate(top,closingTime);
				System.out.println();
				System.out.println("The simulation has successfully completed.");
				System.out.println("Number of events processed: " + Module.fel.numberOfEvents());
				System.out.println("Last event time: " + Module.clock.lastEventTime());
				System.out.println();
				System.out.println();
			}
			else if(choice == 3 && top != null)
			{
				System.out.print("\nEnter closing time: ");
				closingTime = Keyboard.readDouble();
				Module.stepSimulate(top,closingTime);
				System.out.println();
				System.out.println("The simulation has successfully completed.");
				System.out.println("Number of events processed: " + Module.fel.numberOfEvents());
				System.out.println("Last event time: " + Module.clock.lastEventTime());
				System.out.println();
				System.out.println();
			}
			else if(choice == 4)
			{
				System.out.print("Enter closing time: ");
				closingTime = Keyboard.readDouble();
				System.out.print("\nEnter arrival rate: ");
				arrivalRate = Keyboard.readDouble();
				System.out.print("\nEnter service rate: ");
				serviceRate = Keyboard.readDouble();
				System.out.print("\nEnter number of servers: ");
				c = Keyboard.readInt();
				top = buildMarkovSystem(c,arrivalRate,serviceRate);
				
				Module.simulate(top,closingTime);
				System.out.println();
				System.out.println("The simulation has successfully completed.");
				System.out.println("Number of events processed: " + Module.fel.numberOfEvents());
				System.out.println("Last event time: " + Module.clock.lastEventTime());
				System.out.println();
				System.out.println();
				
				System.out.print(markovStatistics(c,arrivalRate,serviceRate));
				System.out.println();
				System.out.println("Markov Queue Statistics\n");
				m = top.getModule("MarkovServer"); 
				ports = m.inPorts();
				ip = (InPort) ports.front();
				System.out.println(ip.portStatsReport());
				System.out.println();
				System.out.println();
			}
			else if(choice == 5)
			{
				System.out.print("Enter closing time: ");
				closingTime = Keyboard.readDouble();
				System.out.print("\nEnter arrival rate: ");
				arrivalRate = Keyboard.readDouble();
				System.out.print("\nEnter service rate: ");
				serviceRate = Keyboard.readDouble();
				System.out.print("\nEnter number of servers: ");
				c = Keyboard.readInt();
				top = buildMarkovSystem(c,arrivalRate,serviceRate);
				Module.stepSimulate(top,closingTime);
				
				System.out.println();
				System.out.println("The simulation has successfully completed.");
				System.out.println("Number of events processed: " + Module.fel.numberOfEvents());
				System.out.println("Last event time: " + Module.clock.lastEventTime());
				System.out.println();
				System.out.println();
				
				System.out.print(markovStatistics(c,arrivalRate,serviceRate));
				System.out.println();
				System.out.println("Markov Queue Statistics\n");
				m = top.getModule("MarkovServer"); 
				ports = m.inPorts();
				ip = (InPort) ports.front();
				System.out.println(ip.portStatsReport());
				System.out.println();
				System.out.println();
			}
			else if(choice == 6)
			{
				System.out.print("\nEnter module name: ");
				moduleName = Keyboard.readString();
				m = top.getModule(moduleName); 
				if(m == null) 
					System.out.println("\nThe module does not exist");
				else
				{
					ports = m.inPorts();
					if(ports.size() == 0)
						System.out.println("\nModule " + m.name() + " has no ports.");	
					else
					{
						for(ports.reset();ports.hasNext();ports.advance())
						{
							ip = (InPort) ports.access();
							System.out.println();
							System.out.println(ip.portStatsReport());
						}
					}
				}
				System.out.println();
				System.out.println();
			}
			else if(choice == 6 || choice == 2 || choice == 3)
			{
				System.out.println();
				System.out.println("A system model has yet to be loaded");
				System.out.println();
			}
			else
			{
			
			}
		}
	}
	
	
	private static RootModule buildMarkovSystem(int c,double lambda,double mu)
	{
		RootModule top = new RootModule("MarkovSystem");
		
		MarkovCustomerGenerator mcg = 
			new MarkovCustomerGenerator("MarkovCustomerGenerator",lambda);
		mcg.addOutPort(new OutRelay("OutRelay"));
		
		MarkovSimpleServer mss = new MarkovSimpleServer("MarkovServer",c,c,mu);
		mss.addOutPort(new OutRelay("ServerOutPort"));
		
		SimpleCollector sc = new SimpleCollector("CustomerCollector");
		
		Module.connect(mcg,mss);
		Module.connect(mss,sc);
		
		top.addSubmodule(mcg);
		top.addSubmodule(mss);
		top.addSubmodule(sc);
		
		return top;
	}
	
	//Assume: c, lambda, mu > 0. 
	//lambda: arrival rate  mu: service rate
	private static String markovStatistics(int c, double lambda, double mu)
	{
		double rho = lambda/(c*mu);
		double rho1 = lambda/mu;
		double sum = 0.0;
		
		for(int n=0; n < c; n++)
			sum += Math.pow(rho1,n)/factorial(n);
		sum += (Math.pow(rho1,c)/factorial(c))*((c*mu)/(c*mu - lambda));
		double P0 = 1.0/sum;
		double Pinf = (Math.pow(rho1,c)*P0)/(factorial(c)*(1-rho));
		double L = rho1 + (rho*Pinf)/(1-rho);
		
		String s = new String("");
		s += "Arrival rate: " + lambda + "\n";
		s += "L: " + L + "\n";
		s += "LQ: " + (L-rho1) + "\n";
		s += "w: " + (L/lambda) + "\n";
		s += "wQ: " + (L/lambda-1.0/mu) + "\n";
		return s;
	}
	
	private static double factorial(int n)
	{
		if(n == 0) return 1.0;
		double prod = 1.0;
		for(int i=1; i <= n; i++) prod *= i;
		return prod;
	}
}
