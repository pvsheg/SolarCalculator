package com.fullstack;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Processor {

	public static Map<LatLngPojo, CountryPojo> latLngCityMap = new HashMap<LatLngPojo, CountryPojo>();
	
	public static Map<String, CountryPojo> cityMap = new HashMap<String, CountryPojo>();
	
	public static Map<String, CountryPojo> cityPopulationMap = new HashMap<String, CountryPojo>();

	public static Map<String, List<CountryPojo>> countryMap = new HashMap<String, List<CountryPojo>>();
 	public void startProcess(){

		int dayOfYear = 0;
		try {
			Date date = new Date();
			generateMap();
			generateCityMap();
			dayOfYear = calculateDayOfYear(date);
			System.out.println("Today is " + dayOfYear);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		CalculateEnergy energy = new CalculateEnergy();
//		double energyVal = energy.calculateEnergy();
//		int noOfHouses = (int) (energyVal / 28.9);
//		System.out.println("Number of houses powered : " + noOfHouses);
//		double costOfEnergy = energyVal * 5;
//		System.out.println("cost of energy : " + costOfEnergy);
	}
	
	private void tryGeocoder(){
		
	}
	
	private void generateMap() throws Exception {
		String line = "";  
		
		BufferedReader br = new BufferedReader(new FileReader(getClass().getClassLoader().getResource("cityPopulationsCsv.csv").getPath()));  

//		Map<LatLng, CountryPojo> cityList = new HashMap<LatLng, CountryPojo>();

		int count = 0;
		while ((line = br.readLine()) != null)  //returns a boolean value  
		{ 
			if(count > 0){
				String[] perLine = line.split("@");
				for(String data : perLine){
					CountryPojo cityData = new CountryPojo();

					String[] segData = data.split(",");

					cityData.setCityName(segData[0].trim().toLowerCase());
//					cityData.setArea(Double.valueOf(segData[3].trim()));
					cityData.setCountryName(segData[1].trim());
					cityData.setPopulation(segData[2].equals(" ") ? 0 : Integer.valueOf(segData[2]));
					cityPopulationMap.put(cityData.getCityName(), cityData);
				}
			}
			count++;
		}
		br.close();
		System.out.println("city population map size : " + cityPopulationMap.size());
	}

	public void processRequest(double longitude, double latitude, String city, String subregion, EnergyData engData){
		DecimalFormat doubleFormat = new DecimalFormat("#.##");
		CalculateEnergy energy = new CalculateEnergy();
		CountryPojo cityData = new CountryPojo();
		double energyVal = 0;
		Date date = new Date();
		int dayOfYear = calculateDayOfYear(date);

		longitude = Double.valueOf(doubleFormat.format(longitude));
		latitude = Double.valueOf(doubleFormat.format(latitude));
		LatLngPojo latLng = new LatLngPojo();
		latLng.setLongitude(longitude);
		latLng.setLatitude(latitude);
		city = city.trim().toLowerCase();
		subregion = subregion.trim().toLowerCase();
		boolean exception = false;
		if(latLngCityMap.containsKey(latLng)){
			System.out.println("found in latLng");
			cityData = (CountryPojo)latLngCityMap.get(latLng);
		}
		else if(cityMap.containsKey(city)){
			System.out.println("found in city");
			cityData =  (CountryPojo)cityMap.get(city);
		}
		else if(cityMap.containsKey(subregion)){
			System.out.println("found in subregion");
			cityData =  (CountryPojo)cityMap.get(subregion);
		}
		else if(cityPopulationMap.containsKey(city) || cityPopulationMap.containsKey(subregion)){
			System.out.println("found city in population map");
			exception = true;
			cityData = cityPopulationMap.containsKey(city) ? cityPopulationMap.get(city) : cityPopulationMap.get(subregion);
			engData.setException("City area not present. above is the population prediction");
			engData.setPopulation(cityData.getPopulation());
			engData.setCityName(cityData.getCityName());
			engData.setCountryName(cityData.getCountryName());

		}
		else{
			exception = true;
			engData.setException("City area is not present in the database.");			
			engData.setCityName(subregion);
			engData.setCountryName(cityData.getCountryName());
			
		}
		if(!exception){
			energyVal = energy.calculateEnergy(cityData, dayOfYear);			
			engData.setCityName(cityData.getCityName());
			engData.setCountryName(cityData.getCountryName());
		}
		engData.setDate(date.toGMTString());
		if(energyVal != 0){
			double noOfHouses = (int) (energyVal / (28.9 * 30));
			System.out.println("Number of houses powered : " + noOfHouses);
			double costOfEnergy = energyVal * 5;
			System.out.println("cost of energy : " + costOfEnergy/1000000);
			engData.setCityName(cityData.getCityName());
			engData.setCountryName(cityData.getCountryName());
			engData.setPower(energyVal);
			engData.setNumberOfHousesPowered(noOfHouses/1000000);
			engData.setCostOfEnergy(costOfEnergy/1000000);
			engData.setException("None");
		}
	}

	private int calculateDayOfYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
		return dayOfYear;
	}

	private void generateCityMap() throws Exception {
		String line = "";  
		
		BufferedReader br = new BufferedReader(new FileReader(getClass().getClassLoader().getResource("finalOutput.csv").getPath()));  

//		Map<LatLng, CountryPojo> cityList = new HashMap<LatLng, CountryPojo>();

		int count = 0;
		while ((line = br.readLine()) != null)  //returns a boolean value  
		{ 
			if(count > 0){
				String[] perLine = line.split("@");
				for(String data : perLine){
					LatLngPojo latlng = new LatLngPojo();
					CountryPojo cityData = new CountryPojo();

					String[] segData = data.split(",");
					latlng.setLongitude(Double.valueOf(segData[1].trim()));
					latlng.setLatitude(Double.valueOf(segData[2].trim()));

					cityData.setCityName(segData[0].trim().toLowerCase());
					cityData.setArea(Double.valueOf(segData[3].trim()));
					cityData.setCountryName(segData[6].trim());
					cityData.setLatitude(latlng.getLatitude());
					cityData.setLongitude(latlng.getLongitude());
					cityData.setOffset(Integer.valueOf(segData[5].trim()));
					cityData.setTimeZone(segData[4].trim());
					latLngCityMap.put(latlng, cityData);
					cityMap.put(cityData.getCityName(), cityData);
				}
			}
			count++;
		}
		br.close();
		System.out.println("Lat Lng Map Size  : " + latLngCityMap.size());
		System.out.println("City Map Size  : " + cityMap.containsKey("akola"));
		
	}
	
}
