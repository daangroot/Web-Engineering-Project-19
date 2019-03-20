package converters;

import java.util.List;

import com.google.gson.Gson;
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
        JsonObject jsonObject = gson.toJsonTree(statistic).getAsJsonObject();
        
        if (!airport) {
            jsonObject.remove("airport");
        }
        
        if (!carrier) {
            jsonObject.remove("carrier");
        }
        
        if (!yearMonth) {
            jsonObject.remove("yearMonth");
        }
        
        return jsonObject.toString();
    }

    @Override
    public String StatisticsToString(List<Statistic> statistics, boolean airport, boolean carrier, boolean yearMonth) {
        JsonArray jsonArray = gson.toJsonTree(statistics).getAsJsonArray();
        
        for (JsonElement jsonElement : jsonArray) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            
            if (!airport) {
                jsonObject.remove("airport");
            }
            
            if (!carrier) {
                jsonObject.remove("carrier");
            }
            
            if (!yearMonth) {
                jsonObject.remove("yearMonth");
            }
        }
        
        return jsonArray.toString();
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
