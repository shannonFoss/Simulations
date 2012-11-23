/* Auth: Shannon Foss
 * Date: Spring 2011
 * File: GRIDTransferUnitRouter
 * Desc: 
 */
public class GRIDTransferUnitRouter extends FIFORouter{
	
	public GRIDTransferUnitRouter()
	{
		super("TransferUnitRouter");
	}
	//selects the wire to route a TU based on the router's value
	public Wire selectWire(Token t)
	{
		GRIDTransferUnit tu = (GRIDTransferUnit)t;
		outputs.reset();
		int r = tu.getRouter() % GRIDConstants.COLS;
		for(int i=0;i<r;i++){
			outputs.advance();
		}
		Wire w = (Wire)outputs.access();
		return w;
	}
}
