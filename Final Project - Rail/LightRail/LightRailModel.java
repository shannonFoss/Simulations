
public class LightRailModel extends SystemModel
{

	public LightRailModel()
	{
		super();
	}
	
	
	@Override
	protected void buildModel() 
	{
		try
		{
			Track trackE_to_C1 = new Track("Track_E_To_C_1");
			Track trackE_to_C2 = new Track("Track_E_To_C_2");
			Track trackC_to_A1 = new Track("Track_C_To_A_1");
			Track trackC_to_A2 = new Track("Track_C_To_A_2");
			Track trackC_to_A3 = new Track("Track_C_To_A_3");
			//Tracks from A - F
			Track trackA_to_F1 = new Track("Track_A_to_F_1");
			Track trackA_to_F2 = new Track("Track_A_to_F_2");
			Track trackA_to_F3 = new Track("Track_A_to_F_3");
			//From F - D
			Track trackF_to_D1 = new Track("Track_F_to_D_1");
			Track trackF_to_D2 = new Track("Track_F_to_D_2");
			//From C - B
			Track trackC_to_B1 = new Track("Track_C_to_B_1");
			Track trackC_to_B2 = new Track("Track_C_to_B_2");
			//From C - F
			JunctionTrack trackC_to_F1 = new JunctionTrack("Track_C_to_F_1");
			Track trackC_to_F2 = new Track("Track_C_to_F_2");
			Track trackC_to_F3 = new Track("Track_C_to_F_3");
			//From C - D
			Track trackC_to_D1 = new Track("Track_C_to_D_1");
			Track trackC_to_D2 = new Track("Track_C_to_D_2");
			
			Station_B_E stationE = new Station_B_E(Station.STATION_E);
			Station_C stationC = new Station_C();
			Station_A stationA = new Station_A();
			Station_F stationF = new Station_F();
			Station_D stationD = new Station_D();
			Station_B_E stationB = new Station_B_E(Station.STATION_B);
			
			RiderGenerator rgen = new RiderGenerator();
			
			Train trainEC = new Train(0, Station.STATION_E, Station.STATION_C);
			Train trainBC = new Train(1, Station.STATION_B, Station.STATION_C);
			trainBC.direction = Train.BACKWARD;
			Train trainCF = new Train(2, Station.STATION_C, Station.STATION_F);
			Train trainAC = new Train(3, Station.STATION_A, Station.STATION_C);
			trainAC.direction = Train.BACKWARD;
			Train trainAFD = new Train(4, Station.STATION_A, Station.STATION_D);
			Train trainDC = new Train(5, Station.STATION_D, Station.STATION_C);
			trainDC.direction = Train.BACKWARD;
			Train trainBCE = new Train(6, Station.STATION_B, Station.STATION_E);
			trainBCE.direction = Train.BACKWARD;
			
			stationE.addTrainToStation(trainEC);
			stationB.addTrainToStation(trainBC);
			stationC.addTrainToStation(trainCF);
			stationA.addTrainToStation(trainAC);
			stationA.addTrainToStation(trainAFD);
			stationD.addTrainToStation(trainDC);
			stationB.addTrainToStation(trainBCE);
			
			SimpleCollector collector = new SimpleCollector("RiderCollector");
			
			add(trackE_to_C1);
			add(trackE_to_C2);
			add(trackC_to_A1);
			add(trackC_to_A2);
			add(trackC_to_A3);
			add(trackA_to_F1);
			add(trackA_to_F2);
			add(trackA_to_F3);
			add(trackF_to_D1);
			add(trackF_to_D2);
			add(trackC_to_B1);
			add(trackC_to_B2);
			add(trackC_to_F1);
			add(trackC_to_F2);
			add(trackC_to_F3);
			add(trackC_to_D1);
			add(trackC_to_D2);
			
			add(stationE);
			add(stationC);
			add(stationA);
			add(stationF);
			add(stationD);
			add(stationB);
			add(rgen);
			add(collector);

			connect(rgen, stationE, "RiderGeneratorOutRouter", "RidersLoadingInport4");
			connect(rgen, stationC, "RiderGeneratorOutRouter", "RidersLoadingInport2");
			connect(rgen, stationA, "RiderGeneratorOutRouter", "RidersLoadingInport0");
			connect(rgen, stationF, "RiderGeneratorOutRouter", "RidersLoadingInport5");
			connect(rgen, stationD, "RiderGeneratorOutRouter", "RidersLoadingInport3");
			connect(rgen, stationB, "RiderGeneratorOutRouter", "RidersLoadingInport1");
			
			connect(stationE, collector, "RidersUnloadingOutport4", "RiderCollectorInPort");
			connect(stationC, collector, "RidersUnloadingOutport2", "RiderCollectorInPort");
			connect(stationA, collector, "RidersUnloadingOutport0", "RiderCollectorInPort");
			connect(stationF, collector, "RidersUnloadingOutport5", "RiderCollectorInPort");
			connect(stationD, collector, "RidersUnloadingOutport3", "RiderCollectorInPort");
			connect(stationB, collector, "RidersUnloadingOutport1", "RiderCollectorInPort");
			
			//E - C - A
			connect(stationE, trackE_to_C1, "OutportToTrack", "InPort1");
			connect(trackE_to_C1, trackE_to_C2, "OutPort1", "InPort1");
			connect(trackE_to_C2, stationC, "OutPort1", "LowerEdgeInport_4");
			connect(stationC, trackC_to_A1, "UpperEdgeOutport_0", "InPort1");
			connect(trackC_to_A1, trackC_to_A2, "OutPort1", "InPort1");
			connect(trackC_to_A2, trackC_to_A3, "OutPort1", "InPort1");
			connect(trackC_to_A3, stationA, "OutPort1", "LeftEdgeInport_1_2_4");
			
			//A - F - D
			connect(stationA, trackA_to_F1, "LowerEdgeOutport_3_5", "InPort1");
			connect(trackA_to_F1, trackA_to_F2, "OutPort1", "InPort1");
			connect(trackA_to_F2, trackA_to_F3, "OutPort1", "InPort1");
			connect(trackA_to_F3, stationF, "OutPort1", "UpperEdgeInport_0");
			connect(stationF, trackF_to_D1, "LowerEdgeOutport_3", "InPort1");
			connect(trackF_to_D1, trackF_to_D2, "OutPort1", "InPort1");
			connect(trackF_to_D2, stationD, "OutPort1", "UpperEdgeInport_0_5");
			
			//C - B
			connect(stationC, trackC_to_B1, "LeftEdgeOutport_1", "InPort1");
			connect(trackC_to_B1, trackC_to_B2, "OutPort1", "InPort1");
			connect(trackC_to_B2, stationB, "OutPort1", "InportFromTrack");
			
			//C - F 
			connect(stationC, trackC_to_F1, "RightEdgeOutport_5", "InPortCtoF");
			connect(trackC_to_F1, trackC_to_F2, "OutPortCtoF", "InPort1");
			connect(trackC_to_F2, trackC_to_F3, "OutPort1", "InPort1");
			connect(trackC_to_F3, stationF, "OutPort1", "LeftEdgeInport_1_2_4");
			
			//C - D
			connect(stationC, trackC_to_F1, "RightEdgeOutport_3", "InPortCtoD");
			connect(trackC_to_F1, trackC_to_D1, "OutPortCtoD", "InPort1");
			connect(trackC_to_D1, trackC_to_D2, "OutPort1", "InPort1");
			connect(trackC_to_D2, stationD, "OutPort1", "LeftEdgeInport_1_2_4");
			
			
			
			//backward connections
			// A - C - E
			connect(stationA, trackC_to_A3, "LeftEdgeOutport_1_2_4", "InPort2");
			connect(trackC_to_A3, trackC_to_A2, "OutPort2", "InPort2");
			connect(trackC_to_A2, trackC_to_A1, "OutPort2", "InPort2");
			connect(trackC_to_A1, stationC, "OutPort2", "UpperEdgeInport_0");
			connect(stationC, trackE_to_C2, "LowerEdgeOutport_4", "InPort2");
			connect(trackE_to_C2, trackE_to_C1, "OutPort2", "InPort2");
			connect(trackE_to_C1, stationE, "OutPort2", "InportFromTrack");
			
			//D - F - A
			connect(stationD, trackF_to_D2, "UpperEdgeOutport_0_5", "InPort2");
			connect(trackF_to_D2, trackF_to_D1, "OutPort2", "InPort2");
			connect(trackF_to_D1, stationF, "OutPort2", "LowerEdgeInport_3");
			connect(stationF, trackA_to_F3, "UpperEdgeOutport_0", "InPort2");
			connect(trackA_to_F3, trackA_to_F2, "OutPort2", "InPort2");
			connect(trackA_to_F2, trackA_to_F1, "OutPort2", "InPort2");
			connect(trackA_to_F1, stationA, "OutPort2", "LowerEdgeInport_3_5");
			
			//B - C
			connect(stationB, trackC_to_B2, "OutportToTrack", "InPort2");
			connect(trackC_to_B2, trackC_to_B1, "OutPort2", "InPort2");
			connect(trackC_to_B1, stationC, "OutPort2", "LeftEdgeInport_1");
			
			//F - C 
			connect(stationF, trackC_to_F3, "LeftEdgeOutport_1_2_4", "InPort2");
			connect(trackC_to_F3, trackC_to_F2, "OutPort2", "InPort2");
			connect(trackC_to_F2, trackC_to_F1, "OutPort2", "InPortFtoC");
			connect(trackC_to_F1, stationC, "OutPortFtoC", "RightEdgeInport_5");
			
			//D - C
			connect(stationD, trackC_to_D2, "LeftEdgeOutport_1_2_4", "InPort2");
			connect(trackC_to_D2, trackC_to_D1, "OutPort2", "InPort2");
			connect(trackC_to_D1, trackC_to_F1, "OutPort2", "InPortDtoC");
			connect(trackC_to_F1, stationC, "OutPortDtoC", "RightEdgeInport_3");

			makeModelPanel(8,8);
			
			assignModule(stationA, 0,7);
			assignModule(trackC_to_A3, 1, 6);
			assignModule(trackC_to_A2, 2, 5);
			assignModule(trackC_to_A1, 3, 4);
			assignModule(stationC, 4, 3);
			assignModule(trackE_to_C2, 5, 2);
			assignModule(trackE_to_C1, 6, 1);
			assignModule(stationE, 7, 0);
			assignModule(trackA_to_F1, 1, 7);
			assignModule(trackA_to_F2, 2, 7);
			assignModule(trackA_to_F3, 3, 7);
			assignModule(stationF, 4, 7);
			assignModule(trackF_to_D1, 5, 7);
			assignModule(trackF_to_D2, 6, 7);
			assignModule(stationD, 7, 7);
			assignModule(stationB, 1, 2);
			assignModule(trackC_to_B2, 2, 2);
			assignModule(trackC_to_B1, 3, 2);
			assignModule(trackC_to_F1, 4, 4);
			assignModule(trackC_to_F2, 4, 5);
			assignModule(trackC_to_F3, 4, 6);
			assignModule(trackC_to_D1, 5, 5);
			assignModule(trackC_to_D2, 6, 6);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}

}
