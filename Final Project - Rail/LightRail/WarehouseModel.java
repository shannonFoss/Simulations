/*
 * WarehouseModel.java
 * Author: Todd Ebert
 * Date: 11/16/2008
 * 
 */
 
import java.lang.Exception;
import java.lang.reflect.*;

public class WarehouseModel extends SystemModel
{
	
	public WarehouseModel()
	{
		super();
	}

	
	public void setParameter(String paramName, String paramValue)
	{
		int policy=0;
		Warehouse w;
		if(paramName.equals("RoutingPolicy"))
		{
			CrateGenerator cg;
			Module m;
			policy = Integer.parseInt(paramValue);
			m = top.getModule("CrateAGenerator");
			cg = (CrateGenerator) m;
			cg.setRoutingPolicy(policy);
			m = top.getModule("CrateBGenerator");
			cg = (CrateGenerator) m;
			cg.setRoutingPolicy(policy);
			m = top.getModule("CrateCGenerator");
			cg = (CrateGenerator) m;
			cg.setRoutingPolicy(policy);
			m = top.getModule("Warehouse");
			w = (Warehouse) m;
			w.setRoutingPolicy(policy);
		}
		else if(paramName.equals("LoadingPolicy"))
		{
			policy = Integer.parseInt(paramValue);
			Module m;
			m = top.getModule("Warehouse");
			w = (Warehouse) m;
			w.setLoadingPolicy(policy);
		}
	}
	
	/**
 	 * Returns the root module of the SystemModel.
     */
	protected void buildModel()
	{
		Warehouse w = new Warehouse();
		CrateGenerator ag = new CrateGenerator("A");
		CrateGenerator bg = new CrateGenerator("B");
		CrateGenerator cg = new CrateGenerator("C");
		SimpleCollector cc = new SimpleCollector("CrateCollector");
		
		add(w);
		add(ag);
		add(bg);
		add(cg);
		add(cc);
		
		connect(ag,w,"CrateGeneratorRouter","CrateRouter");
		connect(bg,w,"CrateGeneratorRouter","CrateRouter");
		connect(cg,w,"CrateGeneratorRouter","CrateRouter");
		connect(ag,w,"CrateGeneratorRouter","LeftRelay");
		connect(bg,w,"CrateGeneratorRouter","RightRelay");
		connect(cg,w,"CrateGeneratorRouter","RightRelay");
		
		connect(w,cc,"CrateOutRelay" ,"CrateCollectorInPort");
		connect(w,cc,"LeftOutRelay" ,"CrateCollectorInPort");
		connect(w,cc,"RightOutRelay","CrateCollectorInPort");
		
		//Assign Modules to the viewing grid
		makeModelPanel(3,3);
		assignModule(w.leftFloor(0),2,0);
		assignModule(w.leftFloor(1),1,0);
		assignModule(w.leftFloor(2),0,0);
		
		assignModule(w.rightFloor(0),2,2);
		assignModule(w.rightFloor(1),1,2);
		assignModule(w.rightFloor(2),0,2);
		
		assignModule(w,2,1);
	}
	
	protected void setModelParameters() 
	{
		modelParameters = new String[2];
		modelParameters[0] = new String("LoadingPolicy");
		modelParameters[1] = new String("RoutingPolicy");
	}
}
