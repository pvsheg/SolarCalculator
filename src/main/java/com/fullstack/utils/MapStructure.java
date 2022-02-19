package com.fullstack.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fullstack.CountryPojo;
import com.fullstack.LatLngPojo;

public class MapStructure {
	private Map<LatLngPojo, CountryPojo> cityMap = new HashMap<LatLngPojo, CountryPojo>();
	private Set<Double> latList = new HashSet<Double>();
	private Map<Double, List<LatLngPojo>> latMap = new HashMap<Double, List<LatLngPojo>>();
	private Set<Double> lngList = new HashSet<Double>();
	private Map<Double, List<LatLngPojo>> lngMap = new HashMap<Double, List<LatLngPojo>>();
	
	public void addCountryData(String country, CountryPojo pojo){
		LatLngPojo latLng = new LatLngPojo();
		latLng.setLatitude(pojo.getLatitude());
		latLng.setLongitude(pojo.getLongitude());
		cityMap.put(latLng, pojo);
		latList.add(pojo.getLatitude());
		lngList.add(pojo.getLongitude());
		if(latMap.containsKey(pojo.getLatitude())){
			latMap.get(pojo.getLatitude()).add(latLng);
		}
		else{
			List<LatLngPojo> tempList = new ArrayList<LatLngPojo>();
			tempList.add(latLng);
			latMap.put(pojo.getLatitude(), tempList);
		}
		if(lngMap.containsKey(pojo.getLongitude())){
			lngMap.get(pojo.getLongitude()).add(latLng);
		}
		else{
			List<LatLngPojo> tempList = new ArrayList<LatLngPojo>();
			tempList.add(latLng);
			lngMap.put(pojo.getLongitude(), tempList);
		}

	}
	
	public Map<LatLngPojo, CountryPojo> getCityMap(){
		return cityMap;
	}
	
	public Double[] getLatList(){
		Double[] array = new Double[latList.size()];
		return latList.toArray(array);
	}

	public Double[] getLngList(){
		Double[] array = new Double[lngList.size()];
		return lngList.toArray(array);
	}
	
	public Map<Double, List<LatLngPojo>> getLatMap(){
		return latMap;
	}

	public Map<Double, List<LatLngPojo>> getLngMap(){
		return lngMap;
	}

	
}
