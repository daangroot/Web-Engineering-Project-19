package converters;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import models.Airport;
import models.Carrier;
import models.ExtraStatistic;
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

        if (!cancelledFlightCount) {
            jsonObject.remove("cancelledFlightCount");
        }

        if (!onTimeFlightCount) {
            jsonObject.remove("onTimeFlightCount");
        }

        if (!delayedFlightCount) {
            jsonObject.remove("delayedFlightCount");
        }

        if (!divertedFlightCount) {
            jsonObject.remove("divertedFlightCount");
        }

        if (!totalFlightCount) {
            jsonObject.remove("totalFlightCount");
        }
        return jsonObject.toString();

    }

    @Override
    public String StatisticsToFlightString(List<Statistic> statistics, boolean airport, boolean carrier,
                                           boolean yearMonth, boolean cancelledFlightCount, boolean onTimeFlightCount, boolean delayedFlightCount,
                                           boolean divertedFlightCount, boolean totalFlightCount) {
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

            if (!cancelledFlightCount) {
                jsonObject.remove("cancelledFlightCount");
            }

            if (!onTimeFlightCount) {
                jsonObject.remove("onTimeFlightCount");
            }

            if (!delayedFlightCount) {
                jsonObject.remove("delayedFlightCount");
            }

            if (!divertedFlightCount) {
                jsonObject.remove("divertedFlightCount");
            }

            if (!totalFlightCount) {
                jsonObject.remove("totalFlightCount");
            }
            return jsonObject.toString();

        }
    }

    @Override
    public String StatisticToDelayString(Statistic statistic, boolean airport, boolean carrier, boolean yearMonth,
                                         boolean lateAircraftDelayCount, boolean carrierDelayCount, boolean weatherDelayCount,
                                         boolean securityDelayCount, boolean nationalAviationSystemDelayCount) {
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

        if (!lateAircraftDelayCount) {
            jsonObject.remove("lateAircraftDelayCount");
        }

        if (!carrierDelayCount) {
            jsonObject.remove("carrierDelayCount");
        }

        if (!weatherDelayCount) {
            jsonObject.remove("weatherDelayCount");
        }

        if (!securityDelayCount) {
            jsonObject.remove("securityDelayCount");
        }

        if (!nationalAviationSystemDelayCount) {
            jsonObject.remove("nationalAviationSystemDelayCount");
        }
        return jsonObject.toString();
    }


    @Override
    public String StatisticsToDelayString(List<Statistic> statistics, boolean airport, boolean carrier, boolean yearMonth,
                                          boolean lateAircraftDelayCount, boolean carrierDelayCount, boolean weatherDelayCount,
                                          boolean securityDelayCount, boolean nationalAviationSystemDelayCount) {
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

            if (!lateAircraftDelayCount) {
                jsonObject.remove("lateAircraftDelayCount");
            }

            if (!carrierDelayCount) {
                jsonObject.remove("carrierDelayCount");
            }

            if (!weatherDelayCount) {
                jsonObject.remove("weatherDelayCount");
            }

            if (!securityDelayCount) {
                jsonObject.remove("securityDelayCount");
            }

            if (!nationalAviationSystemDelayCount) {
                jsonObject.remove("nationalAviationSystemDelayCount");
            }
            return jsonObject.toString();

        }
    }

    @Override
    public String StatisticToDelayTimeString(Statistic statistic, boolean airport, boolean carrier,
                                             boolean yearMonth, boolean lateAircraftDelayTime, boolean carrierDelayTime, boolean weatherDelayTime,
                                             boolean securityDelayTime, boolean nationalAviationSystemDelayTime, boolean totalDelayTime) {
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

        if (!lateAircraftDelayTime) {
            jsonObject.remove("lateAircraftDelayCount");
        }

        if (!carrierDelayTime) {
            jsonObject.remove("carrierDelayCount");
        }

        if (!weatherDelayTime) {
            jsonObject.remove("weatherDelayCount");
        }

        if (!securityDelayTime) {
            jsonObject.remove("securityDelayCount");
        }

        if (!nationalAviationSystemDelayTime) {
            jsonObject.remove("nationalAviationSystemDelayCount");
        }

        if (!totalDelayTime) {
            jsonObject.remove("totalDelayTime")
        }

        return jsonObject.toString();

    }

    @Override
    public String StatisticsToDelayTimeString(List<Statistic> statistics, boolean airport, boolean carrier,
                                              boolean yearMonth, boolean lateAircraftDelayTime, boolean carrierDelayTime, boolean weatherDelayTime,
                                              boolean securityDelayTime, boolean nationalAviationSystemDelayTime, boolean totalDelayTime) {
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

            if (!lateAircraftDelayTime) {
                jsonObject.remove("lateAircraftDelayTime");
            }

            if (!carrierDelayTime) {
                jsonObject.remove("carrierDelayTime");
            }

            if (!weatherDelayTime) {
                jsonObject.remove("weatherDelayTime");
            }

            if (!securityDelayTime) {
                jsonObject.remove("securityDelayTime");
            }

            if (!nationalAviationSystemDelayTime) {
                jsonObject.remove("nationalAviationSystemDelayTime");
            }

            if (!totalDelayTime) {
                jsonObject.remove("totalDelayTime")
            }
        }

        return jsonArray.toString();
    }

    @Override
    public String ExtraStatisticsToString(List<ExtraStatistic> extraStatistics, boolean airport1, boolean airport2, boolean carrier) {
        JsonArray jsonArray = gson.toJsonTree(extraStatistics).getAsJsonArray();

        for (JsonElement jsonElement : jsonArray) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            if (!airport1) {
                jsonObject.remove("airport");
            }

            if (!airport2) {
                jsonObject.remove("airport");
            }

            if (!carrier) {
                jsonObject.remove("carrier");
            }

        }

        return jsonArray.toString();
    }


}
