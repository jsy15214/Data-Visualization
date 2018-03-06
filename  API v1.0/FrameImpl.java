package hw5a;

import java.util.ArrayList;
import java.util.List;


/**
 *  The framework core implementation.
 */
public class FrameImpl implements Framework {
	/** path or URL of the data file*/
	String filePath;
	
	/** loaded data from the source file*/
	private Dataset processedData;
	
	/** current data plug-in*/
    private DataPlugin datp;
    
    /** current list of display plug-in*/
    private List<DisplayPlugin> visps;
    
    /** client defined comparator for sorting*/
    private Comparator cmp;
    
    /** client defined filter for fileter*/
    private Filter flt;
    
    /** The listeners who will be notified of changes in the state */
    private final List<StateChangeListener> stateChangeListeners = new ArrayList<>();
    
    /** Constructor*/
    public FrameImpl(){
   
    }
    
    /**
	 * Register a state change listener to be notified of changing events.
	 *
	 * @param listener
	 *            The listener to be notified of state change events.
	 */
	public void addStateChangeListener(final StateChangeListener listener) {
		stateChangeListeners.add(listener);
	}
    
    @Override
	public void setPath(String inputPath){
    	
    }
    
	@Override
	public void loadDataPlugin(DataPlugin datp){
		
	}
	
	@Override
	public void loadVisualPlugin(DisplayPlugin visp){
		
	}
	
	@Override
	public void createVisualThread(){
		
	}
	
	@Override
	public void registerDataPlugin(DataPlugin datp){
		
	}

	@Override
	public void registerVisualPlugin(DisplayPlugin visp){
		
	}
	
	@Override
	public void sort(List<Integer> cols, Comparator cmp){
		
	}
	
	@Override
	public void filter(List<Integer> cols, Filter flt){
		
	}
	
	
    
    
}
