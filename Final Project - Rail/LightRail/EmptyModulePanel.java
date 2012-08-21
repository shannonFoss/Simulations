/*
 * EmptyModulePanel.java
 * Author: Todd Ebert
 * Date: 11/12/2008
 * 
 */

import java.awt.Color;

/**
 * An EmptyModulePanel is one that lacks an associated Module.
 */
public class EmptyModulePanel extends ModulePanel
{
	private static final long serialVersionUID = 1L;
	
	public EmptyModulePanel()
	{
		super();
		setBackground(Color.BLACK);
	}
	
	/**
 	 *  Creates the initial graphical view of the associated Module.
 	 */
	public void initializeView()
	{
		setBackground(Color.BLACK);
	}
	
	public boolean needsUpdating()
	{
		return false;
	}
	
	/**
 	 *  EmptyModulePanels are not updated. 
 	 */
	public void updateView()
	{
	
	}
	
	public boolean isEmpty() {return true;}
}
