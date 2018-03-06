package hw5a;

/**
 * The data plug-in interface that plug-ins use to implement and register framework
 * with the {@link Framework}.
 */
public interface DataPlugin {
	  /**
	   * Load raw data from fileName, process raw data and return processed data.
	   * @param fileName path/fileName or URL
	   * @return processed data
	   */
      public Dataset processData(String fileName);
      
      /**
       * Called (only once) when the plug-in is first registered with the
       * framework, giving the plug-in a chance to perform any initial set-up
       * before the game has begun (if necessary).
       *
       * @param framework The {@link Framework} instance with which the plug-in
       *                  was registered.
       */
      void onRegister(Framework framework);
}
