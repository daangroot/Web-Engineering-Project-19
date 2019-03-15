package utils;

import java.beans.PropertyVetoException;
import java.io.BufferedReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import database.DatabaseConnector;
import models.Airport;
import models.Carrier;
import models.Statistic;

public class JsonDataImporter {
	public static void main(String[] args) {
		Map<String, String> airportsMap = new HashMap<>();
	    Map<String, String> carriersMap = new HashMap<>();
	    List<Airport> airports = new ArrayList<>();
	    List<Carrier> carriers = new ArrayList<>();
	    List<Statistic> stats = new ArrayList<>();
		
		final String filePath = "C:\\\\Users\\Daan\\Downloads\\airlines.json";
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(filePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		}
		 
		GsonBuilder builder = new GsonBuilder(); 
	    Gson gson = builder.create(); 
		
	    JsonArray jsonArray = gson.fromJson(bufferedReader, JsonArray.class);
	    
	    for (JsonElement jsonElement : jsonArray) {
	    	JsonObject jsonObject = jsonElement.getAsJsonObject();
	        
	    	JsonObject jsonAirport = jsonObject.get("airport").getAsJsonObject();
	        String airportCode = jsonAirport.get("code").getAsString();
	        String airportName = jsonAirport.get("name").getAsString();
	    	airportsMap.put(airportCode, airportName);
	    	Airport airport = new Airport(airportCode, airportName);
	        
	    	JsonObject jsonCarrier = jsonObject.get("carrier").getAsJsonObject();
	        String carrierCode = jsonCarrier.get("code").getAsString();
	        String carrierName = jsonCarrier.get("name").getAsString();
	        carriersMap.put(carrierCode, carrierName);
	        Carrier carrier = new Carrier(carrierCode, carrierName);
	        
	        JsonObject jsonTime = jsonObject.get("time").getAsJsonObject();
	        int year = jsonTime.get("year").getAsInt();
	        int month = jsonTime.get("month").getAsInt();
	        YearMonth yearMonth = YearMonth.of(year, month);
	        
	        JsonObject jsonStatistics = jsonObject.get("statistics").getAsJsonObject();
	        
	        JsonObject jsonFlightCount = jsonStatistics.get("flights").getAsJsonObject();
	        int cancelledFlightCount = jsonFlightCount.get("cancelled").getAsInt();
	        int onTimeFlightCount = jsonFlightCount.get("on time").getAsInt();
	        int delayedFlightCount = jsonFlightCount.get("delayed").getAsInt();
	        int divertedFlightCount = jsonFlightCount.get("diverted").getAsInt();
	        int totalFlightCount = jsonFlightCount.get("total").getAsInt();
	        
	        JsonObject jsonDelayCount = jsonStatistics.get("# of delays").getAsJsonObject();
	        int lateAircraftDelayCount = jsonDelayCount.get("late aircraft").getAsInt();
	        int weatherDelayCount = jsonDelayCount.get("weather").getAsInt();
	        int securityDelayCount = jsonDelayCount.get("security").getAsInt();
	        int nationalAviationSystemDelayCount = jsonDelayCount.get("national aviation system").getAsInt();
	        int carrierDelayCount = jsonDelayCount.get("carrier").getAsInt();
	        
	        JsonObject jsonDelayTime = jsonStatistics.get("minutes delayed").getAsJsonObject();
	        int lateAircraftDelayTime = jsonDelayTime.get("late aircraft").getAsInt();
	        int weatherDelayTime = jsonDelayTime.get("weather").getAsInt();
	        int securityDelayTime = jsonDelayTime.get("security").getAsInt();
	        int nationalAviationSystemDelayTime = jsonDelayTime.get("national aviation system").getAsInt();
	        int carrierDelayTime = jsonDelayTime.get("carrier").getAsInt();
	        int totalDelayTime = jsonDelayTime.get("total").getAsInt();
	        
	        Statistic stat = new Statistic(airport, carrier, yearMonth);
	        stat.setCancelledFlightCount(cancelledFlightCount);
	        stat.setOnTimeFlightCount(onTimeFlightCount);
	        stat.setDelayedFlightCount(delayedFlightCount);
	        stat.setDivertedFlightCount(divertedFlightCount);
	        stat.setTotalFlightCount(totalFlightCount);
	        
	        stat.setLateAircraftDelayCount(lateAircraftDelayCount);
	        stat.setWeatherDelayCount(weatherDelayCount);
	        stat.setSecurityDelayCount(securityDelayCount);
	        stat.setNationalAviationSystemDelayCount(nationalAviationSystemDelayCount);
	        stat.setCarrierDelayCount(carrierDelayCount);
	        
	        stat.setLateAircraftDelayTime(lateAircraftDelayTime);
	        stat.setWeatherDelayTime(weatherDelayTime);
	        stat.setSecurityDelayTime(securityDelayTime);
	        stat.setNationalAviationSystemDelayTime(nationalAviationSystemDelayTime);
	        stat.setCarrierDelayTime(carrierDelayTime);
	        stat.setTotalDelayTime(totalDelayTime);
	        
	        stats.add(stat);
	    }
	    
	    for (Map.Entry<String, String> item : airportsMap.entrySet()) {
	        Airport airport = new Airport(item.getKey(), item.getValue());
	    	airports.add(airport);
	    	System.out.println(airport.getCode());
	    }
	    
	    for (Map.Entry<String, String> item : carriersMap.entrySet()) {
	        Carrier carrier = new Carrier(item.getKey(), item.getValue());
	    	carriers.add(carrier);
	    	System.out.println(carrier.getCode());
	    }
	    
	    try {
			DatabaseConnector dbconn = new DatabaseConnector();
			dbconn.addAirports(airports);
			dbconn.addCarriers(carriers);
			dbconn.addStatistics(stats);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
}
