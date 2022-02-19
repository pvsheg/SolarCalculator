package com.fullstack;

import java.text.DecimalFormat;

import org.springframework.stereotype.Component;

@Component
public class CalculateEnergy {

	public double calculateEnergy(CountryPojo countryPojo, int dayOfYear) {
		long area = (long) (countryPojo.getArea());
		System.out.println("area : " + area);
		int days = dayOfYear;
		double UTC = Double.valueOf(countryPojo.getTimeZone());
		double latitude = countryPojo.getLatitude();
		double longitude = countryPojo.getLongitude();
		Double[] zenithAngles = calculateZenithAngle(latitude, UTC, days, longitude);
		DecimalFormat doubleFormat = new DecimalFormat("#.###");
		double power = calculatePower(zenithAngles, area);
		System.out.println("The power generated per day is : " + doubleFormat.format(power/1000) + " MW/m^2");		
		return power/1000;
	}


	private double calculatePower(Double[] zenithAngles, long area) {
		double powerPerSquareMeter = 0.00;
		DecimalFormat doubleFormat = new DecimalFormat("#.###");
		DecimalFormat zenithFormat = new DecimalFormat("#.#");

		for(double zenithAngle : zenithAngles){
//			System.out.println("Cos of angle : " + zenithAngle);
//			System.out.println("Zenith angle : " + 1/zenithAngle);
//			System.out.println("power zenith angle : " + Math.pow(-2.197, 0.678));
			double massFactor = 0;
			double directInsolation = 0;
			zenithAngle = Double.valueOf(zenithFormat.format(zenithAngle));
//			System.out.println("formatted angle : " + zenithAngle);
			if(zenithAngle > 0){
				massFactor = Double.valueOf(doubleFormat.format(Math.pow((1 / zenithAngle), 0.678)));		
				directInsolation = 1.353 * Math.pow(0.7, massFactor);
				powerPerSquareMeter = powerPerSquareMeter + Double.valueOf(doubleFormat.format(directInsolation * area * zenithAngle)) ;
			}
			else{
				zenithAngle = 0 - zenithAngle;
				if(zenithAngle != 0){
					massFactor  = Double.valueOf(doubleFormat.format(Math.pow((1/zenithAngle), 0.678)));
//					System.out.println("Math power : " + doubleFormat.format(Math.pow(0.7, massFactor)));
					directInsolation = 1.353 * 1/Math.pow(0.7, massFactor);					
					powerPerSquareMeter = powerPerSquareMeter + Double.valueOf(doubleFormat.format(directInsolation * area * zenithAngle)) ;
				}
			}
//			System.out.println("Mass Factor : " + massFactor);
//			System.out.println("Direct insolation : " + directInsolation);
		}
		return powerPerSquareMeter;
	}


	private Double[] calculateZenithAngle(double latitude, double UTC, int days, double longitude) {
		DecimalFormat doubleFormat = new DecimalFormat("#.###");
		//		DecimalFormat trignoFormat = new DecimalFormat("###");
		Double[] hrAngles = calculateHrAngle(latitude, UTC, days, longitude);
		Double[] zenithAngles = new Double[12];
		for(int i = 0; i < hrAngles.length; i++){
			Double declination = Double.valueOf(doubleFormat.format(calculateDeclination(days)));
			//			System.out.println("Declination : " + declination);
			//		* Math.sin(declination)) 0.9610
			//			System.out.println("Sin : " + (Math.sin(declination)));
			//			System.out.println("Cos : " + (Math.cos(latitude)*Math.cos(declination)*Math.cos(hrAngles[i])));
			double zenith = ((Math.sin(latitude) * Math.sin(declination)) + (Math.cos(latitude)*Math.cos(declination)*Math.cos(hrAngles[i])));
			zenithAngles[i] = Double.valueOf(doubleFormat.format(zenith));
		}
		return zenithAngles;
	}

	private double calculateDeclination(int days) {

		return (23.45 * Math.sin(0.98562 * (days - 81)));
	}

	private Double[] calculateHrAngle(double latitude, double UTC, int days, double longitude) {
		DecimalFormat doubleFormat = new DecimalFormat("#.####");
		//		DecimalFormat trignoFormat = new DecimalFormat("###");

		double lstm = 15 * UTC;
		Integer[] localTimes = {7,8,9,10, 11, 12, 13, 14, 15, 16, 17, 18};
		Double[] hrAngles = new Double[12];	
		for(int i = 0; i < localTimes.length; i++){
			double B = Double.valueOf(doubleFormat.format(0.9863 *(days - 81)));
			double BTwice = B * 2;
			//		if(B < 0){
			//			B = 0 - B;
			//		}
			//			System.out.println("B : " + B);
			//			System.out.println("Sin2B : " + (Math.sin(B * 2)));
			//			System.out.println("cosB : " + (Math.cos(B)));
			//			System.out.println("sinB : " + (Math.sin(B)));
			double equationOfTime = Double.valueOf(doubleFormat.format((9.87 * Math.sin(2 * B)) - (7.53 * Math.cos(B)) - (1.5 * Math.sin(B))));
			//			System.out.println("EoT : " + equationOfTime);
			double timeCorrectionFactor = Double.valueOf(doubleFormat.format((4 * (longitude - lstm)) + equationOfTime));
			//			System.out.println("time factor : " + timeCorrectionFactor);
			double localSolarTime = Double.valueOf(doubleFormat.format(localTimes[i] + timeCorrectionFactor/60));
			//			System.out.println("solar time : " + localSolarTime);
			double hrAngle = Double.valueOf(doubleFormat.format(15 * (localSolarTime - 12)));		
			hrAngles[i] = hrAngle;
		}
		return hrAngles;
	}
}
