package edu.cmu.cs.cs214.hw5b.displayplugin;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.util.Rotation;

import edu.cmu.cs.cs214.hw5b.framework.core.DataEntry;
import edu.cmu.cs.cs214.hw5b.framework.core.DataSet;
import edu.cmu.cs.cs214.hw5b.framework.core.Framework;

public final class PieChart extends ApplicationFrame implements DisplayPlugin {
	
	public PieChart() {
		super("123");
		
	}

	@Override
	/**
	 * Only the following data set is valid: 2 columns of data;
	 * same number of values in both columns; values in 0st column 
	 * can be any type while values in 1st column can only be numeric 
	 * types.
	 */
	public boolean isAvailable(DataSet data) {
		if(data.getNumberOfCols() != 2){
			return false;
		}
		if((data.getCol(0).size() < 1) || (data.getCol(1).size() < 1) ||
				(data.getCol(0).size() != data.getCol(1).size())){
			return false;
		}
	    DataEntry myE = data.getCol(1).get(0);
	    if(myE.isDouble() || myE.isInteger()){
	    	return true;
	    }
		return false;
	}


	@Override
	public void onRegister(Framework framework) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public JPanel display(DataSet data) {
		JPanel chartPanel = createPanel(data);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
		return chartPanel;
	}
    
	 /**
	 * Process the input data set to be compatible with pie chart. 
     * @return A sample data set.
     */
    private static PieDataset createDataset(DataSet data) {
    	DefaultPieDataset result = new DefaultPieDataset();
    	
    	List<String> category = new ArrayList<>();
    	
    	System.out.println(data.getNumberOfCols()+"pie");
    	List<DataEntry> myE_0 = data.getCol(0);
    	List<DataEntry> myE_1 = data.getCol(1);
    	
    	for(DataEntry myE: myE_0){
    		category.add(myE.toString());
    	}
    	
    	/** integer values*/
    	if(myE_1.get(0).isInteger()){
    		List<Integer> intv = new ArrayList<>();
    		for(DataEntry myE: myE_1){
        		intv.add(myE.convertToInteger());
        	}
    		for(int i = 0; i < category.size(); i ++){
    	        result.setValue(category.get(i), intv.get(i));
    	    }
    	}
    	/** double values*/
    	else{
    		List<Double> doublev = new ArrayList<>();
    		for(DataEntry myE: myE_0){
        		doublev.add(myE.convertToDouble());
        	}
    		for(int i = 0; i < category.size(); i ++){
            	result.setValue(category.get(i), doublev.get(i));
            }
    	}
        return result;
    }
    
    private JFreeChart createChart(PieDataset dataset){
    	JFreeChart chart = ChartFactory.createPieChart3D(
	            "Weather Distribution Pie Chart",  // chart title
	            dataset,                // dataset
	            true,                   // include legend
	            false,
	            false
	        );
    	 PiePlot3D plot = (PiePlot3D) chart.getPlot();
         plot.setStartAngle(270);
         plot.setDirection(Rotation.ANTICLOCKWISE);
         plot.setForegroundAlpha(0.60f);
         return chart;
    }
    
	private JPanel createPanel(DataSet data){
		JFreeChart chart = createChart(createDataset(data));
		Rotator rotator = new Rotator((PiePlot3D) chart.getPlot());
		rotator.start();
	    return new ChartPanel(chart);
	}

	@Override
	public String getName() {
		return "Pie Chart";
	}

	/**
	 * The rotator. Add reference: 
	 */
	class Rotator extends Timer implements ActionListener {

	    /** The plot. */
	    private PiePlot3D plot;

	    /** The angle. */
	    private int angle = 270;

	    /**
	     * Constructor.
	     *
	     * @param plot  the plot.
	     */
	    Rotator(PiePlot3D plot) {
	        super(100, null);
	        this.plot = plot;
	        addActionListener(this);
	    }

	    /**
	     * Modifies the starting angle.
	     *
	     * @param event  the action event.
	     */
	    public void actionPerformed(ActionEvent event) {
	        this.plot.setStartAngle(this.angle);
	        this.angle = this.angle + 1;
	        if (this.angle == 360) {
	            this.angle = 0;
	        }
	    }

	}
}
