package hw5a;

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
    
    /**
     * Called when a new {@link DisplayPlugin} is registered with the framework
     *
     * @param plugin The Plug-in that has just been registered.
     */
    void onDisplayPluginRegistered(DisplayPlugin plugin);
    
    /**
     * Called when a file is already read from the given URL/local path.
     */
    void onFileReady();
    
    /** Called when data is extracted from the file*/
    void onDataExtracted();
    
    /** Called when a function type is selected*/
    void onFunctionSelected();
    
    /** Called when a visualization is selected*/
	void onChartSelected();
}
