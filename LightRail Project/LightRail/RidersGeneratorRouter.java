
public class RidersGeneratorRouter extends OutRouter
{
	public RidersGeneratorRouter(String name)
	{
		super(name);
	}

	@Override
	public Wire selectWire(Token t) 
	{		
		//selects a route based on the given probability
		Rider rider = (Rider)t;
		Wire w;
		String toPortName;
		for (outputs.reset(); outputs.hasNext(); outputs.advance())
		{
			w = (Wire)outputs.access();
			toPortName = w.to().name();
			if (toPortName.contains(rider.source+""))
			{
				return w;
			}
		}
		return null;
	}

}
