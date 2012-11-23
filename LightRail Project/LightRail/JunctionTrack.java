import java.awt.Color;
import java.util.Vector;


public class JunctionTrack extends Module //ConstantServer
{
	public String name;
	Train train;
	//for single in/out tracks
	InPort inPortCtoF;
	InPort inPortFtoC;	
	OutPort outPortCtoF;
	OutPort outPortFtoC;
	//for the one double in/out track
	InPort inPortCtoD;
	InPort inPortDtoC;
	OutPort outPortCtoD;
	OutPort outPortDtoC;
	Vector trains = new Vector();
	
	public JunctionTrack(String name) 
	{
		super(name, INFINITY, INFINITY);//, Utility.gen_normal(2, 1));
		this.name=name;
		inPortCtoF = new InPort("InPortCtoF");
		outPortCtoF = new OutRelay("OutPortCtoF");
		inPortFtoC = new InPort("InPortFtoC");
		outPortFtoC = new OutRelay("OutPortFtoC");
		inPortCtoD = new InPort("InPortCtoD");
		outPortCtoD = new OutRelay("OutPortCtoD");
		inPortDtoC = new InPort("InPortDtoC");
		outPortDtoC = new OutRelay("OutPortDtoC");
		addInPort(inPortCtoF);
		addOutPort(outPortCtoF);
		addInPort(inPortFtoC);
		addOutPort(outPortFtoC);
		addInPort(inPortCtoD);
		addOutPort(outPortCtoD);
		addInPort(inPortDtoC);
		addOutPort(outPortDtoC);
	}

	//regular enter
	public void enter(Token t, double time)
	{
		super.enter(t,time);
		train= (Train)t;
		trains.addElement(train);
		if(train.destination == 5){ //C to F
			outPortCtoF.enter(t,time+ getServiceTime(t));

		}else if(train.destination == 3){//C to D
			outPortCtoD.enter(t,time+getServiceTime(t));

		}else //if(train.destination == 3){
			if(train.source == 3){ // D to C
				outPortDtoC.enter(t,time+getServiceTime(t));

			}else if(train.source == 5){ //F to C
				outPortFtoC.enter(t,time+getServiceTime(t));
			}
//		}
	}	
	public void exit(Token t, double time)
	{
		super.exit(t, time);
		Train train1 = (Train)t;
		//remove train from vector
		for (int i = 0; i < trains.size(); i++){
			if (((Train)trains.elementAt(i)).id == train1.id){
				trains.removeElementAt(i);
				break;
			}
		}	
	}
	public double getServiceTime(Token t) 
	{
		return Utility.gen_normal(2, 1);
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
	
	//each train needs to have a color so that these may be updated with that train color
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
