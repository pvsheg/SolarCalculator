package com.fullstack;

public class CountryPojo {

	private double area;
	private double latitude;
	private double longitude;
	private String CityName;
	private String CountryName;
	private String timeZoneId;
	private String timeZone;
	private int offset;
	private int population;
	private String cityName2;
	private String exception;
	private double closestDistance;
	
	public double getClosestDistance() {
		return closestDistance;
	}
	public void setClosestDistance(double closestDistance) {
		this.closestDistance = closestDistance;
	}
	
	private String countryCode;
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}	
	public String getException() {
		return exception;
	}
	public void setException(String exception) {
		this.exception = exception;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setValues(CountryPojo pojo){
		this.area = pojo.getArea();
		this.latitude = pojo.getLatitude();
		this.longitude  = pojo.getLongitude();
		this.CityName  = pojo.getCityName();
		this.CountryName  = pojo.getCountryName();
		this.timeZoneId = pojo.getTimeZoneId();
		this.timeZone = pojo.getTimeZone() ;
		this.offset = pojo.getOffset();
		this.population = pojo.getPopulation();
		this.cityName2 = pojo.getCityName2();
		this.exception = pojo.getException();
		this.closestDistance = pojo.getClosestDistance();
	}
	@Override
	public String toString() {
		return "CountryPojo [area=" + area + ", latitude=" + latitude + ", longitude=" + longitude + ", CityName="
				+ CityName + ", CountryName=" + CountryName + ", timeZoneId=" + timeZoneId + ", timeZone=" + timeZone
				+ ", offset=" + offset + ", population=" + population + ", cityName2=" + cityName2 + ", exception="
				+ exception + ", stateName=" + stateName + "]";
	}
	public String getCityName2() {
		return cityName2;
	}
	public void setCityName2(String cityName2) {
		this.cityName2 = cityName2;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	private String stateName;

	public int getPopulation() {
		return population;
	}
	public void setPopulation(int population) {
		this.population = population;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getCityName() {
		return CityName;
	}
	public void setCityName(String cityName) {
		CityName = cityName;
	}
	public String getCountryName() {
		return CountryName;
	}
	public void setCountryName(String countryName) {
		CountryName = countryName;
	}
	public String getTimeZoneId() {
		return timeZoneId;
	}
	public void setTimeZoneId(String timeZoneId) {
		this.timeZoneId = timeZoneId;
	}
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public double getArea() {
		return area;
	}
	public void setArea(double area) {
		this.area = area;
	}

}
