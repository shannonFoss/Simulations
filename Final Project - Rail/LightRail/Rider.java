
public class Rider implements Token
{
	
	public int source;
	public int destination;
	private String name;
	public int id;
	public double startTime;
	public double endTime;
	public boolean displayRider;

	public Rider(int p_source, int id)
	{
		name = "Rider"+id;
		source = p_source;
		startTime = 0;
		endTime = 0;
		this.id = id;
	}
	
	public void setDestination(int p_dest)
	{
		destination = p_dest;
	}
	
	public int id() 
	{
		return id;
	}

	public int load() 
	{
		return 0;
	}

	public String name() 
	{
		return name;
	}

}
