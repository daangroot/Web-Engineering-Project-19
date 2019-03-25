package converters;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.*;
import models.*;
import rest.MainRestController;

public class JsonConverter implements DataConverter {
    private Gson gson;

    public JsonConverter() {
        gson = new Gson();
    }

    @Override
    public String airportsToString(List<Airport> airports) {
        return gson.toJson(airports);
    }

    public String airportsToStringWithLinks(List<Airport> airports, String baseHref) {
        JsonArray jsonArray = new JsonArray();

        for (Airport airport : airports) {
            JsonObject jsonObject = new JsonObject();

            jsonObject.add("airport", gson.toJsonTree(airport));

            Link link = new Link(baseHref + airport.getCode() + "/", "airport", "GET");
            List<Link> links = new ArrayList<>();
            links.add(link);

            jsonObject.add("links", gson.toJsonTree(links));

            jsonArray.add(jsonObject);
        }

        return jsonArray.toString();
    }

    @Override
    public String carriersToString(List<Carrier> carriers) {
        return gson.toJson(carriers);
    }

    public String carriersToStringWithLinks(List<Carrier> carriers, String baseHref) {
        JsonArray jsonArray = new JsonArray();

        for (Carrier carrier : carriers) {
            JsonObject jsonObject = new JsonObject();

            jsonObject.add("carrier", gson.toJsonTree(carrier));

            Link link = new Link(baseHref + carrier.getCode() + "/", "carrier", "GET");
            List<Link> links = new ArrayList<>();
            links.add(link);

            jsonObject.add("links", gson.toJsonTree(links));

            jsonArray.add(jsonObject);
        }

        return jsonArray.toString();
    }

    @Override
    public String statisticsToString(List<Statistic> statistics, StatisticDataSelectorHelper includedData) {
        JsonArray jsonArray = new JsonArray();

        for (Statistic statistic : statistics) {
            JsonObject jsonObject = new JsonObject();

            if (includedData.withAirport()) {
                jsonObject.add("airport", gson.toJsonTree(statistic.getAirport()));
            }

            if (includedData.withCarrier()) {
                jsonObject.add("carrier", gson.toJsonTree(statistic.getCarrier()));
            }

            if (includedData.withYear()) {
                jsonObject.addProperty("year", statistic.getYear());
            }

            if (includedData.withMonth()) {
                jsonObject.addProperty("month", statistic.getMonth());
            }

            if (includedData.withFlightData()) {
                FlightData flightData = statistic.getFlightData();
                JsonObject flightDataJson = new JsonObject();


                if (includedData.withCancelledCount()) {
                    flightDataJson.addProperty("cancelledCount", flightData.getCancelledCount());
                }

                if (includedData.withOnTimeCount()) {
                    flightDataJson.addProperty("onTimeCount", flightData.getOnTimeCount());
                }

                if (includedData.withDelayedCount()) {
                    flightDataJson.addProperty("delayedCount", flightData.getDelayedCount());
                }

                if (includedData.withDivertedCount()) {
                    flightDataJson.addProperty("divertedCount", flightData.getDivertedCount());
                }

                if (includedData.withTotalCount()) {
                    flightDataJson.addProperty("totalCount", flightData.getTotalCount());
                }

                jsonObject.add("flightData", flightDataJson);
            }

            if (includedData.withDelayData()) {
                DelayData delayData = statistic.getDelayData();
                JsonObject delayDataJson = new JsonObject();

                if (includedData.withLateAircraftCount()) {
                    delayDataJson.addProperty("lateAircraftCount", delayData.getLateAircraftCount());
                }

                if (includedData.withCarrierCount()) {
                    delayDataJson.addProperty("carrierCount", delayData.getCarrierCount());
                }

                if (includedData.withWeatherCount()) {
                    delayDataJson.addProperty("weatherCount", delayData.getWeatherCount());
                }

                if (includedData.withSecurityCount()) {
                    delayDataJson.addProperty("securityCount", delayData.getSecurityCount());
                }

                if (includedData.withNationalAviationSystemCount()) {
                    delayDataJson.addProperty("nationalAviationSystemCount",
                            delayData.getNationalAviationSystemCount());
                }

                jsonObject.add("delayData", delayDataJson);
            }

            if (includedData.withDelayTimeData()) {
                DelayTimeData delayTimeData = statistic.getDelayTimeData();
                JsonObject delayTimeDataJson = new JsonObject();

                if (includedData.withLateAircraftTime()) {
                    delayTimeDataJson.addProperty("lateAircraftTime", delayTimeData.getLateAircraftTime());
                }

                if (includedData.withCarrierTime()) {
                    delayTimeDataJson.addProperty("carrierTime", delayTimeData.getCarrierTime());
                }

                if (includedData.withWeatherTime()) {
                    delayTimeDataJson.addProperty("weatherTime", delayTimeData.getWeatherTime());
                }

                if (includedData.withSecurityTime()) {
                    delayTimeDataJson.addProperty("securityTime", delayTimeData.getSecurityTime());
                }

                if (includedData.withNationalAviationSystemTime()) {
                    delayTimeDataJson.addProperty("nationalAviationSystemTime",
                            delayTimeData.getNationalAviationSystemTime());
                }

                if (includedData.withTotalTime()) {
                    delayTimeDataJson.addProperty("totalTime", delayTimeData.getTotalTime());
                }

                jsonObject.add("delayTimeData", delayTimeDataJson);

            }

            jsonArray.add(jsonObject);
        }

        return jsonArray.toString();
    }

    @Override
    public List<Statistic> stringToStatistics(String statisticsData, Airport airport, Carrier carrier, Integer year,
                                              Integer month) {
        List<Statistic> statistics = new ArrayList<>();
        JsonArray jsonArray = gson.fromJson(statisticsData, JsonArray.class);

        Airport tempAirport = airport;
        Carrier tempCarrier = carrier;
        Integer tempYear = year;
        Integer tempMonth = month;

        for (JsonElement jsonElement : jsonArray) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            if (airport == null) {
                tempAirport =  new Airport(jsonObject.get("airport").getAsJsonObject().get("code").getAsString(), null);
            }

            if (carrier == null) {
                tempCarrier = new Carrier(jsonObject.get("carrier").getAsJsonObject().get("code").getAsString(), null);
            }

            if (year == null) {
                tempYear = jsonObject.get("year").getAsInt();
            }

            if (month == null) {
                tempMonth = jsonObject.get("month").getAsInt();
            }

            FlightData flightData = gson.fromJson(jsonObject.get("flightData"), FlightData.class);
            DelayData delayData = gson.fromJson(jsonObject.get("delayData"), DelayData.class);
            DelayTimeData delayTimeData = gson.fromJson(jsonObject.get("delayTimeData"), DelayTimeData.class);

            if (flightData == null || delayData == null || delayTimeData == null) {
                throw new JsonParseException("Missing data in JSON string.");
            }

            Statistic statistic = new Statistic(tempAirport, tempCarrier, tempYear, tempMonth, flightData, delayData,
                    delayTimeData);
            statistics.add(statistic);
        }

        return statistics;
    }

    @Override
    public String extraStatisticsToString(List<ExtraStatistic> extraStatistics, boolean withCarrier) {
        JsonArray jsonArray = new JsonArray();

        for (ExtraStatistic extraStatistic : extraStatistics) {
            JsonObject jsonObject = new JsonObject();

            if (withCarrier) {
                jsonObject.add("carrier", gson.toJsonTree(extraStatistic.getCarrier()));
            }

            jsonObject.addProperty("lateAircraftTimeMean", extraStatistic.getLateAircraftTimeMean());
            jsonObject.addProperty("lateAircraftTimeMedian", extraStatistic.getLateAircraftTimeMedian());
            jsonObject.addProperty("lateAircraftTimeSd", extraStatistic.getLateAircraftTimeSd());

            jsonObject.addProperty("carrierTimeMean", extraStatistic.getCarrierTimeMean());
            jsonObject.addProperty("carrierTimeMedian", extraStatistic.getCarrierTimeMedian());
            jsonObject.addProperty("carrierTimeSd", extraStatistic.getCarrierTimeSd());

            jsonArray.add(jsonObject);
        }

        return jsonArray.toString();
    }
/*
    public String linksToString(List<Link> links) {
        JsonArray jsonLinks = gson.toJsonTree(links).getAsJsonArray();

        JsonObject jsonObject = new JsonObject();
        jsonObject.add("links", jsonLinks);

        return jsonObject.toString();
    }
*/
    public String mergeLinksAndJson(List<Link> links, String json) {
        JsonObject jsonObject = new JsonObject();

        if (json != null) {
            JsonElement jsonElement = gson.fromJson(json, JsonElement.class);
            jsonObject.add("content", jsonElement);
        }

        if (links != null) {
            JsonArray jsonLinks = gson.toJsonTree(links).getAsJsonArray();
            jsonObject.add("links", jsonLinks);
        }

        return jsonObject.toString();
    }
}
