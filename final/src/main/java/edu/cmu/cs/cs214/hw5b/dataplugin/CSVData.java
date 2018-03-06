package edu.cmu.cs.cs214.hw5b.dataplugin;

import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.Arrays;
import java.util.List;

import edu.cmu.cs.cs214.hw5b.framework.core.DataSet;
import edu.cmu.cs.cs214.hw5b.framework.core.Framework;

public final class CSVData implements DataPlugin {
	
	private static final String NAME = "CSV Reader";
	private static final String DESCRIPTION = "Please enter the path of the CSV file";

	private boolean titleflag;
	private DataSet myDat;
	
	/** constructor*/
	public CSVData(){
	}

	@Override
	public DataSet processData(String fileName, String name) {
		titleflag = true;
		BufferedReader csvbuf = null;
		try{
			String csvline;
			csvbuf = new BufferedReader(new FileReader(fileName));
			myDat = new DataSet(name);
			while((csvline = csvbuf.readLine()) != null){
				readRowData(csvline);
			}
		}catch(IOException e){
		}
		return myDat;
	}
	
	private void readRowData(String linevalue){
		String[] linestr = linevalue.split(",");
		List<String> rowcontent = Arrays.asList(linestr);
		if(titleflag){
			titleflag = false;
			myDat.addTitleRow(rowcontent);
		}else{
			myDat.addDataRow(rowcontent);
		}
	}

	@Override
	public void onRegister(Framework framework) {
		// TODO Auto-generated method stubs
		
	}
	
	/**
	 * Gets the name of the data plugin.
	 * 
	 * @return name, name of the data plugin
	 */
	@Override
	public String getName(){
		return NAME;
	}

	/**
	 * Gets the description of the data plugin, which describes what to input for the path.
	 * 
	 * @return description, description of the data plugin
	 */
	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

}

