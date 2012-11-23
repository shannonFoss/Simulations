/*
 * DMVModel1.java
 * Author: Todd Ebert
 * Date: 11/12/2008
 * 
 */
 
/**
* DEVS Model of Example 8 of Queueing Models lecture.
*/
public class DMVModel1 extends SystemModel
{
	/**
	* Original Example 8 System: 2 servers at check-in, 1 server at the photo station.
	*/
	public DMVModel1() 
	{
		super();
	}
	
	//Original Example 8 System: 2 servers at check-in, 1 server at the photo station.
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
		
		ConstantServer testStation = new ConstantServer("TestStation",100,100,20.0);
		testStation.addOutPort(new OutRelay());
		
		MarkovSimpleServer photoStation = new MarkovSimpleServer("PhotoStation",1,1,1.0);
		photoStation.addOutPort(new OutRelay());
		
		SimpleCollector dmvCollector = new SimpleCollector("DMVCollector");
		
		connect(dmvGenerator,checkIn);
		connect(checkIn,testStation);
		connect(checkIn,photoStation);
		connect(testStation,photoStation);
		connect(photoStation,dmvCollector);
		
		add(dmvGenerator);
		add(checkIn);
		add(testStation);
		add(photoStation);
		add(dmvCollector);
		
		makeModelPanel(2,4);
		assignModule(dmvGenerator,1,0);
		assignModule(checkIn,1,1);
		assignModule(testStation,0,2);
		assignModule(photoStation,1,2);
		assignModule(dmvCollector,0,3);
	}
}
