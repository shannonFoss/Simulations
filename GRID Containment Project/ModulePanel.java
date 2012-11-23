/*
 * ModulePanel.java
 * Author: Todd Ebert
 * Date: 11/12/2008
 * 
 */

import java.awt.*;
import javax.swing.*;

/**
 * A ModulePanel is a JPanel that includes a reference to a Module that it is to provide 
 * a graphical representation for. 
 */
public class ModulePanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	protected Module module;
	protected JLabel[] label;
	
	public ModulePanel(Module module)
	{
		int length = module.infoAmount();
		//setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		setLayout(new GridLayout(length,1));
		this.module = module;
		label = new JLabel[length];
		for(int i=0; i < length; i++)
		{
			label[i] = new JLabel();
			label[i].setText(module.info(i).toString());
			add(label[i]);
		}
		
		setBackground(module.color());
	}
	
	protected ModulePanel() {}
	
	/**
 	 *  Creates the initial graphical view of the associated Module.
 	 */
	public void initializeView()
	{
		setBackground(module.color());
		for(int i=0; i < label.length; i++)
			label[i].setText(module.info(i).toString());
	}
	
	public boolean needsUpdating()
	{
		for(int i=0; i < label.length; i++)
			if(!label[i].getText().equals(module.info(i).toString())) return true;
		return false;
	}
	
	/**
 	 *  Udpates the current graphical view of the associated Module. 
 	 */
	public void updateView()
	{
		setBackground(module.color());
		for(int i=0; i < label.length; i++)
			label[i].setText(module.info(i).toString());
	}
	
	public boolean isEmpty() {return false;}
}
