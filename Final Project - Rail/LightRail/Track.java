import java.awt.Color;
import java.util.Vector;


public class Track extends Module//ConstantServer
{
	public String name;
	Vector trains;
	//for single in/out tracks
	InPort inPort1;
	InPort inPort2;	
	OutPort outPort1;
	OutPort outPort2;

	public Track(String name) 
	{
		super(name, 7, INFINITY);//, Utility.gen_normal(2, 1));
		this.name=name;
		inPort1 = new InPort("InPort1");
		outPort1 = new OutRelay("OutPort1");
		inPort2 = new InPort("InPort2");
		outPort2 = new OutRelay("OutPort2");

		addInPort(inPort1);
		addOutPort(outPort1);
		addInPort(inPort2);
		addOutPort(outPort2);
		trains = new Vector();
	}
	
	public double getServiceTime(Token t) 
	{
		return Utility.gen_normal(2, 1);
	}

	public void enter(Token t, double time)
	{
		super.enter(t,time);
		Train train = (Train)t;
		trains.addElement(train);
		if(train.direction == 0){ //0=fwd 1=bkwd
			outPort1.enter(t,time+getServiceTime(t));

		}else{
			outPort2.enter(t,time+getServiceTime(t));
		}
	}

	
	public void exit(Token t, double time)
	{
		super.exit(t, time);
		Train tr = (Train)t;
		//remove train from vector
		for (int i = 0; i < trains.size(); i++) 
		{
			if (((Train)trains.elementAt(i)).id == tr.id)
			{
				trains.removeElementAt(i);
				break;
			}
		}
	}
	
	public Object info(int i)
	{
		String s = new String("");
		Train train1;
		Rider rider;
		int rid = -1;
		if(i == 0) return name;
		if(i == 1)
		{
			if(tokenCount > 0)
			{
				int count = 0;
				s += "Trains: ";
				for(int j=0; j < trains.size(); j++)
				{
					train1 = ((Train)trains.elementAt(j));
					if(train1 != null)
			   	 	{
			    		s += train1.name;
						rid = train1.getMinRider();
						if (rid != -1)
							s += "(R"+rid+")";;//rider.id()+")";
//			    		for(int k=0; k<train1.riders.size(); k++){
//			    			rider = (Rider)train1.riders.elementAt(k);
//			    			if(rider.displayRider == true){
//					    		s += "(R"+ rider.id()+")";
//			    			}
//			    		}
			    		count++;
			    		if(count < tokenCount)
			    			s += ",";
			    	}
			    }
			    return s;
			}else 
				return "Trains: none";
		}
		if(tokenCount>0){
			int count =0;
			s+="People: ";
			for(int j=0;j<trains.size();j++){
				if((Train)trains.elementAt(j) !=null)
				{
					s+= ((Train)trains.elementAt(0)).riders.size();
					count++;
					if(count<tokenCount)
						s+=",";
				}
			}
			return s;
		}else
			return "People: none";
	}	
	
	public int infoAmount() {return 3;}

	public Color color() 
	{
		if (trains.size() == 0)
			return Color.GRAY;
		else if(((Train)trains.elementAt(0)).name.compareTo("T_EC")==0){
			return Color.ORANGE;
		}else if(((Train)trains.elementAt(0)).name.compareTo("T_BC")==0){
			return Color.BLUE;
		}else if(((Train)trains.elementAt(0)).name.compareTo("T_CF")==0){
			return Color.PINK;
		}else if(((Train)trains.elementAt(0)).name.compareTo("T_AC")==0){
			return Color.WHITE;
		}else if(((Train)trains.elementAt(0)).name.compareTo("T_AFD")==0){
			return Color.YELLOW;
		}else if(((Train)trains.elementAt(0)).name.compareTo("T_DC")==0){
			return Color.MAGENTA;		
		}else if(((Train)trains.elementAt(0)).name.compareTo("T_BCE")==0){
			return Color.RED;
		}else if(((Train)trains.elementAt(0)).name.compareTo("Train7")==0){
			return Color.CYAN;
		}else{
			return Color.LIGHT_GRAY;
		}
	}
}
