package edu.cmu.cs.cs214.hw5b.dataplugin;

import java.util.ArrayList;
import java.util.List;

import edu.cmu.cs.cs214.hw5b.displayplugin.LineChartSample;
import edu.cmu.cs.cs214.hw5b.displayplugin.Scatter;

import edu.cmu.cs.cs214.hw5b.framework.core.DataEntry;
import edu.cmu.cs.cs214.hw5b.framework.core.DataSet;

public class Main {

	public static void main(String[] args) {
		DataPlugin myPlug = new CSVData();
		DataSet mydat = myPlug.processData("/Users/jinchuanchao/Team3/weatherFiles/sample_data.csv");
		List<DataEntry> temp = mydat.getCol(7);
		List<DataEntry> time = mydat.getCol(0);
		System.out.println(temp.get(0));
		List<Double> tempV = new ArrayList<Double>();
		if(temp.get(0).isDouble()){
			for(DataEntry tempv:temp){
				Double c_v = tempv.convertToDouble();
				tempV.add(c_v);
			}
		}else{
			return;
		}
		List<String> timeV = new ArrayList<String>();
		for(DataEntry timev:time){
			String c_v = timev.convertToString();
			timeV.add(c_v);
		}
		List<Integer> timeSlot = new ArrayList<Integer>();
		for(int i = 0; i < tempV.size(); i ++){
			timeSlot.add(i);
		}
		
		LineChartSample sample = new LineChartSample();
		sample.setStringX(timeV);
		sample.setY(tempV);
		LineChartSample.main(args);
	}

}
