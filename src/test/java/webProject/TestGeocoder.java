package webProject;

import java.util.List;

import org.junit.Test;

import com.byteowls.jopencage.JOpenCageGeocoder;
import com.byteowls.jopencage.model.JOpenCageResponse;
import com.byteowls.jopencage.model.JOpenCageReverseRequest;
import com.fullstack.CountryPojo;
import com.fullstack.LatLngPojo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

public class TestGeocoder {

//	@Test
//	public void testOpenCage(){
//		JOpenCageGeocoder jOpenCageGeocoder = new JOpenCageGeocoder("28c1f5ec9373442f8ab0428f8d277f96");
//
//		JOpenCageReverseRequest request = new JOpenCageReverseRequest(33.46273, 111.5826);
//		request.setLanguage("es"); // prioritize results in a specific language using an IETF format language code
//		request.setNoDedupe(true); // don't return duplicate results
//		request.setLimit(5); // only return the first 5 results (default is 10)
//		request.setNoAnnotations(true); // exclude additional info such as calling code, timezone, and currency
//		request.setMinConfidence(3); // restrict to results with a confidence rating of at least 3 (out of 10)
//
//		JOpenCageResponse response = jOpenCageGeocoder.reverse(request);
//
//		// get the formatted address of the first result:
//		String formattedAddress = response.getResults().get(0).getFormatted(); 
//		String city = response.getResults().get(0).getComponents().getCityDistrict();
//		System.out.println("City : " + city);
//		System.out.println(formattedAddress);	
//	}
	
//	@Test
//	public void reverseGeocode() throws Exception {
//		 
//		double longitude = 66.44387678639622;
//		double latitude = 36.21463665263483;
//	    final GeoApiContext context = new GeoApiContext.Builder()
//	            .apiKey("AIzaSyAyzvBaXShtVcxc9g0fLCBr8qMLtR0rstQ")
//	            .build();
//	    final LatLng latlng = new LatLng(latitude, longitude);
//	    final GeocodingResult[] results;
//	    try {
//	        results = GeocodingApi.reverseGeocode(context, latlng).language("english").await();
//	        for (AddressComponent ac : results[0].addressComponents) {
//	        	System.out.println(ac);
//	        for(AddressComponentType acType : ac.types){
//	        	if (acType == AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_1) {
//
//	        		System.out.println("State : " + ac.longName);
//
//	            } else if (acType == AddressComponentType.LOCALITY) {
//
//	        		System.out.println("City : " + ac.longName);
//
//	            } else if (acType == AddressComponentType.COUNTRY) {
//
//	        		System.out.println("Country : " + ac.longName);
//	        		System.out.println("Country code : " + ac.shortName);
//	            }else if (acType == AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_2) {
//
//	        		System.out.println("city 2 : " + ac.longName);
//	            }
//	        	
//	        }
//	        }
//	    } catch (final Exception e) {
//	        throw e;
//	    }
//	}
	
	@Test
	public void getClosest() {	
		double dist = Double.MAX_VALUE;
		double lat1 = 25.56;
		double lng1 = 79.41;
		double lat2 = 10.76;
		double lng2 = 79.83;
		
		
//		for(LatLngPojo pojo : lastList){
			double latVal = lat1 - lat2;
			double lngVal = lng1 - lng2;
			double val = Math.sqrt(latVal * latVal + lngVal * lngVal);
//			if(val < dist){
//				dist = val;
//				lastVal.setLatitude(pojo.getLatitude());
//				lastVal.setLongitude(pojo.getLongitude());
//			}
//		}
		System.out.println("Val : " + val);
	}

	
//	@Test
//	public void testString(){
//		String str ="UTC-01:30";
//		System.out.println(str.split("\\-")[1]);
//		String val = str.split("\\-")[1];
//		String[] arr = val.split(":");
//		double timeZone = Double.valueOf(arr[0]) + (Double.valueOf(arr[1]) / 60);
//		timeZone = 0 - timeZone;
//		System.out.println("Val : " + timeZone);
//	}
}
