package com.fullstack;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fullstack.utils.MapStructure;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

@Component
public class TempProcessor {

	@Autowired
	private CalculateEnergy calculateEnergy;
	
	public static Map<String, CountryPojo> cityMap = new HashMap<String, CountryPojo>();
	public static Map<String, CountryPojo> populationMap = new HashMap<String, CountryPojo>();
	public static Map<String,MapStructure> countryCityMap = new HashMap<String, MapStructure>();
	public static Map<String, Map<LatLngPojo, CountryPojo>> countryPopMap = new HashMap<String, Map<LatLngPojo, CountryPojo>>();
	
	public void generateMaps() throws Exception{
		try{
			generateCityMap();			
		}catch (Exception e) {
			throw new Exception("exeption while generating map : " + e);
		}
	}

	public void process(double lng, double lat, String date, EnergyData data) throws Exception {
		DecimalFormat doubleFormat = new DecimalFormat("#.##");
		CountryPojo inputs = new CountryPojo();
		CountryPojo output = new CountryPojo();
//		Date date = new Date();
		inputs.setLongitude(lng);
		inputs.setLatitude(lat);
		reverseGeocode(inputs);
		int dayOfYear = 0;
		output.setLongitude(lng);
		output.setLatitude(lat);
		findCityData(inputs, output);
		System.out.println("output is : " + output.toString());
		if(output.getException().contains("CNF")){
			data.setException(output.getException());
		}
		else{
//			CalculateEnergy calculate = new CalculateEnergy();
			dayOfYear = calculateDayOfYear(date);
			System.out.println("DAY### : " + dayOfYear);
			double power = calculateEnergy.calculateEnergy(output, dayOfYear);
			double noOfHouses = (power / (28.9 * 30))/1000;
			double costOfEnergy = (power * 0.13)/1000000;
			data.setArea(output.getArea());
			data.setCityName(convertCaps(output.getCityName()));
			data.setCountryName(convertCaps(output.getCountryName()));
			data.setCountryCode(inputs.getCountryCode());
			data.setPower(Double.valueOf(doubleFormat.format(power)));
			data.setCostOfEnergy(Double.valueOf(doubleFormat.format(costOfEnergy)));
			data.setNumberOfHousesPowered(Double.valueOf(doubleFormat.format(noOfHouses)));
			data.setPopulation(output.getPopulation());
			data.setException(output.getException());			
		}
	}
	
	private String convertCaps(String str){
		String s1 = str.substring(0, 1).toUpperCase();
	    String nameCapitalized = s1 + str.substring(1);
		return nameCapitalized;
	}
	
	private void generateCityMap() throws Exception {
		String line = "";  
		InputStream inputStream = getClass().getResourceAsStream("/cityAreaPopulationTimezoneOutput2.csv");
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));  

		int count = 0;
		while ((line = br.readLine()) != null)  //returns a boolean value  
		{ 
			if(count > 0){
				String[] perLine = line.split("@");
//				for(String data : perLine){
					String data = perLine[0];
					LatLngPojo latlng = new LatLngPojo();
					CountryPojo cityData = new CountryPojo();

					String[] segData = data.split(",");
					latlng.setLongitude(Double.valueOf(segData[2].trim()));
					latlng.setLatitude(Double.valueOf(segData[1].trim()));

					cityData.setCityName(segData[0].trim().toLowerCase());
					cityData.setArea(Double.valueOf(segData[3].trim()));
					cityData.setCountryName(segData[6].trim().toLowerCase());
					cityData.setLatitude(latlng.getLatitude());
					cityData.setLongitude(latlng.getLongitude());
					cityData.setOffset(Integer.valueOf(segData[5].trim()));
					cityData.setTimeZone(segData[4].trim());
					cityData.setPopulation(Integer.valueOf(segData[7]));
					cityMap.put(cityData.getCityName(), cityData);
					if(countryCityMap.containsKey(cityData.getCountryName())){
						countryCityMap.get(cityData.getCountryName()).addCountryData(cityData.getCountryName(), cityData);
					}
					else{
						MapStructure structure = new MapStructure();
						structure.addCountryData(cityData.getCountryName(), cityData);
						countryCityMap.put(cityData.getCountryName(), structure);
					}
//				}
			}
			count++;
		}
		br.close();
		System.out.println("maps generated successfully.");
		System.out.println("city map size :" + cityMap.size());
		System.out.println("country map size : " + countryCityMap.size());
	}
	 
	private int calculateDayOfYear(String date2) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(); 
		if(date2 != null && !date2.isEmpty()){
			date = format.parse(date2);
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
		return dayOfYear;
	}
	
	private void findCityData(CountryPojo inputs, CountryPojo output) {
		System.out.println("inside find city : " + inputs.toString());
		List<String> cityNames = new ArrayList<String>();
		if(inputs.getCityName2() != null){
			cityNames.add(inputs.getCityName2());
		}
		if(inputs.getCityName()!= null){
			cityNames.add(inputs.getCityName());			
		}
		boolean foundCity = false;
		System.out.println("citynames size : " + cityNames.size());
		if(cityNames.isEmpty()){
			if(countryCityMap.containsKey(inputs.getCountryName()) && !foundCity){
				System.out.println("country found in countryMap");
				processLatLng(output, countryCityMap.get(inputs.getCountryName()));
				output.setException("None");
				foundCity = true;
			}
			else{
				output.setException("Country Name not found in the data CNF");
			}
		}
		else{
			for(String city : cityNames){ 
				System.out.println("finding city: " + city);
				if(cityMap.containsKey(city) && !foundCity){
					System.out.println("city found in cityMap");
					output.setValues(cityMap.get(city));
					output.setException("None");
					foundCity = true;
				}
				else if(countryCityMap.containsKey(inputs.getCountryName()) && !foundCity){
					System.out.println("country found in countryMap");
					processLatLng(output, countryCityMap.get(inputs.getCountryName()));
					output.setException("None");
					foundCity = true;
				}
				else if(!foundCity){
					System.out.println("Not Found");
					output.setException("Country Name not found in the data CNF");
				}			
			}			
		}
	}

	private void processLatLng(CountryPojo output, MapStructure mapStructure) {
//		Set<LatLngPojo> latLngList = ctryMap.keySet();
		System.out.println("inside process Lat Lng : " + output.getLatitude());
		Double[] lat = mapStructure.getLatList();
		Double[] lng = mapStructure.getLngList();
		System.out.println("lat size : " + lat.length);
		Map<Double, List<LatLngPojo>> latMap = mapStructure.getLatMap();		
		Map<Double, List<LatLngPojo>> lngMap = mapStructure.getLngMap();		
		System.out.println("lat map size : " + latMap.size());
		System.out.println("lng map size : " + latMap.size());

		Arrays.sort(lat);
		Arrays.sort(lng);
		CountryPojo output1 = new CountryPojo();
		CountryPojo output2 = new CountryPojo();
		output1.setValues(output);
		output2.setValues(output);
		findAndCalculate(lat, output1, latMap, mapStructure, output.getLatitude());
		findAndCalculate(lng, output2, lngMap, mapStructure, output.getLongitude());
//		System.out.println("@@City 1: " + output1.getCityName() + " distance : " + output1.getClosestDistance() + " " + output1.getLatitude() + " : " + output1.getLongitude());
//		System.out.println("@@City 2: " + output2.getCityName() + " distance : " + output2.getClosestDistance()+ " " + output2.getLatitude() + " : " + output2.getLongitude());
		if(output1.getClosestDistance() < output2.getClosestDistance()){
			output.setValues(output1);
		}
		else{
			output.setValues(output2);			
		}
	}


	
	private void findAndCalculate(Double[] array, CountryPojo output, Map<Double, List<LatLngPojo>> dataMap, MapStructure mapStructure, double keyVal) {
//		System.out.println("lat array : " + Arrays.toString(array));
		int pos = Arrays.binarySearch(array, keyVal);
		System.out.println("pos : " + pos);
		LatLngPojo lastVal = new LatLngPojo();
		List<LatLngPojo> lastList = new ArrayList<LatLngPojo>();
		if(pos  > 0){
			List<LatLngPojo> list = dataMap.get(keyVal);
			lastList.addAll(list);
			if(pos > 1 && pos < array.length - 1){
				lastList.addAll(dataMap.get(array[pos - 1]));
				lastList.addAll(dataMap.get(array[pos + 1]));				
			}
		}
		else{
			pos = 0 - pos;
			if(pos > 1 && pos < array.length - 1){
				lastList.addAll(dataMap.get(array[pos]));
				if(array.length > 1){
					lastList.addAll(dataMap.get(array[pos - 1]));
					lastList.addAll(dataMap.get(array[pos + 1]));
				}
			}
			else if(pos == 1){
				lastList.addAll(dataMap.get(array[pos - 1]));
				if(array.length > 1){
					lastList.addAll(dataMap.get(array[pos]));					
				}
				if(array.length > 2){
					lastList.addAll(dataMap.get(array[pos + 1]));									
				}
			}
			else if(pos >= array.length - 1){
				if(pos == array.length - 1){
					lastList.addAll(dataMap.get(array[pos]));
					if(array.length > 1)
						lastList.addAll(dataMap.get(array[pos - 1]));
					if(array.length > 2)
						lastList.addAll(dataMap.get(array[pos - 2]));									
				}
				else if(pos == array.length){
					lastList.addAll(dataMap.get(array[pos - 1]));
					if(array.length > 1)
						lastList.addAll(dataMap.get(array[pos - 2]));
					if(array.length > 2)
						lastList.addAll(dataMap.get(array[pos - 3]));				
				}
				else{
					lastList.addAll(dataMap.get(array[pos - 2]));
					if(array.length > 1){
						lastList.addAll(dataMap.get(array[pos - 3]));						
					}
					if(array.length > 2){
						lastList.addAll(dataMap.get(array[pos - 4]));															
					}
				}
			}			
		}
//		System.out.println("list size : " + lastList.size());
		double distance = getClosest(output, lastList, lastVal);
//		System.out.println("last val ; " + lastVal.toString());
//		System.out.println("output Pojo : " + mapStructure.getCityMap().get(lastVal).toString());
		output.setValues(mapStructure.getCityMap().get(lastVal));
		output.setClosestDistance(distance);
		System.out.println("output refered Pojo : " + output.toString());
	}

	private double getClosest(CountryPojo output, List<LatLngPojo> lastList, LatLngPojo lastVal) {	
		double dist = Double.MAX_VALUE;
//		System.out.println("####Inside calculator : " + output.toString());
		for(LatLngPojo pojo : lastList){
			double latVal = (output.getLatitude() - pojo.getLatitude());
			double lngVal = (output.getLongitude() - pojo.getLongitude());
//			System.out.println("####POJO : " + pojo.toString());
			double val = Math.sqrt(latVal * latVal + lngVal * lngVal);
//			System.out.println("####Val : " + pojo.toString());			
			if(val < dist){
				dist = val;
				lastVal.setLatitude(pojo.getLatitude());
				lastVal.setLongitude(pojo.getLongitude());
			}
		}
		System.out.println("calculated min distance : " + dist);
		return dist;
	}


	public void reverseGeocode(CountryPojo pojo) throws Exception {
		System.out.println("inside reverse geocode");
		double latitude = pojo.getLatitude();
		double longitude = pojo.getLongitude();
	    final GeoApiContext context = new GeoApiContext.Builder()
	            .apiKey("AIzaSyAyzvBaXShtVcxc9g0fLCBr8qMLtR0rstQ")
	            .build();
	    final LatLng latlng = new LatLng(latitude, longitude);
	    final GeocodingResult[] results;
	    try {
	    	System.out.println("lat : " + latlng.lat + " lng : " + latlng.lng);
	        results = GeocodingApi.reverseGeocode(context, latlng).language("english").await();
	        for (AddressComponent ac : results[0].addressComponents) {
	        for(AddressComponentType acType : ac.types){
	        	if (acType == AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_1) {

	        		System.out.println("State : " + ac.longName);
	        		pojo.setStateName(ac.longName.trim().toLowerCase());

	            } else if (acType == AddressComponentType.LOCALITY) {

	        		System.out.println("City : " + ac.longName);
	        		pojo.setCityName(ac.longName.trim().toLowerCase());

	            } else if (acType == AddressComponentType.COUNTRY) {

	        		System.out.println("Country : " + ac.longName);
	        		pojo.setCountryName(ac.longName.trim().toLowerCase());
	        		pojo.setCountryCode(ac.shortName.trim().toLowerCase());
	            }
	            else if (acType == AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_2) {
	        		pojo.setCityName2(ac.longName.trim().toLowerCase());
	        		System.out.println("city 2 : " + ac.longName);
	            }
	        }
	        }
	    } catch (final Exception e) {
	        throw e;
	    }
	}
}
