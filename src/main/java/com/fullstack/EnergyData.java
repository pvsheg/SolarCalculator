package com.fullstack;

public class EnergyData {
	private String cityName;
	private String countryName;
	private String date;
	private double power;
	private double numberOfHousesPowered;
	private double costOfEnergy;
	private int population;
	private double area;
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	private String countryCode;
	
	public double getArea() {
		return area;
	}
	public void setArea(double area) {
		this.area = area;
	}
	public int getPopulation() {
		return population;
	}
	public void setPopulation(int population) {
		this.population = population;
	}
	public String getException() {
		return exception;
	}
	public void setException(String exception) {
		this.exception = exception;
	}
	private String exception;
	
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public double getPower() {
		return power;
	}
	public void setPower(double power) {
		this.power = power;
	}
	public double getNumberOfHousesPowered() {
		return numberOfHousesPowered;
	}
	public void setNumberOfHousesPowered(double numberOfHousesPowered) {
		this.numberOfHousesPowered = numberOfHousesPowered;
	}
	public double getCostOfEnergy() {
		return costOfEnergy;
	}
	public void setCostOfEnergy(double costOfEnergy) {
		this.costOfEnergy = costOfEnergy;
	}
}
