package edu.cmu.cs.cs214.hw5b.displayplugin;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class LineChartSample extends Application {

	private static List<String> myX = new ArrayList<String>();
	private static List<Double> myY = new ArrayList<Double>();

	@Override
	public void start(Stage stage) {
	
		stage.setTitle("Temprature");
		// defining the axes
		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("Time");
		// creating the chart
		final LineChart<String, Number> lineChart = new LineChart<String, Number>(xAxis, yAxis);

		lineChart.setTitle("Temprature");
		// defining a series
		XYChart.Series<String, Number> series = new XYChart.Series<>();
		series.setName("temprature of time t");
		// populating the series with data
		for (int i = 0; i < Math.min(myX.size(), myY.size()); i++) {
			series.getData().add(new XYChart.Data<>(myX.get(i), myY.get(i)));
		}

		Scene scene = new Scene(lineChart, 800, 600);
		lineChart.getData().add(series);

		stage.setScene(scene);
		stage.show();
	}

	public void setStringX(List<String> xv) {
		myX = xv;
	}

	public void setY(List<Double> yv) {
		myY = yv;
	}

	public static void main(String[] args) {
		launch(args);
	}
}