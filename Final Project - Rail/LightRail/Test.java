/**
 * Can be used as the starting point for customized testing
 */
public class Test
{

	public static void main(String[] args)
	{
		WarehouseModel wm = new WarehouseModel();
		RootModule rm = wm.top();
		Module.simulate(rm,2000);
	}
}
