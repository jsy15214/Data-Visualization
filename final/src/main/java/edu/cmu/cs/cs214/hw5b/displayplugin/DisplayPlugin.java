package edu.cmu.cs.cs214.hw5b.displayplugin;

import javax.swing.JPanel;

import edu.cmu.cs.cs214.hw5b.framework.core.DataSet;
import edu.cmu.cs.cs214.hw5b.framework.core.Framework;

/**
 * The data plug-in interface that plug-ins use to implement and register framework
 * with the {@link Framework}.
 */
public interface DisplayPlugin {
	/**
	 * Return whether the data can be displayed in this visual format.
	 * @param data the data that needs to be displayed
	 * @return whether the data can be displayed in this visual format.
	 */
	public boolean isAvailable(DataSet data);

	/**
	 * Create a JPanel to display data visualization
	 * 
	 * @param data the data that needs to be displayed
	 * @return the painted panel.
	 */
	public JPanel display(DataSet data);

	/**
	 * Called (only once) when the plug-in is first registered with the
	 * framework, giving the plug-in a chance to perform any initial set-up
	 * before the game has begun (if necessary).
	 *
	 * @param framework
	 *            The {@link Framework} instance with which the plug-in was
	 *            registered.
	 */
	void onRegister(Framework framework);
	
	/**
	 * Return name of the plug-in
	 * 
	 * @return name
	 */
	String getName();
}
