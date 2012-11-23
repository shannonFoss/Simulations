//JavaDEVSGUI.java
//Author: Todd Ebert
//Date: 11-13-2008
/*
 * Descriptions: JAVADEVSGUI is a JApplet that can be used to view a discrete event simulation 
 * using the JavaDEVS classes. Within the applet is a SimulationPanel which enables one to 
 * graphically view a simulation along a defined time scale. The ModelPanel object uses a 
 * GridLayout, and each Module of the SystemModel that one desires to view is mapped to one of 
 * the grid areas. The JavaDEVSGUI applet has the following buttons/capabilities.
 * 1. Load Model: load an existing SystemModel so that it may be simulated. Button is enabled
 *    whenever a there is no simulation in progress.
 * 2. Start/Restart button: enables one to start a new simulation or restart an existing 
 *    simulation. 
 * 3. Pause/Continue button: enables one to pause and later continue the simulation. Button 
 *    is enabled whenever a simulation is in progress.
 * 4. Stop button: stops the current simulation. Enabled whenever there is a simulation in 
 *    progress.
 * 5. View Port Stats button: view the available statistics for the current system under view.
 *     The button is enabled whenever there is no current simulation running, or if a 
 *	   simulation is paused.
 * 6. Set time units button: the available time units for the 
 *    simulation are nanoseconds, milliseconds,
 *    microseconds, seconds, minutes, hours, days, years. Enabled only when there is no
 *    simulation in progress (paused or unpaused).
 * 7. Set closing time button: sets the last time for which a system activity may begin. 
 *		Enabled when either a simulation is paused, or there is no simulation in progress.
 * 8. Set time scale: Allows one to input a positive number x so that the following will happen.
 *    Suppose the next event e is set to occur in t time units from the current time, where
 *    the unit of time measurement is equal to one of the units available in the 
 *    "Time Units" button. Then the event will be witnessed in real time
 *    t/x units from the current time. For example, if x = 5, then x will be witnessed in
 *    real time t/5 units. So if t=10 sec, then the event will be witnessed in 10/5 = 2 
 *    seconds. Thus, x represents a slow-down/speed-up factor. Enabled only when the simulation
 *    is paused, or there is no simulation in progress. 
 *    
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.lang.SecurityException;
import java.text.DecimalFormat;

public class JavaDEVSGUI
{
	private static final int NUM_BUTTONS = 8;
	public static final int LOAD_BUTTON = 0;
	public static final int START_BUTTON = 1;
	public static final int PAUSE_BUTTON = 2;
	public static final int STOP_BUTTON = 3;
	public static final int STATS_BUTTON = 6;
	public static final int TIME_BUTTON = 4;
	public static final int QUIT_BUTTON = 7;
	public static final int PARAM_BUTTON = 5;
	
	private static final long serialVersionUID = 1L;
	private int WIDTH=1000;
	private int HEIGHT=600;
	private int MENU_HEIGHT = 120;
	private int DISPLAY_HEIGHT = 480;
	private JFrame frame;
	private JPanel framePanel;
	private ModelPanel modelPanel;
	private JPanel viewPanel;
	private JPanel northPanel;
	private JLabel timeLabel;
	private JLabel eventsLabel;
	private JPanel infoPanel;
	private CardLayout cardLayout;
	private SystemModel systemModel; //Model that is to be simulated
	private JButton[] button;
	private Timer timer;
	private DecimalFormat fmt;
	private double closingTime = 0.0;
	
	protected boolean inProgress;  //true if there is a simulation in progress
	protected boolean isPaused;
	
	public JavaDEVSGUI()
	{
		fmt = new DecimalFormat("0.##");
		frame = new JFrame("JavaDEVS");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH,HEIGHT);
		frame.setVisible(true);
		
		framePanel = new JPanel();
		framePanel.setLayout(new BorderLayout());
		frame.getContentPane().add(framePanel);
		
		northPanel = new JPanel();
		northPanel.setSize(WIDTH,MENU_HEIGHT);
		northPanel.setVisible(true);
		northPanel.setLayout(new FlowLayout());
		
		timer = new Timer(1,new DiscreteEventListener());
		timer.stop();
		
		inProgress = false;
		isPaused = false;
		
		//create buttons
		JButton bLoad = new JButton("Load Model"); 
	    JButton bStart = new JButton("Start");
		JButton bPause = new JButton("Pause");
		JButton bStop = new JButton("Stop");
		JButton bStats = new JButton("Statistics");
		JButton bTime = new JButton("Time");
		JButton bParam = new JButton("Model Params");
		JButton bQuit = new JButton("Quit");
		
		bStart.setEnabled(false);
		bPause.setEnabled(false);
		bStop.setEnabled(false);
		bStats.setEnabled(false);
		bParam.setEnabled(false);
		
		//add buttons to the button array
		button = new JButton[JavaDEVSGUI.NUM_BUTTONS];
		button[JavaDEVSGUI.LOAD_BUTTON] = bLoad;
		button[JavaDEVSGUI.START_BUTTON] = bStart;
		button[JavaDEVSGUI.PAUSE_BUTTON] = bPause;
		button[JavaDEVSGUI.STOP_BUTTON] = bStop;
		button[JavaDEVSGUI.STATS_BUTTON] = bStats;
		button[JavaDEVSGUI.TIME_BUTTON] = bTime;
		button[JavaDEVSGUI.QUIT_BUTTON] = bQuit;
		button[JavaDEVSGUI.PARAM_BUTTON] = bParam;
		
		//add button listeners
		bLoad.addActionListener(new LoadButtonListener());
		bStart.addActionListener(new StartButtonListener());
		bPause.addActionListener(new PauseButtonListener());
		bStop.addActionListener(new StopButtonListener());
		bStats.addActionListener(new StatsButtonListener());
		bTime.addActionListener(new TimeButtonListener());
		bParam.addActionListener(new ParamButtonListener());
		bQuit.addActionListener(new QuitButtonListener());
		
		//add buttons to panel
		northPanel.add(bLoad);
		northPanel.add(bStart);
		northPanel.add(bPause);
		northPanel.add(bStop);
		northPanel.add(bTime);
		northPanel.add(bParam);
		northPanel.add(bStats);
		northPanel.add(bQuit);
		
		infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel,BoxLayout.Y_AXIS));
		timeLabel = new JLabel();
		eventsLabel = new JLabel();
		infoPanel.add(timeLabel);
		infoPanel.add(eventsLabel);
		northPanel.add(infoPanel);
		timeLabel.setText("System time: " + fmt.format(Module.clock.lastEventTime()));
		eventsLabel.setText("Events processed: " + Module.fel.numberOfEvents());
		
		framePanel.add(northPanel,BorderLayout.NORTH);
		
		viewPanel = new JPanel(new BorderLayout());
		viewPanel.setSize(WIDTH,DISPLAY_HEIGHT);
		viewPanel.setVisible(true);
		viewPanel.setBackground(Color.BLACK);
		
		framePanel.add(viewPanel, BorderLayout.CENTER);
	}
	
	/**
	* Displays the application frame.
	*/
	public void display()
	{
		frame.pack();
		frame.setVisible(true);
	}
	
	private class LoadButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			SystemModel sm;
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter filter = 
				new FileNameExtensionFilter("select system model", "java");
    		chooser.setFileFilter(filter);
    		int returnVal = chooser.showOpenDialog(frame);
			File file = chooser.getSelectedFile();
			if(file == null) 
			{ 
				userMessage("No model was loaded.");
				return;
			}
			String filename = file.getName();
			filename = processFileName(filename);
			sm = SystemModel.getSystemModel(filename);
			if(sm == null) 
			{
				userMessage("No model was loaded.");
				return;
			}
			else 
			{
				userMessage("Model " + filename + " loaded successfully.");
				if(systemModel != null) 
				{
					viewPanel.remove(systemModel.modelPanel());
					viewPanel.validate();
				}
				systemModel = sm;
				systemModel.modelPanel().initializeView(WIDTH, DISPLAY_HEIGHT);

				viewPanel.add(systemModel.modelPanel(),BorderLayout.CENTER);
				viewPanel.validate();
				viewPanel.repaint();
				button[JavaDEVSGUI.START_BUTTON].setEnabled(true);
				button[JavaDEVSGUI.START_BUTTON].setText("Start");
				button[JavaDEVSGUI.PAUSE_BUTTON].setEnabled(false);
				button[JavaDEVSGUI.PAUSE_BUTTON].setText("Pause");
				button[JavaDEVSGUI.STOP_BUTTON].setEnabled(false);
				button[JavaDEVSGUI.STATS_BUTTON].setEnabled(false);
				button[JavaDEVSGUI.TIME_BUTTON].setEnabled(true);
				button[JavaDEVSGUI.PARAM_BUTTON].setEnabled(true);
				northPanel.repaint();
			}
		}
		
		private String processFileName(String s)
		{
			int pIndex = s.lastIndexOf('.'); //period index
			int sIndex = -1; //slash index
			for(int i = 0; i < pIndex; i++)
				if(!Character.isLetterOrDigit(s.charAt(i)) && s.charAt(i) != '_')
					sIndex = i;
			return s.substring(sIndex+1,pIndex);
		}
	}
	
	private void userMessage(String message)
	{
		JOptionPane.showMessageDialog(frame, message);
	}
	
	private class StartButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			if(systemModel == null) userMessage("First load a system model.");
			else
			{
				button[JavaDEVSGUI.LOAD_BUTTON].setEnabled(false);
				button[JavaDEVSGUI.START_BUTTON].setText("Restart");
				button[JavaDEVSGUI.PAUSE_BUTTON].setEnabled(true);
				button[JavaDEVSGUI.PAUSE_BUTTON].setText("Pause");
				button[JavaDEVSGUI.STOP_BUTTON].setEnabled(true);
				button[JavaDEVSGUI.STATS_BUTTON].setEnabled(false);
				button[JavaDEVSGUI.TIME_BUTTON].setEnabled(false);
				button[JavaDEVSGUI.PARAM_BUTTON].setEnabled(false);
				northPanel.repaint();
				startSimulation();
			}
		}
	}
	
	protected void startSimulation()
	{
		inProgress = true;
		isPaused=false;
		
		Module.fel.initialize();
		Module.clock.initialize(closingTime);
		systemModel.top().initialize();
		timer.setDelay(1);
		timer.start();
	}
	
	protected void endSimulation()
	{
		inProgress = false;
		isPaused=false;
		
		systemModel.top().finalize(Module.clock.lastEventTime());
		timer.stop();
	}
	
	protected void pauseSimulation()
	{
		timer.stop();
		systemModel.top().setTime(Module.clock.lastEventTime());
		Module.clock.resetPause();
	}
	
	protected void continueSimulation()
	{
		timer.setDelay(1);
		timer.start();
	}
	
	private class StopButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			if(inProgress)
			{
				button[JavaDEVSGUI.LOAD_BUTTON].setEnabled(true);
				button[JavaDEVSGUI.START_BUTTON].setText("Start");
				button[JavaDEVSGUI.PAUSE_BUTTON].setText("Pause");
				button[JavaDEVSGUI.PAUSE_BUTTON].setEnabled(false);
				button[JavaDEVSGUI.STOP_BUTTON].setEnabled(false);
				button[JavaDEVSGUI.STATS_BUTTON].setEnabled(true);
				button[JavaDEVSGUI.TIME_BUTTON].setEnabled(true);
				button[JavaDEVSGUI.PARAM_BUTTON].setEnabled(true);
				northPanel.repaint();
				double time = Module.clock.lastEventTime();
				double eNum = Module.fel.numberOfEvents();
				String units = Module.clock.unitsLabel();
				endSimulation();
				userMessage("Simulation was stopped at time " + fmt.format(time) + " " + units + 
					".\nEvents processed: " + eNum);
			}
		}
	}

	private class PauseButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			if(!isPaused)
			{
				isPaused = true;
				button[JavaDEVSGUI.PAUSE_BUTTON].setText("Continue");
				button[JavaDEVSGUI.STATS_BUTTON].setEnabled(true);
				button[JavaDEVSGUI.TIME_BUTTON].setEnabled(true);
				button[JavaDEVSGUI.PARAM_BUTTON].setEnabled(true);
				northPanel.repaint();
				pauseSimulation();
			}
			else
			{
				isPaused = false;
				button[JavaDEVSGUI.PAUSE_BUTTON].setText("Pause");
				button[JavaDEVSGUI.STATS_BUTTON].setEnabled(false);
				button[JavaDEVSGUI.TIME_BUTTON].setEnabled(false);
				button[JavaDEVSGUI.PARAM_BUTTON].setEnabled(false);
				northPanel.repaint();
				continueSimulation();
			}
		}
	}
	
	private class StatsButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event) 
		{
			FileWriter fw=null;
			String filename;
			filename = JOptionPane.showInputDialog("Enter file name to write to");
			try
			{
				fw = new FileWriter(filename);
			}
			catch(IOException e) {}
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter outFile = new PrintWriter(bw);
			
			Lst modList = systemModel.top().descendants();
			if(modList.size == 0)
			{
				userMessage("There are no submodules in this model.\nNo output written.");
				return;
			}
			
			Lst ports;
			Module m;
			InPort p;
			outFile.println("System model " + systemModel.top().name() + " in-port statistics");
			outFile.println();
			
			for(modList.reset(); modList.hasNext(); modList.advance())
			{
				m = (Module) modList.access();
				ports = m.inPorts();
				if(ports.size == 0) 
				{
					outFile.println("Module " + m.name() + ": no input ports");
					outFile.println();
				}
				for(ports.reset();ports.hasNext();ports.advance())
				{
					outFile.println("Module " + m.name());
					p = (InPort) ports.access();
					outFile.print(p.portStatsReport());
					outFile.println();
				}
				outFile.println();
				outFile.println();
			}
			
			outFile.close();
			
			userMessage("Port statistics successfully written to file " + filename);
		}
	}
	
	private class TimeButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			String numStr;
			double scaleFactor;
			double delay;
			//int units;
			int size,i;
			
			SystemClock clock = Module.clock;
			
			Object[] option = new Object[4];
			option[0] = new String("Set closing time");
			option[1] = new String("Set delay");
			option[2] = new String("Set time-scale units");
			option[3] = new String("Set time-scale factor");
				
			Object obj = JOptionPane.showInputDialog(frame, 
				"Choose:", "Options", JOptionPane.QUESTION_MESSAGE, null, option, null);
				
			if(obj != null) //locate the selected subsystem 
			{
				String optName = (String) obj;
				if(optName.equals("Set closing time"))
				{
					numStr = JOptionPane.showInputDialog("Enter closing time");
					if(numStr != null)
					{
						closingTime = Double.parseDouble(numStr);
						clock.setClosingTime(closingTime);
					}
				}
				else if(optName.equals("Set time-scale factor"))
				{
					numStr = JOptionPane.showInputDialog("Enter scale factor");
					if(numStr != null)
					{
						scaleFactor = Double.parseDouble(numStr);
						clock.setScaleFactor(scaleFactor);
					}
				}
				else if(optName.equals("Set delay"))
				{
					numStr = JOptionPane.showInputDialog("Enter delay (in seconds)");
					if(numStr != null)
					{
						delay = Double.parseDouble(numStr);
						clock.setDelay(delay);
					}
				}
				else //if(optName.equals("Set time scale units"))
				{
					String unitName[] = clock.namesOfUnits();
					size = unitName.length;
					option = new Object[size];
					for(i=0; i < size; i++) 
						option[i] = unitName[i];
					
					obj = JOptionPane.showInputDialog(frame, 
						"Choose time units:", "Time Units", JOptionPane.QUESTION_MESSAGE, 
						null, option, null);
					if(obj != null) 
					{
						optName = (String) obj;
						clock.setUnits(optName);
					}
				}
					
			}
		}
	}
	
	private class ParamButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			String paramStr;
			String[] params = systemModel.modelParameters();
			if(params == null)
			{
				userMessage("No parameters have been set for this model.");
				return;
			}
			
			Object[] option = new Object[params.length];
			for(int i=0; i < params.length; i++)
				option[i] = params[i];
				
			Object obj = JOptionPane.showInputDialog(frame, 
				"Choose parameter to set:", "Model Parameters", 
				JOptionPane.QUESTION_MESSAGE, null, option, null);
				
			if(obj != null) 
			{
				String s = (String) obj;
				paramStr = JOptionPane.showInputDialog("Enter value for parameter " + s);
				if(paramStr != null)
					systemModel.setParameter(s,paramStr);
			}
			
			return;
		}
	}
	
	private class QuitButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			frame.setVisible(false);
			try 
			{
				System.exit(0);
			}
			catch(SecurityException e){}
		}
	}
	
	private class DiscreteEventListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			
			if(Module.fel.hasMoreEvents())
			{
				double eventTime =  Module.processNextEvent();
				Module.clock.setLastEventTime(eventTime);
				if(systemModel.modelPanel().needsUpdating())
				{
					Module.clock.pauseUntilNextEvent(eventTime);
					systemModel.modelPanel().updateView();
					viewPanel.repaint();
					timeLabel.setText("System time: " + fmt.format(Module.clock.lastEventTime()));
					eventsLabel.setText("Events processed: " + Module.fel.numberOfEvents());
					infoPanel.repaint();
				}	
			}
			else
			{
				String s = new String("");
				s += "The simulation ended successfully.\n";
				s += "Last event: " + fmt.format(Module.clock.lastEventTime()) + " ";
				s += Module.clock.unitsLabel() + "\n";
				s += "Events processed: " + Module.fel.numberOfEvents() + "\n";
				//remove
				s += "Number of riders in system: "+Utility.numOfRider +"\n";
				s += "Avg time spent in system: "+Utility.avgTimeInSystem()+"\n";
				//*****
				userMessage(s);
				button[JavaDEVSGUI.LOAD_BUTTON].setEnabled(true);
				button[JavaDEVSGUI.START_BUTTON].setText("Start");
				button[JavaDEVSGUI.PAUSE_BUTTON].setText("Pause");
				button[JavaDEVSGUI.PAUSE_BUTTON].setEnabled(false);
				button[JavaDEVSGUI.STOP_BUTTON].setEnabled(false);
				button[JavaDEVSGUI.STATS_BUTTON].setEnabled(true);
				button[JavaDEVSGUI.TIME_BUTTON].setEnabled(true);
				button[JavaDEVSGUI.PARAM_BUTTON].setEnabled(true);
				northPanel.repaint();
				endSimulation(); 
			}
		}
	}
}
