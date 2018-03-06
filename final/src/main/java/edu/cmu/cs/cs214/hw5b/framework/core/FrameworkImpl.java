package edu.cmu.cs.cs214.hw5b.framework.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import edu.cmu.cs.cs214.hw5b.dataplugin.DataPlugin;
import edu.cmu.cs.cs214.hw5b.displayplugin.DisplayPlugin;

import edu.cmu.cs.cs214.hw5b.framework.core.DataSet;

/**
 *  The framework core implementation.
 */
public class FrameworkImpl implements Framework {

	
	/** loaded data from the source file*/
	private DataSet currData;
	
	//all originally data sets
    private List<DataSet> originalData;
    private int count;
    
    //all current modified data sets
    private List<DataSet> modifiedData;
	
	/** selected data plug-in*/
    private DataPlugin datp;
    
    /** current list of display plug-in*/
    private List<DisplayPlugin> visps;
    
    /** current list of all display plug-ins*/
    private List<DisplayPlugin> allVisps;
    
    
    /** The listeners who will be notified of changes in the state */
    private StateChangeListener stateChangeListener;
    
    /**
     * Selected datas to display
     */
    private Map<DataSet,List<Integer>> selectedCols = new HashMap<>();
    private int selectedColsCount = 0;
    private String selectedString = "";
    
    /** Constructor*/
    public FrameworkImpl(){
    	originalData = new ArrayList<>();
    	count = 0;
    	modifiedData = new ArrayList<>();
    	visps = new ArrayList<>();
    	allVisps = new ArrayList<>();
    	datp = null;
    }
    
    /**
     * add a selected col
     * @param d data set that contains the col
     * @colIdx column index
     */
    public void addSelectedCols(DataSet d, int colIdx){
    	if (selectedCols.containsKey(d)) {
    		selectedCols.get(d).add(colIdx);
    		System.out.println(selectedCols.get(d));
    	}
    	else {
    		List<Integer> list = new ArrayList<Integer>();
    		list.add(colIdx);
    		selectedCols.put(d,list);
    	}
    	selectedColsCount++;
    	if (selectedString.isEmpty()) {
    		selectedString = d.getName() + ": " + d.getTitleRow().get(colIdx);
    	}
    	else {
    		selectedString = selectedString + ","+ d.getName() + ": " + d.getTitleRow().get(colIdx);
    	}
    	notifySelectedColsChanged();
    }
    

	/**
     * Clear all selection related fields
     */
    public void clearSelection() {
    	selectedCols = new HashMap<>();
    	selectedString = "";
        selectedColsCount = 0;
        notifySelectedColsChanged();
    }
    
    /**
     * Create a new data sets based on the cols selected
     * @return new data set
     */
    public DataSet getNewDataSet() {
    	System.out.println(selectedCols.get(originalData.get(0))+"before");
    	return DataSet.innerJoinDataSets(selectedCols);
    }
    
    /**
     * Get list of display plugins
     * @return list of display plugins
     */
    public List<DisplayPlugin> getDisplayPlugins() {
    	return allVisps;
    }
    
    /**
     * Get data set
     * @return data set
     */
    public DataSet getCurrData() {
    	return currData;
    }
    
    /**
     * Set curr data set
     * @param data set
     */
    public void setCurrData(DataSet data) {
    	if (!originalData.contains(data)) {
    		throw new IllegalArgumentException("Don't have this data set");
    	}
    	currData = data;
    	notifyDataSetChanged();
    }
    
    /**
     * Get all data sets added to framework
     */
    public List<DataSet> getAllData() {
    	return originalData;
    }
    
    /**
     * Get all modified data sets
     */
    public List<DataSet> getAllModifiedData() {
    	return modifiedData;
    }


	/**
     * Sets the framework's {@link StateChangeListener} listener to be notified
     * about changes made to the game's state.
     * @param listener
	 *            The listener to be notified of state change events.
     */
	public void setStateChangeListener(StateChangeListener listener) {
		stateChangeListener = listener;
	}
	
    
    /**
	 * Set this.datp to datp.
	 * 
	 * @param datp
	 *            input data plug-in
	 */
    public void selectDataPlugin(DataPlugin datp){
    	this.datp = datp;
    }
    
    /**
	 * activate data plugin to read data.
	 * 
	 * @param filePath path of the source data
	 */
	public void loadDataPlugin(String filePath){
		if (filePath == "") {
			throw new IllegalArgumentException("No path");
		}
		String name = "DataSet " + Integer.toString(originalData.size()+1);
		DataSet newData;
		try {
			newData = datp.processData(filePath, name);
		} catch(Exception exp) {
			throw new IllegalArgumentException("Illegal path");
		}
		if (newData != null) {
			originalData.add(newData);
			currData = newData;
		}
		notifyDataExtracted();
	}
	
	/**
	 * Set current display plug-in to visp.
	 * 
	 * @param visp
	 *            input display plug-in
	 */
	public void loadDisplayPlugin(DisplayPlugin visp) {
		if (!visp.isAvailable(currData)) {
			throw new IllegalArgumentException("cannot visualize.");
		}
		JPanel newDisplay = visp.display(currData);
		notifyChartSelected(newDisplay);
	}
	

	/**
	 * Register data plug-in to framework.
	 */
	public void registerDataPlugin(DataPlugin datp){
		datp.onRegister(this);
        notifyDataPluginRegistered(datp);
	}

	/**
	 * Register display plug-in to framework.
	 */
	public void registerDisplayPlugin(DisplayPlugin visp){
		visp.onRegister(this);
		allVisps.add(visp);
	}
	
	public void sort(List<Integer> cols, boolean order) {
		DataSet modified = currData.sortRows(cols, order);
		if (modifiedData.contains(modified)) {
			modifiedData.remove(modified);
		}
		modifiedData.add(modified);
		currData = modified;
		notifyEffectApplied();
	}
	
	public void filterString(Integer colIndex, String start, String end, List<String> equals) {
		DataSet modified;
		if (equals.isEmpty()){
			modified = currData.filterRowsByString(colIndex, start, end);
		}
		else {
			modified = currData.filterRowsByString(colIndex, equals);
		}
		if (modifiedData.contains(modified)) {
			modifiedData.remove(modified);
		}
		modifiedData.add(modified);
		currData = modified;
		notifyEffectApplied();
	}
	
	public void filterNumber(Integer colIndex, Double lo, Double hi, List<Double> equals) {
		DataSet modified;
		if (equals.isEmpty()){
			modified = currData.filterRowsByDouble(colIndex, lo, hi);
		}
		else {
			modified = currData.filterRowsByDouble(colIndex, equals);
		}		
		if (modifiedData.contains(modified)) {
			modifiedData.remove(modified);
		}
		modifiedData.add(modified);
		currData = modified;
		notifyEffectApplied();
	}
	
	
	
	//-----------------------------------gui connection------------------------------------//
	private void notifyDataExtracted() {
		stateChangeListener.onDataExtracted();
	}
    
	private void notifyChartSelected(JPanel display) {
		stateChangeListener.onChartSelected(display);
	}
	
	private void notifyDataPluginRegistered(DataPlugin plugin) {
        stateChangeListener.onDataPluginRegistered(plugin);
    }
	
	private void notifyEffectApplied(){
		stateChangeListener.onEffectApplied();
	}

    
    private void notifyDataSetChanged() {
		stateChangeListener.onDataChanged();
	}
	
	private void notifySelectedColsChanged() {
		stateChangeListener.onSelectedColsChanged(selectedString,selectedColsCount);
	}
    
}
