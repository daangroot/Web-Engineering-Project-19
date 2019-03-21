package converters;

import java.text.ParseException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import models.Airport;
import models.Carrier;
import models.Statistic;

public class JsonConverter implements DataConverter {
    private Gson gson;
    
    public JsonConverter() {
        gson = new Gson();
    }

    @Override
    public String AirportsToString(List<Airport> airports) {
        return gson.toJson(airports);
    }

    @Override
    public String CarriersToString(List<Carrier> carriers) {
        return gson.toJson(carriers);
    }

    @Override
    public String StatisticToString(Statistic statistic, boolean airport, boolean carrier, boolean yearMonth) {    
        JsonObject jsonObject = new JsonObject();
        
        if (statistic == null) {
            return jsonObject.toString();
        }
        
        if (airport) {
            jsonObject.add("airport", gson.toJsonTree(statistic.getAirport()));
        }
        
        if (carrier) {
            jsonObject.add("carrier", gson.toJsonTree(statistic.getCarrier()));
        }
        
        if (yearMonth) {
            jsonObject.add("yearMonth", gson.toJsonTree(statistic.getYearMonth()));
        }
        
        jsonObject.addProperty("cancelledFlightCount", statistic.getCancelledFlightCount());
        jsonObject.addProperty("onTimeFlightCount", statistic.getOnTimeFlightCount());
        jsonObject.addProperty("delayedFlightCount", statistic.getDelayedFlightCount());
        jsonObject.addProperty("divertedFlightCount", statistic.getDivertedFlightCount());
        jsonObject.addProperty("totalFlightCount", statistic.getTotalFlightCount());
        
        jsonObject.addProperty("lateAircraftDelayCount", statistic.getLateAircraftDelayCount());
        jsonObject.addProperty("carrierDelayCount", statistic.getCarrierDelayCount());
        jsonObject.addProperty("weatherDelayCount", statistic.getWeatherDelayCount());
        jsonObject.addProperty("securityDelayCount", statistic.getSecurityDelayCount());
        jsonObject.addProperty("nationalAviationSystemDelayCount", statistic.getNationalAviationSystemDelayCount());
        
        jsonObject.addProperty("lateAircraftDelayTime", statistic.getLateAircraftDelayTime());
        jsonObject.addProperty("carrierDelayTime", statistic.getCarrierDelayTime());
        jsonObject.addProperty("weatherDelayTime", statistic.getWeatherDelayTime());
        jsonObject.addProperty("securityDelayTime", statistic.getSecurityDelayTime());
        jsonObject.addProperty("nationalAviationSystemDelayTime", statistic.getNationalAviationSystemDelayTime());
        jsonObject.addProperty("totalDelayTime", statistic.getTotalDelayTime());
        
        return jsonObject.toString();
    }

    @Override
    public String StatisticsToString(List<Statistic> statistics, boolean airport, boolean carrier, boolean yearMonth) {
        JsonArray jsonArray = new JsonArray();
        
        for (Statistic statistic : statistics) {
            JsonObject jsonObject = new JsonObject();
            
            if (airport) {
                jsonObject.add("airport", gson.toJsonTree(statistic.getAirport()));
            }
            
            if (carrier) {
                jsonObject.add("carrier", gson.toJsonTree(statistic.getCarrier()));
            }
            
            if (yearMonth) {
                jsonObject.add("yearMonth", gson.toJsonTree(statistic.getYearMonth()));
            }
            
            jsonObject.addProperty("cancelledFlightCount", statistic.getCancelledFlightCount());
            jsonObject.addProperty("onTimeFlightCount", statistic.getOnTimeFlightCount());
            jsonObject.addProperty("delayedFlightCount", statistic.getDelayedFlightCount());
            jsonObject.addProperty("divertedFlightCount", statistic.getDivertedFlightCount());
            jsonObject.addProperty("totalFlightCount", statistic.getTotalFlightCount());
            
            jsonObject.addProperty("lateAircraftDelayCount", statistic.getLateAircraftDelayCount());
            jsonObject.addProperty("carrierDelayCount", statistic.getCarrierDelayCount());
            jsonObject.addProperty("weatherDelayCount", statistic.getWeatherDelayCount());
            jsonObject.addProperty("securityDelayCount", statistic.getSecurityDelayCount());
            jsonObject.addProperty("nationalAviationSystemDelayCount", statistic.getNationalAviationSystemDelayCount());
            
            jsonObject.addProperty("lateAircraftDelayTime", statistic.getLateAircraftDelayTime());
            jsonObject.addProperty("carrierDelayTime", statistic.getCarrierDelayTime());
            jsonObject.addProperty("weatherDelayTime", statistic.getWeatherDelayTime());
            jsonObject.addProperty("securityDelayTime", statistic.getSecurityDelayTime());
            jsonObject.addProperty("nationalAviationSystemDelayTime", statistic.getNationalAviationSystemDelayTime());
            jsonObject.addProperty("totalDelayTime", statistic.getTotalDelayTime());
            
            jsonArray.add(jsonObject);
        }
        
        return jsonArray.toString();
    }
    
    @Override
    public List<Statistic> StringToStatistics(String statisticData) throws Exception {
        List<Statistic> statistics = new ArrayList<>();
        JsonArray jsonArray = gson.fromJson(statisticData, JsonArray.class);
        
        for (JsonElement jsonElement : jsonArray) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Statistic statistic = gson.fromJson(jsonObject, Statistic.class);
            statistics.add(statistic);
        }
        
        return statistics;
    }

    @Override
    public String StatisticToFlightString(Statistic statistic, boolean airport, boolean carrier, boolean yearMonth,
            boolean cancelledFlightCount, boolean onTimeFlightCount, boolean delayedFlightCount,
            boolean divertedFlightCount, boolean totalFlightCount) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String StatisticsToFlightString(List<Statistic> statistics, boolean airport, boolean carrier,
            boolean yearMonth, boolean cancelledFlightCount, boolean onTimeFlightCount, boolean delayedFlightCount,
            boolean divertedFlightCount, boolean totalFlightCount) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String StatisticToDelayString(Statistic statistic, boolean airport, boolean carrier, boolean yearMonth,
            boolean lateAircraftDelayCount, boolean carrierDelayCount, boolean weatherDelayCount,
            boolean securityDelayCount, boolean nationalAviationSystemDelayCount) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String StatisticsToDelayString(List<Statistic> statistics, boolean airport, boolean carrier,
            boolean yearMonth, boolean lateAircraftDelayCount, boolean carrierDelayCount, boolean weatherDelayCount,
            boolean securityDelayCount, boolean nationalAviationSystemDelayCount) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String StatisticToDelayTimeString(Statistic statistic, boolean airport, boolean carrier, boolean yearMonth,
            boolean lateAircraftDelayTime, boolean carrierDelayTime, boolean weatherDelayTime,
            boolean securityDelayTime, boolean nationalAviationSystemDelayTime, boolean totalDelayTime) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String StatisticsToDelayTimeString(List<Statistic> statistics, boolean airport, boolean carrier,
            boolean yearMonth, boolean lateAircraftDelayTime, boolean carrierDelayTime, boolean weatherDelayTime,
            boolean securityDelayTime, boolean nationalAviationSystemDelayTime, boolean totalDelayTime) {
        // TODO Auto-generated method stub
        return null;
    }
}
