/* Auth: Shannon Foss
 * Date: Spring 2011
 * File: GRIDTransferUnit
 * Desc: Representation of a Transfer Unit that is 
 * 		 used in a container terminal to move containers
 * 		 from one location to another.  A Transfer Unit
 * 		 may only hold one container at a time.
 */
public class GRIDTransferUnit implements Token {
	protected static int tuCtr = 0;
	
	protected int id;
	protected String name;
	protected int load;
	protected int [] dest = new int[2];//row, col
	protected int [] curLoc = new int[2];
	protected boolean hasContainer;
	protected double v0;
	protected int router; //location that the tu will be routed through
	protected int areaStart;
	protected int areaEnd;
	protected GRIDContainer container;
	protected String trainDest;
	protected boolean working; //true for working on train, false for idle
	protected boolean workInGrid;
	protected String dir; //either UD or LR
	protected boolean fwd; //either going forwards on the train or backwards
	protected int moves;
	
	public GRIDTransferUnit(int [] loc){
		load=0;
		id =tuCtr;
		name = "TU:"+id;
		dest[0]=0;
		dest[1]=0;
		curLoc[0]=loc[0];
		curLoc[1]=loc[1];
		router=0;
		container = null;
		v0 = 0;
		areaStart=0;
		areaEnd=0;
		working = false;
		trainDest = null;
		dir = "UD";
		fwd = true;
		moves = 0;
		tuCtr++;
		workInGrid=false;
	}
	//set and return methods
	public String name() {
		return name;
	}
	public int id() {
		return id;
	}
	public int load() {
		return load;
	}
	public void resetV(){
		v0=0;
	}
	public void setDest(int [] d){
		dest[0] = d[0];
		dest[1] = d[1];
	}
	public int[] getDest(){
		return dest;
	}
	public void setCurLoc(int [] loc){
		curLoc[0]= loc[0];
		curLoc[1]= loc[1];
	}
	public int[] getCurLoc(){
		return curLoc;
	}
	public boolean getHasContainer(){
		return hasContainer;
	}
	private void changeHasContainer(){
		hasContainer = !hasContainer;
	}
	public int getRouter(){
		return router;
	}
	public void setRouter(int r){
		router = r;
	}
	public String getTrainDest(){
		return trainDest;
	}
	public void setTrainDest(String s){
		trainDest = s;
	}
	public int getAreaStart(){
		return areaStart;
	}
	public int getAreaEnd(){
		return areaEnd;
	}
	public void setAreaStart(int s){
		areaStart = s;
	}
	public void setAreaEnd(int e){
		areaEnd = e;
	}
	public boolean getWorking(){
		return working;
	}
	public void setWorking(boolean w){
		working = w;
	}
	public boolean getWorkInGrid(){
		return workInGrid;
	}
	public void setWorkInGrid(boolean g){
		workInGrid = g;
	}
	public String getDir(){
		return dir;
	}
	public void setDir(String d){
		dir = d;
	}
	public void changeFwd(){
		fwd = !fwd;
	}
	public boolean getFwd(){
		return fwd;
	}
	public void incMoves(){
		moves++;
	}
	public int getMoves(){
		return moves;
	}
	//method to pick up a container from a location
	public void pickUpContainer(GRIDContainer c){
		container = c;
		changeHasContainer();
	}
	//method to leave a container at a location
	public GRIDContainer dropContainer(){
		GRIDContainer c = container;
		container = null;
		changeHasContainer();
		return c;
	}
	//method to increment the router when working on a train
	public void changeRouted(){
		if(fwd){
			router++;
			if(router!=0){
				if(router%GRIDConstants.COLS ==areaEnd ||router % GRIDConstants.COLS==0){
					router+= GRIDConstants.COLS-1;
					changeFwd();
				}
			}
		}else{
			if(router%GRIDConstants.COLS == areaStart){
				router+= GRIDConstants.COLS+1;
				changeFwd();
			}
			router--;
		}
//		router++;
//		if(tuCtr>1){
//			if(router != 0){
//				if(router % GRIDConstants.COLS==areaEnd||router % GRIDConstants.COLS==0){
//					router+=GRIDConstants.COLS-(areaEnd-areaStart);	
//				}
//			}
//		}
	}
	//method to calculate the time it takes the TU crane to hoist a container
	public double calcBeamMoveTime(int p){
		double time =0;
		if(hasContainer){
			if(p==6){
				time = 7.12;
			}else if(p==5){
				time = 10.08;
			}else if(p==4){
				time = 12.63;
			}else if(p==3){
				time = 15.18;
			}else if(p==2){
				time = 17.73;
			}else if(p==1){
				time = 20.28;
			}
			//add on 2.55s for each extra container
		}else{
			if(p==6){
				time = 7.12;
			}else if(p==5){
				time = 10.08;
			}else if(p==4){
				time = 12.34;
			}else if(p==3){
				time = 14.26;
			}else if(p==2){
				time = 15.97;
			}else if(p==1){
				time = 17.68;
			}
			//add on 1.69s for each extra container
		}
		return time;
	}
	//method to calculate the time to move from one location on a train to another
	public double calcStationMoveTime(int r){
		double time = 0;
		if(Math.abs(router - r) == 1){
			if(hasContainer){
				time = 17.09;
			}else{
				time = 15.56;
			}
		}else{
			//int num = r+ GRIDConstants.COLS - router;
			time = 37.12;//2 ch dir 2st 7.12
//			if(hasContainer){
//				if(num==1){
//					time+=17.09;
//				}else if(num>1){
//					while(num >2){
//						time+=12.09;
//						num--;
//					}
//					time+=14.59*2;
//				}
//			}else{
//				if(num==1){
//					time+=15.56;
//				}else if(num>1){
//					while(num >2){
//						time+=8.06;
//						num--;
//					}
//					time+=11.81*2;
//				}
//			}
		}
		return time;
	}
	//method to calculate the time needed to move from one grid location to another
	public double calcGridMoveTime(boolean direction)//true = N/S, false = E/W
	{
		double vMax;
		if(hasContainer){
			vMax = GRIDConstants.VMAXL;
		}else{
			vMax = GRIDConstants.VMAXU;
		}
		double dt;
		double dist;
		if(direction){
			dist = GRIDConstants.CTRWIDTH;
			dt = Math.abs(curLoc[0] - dest[0]) *dist;
		}else{
			dist = GRIDConstants.CTRLENGTH;
			dt = Math.abs(curLoc[1] - dest[1]) *dist;
		}
		
		double tNext = 0;
		double vNext = 0;
		
		double dab = (v0*v0 - 2*GRIDConstants.ACCEL*dt)/(-4*GRIDConstants.ACCEL);
		double dvmax = (vMax*vMax - v0*v0)/(2*GRIDConstants.ACCEL);
		if(dvmax <dab){
			double dsmax = dt - (vMax*vMax/(2*GRIDConstants.ACCEL));
			if(dist < dvmax){
				tNext = (-v0 + Math.sqrt(v0*v0+2*GRIDConstants.ACCEL*dist))/GRIDConstants.ACCEL;
				vNext = v0+GRIDConstants.ACCEL*tNext;
			}else if(dist < dsmax){
				double t1 = (-v0 + Math.sqrt(v0*v0+2*GRIDConstants.ACCEL*dvmax))/GRIDConstants.ACCEL;
				double t2 = (dist - dvmax)/vMax;
				vNext = vMax;
				tNext = t1+t2;
			}else{
				double t1 = (-v0 + Math.sqrt(v0*v0+2*GRIDConstants.ACCEL*dvmax))/GRIDConstants.ACCEL;
				double t2 = (dsmax - dvmax) / vMax;
				double t3 = (-vMax + Math.sqrt(vMax*vMax - 2*GRIDConstants.ACCEL*(dist -dsmax)))/(-GRIDConstants.ACCEL);
				vNext = vMax - GRIDConstants.ACCEL*t3;
				tNext = t1+t2+t3;
			}
		}else{
			if(dist < dab){
				double t1 = (-v0+Math.sqrt(v0*v0 + 2*GRIDConstants.ACCEL*dist))/GRIDConstants.ACCEL;
				vNext = v0+GRIDConstants.ACCEL*t1;
				tNext = t1;
			}else{
				double vab = Math.sqrt(v0*v0 + 2*GRIDConstants.ACCEL*dab);
				double t1 = (-v0 + Math.sqrt(v0*v0 +2*GRIDConstants.ACCEL*dab))/GRIDConstants.ACCEL;
				double t2 = (-vab + Math.sqrt(vab*vab - 2*GRIDConstants.ACCEL*(dist - dab)))/(-GRIDConstants.ACCEL);
				vNext = vab-GRIDConstants.ACCEL*t2;
				tNext = t1+t2;
			}
		}
		if (Double.isNaN(tNext)){
			System.out.print("D'oh");
		}
		v0=vNext;
		
		return tNext;
	}
	//method to find the next destination the TU should go to 
	public void findDestination(boolean sendToArea){//true = grid, false = station
		if(sendToArea){//grid
			GRIDContainerGridStack [][] stack = GRIDContainerGrid.getStacks();
			if(hasContainer){//putting container in grid
				boolean p1found = false;
				boolean p2found = false;
				boolean p3found = false;
				boolean p4found = false;
				int [][] priorities = new int [2][5];
				//starts from the bottom left of the grid and moves right and then upward
				//these loops find the best 4 locations and gives the best based on priority
				for(int i=GRIDConstants.ROWS-1;i>=0;i--){
					for(int j=0;j<GRIDConstants.COLS;j++){
						if(!stack[i][j].getIsFull()){//any non-full stack
							if(j>=areaStart&&j<areaEnd){
								if(container.getDestination().equals(stack[i][j].getDest())&&!p1found){	
									priorities[0][0]=i;
									priorities[1][0]=j;
									p1found=true;
								}else if(stack[i][j].getDest().equals("none")&&!p2found){
									priorities[0][1]=i;
									priorities[1][1]=j;
									p2found=true;
								}
							}else{
								if(container.getDestination().equals(stack[i][j].getDest())&&!p3found){
									priorities[0][2]=i;
									priorities[1][2]=j;
									p3found=true;
								}else if(stack[i][j].getDest().equals("none")&&!p4found){
									priorities[0][3]=i;
									priorities[1][3]=j;
									p4found=true;
								}
								else{//fix
									priorities[0][4]=i;
									priorities[1][4]=j;
								}
							}
						}
					}
				}
				if(p1found){
					dest[0]=priorities[0][0];
					dest[1]=priorities[1][0];
				}else if(p2found){
					dest[0]=priorities[0][1];
					dest[1]=priorities[1][1];
				}else if(p3found){
					dest[0]=priorities[0][2];
					dest[1]=priorities[1][2];
				}else if(p4found){
					dest[0]=priorities[0][3];
					dest[1]=priorities[1][3];
				}else{
					dest[0]=priorities[0][4];
					dest[1]=priorities[1][4];
				}
			}else{//getting container from grid
				boolean p1found = false;
				boolean p2found = false;
				int [][] priorities = new int [2][3];
				for(int i=GRIDConstants.ROWS-1;i>=0;i--){
					for(int j=0;j<GRIDConstants.COLS;j++){
						if(!stack[i][j].getIsEmpty()){//any non-empty stack
							if(j>=areaStart&&j<areaEnd){
								if(trainDest.equals(stack[i][j].getDest())&&!p1found){	
									priorities[0][0]=i;
									priorities[1][0]=j;
									p1found=true;
								}
							}else{
								if(trainDest.equals(stack[i][j].getDest())&&!p2found){
									priorities[0][1]=i;
									priorities[1][1]=j;
									p2found=true;
								}
							}
						}
					}
				}
				if(p1found){
					dest[0]=priorities[0][0];
					dest[1]=priorities[1][0];
				}else if(p2found){
					dest[0]=priorities[0][1];
					dest[1]=priorities[1][1];
				}else{
					sendToArea=false;//no containers of that destination in grid, so keep unloading the train
				}
			}
		}//going to station
		if(!sendToArea){
			dest[0]=GRIDConstants.ROWS;
			dest[1]=router % GRIDConstants.COLS;
		}
		//System.out.println(name+" Dest["+dest[0]+","+dest[1]+"]");
	}
	
	public void findDestinationGrid(){
		GRIDContainerGridStack [][] stack = GRIDContainerGrid.getStacks();
		String c="";
		if(hasContainer){//container to unload
			System.out.print(id+" Unloading");
			boolean [] pfound = {false,false,false,false,false,false,false,false};
			int [][] priorities = new int [2][8];
			for(int i=GRIDConstants.ROWS-1;i>=0;i--){ //start from front
				for(int j=0;j<GRIDConstants.COLS;j++){//left to right
					if(curLoc[0]<i){//moving a container to the front
						if(!(curLoc[0]==i && curLoc[1]==j)){
							if(stack[i][j].getIsEmpty()){//empty spot in front
								if(container.getDestination().equals(trainDest)){//same dest as train
									if(j>=areaStart&&j<areaEnd){
										if(!pfound[2]){
											priorities[0][2]=i;
											priorities[1][2]=j;
											pfound[2]=true;
										}
									}else{
										if(!pfound[3]){
											priorities[0][3]=i;
											priorities[1][3]=j;
											pfound[3]=true;
										}
									}
								}else{//other destination
									if(j>=areaStart&&j<areaEnd){
										if(!pfound[2]){
											priorities[0][2]=i;
											priorities[1][2]=j;
											pfound[2]=true;
										}
									}else{
										if(!pfound[3]){
											priorities[0][3]=i;
											priorities[1][3]=j;
											pfound[3]=true;
										}
									}
								}
							}else if(!stack[i][j].getIsFull()){
								if(j>=areaStart&&j<areaEnd){
									if(container.getDestination().equals(stack[i][j].getDest())&&!pfound[0]){
										priorities[0][0]=i;
										priorities[1][0]=j;
										pfound[0]=true;
									}
								}else{
									if(container.getDestination().equals(stack[i][j].getDest())&&!pfound[1]){
										priorities[0][1]=i;
										priorities[1][1]=j;
										pfound[1]=true;
									}
								}
							}
						}
					}else{//moving a container to the back
						if(!(curLoc[0]==i&& curLoc[1]==j)){
							if(!stack[i][j].getIsFull()){
								if(container.getDestination().equals(stack[i][j].getDest())){
									if(j>=areaStart&&j<areaEnd){
										if(!pfound[4]){
											priorities[0][4]=i;
											priorities[1][4]=j;
											pfound[4]=true;
										}
									}else{
										if(!pfound[5]){
											priorities[0][5]=i;
											priorities[1][5]=j;
											pfound[5]=true;
										}
									}
								}else if(stack[i][j].getIsEmpty()){
									if(j>=areaStart&&j<areaEnd){
										if(!pfound[6]){
											priorities[0][6]=i;
											priorities[1][6]=j;
											pfound[6]=true;
										}
									}else{
										if(!pfound[7]){
											priorities[0][7]=i;
											priorities[1][7]=j;
											pfound[7]=true;
										}
									}
								}
							}
						}
					}
				}
			}
			
			if(pfound[0]){
				dest[0]=priorities[0][0];
				dest[1]=priorities[1][0];
			}else if(pfound[1]){
				dest[0]=priorities[0][1];
				dest[1]=priorities[1][1];
			}else if(pfound[2]){
				dest[0]=priorities[0][2];
				dest[1]=priorities[1][2];
			}else if(pfound[3]){
				dest[0]=priorities[0][3];
				dest[1]=priorities[1][3];
			}else if(pfound[4]){
				dest[0]=priorities[0][4];
				dest[1]=priorities[1][4];
			}else if(pfound[5]){
				dest[0]=priorities[0][5];
				dest[1]=priorities[1][5];
			}else if(pfound[6]){
				dest[0]=priorities[0][6];
				dest[1]=priorities[1][6];
			}else if(pfound[7]){
				dest[0]=priorities[0][7];
				dest[1]=priorities[1][7];
			}else{
				dest[0]=0;
				if(curLoc[1]==router){
					dest[1]= router+1;
				}else{
					dest[1]=router;
				}
			}
		}else{//need container
			System.out.print(id+" Loading");
			boolean [] pfound = {false,false,false,false};
			int [][] priorities = new int [2][4];
			for(int i=0;i<GRIDConstants.ROWS;i++){ //start from back
				for(int j=0;j<GRIDConstants.COLS;j++){//left to right
					if(!(curLoc[0]==i&& curLoc[1]==j)){
						if(!stack[i][j].getIsEmpty()){//any non-empty stack
							if(j>=areaStart&&j<areaEnd){
								if(trainDest.equals(stack[i][j].getDest())&&!pfound[0]){	
									priorities[0][0]= i;
									priorities[1][0]= j;
									pfound[0]=true;
									c = stack[i][j].getDest();
								}
							}else{
								if(trainDest.equals(stack[i][j].getDest())&&!pfound[1]){	
									priorities[0][1]= i;
									priorities[1][1]= j;
									pfound[1]=true;
									c = stack[i][j].getDest();
								}
							}
						}
					}
				}
			}
			boolean spot = false;
			for(int i=GRIDConstants.ROWS-1;i>=0;i--){ //start from front
				for(int j=0;j<GRIDConstants.COLS;j++){//left to right
					if(!(curLoc[0]==i&& curLoc[1]==j)){
						if(stack[i][j].getIsEmpty()){//empty spot
							if(pfound[0]){//own section
								if(priorities[0][0] < i){
									spot = true;
								}
							}
							if(pfound[1]){//other section
								if(priorities[0][1] < i){
									spot = true;
								}
							}
						}else if(!stack[i][j].getIsFull()){//any non-full stack
							if(c.equals(stack[i][j].getDest())){//of the same dest
								if(pfound[0]){//own section
									if(priorities[0][0] < i){
										spot = true;
									}
								}
								if(pfound[1]){//other section
									if(priorities[0][1]<i){
										spot = true;
									}
								}
							}else if((!trainDest.equals(stack[i][j].getDest()))&&!pfound[2]){
								if(j>=areaStart&&j<areaEnd){
									if(!pfound[2]){
										priorities[0][2] = i;
										priorities[1][2] = j;
										pfound[2] = true;
									}
								}else{
									if(!pfound[3]){
										priorities[0][3] = i;
										priorities[1][3] = j;
										pfound[3] = true;
									}
								}
							}
						}
					}
				}
			}
			if(spot){
				if(pfound[0]){
					dest[0]=priorities[0][0];
					dest[1]=priorities[1][0];
				}else if(pfound[1]){
					dest[0]=priorities[0][1];
					dest[1]=priorities[1][1];
				}
			}else{
				if(pfound[2]){
					dest[0]=priorities[0][2];
					dest[1]=priorities[1][2];
				}else if(pfound[3]){
					dest[0]=priorities[0][3];
					dest[1]=priorities[1][3];
				}else{
					dest[0]=0;
					if(curLoc[1]==router){
						dest[1]=router+1;
					}else{
						dest[1]=router;
					}
				}
			}
		}
		System.out.println("Dest: "+dest[0]+" "+dest[1]);
	}
	public static int getTUCtr(){
		return tuCtr;
	}
}
