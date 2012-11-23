import java.util.Random;


public class RiderGenerator extends Generator
{
	RidersGeneratorRouter router;
	Random random;
	
	public int id;
	
	public RiderGenerator() 
	{
		super("RiderGenerator");
		router = new RidersGeneratorRouter("RiderGeneratorOutRouter");
		random = new Random();
		addOutPort(router);
		id = 0;
	}
	

	@Override
	public Token generateToken() 
	{
		double rand = random.nextDouble();
		Rider r = null;
		if (rand < 0.1)
			r = new Rider(Station.STATION_A, id++);
		else if (rand < 0.25)
			r = new Rider(Station.STATION_B, id++);
		else if (rand < 0.45)
			r = new Rider(Station.STATION_C, id++);
		else if (rand < 0.50)
			r = new Rider(Station.STATION_D, id++);
		else if (rand < 0.75)
			r = new Rider(Station.STATION_E, id++);
		else r = new Rider(Station.STATION_F, id++);
		setDestination(r);
		//set if rider is displayed
		rand = random.nextDouble();
		r.displayRider = false;
		if(rand < 0.1)
			r.displayRider = true;
		
		return r;
	}
	
	private void setDestination(Rider r)
	{
		double rand = random.nextDouble(); 
		switch (r.source) 
		{
		case Station.STATION_A:
			if (rand < 0.4) r.setDestination(Station.STATION_B);
			else if (rand < 0.5) r.setDestination(Station.STATION_C);
			else if (rand < 0.55) r.setDestination(Station.STATION_D);
			else if (rand < 0.7) r.setDestination(Station.STATION_E);
			else r.setDestination(Station.STATION_F);
			break;

		case Station.STATION_B:
			if (rand < 0.3) r.setDestination(Station.STATION_A);
			else if (rand < 0.6) r.setDestination(Station.STATION_C);
			else if (rand < 0.65) r.setDestination(Station.STATION_D);
			else if (rand < 0.85) r.setDestination(Station.STATION_E);
			else r.setDestination(Station.STATION_F);
			break;

		case Station.STATION_C:
			if (rand < 0.2) r.setDestination(Station.STATION_A);
			else if (rand < 0.4) r.setDestination(Station.STATION_B);
			else if (rand < 0.55) r.setDestination(Station.STATION_D);
			else if (rand < 0.8) r.setDestination(Station.STATION_E);
			else r.setDestination(Station.STATION_F);
			break;
		case Station.STATION_D:
			if (rand < 0.1) r.setDestination(Station.STATION_A);
			else if (rand < 0.2) r.setDestination(Station.STATION_B);
			else if (rand < 0.4) r.setDestination(Station.STATION_C);
			else if (rand < 0.7) r.setDestination(Station.STATION_E);
			else r.setDestination(Station.STATION_F);

			break;
	
		case Station.STATION_E:
			if (rand < 0.2) r.setDestination(Station.STATION_A);
			else if (rand < 0.4) r.setDestination(Station.STATION_B);
			else if (rand < 0.6) r.setDestination(Station.STATION_C);
			else if (rand < 0.8) r.setDestination(Station.STATION_D);
			else r.setDestination(Station.STATION_F);

			break;
	
		default: // station F
			if (rand < 0.2) r.setDestination(Station.STATION_A);
			else if (rand < 0.4) r.setDestination(Station.STATION_B);
			else if (rand < 0.6) r.setDestination(Station.STATION_C);
			else if (rand < 0.8) r.setDestination(Station.STATION_D);
			else r.setDestination(Station.STATION_E);

			break;
		}
	}
	
	@Override
	public double nextDepartureTime(double lastDepartureTime) 
	{
		return lastDepartureTime + Utility.gen_expo(3);
	}

}
