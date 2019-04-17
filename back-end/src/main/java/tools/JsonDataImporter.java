package tools;

import java.io.BufferedReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import database.DatabaseConnector;
import models.*;

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

            JsonObject jsonStatistics = jsonObject.get("statistics").getAsJsonObject();

            JsonObject jsonFlightCount = jsonStatistics.get("flights").getAsJsonObject();
            int cancelledCount = jsonFlightCount.get("cancelled").getAsInt();
            int onTimeCount = jsonFlightCount.get("on time").getAsInt();
            int delayedCount = jsonFlightCount.get("delayed").getAsInt();
            int divertedCount = jsonFlightCount.get("diverted").getAsInt();
            int totalCount = jsonFlightCount.get("total").getAsInt();
            FlightData flightData = new FlightData((cancelledCount > 0) ? cancelledCount:0,
                    (onTimeCount > 0) ? onTimeCount:0, (delayedCount > 0) ? delayedCount:0,
                    (divertedCount > 0) ? divertedCount:0, (totalCount > 0) ? totalCount:0);

            JsonObject jsonDelay = jsonStatistics.get("# of delays").getAsJsonObject();
            int lateAircraftCount = jsonDelay.get("late aircraft").getAsInt();
            int carrierCount = jsonDelay.get("carrier").getAsInt();
            int weatherCount = jsonDelay.get("weather").getAsInt();
            int securityCount = jsonDelay.get("security").getAsInt();
            int nationalAviationSystemCount = jsonDelay.get("national aviation system").getAsInt();
            DelayData delayData = new DelayData((lateAircraftCount > 0) ? lateAircraftCount:0,
                    (carrierCount > 0) ? carrierCount:0, (weatherCount > 0) ? weatherCount:0,
                    (securityCount > 0) ? securityCount:0,
                    (nationalAviationSystemCount > 0) ? nationalAviationSystemCount:0);

            JsonObject jsonDelayTime = jsonStatistics.get("minutes delayed").getAsJsonObject();
            int lateAircraftTime = jsonDelayTime.get("late aircraft").getAsInt();
            int carrierTime = jsonDelayTime.get("carrier").getAsInt();
            int weatherTime = jsonDelayTime.get("weather").getAsInt();
            int securityTime = jsonDelayTime.get("security").getAsInt();
            int nationalAviationSystemTime = jsonDelayTime.get("national aviation system").getAsInt();
            int totalTime = jsonDelayTime.get("total").getAsInt();
            DelayTimeData delayTimeData = new DelayTimeData((lateAircraftTime > 0) ? lateAircraftTime:0,
                    (carrierTime > 0) ? carrierTime:0, (weatherTime > 0) ? weatherTime:0,
                    (securityTime > 0) ? securityTime:0,
                    (nationalAviationSystemTime > 0) ? nationalAviationSystemTime:0,
                    (totalTime > 0) ? totalTime:0);

            Statistic statistic = new Statistic(airport, carrier, year, month, flightData, delayData, delayTimeData);

            stats.add(statistic);
        }

        for (Map.Entry<String, String> item : airportsMap.entrySet()) {
            Airport airport = new Airport(item.getKey(), item.getValue());
            airports.add(airport);
        }

        for (Map.Entry<String, String> item : carriersMap.entrySet()) {
            Carrier carrier = new Carrier(item.getKey(), item.getValue());
            carriers.add(carrier);
        }

        try {
            DatabaseConnector databaseConnector = new DatabaseConnector();
            databaseConnector.addAirports(airports);
            databaseConnector.addCarriers(carriers);
            databaseConnector.addStatistics(stats);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
