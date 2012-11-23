/*
 * ModelPanel.java
 * Author: Todd Ebert
 * Date: 11/12/2008
 * 
 */

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * A ModelPanel is a JPanel that uses a GridLayout to display a graphical view of a given SystemModel.
 */
public class ModelPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	//Pixel spacing between adjancent Modules
	private static final int VERTICAL_GAP = 10;
	private static final int HORIZONTAL_GAP = 10;
	//Width of the matte border that borders the GUI model view
	private static final int MATTE_BORDER = 5;
	protected int rows;
	protected int columns;
	protected ModulePanel[][] panelArray;
	
	public ModelPanel(String systemName, int rows, int columns)
	{
		GridLayout g = new GridLayout(rows,columns);
		g.setVgap(VERTICAL_GAP);
		g.setHgap(VERTICAL_GAP);
		setLayout(g);
		
		Border matte = 
			BorderFactory.createMatteBorder(MATTE_BORDER,MATTE_BORDER,
				MATTE_BORDER,MATTE_BORDER,Color.WHITE);
		TitledBorder tb = BorderFactory.createTitledBorder("System Model " + systemName);
		tb.setTitleJustification(TitledBorder.CENTER);
		setBorder(BorderFactory.createCompoundBorder(matte,tb));
		
		this.rows = rows;
		this.columns = columns;
		panelArray = new ModulePanel[rows][columns];
	}
	
	public void assignModule(Module m,int x,int y) {panelArray[x][y] = new ModulePanel(m);}
	
	/**
 	 *  Creates the initial graphical view of this ModelPanel.
 	 */
	public void initializeView(int width, int height)
	{
		int i,j;
		int boxWidth = width/columns;
		int boxHeight = height/rows;
		setSize(width,height);
		
		for(i=0; i < rows; i++)
		{
			for(j=0; j < columns; j++)
			{
				if(panelArray[i][j] == null) 
				{
					panelArray[i][j] = new EmptyModulePanel();
					panelArray[i][j].setSize(boxWidth,boxHeight);
					add(panelArray[i][j]);
				}
				else 
				{
					panelArray[i][j].setSize(boxWidth,boxHeight);
					panelArray[i][j].initializeView();
					add(panelArray[i][j]);
				}
			}
		}
	}
	
	public boolean needsUpdating()
	{
		int i,j;
		
		for(i=0; i < rows; i++)
			for(j=0; j < columns; j++)
				if(panelArray[i][j].needsUpdating()) return true;
		
		return false;
	}
	
	/**
 	 *  Udpates the current graphical view of the associated Module. 
 	 */
	public void updateView()
	{
		int i,j;
		
		for(i=0; i < rows; i++)
			for(j=0; j < columns; j++)
				panelArray[i][j].updateView();
	}
}
