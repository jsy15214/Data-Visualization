package edu.cmu.cs.cs214.hw5b.dataplugin;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.aksingh.owmjapis.CurrentWeather;
import net.aksingh.owmjapis.OpenWeatherMap;
import org.json.JSONException;

import edu.cmu.cs.cs214.hw5b.framework.core.DataSet;
import edu.cmu.cs.cs214.hw5b.framework.core.Framework;

public class OwmMultipleCityWeather implements DataPlugin{

	private OpenWeatherMap owm;
	private List<CurrentWeather> cwds;
	private List<String> citys;
	private List<String> minT;
	private List<String> maxT;
	private List<String> pressure;
	private List<String> humidity;
	private List<String> windDegree;
	private List<String> windSpeed;
	private List<String> fieldName;
	private static final float InvalidNumber = (float) -999.0;
	
	public OwmMultipleCityWeather()
			throws IOException, MalformedURLException, JSONException
	{
		owm = new OpenWeatherMap("ee690c70068b03a8c923e3ffee8a61aa");
		cwds = new ArrayList<>();
		citys = new ArrayList<>();
		minT = new ArrayList<>();
		maxT = new ArrayList<>();
		pressure = new ArrayList<>();
		humidity = new ArrayList<>();
		windDegree = new ArrayList<>();
		windSpeed = new ArrayList<>();
		fieldName = new ArrayList<>();
		fieldName.add("City Name");
		fieldName.add("Maximum temperature'F");
		fieldName.add("Minimum temperature'F");
		fieldName.add("Pressure");
		fieldName.add("Humidity");
		fieldName.add("Wind Degree");
		fieldName.add("Wind Speed");
	}
 
	@Override
	public DataSet processData(String cityName, String name) {
		String[] linestr = cityName.split(",");
		List<String> mystr = Arrays.asList(linestr);
	    for(int i = 0; i < linestr.length; i ++){
	    	try {
				CurrentWeather c_w = owm.currentWeatherByCityName(mystr.get(i));
				if (c_w.isValid()){
					cwds.add(c_w);
					citys.add(mystr.get(i));
				}
			} catch (JSONException | IOException e) {
				e.printStackTrace();
				continue;
			}
	    }
	    
	    for(CurrentWeather c_w: cwds){
	    	/** max temperature*/
	    	if(c_w.getMainInstance().hasMaxTemperature()){
	    		float maxt = c_w.getMainInstance().getMaxTemperature();
	    		maxT.add(Float.toString(maxt));
	    	}else{
	    		maxT.add(Float.toString(InvalidNumber));
	    	}
	    	
	    	/** min temperature*/
	    	if(c_w.getMainInstance().hasMinTemperature()){
	    		float mint = c_w.getMainInstance().getMinTemperature();
	    		minT.add(Float.toString(mint));
	    	}else{
	    		minT.add(Float.toString(InvalidNumber));
	    	}
	    	
	    	/** pressure*/
	    	if(c_w.getMainInstance().hasPressure()){
	    		float press = c_w.getMainInstance().getPressure();
	    		pressure.add(Float.toString(press));
	    	}else{
	    		pressure.add(Float.toString(InvalidNumber));
	    	}
	    	
	    	/** humidity*/
	    	if(c_w.getMainInstance().hasHumidity()){
	    		float hum = c_w.getMainInstance().getHumidity();
	    		humidity.add(Float.toString(hum));
	    	}else{
	    		humidity.add(Float.toString(InvalidNumber));
	    	}
	    	
	    	/** wind degree*/
	    	if(c_w.getWindInstance().hasWindDegree()){
	    		float windd = c_w.getWindInstance().getWindDegree();
	    		windDegree.add(Float.toString(windd));
	    	}else{
	    		windDegree.add(Float.toString(InvalidNumber));
	    	}
	    	
	    	/** wind speed*/
	    	if(c_w.getWindInstance().hasWindSpeed()){
	    		float winds = c_w.getWindInstance().getWindSpeed();
	    		windSpeed.add(Float.toString(winds));
	    	}else{
	    		windSpeed.add(Float.toString(InvalidNumber));
	    	}
	    }
	    
	    DataSet myDat = new DataSet(name);
	    myDat.addTitleRow(fieldName);
	    myDat.addDataCol(citys);
	    myDat.addDataCol(maxT);
	    myDat.addDataCol(minT);
	    myDat.addDataCol(pressure);
	    myDat.addDataCol(humidity);
	    myDat.addDataCol(windDegree);
	    myDat.addDataCol(windSpeed);
	    
		return myDat;
	}

	@Override
	public void onRegister(Framework framework) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		return "OpenWeatherMap";
	}
}