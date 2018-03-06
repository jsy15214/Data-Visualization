package edu.cmu.cs.cs214.hw5b.framework.core;

import javax.swing.JPanel;

import edu.cmu.cs.cs214.hw5b.dataplugin.DataPlugin;

/**
 * An observer interface that listens for changes of state. The
 * {@FrameworkImpl} calls these methods to notify the {@link FrameworkGui}
 * when it should update its display.
 */
public interface StateChangeListener {
	/**
     * Called when a new {@link DataPlugin} is registered with the framework
     *
     * @param plugin The Plug-in that has just been registered.
     */
    void onDataPluginRegistered(DataPlugin plugin);
    
    
    /** Called when data is extracted from the file*/
    void onDataExtracted();
    
    /** Called when a visualization is selected to zoom out*/
	void onChartSelected(JPanel chart);
	
	/** Called when an effect is applied to data set 
	 */
	void onEffectApplied();
	
	/**
	 * Called when an error happens.
	 * @param message error message
	 */
	void onError(String message);

	/** Called when current data set is changed
	 */
	void onDataChanged();

	/** Called selected columns changed
	 * @param message message to display
	 * @param count number of columns added
	 */
	void onSelectedColsChanged(String message, int count);

	
}
