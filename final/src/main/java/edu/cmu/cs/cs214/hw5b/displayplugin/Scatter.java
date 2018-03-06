package edu.cmu.cs.cs214.hw5b.displayplugin;


/*
private static DataSet data;

@Override
public void start(Stage stage) {

	// set chart title
	stage.setTitle("Scatter Chart");
	
	// set chart axis's
	final CategoryAxis xAxis = new CategoryAxis();
	final NumberAxis yAxis = new NumberAxis();
	final ScatterChart<String, Number> sc = new ScatterChart<String, Number>(xAxis, yAxis);
	xAxis.setLabel("Time");
	yAxis.setLabel("Temerature");
	sc.setTitle("Temperature in Different Cities");

	XYChart.Series<String, Number> series1 = new XYChart.Series<>();
	series1.setName("Pittsburgh");
	series1.getData().add(new XYChart.Data<String, Number>("Monday", 3));
	series1.getData().add(new XYChart.Data<>("Tuesday", 5));
	series1.getData().add(new XYChart.Data<>("Wednesday", 8));
	series1.getData().add(new XYChart.Data<>("Thursday", 5));
	series1.getData().add(new XYChart.Data<>("Friday", 10));
	series1.getData().add(new XYChart.Data<>("Saturday", 15));
	series1.getData().add(new XYChart.Data<>("Sunday", 20));

	XYChart.Series<String, Number> series2 = new XYChart.Series<>();
	series2.setName("New York");
	series2.getData().add(new XYChart.Data<>("Monday", 20));
	series2.getData().add(new XYChart.Data<>("Tuesday", 25));
	series2.getData().add(new XYChart.Data<>("Wednesday", 28));
	series2.getData().add(new XYChart.Data<>("Thursday", 30));
	series2.getData().add(new XYChart.Data<>("Friday", 28));
	series2.getData().add(new XYChart.Data<>("Saturday", 33));
	series2.getData().add(new XYChart.Data<>("Sunday", 25));
	
	XYChart.Series<String, Number> series3 = new XYChart.Series<>();
	series3.setName("哈尔滨");
	series3.getData().add(new XYChart.Data<String, Number>("Monday", -3));
	series3.getData().add(new XYChart.Data<>("Tuesday", -10));
	series3.getData().add(new XYChart.Data<>("Wednesday", -20));
	series3.getData().add(new XYChart.Data<>("Thursday", -25));
	series3.getData().add(new XYChart.Data<>("Friday", -12));
	series3.getData().add(new XYChart.Data<>("Saturday", -15));
	series3.getData().add(new XYChart.Data<>("Sunday", -20));
	
	sc.getData().add(series1);
	sc.getData().add(series2);
	sc.getData().add(series3);
	Scene scene = new Scene(sc, 500, 400);
	stage.setScene(scene);
	stage.show();
}

public static void main(String[] args) {
	launch(args);
}
*/

import org.jfree.ui.ApplicationFrame;

import edu.cmu.cs.cs214.hw5b.framework.core.DataSet;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public final class Scatter implements DisplayPlugin {
	
	/**
	 * Checks whether scatter plot is available for the set of data.
	 * A scatter requires at least two columns of data, with the first column displayed on the
	 * independent x-axis, and the following columns displayed on the dependent y-axis.
	 * The x-axis can be any type; the y-axis requires at least one column of numeric data.
	 * 
	 * @param data, the data set to be displayed
	 * @return available, true if the data can be displayed as scatter; false otherwise
	 */
	@Override
	public final boolean isAvailable(DataSet data) {
		
		// check null
		if (data == null) {
			return false;
		}
		
		// check size
		if (data.getNumberOfCols() <= 1 || data.getNumberOfRows() <= 0) {
			return false;
		}
		
		// check if any dependent column has numeric data
		for (int j = 1; j < data.getNumberOfCols()) {
			assert(data.getCol(j).size() > 0);
			DataEntry data = data.getCol(j).get(0);
			if (data.isInteger() || data.isDouble()) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public 
	
}