/*
 * DMVModel2.java
 * Author: Todd Ebert
 * Date: 11/12/2008
 * 
 */
 
/**
* DEVS Model of Example 8 of Queueing Models lecture.
*/
public class DMVModel2 extends SystemModel
{
	/**
	* Original Example 8 System: 2 servers at check-in, 1 server at the photo station.
	* This time the testing station is modeled using a PhysicalWire with delay 20.0 minutes.
	*/
	public DMVModel2()
	{
		super();
	}
	
	
	//Original Example 8 System: 2 servers at check-in, 1 server at the photo station.
	//This time the testing station is modeled using a PhysicalWire with delay 20.0 minutes.
	protected void buildModel()
	{
		MarkovCustomerGenerator dmvGenerator = 
			new MarkovCustomerGenerator("DMVGenerator",5.0/6.0);
		dmvGenerator.addOutPort(new OutRelay());
		
		MarkovSimpleServer checkIn = new MarkovSimpleServer("CheckIn",2,2,0.5);
		double[] p = new double[2];
		p[0] = 0.15; 
		p[1] = 0.85;
		RandomRouter rr = new RandomRouter(p);
		checkIn.addOutPort(rr);
		
		MarkovSimpleServer photoStation = new MarkovSimpleServer("PhotoStation",1,1,1.0);
		photoStation.addOutPort(new OutRelay());
		
		SimpleCollector dmvCollector = new SimpleCollector("DMVCollector");
		
		connect(dmvGenerator,checkIn);
		connect(checkIn,photoStation,20.0); //Represents the 20.0 minute delay for Test Takers
		connect(checkIn,photoStation);
		connect(photoStation,dmvCollector);
		
		add(dmvGenerator);
		add(checkIn);
		add(photoStation);
		add(dmvCollector);
		
		makeModelPanel(1,4);
		assignModule(dmvGenerator,1,0);
		assignModule(checkIn,1,1);
		assignModule(photoStation,1,2);
		assignModule(dmvCollector,0,3);
	}
	
}
