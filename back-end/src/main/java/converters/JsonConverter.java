package converters;

import java.util.List;

import com.google.gson.Gson;

import models.Airport;
import models.Carrier;
import models.Statistic;

public class JsonConverter {
    private Gson gson;
    
    public JsonConverter() {
        gson = new Gson(); 
    }
    
    public String AirportsToJson(List<Airport> airports) {
        return gson.toJson(airports);
    }
    
    public String CarriersToJson(List<Carrier> carriers) {
        return gson.toJson(carriers);
    }
    
    public String StatisticToJson(Statistic statistic) {
        return gson.toJson(statistic);
    }
    
    public String StatisticsToJson(List<Statistic> statistics) {
        return gson.toJson(statistics);
    }
}
