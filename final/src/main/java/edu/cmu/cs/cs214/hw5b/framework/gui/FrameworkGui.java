package edu.cmu.cs.cs214.hw5b.framework.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;


import edu.cmu.cs.cs214.hw5b.dataplugin.DataPlugin;
import edu.cmu.cs.cs214.hw5b.displayplugin.DisplayPlugin;
import edu.cmu.cs.cs214.hw5b.framework.core.DataSet;
import edu.cmu.cs.cs214.hw5b.framework.core.FrameworkImpl;
import edu.cmu.cs.cs214.hw5b.framework.core.StateChangeListener;


public class FrameworkGui implements StateChangeListener{
	
	private static final String FRAME_TITLE = "Weather Data Visualization";
	private static final String OK = "OK";
	private static final String CANCEL = "Cancel";
	
	private static final String MENU_TITLE = "File";
    private static final String MENU_NEW_DATA = "New Data";
    private static final String MENU_SELECT_DATA = "Select Data...";
    
    private static final String EFFECT_PROMPT = "Select Data effect to apply...";
    private static final String EQUAL_VALUE_PROMPT = "Entering comma seperated values...";
    private static final String CURRENT_DATA_LABEL = "Current Data Set: ";
    
    // data effects related
	private static final String[] effectStrings = {"","Sort", "Filter"};
	
	private static final int COL = 1;
	
	// The parent JFrame window.
    private final JFrame frame;
	
	private FrameworkImpl core;

	private JPanel outerPanel;
	
	// menu related
	private final JMenuBar menuBar;
	private final JMenuItem dataPluginMenu;
	private final ButtonGroup dataPluginGroup = new ButtonGroup();
	private final JMenuItem dataMenu;
	private final ButtonGroup dataGroup = new ButtonGroup();

	
	// The panels that make up the display grid.
    private JPanel[][] visuals;
    
    // related to sort
    private Integer colIndex = 0;
    private boolean whichOrder;
    
    //indicating the name of data set that is currently working on
    private JLabel currentData;
    
    // data set selected related
    private DataSet selected = null;
	private JLabel addedNamesString;
	private JLabel addedCountLabel;
	private String[] colsToSelectStrings;
	private String pathPrompt;
	
	
	public FrameworkGui(FrameworkImpl fc) {
		// Set the framework core instance that the GUI will talk to in response
        // to GUI-related events.
        core = fc;
        frame = new JFrame(FRAME_TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500, 500));
        
        outerPanel = new JPanel();

        frame.getContentPane().add(outerPanel);
        
        dataPluginMenu = new JMenu(MENU_NEW_DATA);
        dataPluginMenu.setMnemonic(KeyEvent.VK_N);
        
        dataMenu = new JMenu(MENU_SELECT_DATA);
        dataMenu.setMnemonic(KeyEvent.VK_S);
        dataMenu.setEnabled(false);
        
        menuBar = createMenuBar();
        frame.setJMenuBar(menuBar);
        
                
        frame.pack();
        frame.setVisible(true);   
        
	}
	

	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu(MENU_TITLE);
        fileMenu.setMnemonic(KeyEvent.VK_F);
        fileMenu.add(dataPluginMenu);
        fileMenu.add(dataMenu);
        
        
        menuBar.add(fileMenu);
        return menuBar;
	}



	private JPanel createEnterPathPanel() {
		JPanel enterPanel = new JPanel(new BorderLayout());
		JTextField pathField = new JTextField();
		JButton okButton = new JButton(OK);
		JLabel prompt = new JLabel(pathPrompt);
		prompt.setFont(new Font("Serif", Font.PLAIN, 30));
		enterPanel.add(prompt,BorderLayout.NORTH);
		enterPanel.add(pathField,BorderLayout.CENTER);
		enterPanel.add(okButton,BorderLayout.SOUTH);
		
		// Observer to add a dataSet to the framework when the user presses the ok button or
        // hits enter in the path field.
        ActionListener addDataListener = e -> {
            String path = pathField.getText();
            if (!path.isEmpty()) {
                //path set, load data plugin to read data
            	try {
            		core.loadDataPlugin(path);
            	} catch (Exception exp) {
            		errorMessage(frame,"Invalid path");
            		frame.getContentPane().removeAll();
                	outerPanel = createEnterPathPanel();
                	frame.getContentPane().add(outerPanel);
                	frame.repaint();
                	frame.setVisible(true);
            	}
            }
        };

        // when add button is clicked
        okButton.addActionListener(addDataListener);
        // when "Enter" key is pressed
        pathField.addActionListener(addDataListener);
		return enterPanel;
	}


	private JPanel createDisplayPanel() {
		JPanel displayPanel = new JPanel(new BorderLayout());
		
		JPanel effectPanel = createEffectPanel();
		displayPanel.add(effectPanel,BorderLayout.NORTH);
		
		JPanel visualPanel = new JPanel();
		List<DisplayPlugin> disp = core.getDisplayPlugins();
		int numVisual = disp.size();
		int row = (numVisual % COL == 0) ? (numVisual/COL) : (numVisual/COL + 1);
		visuals = new JPanel[row][COL];
		visualPanel.setLayout(new GridLayout(row,COL));
		for (int i=0; i<row; i++) {
			for (int j=0; j<COL; j++) {
				int index = i*COL+j;
				if (index >= numVisual) break;
				visuals[i][j] = new JPanel(new BorderLayout());
				DisplayPlugin dp = disp.get(index);
				JPanel plot = new JPanel();
				if (dp.isAvailable(core.getCurrData())) {
					plot = dp.display(core.getCurrData());
				}
				visuals[i][j].add(plot, BorderLayout.PAGE_START);
				
				JButton name = new JButton(dp.getName());
				ActionListener nameButtonListener = e -> {
		            frame.getContentPane().removeAll();
		            outerPanel = createColSelectionPanel(dp);
		            frame.getContentPane().add(outerPanel);
		            frame.repaint();
		            frame.setVisible(true);
		        };
		        name.addActionListener(nameButtonListener);
				
				visuals[i][j].add(name,BorderLayout.PAGE_END);
				visuals[i][j].repaint();
				visuals[i][j].setVisible(true);
				visualPanel.add(visuals[i][j]);
			}
		}
		displayPanel.add(visualPanel, BorderLayout.CENTER);
		
		currentData = new JLabel(CURRENT_DATA_LABEL+core.getCurrData().getName());
		displayPanel.add(currentData,BorderLayout.SOUTH);
		return displayPanel;
	}
	
	private JPanel createEffectPanel() {
		JPanel effectPanel = new JPanel(new BorderLayout());
		JLabel prompt = new JLabel(EFFECT_PROMPT);
		JComboBox<String> effectList = new JComboBox<>(effectStrings);
		effectList.setSelectedIndex(0);
		effectPanel.add(prompt, BorderLayout.NORTH);
		effectPanel.add(effectList, BorderLayout.CENTER);
		
		// Observer to invoke new panel based on selection of effect
        ActionListener selectEffectListener = e -> {
        	JComboBox<String> cb = (JComboBox<String>)e.getSource();
            String effectName = (String)cb.getSelectedItem();
            if (effectName == "Sort") {
            	frame.getContentPane().removeAll();
        		outerPanel = createSortPanel();
            	frame.getContentPane().add(outerPanel);
            	frame.repaint();
            	frame.setVisible(true);
            }
            else if (effectName == "Filter") {
            	frame.getContentPane().removeAll();
        		outerPanel = createFilterPanel();
            	frame.getContentPane().add(outerPanel);
            	frame.repaint();
            	frame.setVisible(true);
            }
            else {
            	throw new IllegalArgumentException("No other effects");
            }
        };
        effectList.addActionListener(selectEffectListener);
		return effectPanel;  
	}


	private JPanel createFilterPanel() {
		JPanel filterPanel = new JPanel();
		filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
		
		JLabel col = new JLabel("Column");
		String[] colStrings = core.getCurrData().getTitleRow().toArray(new String[0]);
		JComboBox<String> colList = new JComboBox<>(colStrings);
		col.setAlignmentX(Component.CENTER_ALIGNMENT);
		filterPanel.add(col);
		colList.setAlignmentX(Component.CENTER_ALIGNMENT);
		filterPanel.add(colList);
		// choose col index to be sorted
		ActionListener selectColListener = e -> {
        	JComboBox<String> cb = (JComboBox<String>)e.getSource();
            colIndex = cb.getSelectedIndex();
        };
        colList.addActionListener(selectColListener);
        
        JButton okButton = new JButton(OK);
        ActionListener finishColListener = e -> {
        	DataSet data = core.getCurrData();
        	if (data.getNumberOfRows() > 0){
        		boolean isString = data.getCol(colIndex).get(0).isString();
        		if (isString) {
        			frame.getContentPane().removeAll();
            		outerPanel = createFilterStringPanel();
                	frame.getContentPane().add(outerPanel);
                	frame.repaint();
                	frame.setVisible(true);
        		}
        		else {
        			frame.getContentPane().removeAll();
            		outerPanel = createFilterNumberPanel();
                	frame.getContentPane().add(outerPanel);
                	frame.repaint();
                	frame.setVisible(true);
        		}
        	}
        };
        okButton.addActionListener(finishColListener); 
        okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		filterPanel.add(okButton);
		
		JButton cancelButton = new JButton(CANCEL);
		ActionListener cancelListener = e -> {
			colIndex = 0;
        	updateVisualizations();
        };
        cancelButton.addActionListener(cancelListener); 
        cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		filterPanel.add(cancelButton);
		return filterPanel;
	}


	private JPanel createFilterNumberPanel() {
		JPanel pane = new JPanel();
		pane.setLayout(new BoxLayout(pane,BoxLayout.Y_AXIS));
		
		JPanel p1 = new JPanel(new BorderLayout());
		JLabel low = new JLabel("Greater than: ");
		JTextField lowField = new JTextField();
		low.setFont(new Font("Serif", Font.PLAIN, 20));
		p1.add(low,BorderLayout.WEST);
		p1.add(lowField,BorderLayout.CENTER);
		pane.add(p1);
		
		JPanel p2 = new JPanel(new BorderLayout());
		JLabel upper = new JLabel("Less than: ");
		JTextField upperField = new JTextField();
		upper.setFont(new Font("Serif", Font.PLAIN, 20));
		p2.add(upper,BorderLayout.WEST);
		p2.add(upperField,BorderLayout.CENTER);
		pane.add(p2);
		
		JPanel p3 = new JPanel(new BorderLayout());
		JLabel equalTo = new JLabel("Equal to: ");
		JLabel prompt = new JLabel(EQUAL_VALUE_PROMPT);
		JTextField equalField = new JTextField();
		equalTo.setFont(new Font("Serif", Font.PLAIN, 20));
		p3.add(equalTo,BorderLayout.WEST);
		p3.add(equalField,BorderLayout.CENTER);
		p3.add(prompt,BorderLayout.SOUTH);
		pane.add(p3);
		
		JButton okButton = new JButton(OK);
		ActionListener startFilterListener = e -> {
	        String lowStr = lowField.getText();
	        String upperStr = upperField.getText();
	        String equalStr = equalField.getText();
	        List<Double> equals = new ArrayList<>();
	        if (equalStr.isEmpty()) {
	        	Double lo = Double.NEGATIVE_INFINITY;
		        Double hi = Double.POSITIVE_INFINITY;
	        	if (isDouble(lowStr)) {
	        		lo = Double.parseDouble(lowStr);
	        	}
	        	else if (lowStr.isEmpty()) {
	        		lo = Double.NEGATIVE_INFINITY;
	        	}
	        	else {
	        		errorMessage(frame, "All constraints must be numeric values.");
	        	}
	        	if (isDouble(upperStr)) {
	        		hi = Double.parseDouble(lowStr);
	        	}
	        	else if (lowStr.isEmpty()) {
	        		hi = Double.POSITIVE_INFINITY;
	        	}
	        	else {
	        		errorMessage(frame, "All constraints must be numeric values.");
	        	}
	        	core.filterNumber(colIndex,lo,hi,equals);
	        }
	        else {
	        	boolean isNum = true;
	        	String[] equalArray = equalStr.split(",");
	        	for (String s: equalArray) {
	        		if (!isDouble(s.trim())) {
	        			isNum = false;
	        			break;
	        		}
	        		else {
	        			equals.add(Double.parseDouble(s.trim()));
	        		}
	        	}
	        	if (isNum) {
	        		core.filterNumber(colIndex,Double.NEGATIVE_INFINITY,Double.POSITIVE_INFINITY,equals);
	        	}
	        	else {
	        		errorMessage(frame, "All constraints must be numeric values.");
	        	}
	        }   
	    };
	    okButton.addActionListener(startFilterListener); 
	    okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		pane.add(okButton);
		
		JButton cancelButton = new JButton(CANCEL);
		ActionListener cancelListener = e -> {
        	updateVisualizations();
        };
        cancelButton.addActionListener(cancelListener); 
        cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		pane.add(cancelButton);
		
		return pane;
	}
	
	private boolean isDouble(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}


	private JPanel createFilterStringPanel() {
		JPanel pane = new JPanel();
		pane.setLayout(new BoxLayout(pane,BoxLayout.Y_AXIS));
		
		JPanel p1 = new JPanel(new BorderLayout());
		JLabel startWith = new JLabel("Start with: ");
		JTextField startField = new JTextField();
		startWith.setFont(new Font("Serif", Font.PLAIN, 20));
		p1.add(startWith,BorderLayout.WEST);
		p1.add(startField,BorderLayout.CENTER);
		pane.add(p1);
		
		JPanel p2 = new JPanel(new BorderLayout());
		JLabel endWith = new JLabel("End with: ");
		JTextField endField = new JTextField();
		endWith.setFont(new Font("Serif", Font.PLAIN, 20));
		p2.add(endWith,BorderLayout.WEST);
		p2.add(endField,BorderLayout.CENTER);
		pane.add(p2);
		
		JPanel p3 = new JPanel(new BorderLayout());
		JLabel equalTo = new JLabel("Equal to: ");
		JLabel prompt = new JLabel(EQUAL_VALUE_PROMPT);
		JTextField equalField = new JTextField();
		equalTo.setFont(new Font("Serif", Font.PLAIN, 20));
		p3.add(equalTo,BorderLayout.WEST);
		p3.add(equalField,BorderLayout.CENTER);
		p3.add(prompt,BorderLayout.SOUTH);
		pane.add(p3);
		
		JButton okButton = new JButton(OK);
		ActionListener startFilterListener = e -> {
	        String start = startField.getText();
	        String end = endField.getText();
	        String equal = equalField.getText();
	        List<String> equals = new ArrayList<>();
	        if (equal.isEmpty()) {
	        	core.filterString(colIndex, start, end, equals);
	        }
	        else {
	        	String[] equalArray = equal.split(",");
	        	for (String s: equalArray) {
	        		equals.add(s.trim());
	        	}
	        	core.filterString(colIndex,"","", equals);
	        }   
	    };
	    okButton.addActionListener(startFilterListener); 
	    okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		pane.add(okButton);
		
		JButton cancelButton = new JButton(CANCEL);
		ActionListener cancelListener = e -> {
			colIndex = 0;
        	updateVisualizations();
        };
        cancelButton.addActionListener(cancelListener); 
        cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		pane.add(cancelButton);
		
		return pane;
	}


	private JPanel createSortPanel() {
		JPanel sortPanel = new JPanel();
		sortPanel.setLayout(new BoxLayout(sortPanel,BoxLayout.Y_AXIS));
		//------------------------selectPane---------------------------
		JPanel selectPanel = new JPanel(new BorderLayout());
		JLabel col = new JLabel("Column");
		String[] colStrings = core.getCurrData().getTitleRow().toArray(new String[0]);
		JComboBox<String> colList = new JComboBox<>(colStrings);
		selectPanel.add(col,BorderLayout.NORTH);
		selectPanel.add(colList,BorderLayout.CENTER);
		// choose col index to be sorted
        ActionListener selectColListener = e -> {
        	JComboBox<String> cb = (JComboBox<String>)e.getSource();
            colIndex = cb.getSelectedIndex(); 
        };
        colList.addActionListener(selectColListener);
		
		JPanel orderPanel = new JPanel(new BorderLayout());
		JLabel orderLabel = new JLabel("Order");
		String[] orderStrings = {"A to Z","Z to A"};
		JComboBox<String> orderList = new JComboBox<>(orderStrings);
		colList.setSelectedIndex(0);
		orderPanel.add(orderLabel,BorderLayout.NORTH);
		orderPanel.add(orderList,BorderLayout.CENTER);
		// choose sort order
        ActionListener selectOrderListener = e -> {
        	JComboBox<String> cb = (JComboBox<String>)e.getSource();
            String order = (String)cb.getSelectedItem();
            whichOrder = (order == "A to Z");
        };
        colList.addActionListener(selectOrderListener);
		//-------------------------------------------------------------
        JButton okButton = new JButton(OK);
        //start sorting 
        ActionListener startSortListener = e -> {
            core.sort(new ArrayList<Integer>(colIndex), whichOrder);
        };
        okButton.addActionListener(startSortListener); 
        okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton cancelButton = new JButton(CANCEL);
		ActionListener cancelListener = e -> {
			colIndex = 0;
        	updateVisualizations();
        };
        cancelButton.addActionListener(cancelListener); 
        cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		sortPanel.add(selectPanel);
		sortPanel.add(orderPanel);
		sortPanel.add(okButton);
		sortPanel.add(cancelButton);
		return sortPanel;
	}
	
	private JPanel createColSelectionPanel(DisplayPlugin disp) {
		JPanel selectPanel = new JPanel();
		selectPanel.setLayout(new BoxLayout(selectPanel,BoxLayout.Y_AXIS));
		JLabel prompt = new JLabel("Select data series...");
		prompt.setAlignmentX(Component.LEFT_ALIGNMENT);
		selectPanel.add(prompt);
		//-----------------------add selection---------------------------
		JPanel dataPanel = new JPanel();
		dataPanel.setLayout(new BoxLayout(dataPanel,BoxLayout.X_AXIS));
		
		JButton addButton = new JButton("Add");
		JComboBox<String> colList = new JComboBox<>();
		
		DataSet[] datasets = core.getAllData().toArray(new DataSet[0]);
		//create a string data set name array
		String[] dataStrings = new String[datasets.length];
		for (int i=0; i< datasets.length; i++) {
			dataStrings[i] = datasets[i].getName();
		}
		JComboBox<String> dataList = new JComboBox<>(dataStrings);
		dataList.setSelectedIndex(0);
		dataPanel.add(dataList,BorderLayout.CENTER);
		//select data set
        ActionListener selectDataListener = e -> {
        	JComboBox<String> cb = (JComboBox<String>)e.getSource();
            DataSet d = datasets[cb.getSelectedIndex()]; 
            if (core.getAllModifiedData().contains(d)) {
            	selected = core.getAllModifiedData().get(core.getAllModifiedData().indexOf(d));
            }
            else {
            	selected = d;
            }
            //update column selection box
            //System.out.println(selected.getTitleRow());
            colsToSelectStrings = selected.getTitleRow().toArray(new String[0]);
            updateColSelectionBox(colList, dataPanel);
            dataPanel.add(addButton);
            addButton.setEnabled(true);
        };
        dataList.addActionListener(selectDataListener);
        dataPanel.add(dataList);
      
		// choose col index to add
        ActionListener selectColListener = e -> {
        	JComboBox<String> cb = (JComboBox<String>)e.getSource();
            colIndex = cb.getSelectedIndex(); 
        };
        colList.addActionListener(selectColListener);
        dataPanel.add(colList);
        
        
        ActionListener addListener = e -> {
            core.addSelectedCols(selected, colIndex);
            
        };
        addButton.addActionListener(addListener); 
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addButton.setEnabled(false);
       
        selectPanel.add(dataPanel);
        //--------------------------------------------------------------//
        JLabel addedPrompt = new JLabel("Series added:");
        addedPrompt.setAlignmentX(Component.LEFT_ALIGNMENT);
		selectPanel.add(addedPrompt);
		
		addedNamesString = new JLabel("");
		addedNamesString.setAlignmentX(Component.LEFT_ALIGNMENT);
		selectPanel.add(addedNamesString);
		
		addedCountLabel = new JLabel("0 added");
		addedCountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		selectPanel.add(addedCountLabel);
		
		JButton okButton = new JButton(OK);
        ActionListener startSortListener = e -> {
        	//get a new dataSet
        	DataSet newData = core.getNewDataSet();
        	core.clearSelection();
        	// pop up a new window
        	createNewChartWindow(disp, newData);
        	//go back to display panel
            updateVisualizations();
        };
        okButton.addActionListener(startSortListener); 
        okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectPanel.add(okButton);
        
        JButton cancelButton = new JButton(CANCEL);
		ActionListener cancelListener = e -> {
			// go back to display panel
			core.clearSelection();
			updateVisualizations();
        };
        cancelButton.addActionListener(cancelListener); 
        cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		selectPanel.add(cancelButton);
		
		return selectPanel;
		
	}

	private void updateColSelectionBox(JComboBox<String> jcbo, JPanel pane) {
		DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>)jcbo.getModel();
	    // removing old data
	    model.removeAllElements();
	    for (String item : colsToSelectStrings) {
	            model.addElement(item);
	    }
	    // setting model with new data
	    jcbo.setModel(model);
	    // adding combobox to panel
	    pane.add(jcbo);
	}


	private void createNewChartWindow(DisplayPlugin disp, DataSet d) {
		JFrame newFrame = new JFrame(disp.getName());
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel pane = new JPanel();
        pane = disp.display(d);
    	newFrame.getContentPane().add(pane);
    	newFrame.pack();
    	newFrame.setVisible(true);
//        if (disp.isAvailable(d)) {
//        	pane = disp.display(d);
//        	newFrame.getContentPane().add(pane);
//        	newFrame.pack();
//        	newFrame.setVisible(true);
//        }
//        else {
//        	errorMessage(frame,"Not correct data form.");
//        }    
	}
	
//-----------------------------------State change listener----------------------------



	@Override
	public void onDataPluginRegistered(DataPlugin datp) {
		JRadioButtonMenuItem dataPluginMenuItem = new JRadioButtonMenuItem(datp.getName());
        dataPluginMenuItem.setSelected(false);
        dataPluginMenuItem.addActionListener(event -> {
            // data plugin selected, need read path
        	core.selectDataPlugin(datp);
        	// repaint to enter path panel
        	pathPrompt = datp.getDescription();
        	frame.getContentPane().removeAll();
        	outerPanel = createEnterPathPanel();
        	frame.getContentPane().add(outerPanel);
        	frame.repaint();
        	frame.setVisible(true);
        });
        dataPluginGroup.add(dataPluginMenuItem);
        dataPluginMenu.add(dataPluginMenuItem);
	}



	@Override
	public void onDataExtracted() {
		addDataSelection(core.getCurrData());
		updateVisualizations();
	}
	
	
	private void addDataSelection(DataSet data) {
		JRadioButtonMenuItem dataMenuItem = new JRadioButtonMenuItem(data.getName());
        dataMenuItem.setSelected(false);
        dataMenuItem.addActionListener(event -> {
            // select data set
        	core.setCurrData(data);
        });
        dataGroup.add(dataMenuItem);
        dataMenu.add(dataMenuItem);
		dataMenu.setEnabled(true);
	}


	private void updateVisualizations() {
		//repaint to display panel
		frame.getContentPane().removeAll();
		outerPanel = createDisplayPanel();
		//updateVisualizations();
    	frame.getContentPane().add(outerPanel);
		frame.repaint();
    	frame.setVisible(true);
	}

	
	@Override
	public void onError(String message){
		errorMessage(frame,message);
	}
	
	private static void errorMessage(final Component component, final String message) {
		JOptionPane.showMessageDialog(component, message, "Error", JOptionPane.INFORMATION_MESSAGE);
	}


	@Override
	public void onEffectApplied() {
		updateVisualizations();	
	}


	@Override
	public void onDataChanged() {
		updateVisualizations();
	}


	@Override
	public void onSelectedColsChanged(String added, int count) {
		addedNamesString.setText(added);
		addedCountLabel.setText(Integer.toString(count)+" added");
	}


	@Override
	public void onChartSelected(JPanel chart) {
		// TODO Auto-generated method stub
		
	}

}
