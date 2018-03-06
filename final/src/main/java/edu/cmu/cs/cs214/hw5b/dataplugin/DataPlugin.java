package edu.cmu.cs.cs214.hw5b.dataplugin;

import java.io.IOException;

import edu.cmu.cs.cs214.hw5b.framework.core.DataSet;
import edu.cmu.cs.cs214.hw5b.framework.core.Framework;

/**
 * The data plug-in interface that plug-ins use to implement and register
 * framework with the {@link Framework}.
 */
public interface DataPlugin {
	
	/**
	 * Load raw data from fileName, process raw data and return processed
	 * data.
	 * 
	 * @param fileName
	 *                path/fileName or URL
	 * @param name
	 *                data set name
	 * @return processed data
	 * @throws IOException
	 */
	public DataSet processData(String fileName, String name) throws IOException;

	/**
	 * Called (only once) when the plug-in is first registered with the
	 * framework, giving the plug-in a chance to perform any initial set-up
	 * before the game has begun (if necessary).
	 *
	 * @param framework
	 *                The {@link Framework} instance with which the plug-in
	 *                was registered.
	 */
	void onRegister(Framework framework);
	
	/* -------------- getters --------------- */
	
	public String getDescription();

	public String getName();
}