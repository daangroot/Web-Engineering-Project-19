package rest;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import converters.StatisticDataSelectorHelper;
import models.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import converters.CsvConverter;
import converters.JsonConverter;
import database.DatabaseConnector;

@RestController
public class MainRestController {
    private DatabaseConnector databaseConnector;
    private JsonConverter jsonConverter;
    private CsvConverter csvConverter;
    private final String URI = "http://94.212.164.28:8080/";

    public MainRestController() {
        try {
            databaseConnector = new DatabaseConnector();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }

        jsonConverter = new JsonConverter();
        csvConverter = new CsvConverter();
    }

    private StatisticDataSelectorHelper initializeDataSelector(boolean withAirport, boolean withCarrier, Integer year,
                                                               Integer month, List<String> delayTypes) {
        StatisticDataSelectorHelper dataSelector;

        if (month != null && year != null) {
            dataSelector = new StatisticDataSelectorHelper(withAirport, withCarrier, false, false);
        } else if (year != null) {
            dataSelector = new StatisticDataSelectorHelper(withAirport, withCarrier, false, true);
        } else if (month != null) {
            dataSelector = new StatisticDataSelectorHelper(withAirport, withCarrier, true, false);
        } else {
            dataSelector = new StatisticDataSelectorHelper(withAirport, withCarrier, true, true);
        }

        if (delayTypes != null && delayTypes.isEmpty()) {
            // No reasons given, select all reasons.
            dataSelector.setDelayTimeData(true, true, true, true, true, true);
        } else if (delayTypes != null) {
            boolean lateAircraftTime = false;
            boolean carrierTime = false;
            boolean weatherTime = false;
            boolean securityTime = false;
            boolean nationalAviationSystemTime = false;
            boolean totalTime = false;

            for (String delayReason : delayTypes) {
                switch (delayReason) {
                    case "late-aircraft":
                        lateAircraftTime = true;
                        break;
                    case "carrier":
                        carrierTime = true;
                        break;
                    case "weather":
                        weatherTime = true;
                        break;
                    case "security":
                        securityTime = true;
                        break;
                    case "national-aviation-system":
                        nationalAviationSystemTime = true;
                        break;
                    case "total":
                        totalTime = true;
                        break;
                }
            }

            dataSelector.setDelayTimeData(lateAircraftTime, carrierTime, weatherTime, securityTime,
                        nationalAviationSystemTime, totalTime);
        }

        return dataSelector;
    }

    private ResponseEntity<String> createGetStatisticsResponse(String airportCode, String carrierCode, Integer year,
                                                               Integer month, String acceptHeader,
                                                               StatisticDataSelectorHelper dataSelector,
                                                               List<Link> links) {
        String responseBody;

        Airport airport = null;
        Carrier carrier = null;

        if (airportCode != null) {
            airport = new Airport(airportCode, null);

            if (!databaseConnector.hasAirport(airport)) {
                responseBody = "Airport with code " + airportCode + " does not exist!";
                return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
            }
        }

        if (carrierCode != null) {
            carrier = new Carrier(carrierCode, null);

            if (!databaseConnector.hasCarrier(carrier)) {
                responseBody = "Carrier with code " + carrierCode + " does not exist!";
                return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
            }
        }

        if (year != null) {
            if (year < 1901) {
                responseBody = "Year can not be smaller than 1901.";
                return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
            }

            if (year > Calendar.getInstance().get(Calendar.YEAR)) {
                responseBody = "Year can not be greater than the current year.";
                return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
            }
        }

        if (month != null) {
            if (month < 1) {
                responseBody = "Month can not be smaller than 1.";
                return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
            }

            if (month > 12) {
                responseBody = "Month can not be greater than 12.";
                return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
            }

            if (year != null && year == Calendar.getInstance().get(Calendar.YEAR)) {
                if (month > Calendar.getInstance().get(Calendar.MONTH)) {

                    responseBody = "Month must be smaller than the current month.";
                    return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
                }
            }
        }

        List<Statistic> statistics;
        try {
            statistics = databaseConnector.getStatistics(airport, carrier, year, month, dataSelector);
        } catch (SQLException e) {
            e.printStackTrace();
            responseBody = "Something went wrong!";
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        if (acceptHeader.equals("text/csv")) {
            responseHeaders.set("Content-Type", "text/csv");
            try {
                responseBody = csvConverter.statisticsToString(statistics, dataSelector);
            } catch (IOException e) {
                e.printStackTrace();
                responseBody = "Something went wrong!";
                return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            responseHeaders.set("Content-Type", "application/json");
            String json = jsonConverter.statisticsToString(statistics, dataSelector);
            responseBody = jsonConverter.mergeLinksAndJson(links, json);
        }

        return new ResponseEntity<>(responseBody, responseHeaders, HttpStatus.OK);
    }

    private ResponseEntity<String> createGetExtraStatisticsResponse(String airportCode1, String airportCode2,
                                                                    String carrierCode, String acceptHeader) {
        HttpHeaders responseHeaders = new HttpHeaders();
        String responseBody;

        Airport airport1 = new Airport(airportCode1, null);
        Airport airport2 = new Airport(airportCode2, null);
        Carrier carrier = (carrierCode == null) ? null : new Carrier(carrierCode, null);

        if (!databaseConnector.hasAirport(airport1)) {
            responseBody = "Airport with code " + airportCode1 + " does not exist!";
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        }

        if (!databaseConnector.hasAirport(airport2)) {
            responseBody = "Airport with code " + airportCode2 + " does not exist!";
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        }

        if (carrier != null && !databaseConnector.hasCarrier(carrier)) {
            responseBody = "Carrier with code " + carrierCode + " does not exist!";
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        }

        List<ExtraStatistic> extraStatistics;

        try {
            extraStatistics = databaseConnector.getExtraStatistics(airport1, airport2, carrier);
        } catch (SQLException e) {
            e.printStackTrace();
            responseBody = "Something went wrong!";
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (acceptHeader.equals("text/csv")) {
            responseHeaders.set("Content-Type", "text/csv");
            try {
                responseBody = csvConverter.extraStatisticsToString(extraStatistics, carrierCode == null);
            } catch (IOException e) {
                e.printStackTrace();
                responseBody = "Something went wrong!";
                return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            responseHeaders.set("Content-Type", "application/json");
            responseBody = jsonConverter.extraStatisticsToString(extraStatistics, carrierCode == null);
        }

        return new ResponseEntity<>(responseBody, responseHeaders, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<String> getRoot() {
        ArrayList<Link> links = new ArrayList<>();

        links.add(new Link(URI + "airports/", "airports", "GET"));
        links.add(new Link(URI + "carriers/", "carriers", "GET"));

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "application/json");
        String responseBody = jsonConverter.mergeLinksAndJson(links, null);

        return new ResponseEntity<>(responseBody, responseHeaders, HttpStatus.OK);
    }

    @GetMapping("/airports")
    public ResponseEntity<String> getAirports(@RequestHeader("Accept") String mediaType) {
        HttpHeaders responseHeaders = new HttpHeaders();
        String responseBody;
        List<Airport> airports;

        try {
            airports = databaseConnector.getAirports();
        } catch (SQLException e) {
            e.printStackTrace();
            responseBody = "Something went wrong!";
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (mediaType.equals("text/csv")) {
            responseHeaders.set("Content-Type", "text/csv");
            try {
                responseBody = csvConverter.airportsToString(airports);
            } catch (IOException e) {
                e.printStackTrace();
                responseBody = "Something went wrong!";
                return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            responseHeaders.set("Content-Type", "application/json");
            String jsonAirports = jsonConverter.airportsToStringWithLinks(airports, URI + "airports/");

            List<Link> links = new ArrayList<>();
            links.add(new Link(URI + "airports/carriers", "stats", "POST"));

            responseBody = jsonConverter.mergeLinksAndJson(links, jsonAirports);
        }

        return new ResponseEntity<>(responseBody, responseHeaders, HttpStatus.OK);
    }

    @GetMapping("/carriers")
    public ResponseEntity<String> getCarriers(@RequestHeader("Accept") String mediaType) {
        HttpHeaders responseHeaders = new HttpHeaders();
        String responseBody;
        List<Carrier> carriers;

        try {
            carriers = databaseConnector.getCarriers();
        } catch (SQLException e) {
            e.printStackTrace();
            responseBody = "Something went wrong!";
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (mediaType.equals("text/csv")) {
            responseHeaders.set("Content-Type", "text/csv");
            try {
                responseBody = csvConverter.carriersToString(carriers);
            } catch (IOException e) {
                e.printStackTrace();
                responseBody = "Something went wrong!";
                return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            responseHeaders.set("Content-Type", "application/json");
            String jsonCarriers = jsonConverter.carriersToString(carriers);
            responseBody = jsonConverter.mergeLinksAndJson(null, jsonCarriers);
        }

        return new ResponseEntity<>(responseBody, responseHeaders, HttpStatus.OK);
    }

    @GetMapping("/airports/{airportCode}")
    public ResponseEntity<String> getAirport(@PathVariable String airportCode) {
        String responseBody;
        HttpHeaders responseHeaders = new HttpHeaders();

        if (!databaseConnector.hasAirport(new Airport(airportCode, null))) {
            responseBody = "Airport with code " + airportCode + " does not exist!";
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        }

        ArrayList<Link> links = new ArrayList<>();

        String baseHref = URI + "airports/" + airportCode + "/";
        links.add(new Link(baseHref + "carriers/", "carriers", "GET"));

        List<Airport> airports;
        try {
            airports = databaseConnector.getAirports();
        } catch (SQLException e) {
            e.printStackTrace();
            responseBody = "Something went wrong!";
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        for (Airport airport : airports) {
            if (!airport.getCode().equals(airportCode)) {
                links.add(new Link(baseHref + airport.getCode() + "/", "airport", "GET"));
            }
        }

        responseHeaders.set("Content-Type", "application/json");
        responseBody = jsonConverter.mergeLinksAndJson(links, null);

        return new ResponseEntity<>(responseBody, responseHeaders, HttpStatus.OK);
    }


    @GetMapping("/airports/{airportCode}/carriers")
    public ResponseEntity<String> getCarriersAtAirport(@PathVariable String airportCode,
                                                       @RequestHeader("Accept") String mediaType) {
        HttpHeaders responseHeaders = new HttpHeaders();
        String responseBody;

        Airport airport = new Airport(airportCode, null);
        if (!databaseConnector.hasAirport(airport)) {
            responseBody = "Airport with code " + airportCode + " does not exist!";
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        }

        List<Carrier> carriers;
        try {
            carriers = databaseConnector.getCarriersAtAirport(airport);
        } catch (SQLException e) {
            e.printStackTrace();
            responseBody = "Something went wrong!";
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (mediaType.equals("text/csv")) {
            responseHeaders.set("Content-Type", "text/csv");
            try {
                responseBody = csvConverter.carriersToString(carriers);
            } catch (IOException e) {
                e.printStackTrace();
                responseBody = "Something went wrong!";
                return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            responseHeaders.set("Content-Type", "application/json");
            String baseHref = URI + "airports/" + airportCode + "/carriers/";
            String jsonCarriers = jsonConverter.carriersToStringWithLinks(carriers, baseHref);
            responseBody = jsonConverter.mergeLinksAndJson(null, jsonCarriers);
        }

        return new ResponseEntity<>(responseBody, responseHeaders, HttpStatus.OK);
    }

    @GetMapping("/airports/{airportCode}/carriers/{carrierCode}")
    public ResponseEntity<String> getAirportCarrier(@PathVariable String airportCode, @PathVariable String carrierCode) {
        ArrayList<Link> links = new ArrayList<>();

        String baseHref = URI + "airports/" + airportCode + "/carriers/" + carrierCode + "/";
        links.add(new Link(baseHref + "stats/", "stats", "GET"));
        links.add(new Link(baseHref + "stats/", "stats", "PUT"));
        links.add(new Link(baseHref + "stats/", "stats", "DELETE"));

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "application/json");
        String responseBody = jsonConverter.mergeLinksAndJson(links, null);

        return new ResponseEntity<>(responseBody, responseHeaders, HttpStatus.OK);
    }

    @GetMapping("/airports/{airportCode}/carriers/{carrierCode}/stats")
    public ResponseEntity<String> getStats(@PathVariable String airportCode, @PathVariable String carrierCode,
                                           @RequestParam(value = "year", required = false) Integer year,
                                           @RequestParam(value = "month", required = false) Integer month,
                                           @RequestHeader("Accept") String acceptHeader) {
        StatisticDataSelectorHelper dataSelector = initializeDataSelector(false, false, year, month, null);
        dataSelector.setFlightDataTrue();
        dataSelector.setDelayDataTrue();
        dataSelector.setDelayDataTimeTrue();

        List<Link> links = new ArrayList<>();
        String baseHref = URI + "airports/" + airportCode + "/carriers/" + carrierCode + "/stats/";
        links.add(new Link(baseHref + "flight/", "flight", "GET"));
        links.add(new Link(baseHref + "delay-time/", "delay-time", "GET"));

        return createGetStatisticsResponse(airportCode, carrierCode, year, month, acceptHeader, dataSelector, links);
    }


    @PostMapping("/airports/carriers/stats")
    public ResponseEntity<String> postStats(@RequestHeader("Content-Type") String mediaType, @RequestBody String data) {
        String responseBody;

        List<Statistic> statistics;

        if (mediaType.equals("text/csv")) {
            try {
                statistics = csvConverter.stringToStatistics(data, null, null, null, null);
            } catch (Exception e) {
                e.printStackTrace();
                responseBody = "Syntax error in CSV string.";
                return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
            }
        } else {
            try {
                statistics = jsonConverter.stringToStatistics(data, null, null, null, null);
            } catch (Exception e) {
                e.printStackTrace();
                responseBody = "Syntax error in JSON string.";
                return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
            }
        }

        try {
            databaseConnector.addStatistics(statistics);
        } catch (SQLException e) {
            e.printStackTrace();
            responseBody = "Something went wrong!";
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/airports/{airportCode}/carriers/{carrierCode}/stats")
    public ResponseEntity<String> putStats(@PathVariable String airportCode, @PathVariable String carrierCode,
                                           @RequestParam(value = "year", required = false) Integer year,
                                           @RequestParam(value = "month", required = false) Integer month,
                                           @RequestHeader("Content-Type") String mediaType, @RequestBody String data) {
        String responseBody;

        Airport airport = new Airport(airportCode, null);
        Carrier carrier = new Carrier(carrierCode, null);

        List<Statistic> statistics;

        if (!databaseConnector.hasAirport(airport)) {
            responseBody = "Airport with code " + airportCode + " does not exist!";
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        }

        if (!databaseConnector.hasCarrier(carrier)) {
            responseBody = "Carrier with code " + carrierCode + " does not exist!";
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        }

        if (year != null) {
            if (year < 1901) {
                responseBody = "Year can not be smaller than 1901.";
                return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
            }

            if (year > Calendar.getInstance().get(Calendar.YEAR)) {
                responseBody = "Year can not be greater than the current year.";
                return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
            }
        }

        if (month != null) {
            if (month < 1) {
                responseBody = "Month can not be smaller than 1.";
                return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
            }

            if (month > 12) {
                responseBody = "Month can not be greater than 12.";
                return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
            }

            if (year != null && year == Calendar.getInstance().get(Calendar.YEAR)) {
                if (month > Calendar.getInstance().get(Calendar.MONTH)) {

                    responseBody = "Month must be smaller than the current month.";
                    return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
                }
            }
        }

        if (mediaType.equals("text/csv")) {
            try {
                statistics = csvConverter.stringToStatistics(data, airport, carrier, year, month);
            } catch (Exception e) {
                e.printStackTrace();
                responseBody = "Syntax error in CSV string.";
                return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
            }
        } else {
            try {
                statistics = jsonConverter.stringToStatistics(data, airport, carrier, year, month);
            } catch (Exception e) {
                e.printStackTrace();
                responseBody = "Syntax error in JSON string.";
                return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
            }
        }

        try {
            databaseConnector.updateStatistics(statistics);
        } catch (SQLException e) {
            e.printStackTrace();
            responseBody = "Something went wrong!";
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/airports/{airportCode}/carriers/{carrierCode}/stats")
    public ResponseEntity<String> deleteStats(@PathVariable String airportCode, @PathVariable String carrierCode,
                                              @RequestParam(value = "year", required = false) Integer year,
                                              @RequestParam(value = "month", required = false) Integer month) {
        String responseBody;

        Airport airport = new Airport(airportCode, null);
        Carrier carrier = new Carrier(carrierCode, null);

        if (!databaseConnector.hasAirport(airport)) {
            responseBody = "Airport with code " + airportCode + " does not exist!";
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        }

        if (!databaseConnector.hasCarrier(carrier)) {
            responseBody = "Carrier with code " + carrierCode + " does not exist!";
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        }

        if (year != null) {
            if (year < 1901) {
                responseBody = "Year can not be smaller than 1901.";
                return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
            }

            if (year > Calendar.getInstance().get(Calendar.YEAR)) {
                responseBody = "Year can not be greater than the current year.";
                return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
            }
        }

        if (month != null) {
            if (month < 1) {
                responseBody = "Month can not be smaller than 1.";
                return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
            }

            if (month > 12) {
                responseBody = "Month can not be greater than 12.";
                return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
            }

            if (year != null && year == Calendar.getInstance().get(Calendar.YEAR)) {
                if (month > Calendar.getInstance().get(Calendar.MONTH)) {

                    responseBody = "Month must be smaller than the current month.";
                    return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
                }
            }
        }

        try {
            databaseConnector.deleteStatistics(airport, carrier, year, month);
        } catch (SQLException e) {
            e.printStackTrace();
            responseBody = "Something went wrong!";
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/airports/{airportCode}/carriers/{carrierCode}/stats/flight")
    public ResponseEntity<String> getFlightData(@PathVariable String airportCode, @PathVariable String carrierCode,
                                                @RequestParam(value = "year", required = false) Integer year,
                                                @RequestParam(value = "month", required = false) Integer month,
                                                @RequestHeader("Accept") String acceptHeader) {
        StatisticDataSelectorHelper dataSelector = initializeDataSelector(false, false, year, month, null);
        dataSelector.setFlightData(true, true, true, false, false);

        return createGetStatisticsResponse(airportCode, carrierCode, year, month, acceptHeader, dataSelector, null);
    }

    @GetMapping("/airports/carriers/{carrierCode}/stats/delay-time")
    public ResponseEntity<String> getDelayTime(@PathVariable String carrierCode,
                                               @RequestParam(value = "type", required = false)
                                                       List<String> delayTypes,
                                               @RequestParam(value = "year", required = false) Integer year,
                                               @RequestParam(value = "month", required = false) Integer month,
                                               @RequestHeader("Accept") String acceptHeader) {
        if (delayTypes == null) {
            delayTypes = new ArrayList<>();
        }

        StatisticDataSelectorHelper dataSelector = initializeDataSelector(true, false, year, month,
                delayTypes);

        return createGetStatisticsResponse(null, carrierCode, year, month, acceptHeader, dataSelector, null);
    }

    @GetMapping("/airports/{airportCode}/carriers/{carrierCode}/stats/delay-time")
    public ResponseEntity<String> getDelayTime(@PathVariable String airportCode, @PathVariable String carrierCode,
                                               @RequestParam(value = "type", required = false)
                                                       List<String> delayTypes,
                                               @RequestParam(value = "year", required = false) Integer year,
                                               @RequestParam(value = "month", required = false) Integer month,
                                               @RequestHeader("Accept") String acceptHeader) {
        if (delayTypes == null) {
            delayTypes = new ArrayList<>();
        }

        StatisticDataSelectorHelper dataSelector = initializeDataSelector(false, false, year, month,
                delayTypes);

        return createGetStatisticsResponse(airportCode, carrierCode, year, month, acceptHeader, dataSelector, null);
    }

    @GetMapping("/airports/{airportCode1}/{airportCode2}")
    public ResponseEntity<String> get2Airports(@PathVariable String airportCode1, @PathVariable String airportCode2) {
        String responseBody;
        HttpHeaders responseHeaders = new HttpHeaders();

        Airport airport1 = new Airport(airportCode1, null);
        if (!databaseConnector.hasAirport(airport1)) {
            responseBody = "Airport with code " + airportCode1 + " does not exist!";
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        }

        Airport airport2 = new Airport(airportCode2, null);
        if (!databaseConnector.hasAirport(airport2)) {
            responseBody = "Airport with code " + airportCode2 + " does not exist!";
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        }

        ArrayList<Link> links = new ArrayList<>();

        String baseHref = URI + "airports/" + airportCode1 + "/" + airportCode2 + "/";
        links.add(new Link(baseHref + "carriers/", "carriers", "GET"));

        responseHeaders.set("Content-Type", "application/json");
        responseBody = jsonConverter.mergeLinksAndJson(links, null);

        return new ResponseEntity<>(responseBody, responseHeaders, HttpStatus.OK);
    }

    @GetMapping("/airports/{airportCode1}/{airportCode2}/carriers")
    public ResponseEntity<String> get2AirportsCarriers(@PathVariable String airportCode1,
                                                       @PathVariable String airportCode2) {
        String responseBody;
        HttpHeaders responseHeaders = new HttpHeaders();

        Airport airport1 = new Airport(airportCode1, null);
        if (!databaseConnector.hasAirport(airport1)) {
            responseBody = "Airport with code " + airportCode1 + " does not exist!";
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        }

        Airport airport2 = new Airport(airportCode2, null);
        if (!databaseConnector.hasAirport(airport2)) {
            responseBody = "Airport with code " + airportCode2 + " does not exist!";
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        }

        ArrayList<Link> links = new ArrayList<>();
        String baseHref = URI + "airports/" + airportCode1 + "/" + airportCode2 + "/carriers/";
        links.add(new Link(baseHref + "extra-stats/", "extra-stats", "GET"));

        List<Carrier> carriers;
        try {
            carriers = databaseConnector.getCarriers();
        } catch (SQLException e) {
            e.printStackTrace();
            responseBody = "Something went wrong!";
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        responseHeaders.set("Content-Type", "application/json");
        String jsonCarriers = jsonConverter.carriersToStringWithLinks(carriers, baseHref);
        responseBody = jsonConverter.mergeLinksAndJson(links, jsonCarriers);

        return new ResponseEntity<>(responseBody, responseHeaders, HttpStatus.OK);
    }

    @GetMapping("/airports/{airportCode1}/{airportCode2}/carriers/{carrierCode}")
    public ResponseEntity<String> get2AirportsCarrier(@PathVariable String airportCode1,
                                                      @PathVariable String airportCode2,
                                                      @PathVariable String carrierCode) {
        String responseBody;
        HttpHeaders responseHeaders = new HttpHeaders();

        Airport airport1 = new Airport(airportCode1, null);
        if (!databaseConnector.hasAirport(airport1)) {
            responseBody = "Airport with code " + airportCode1 + " does not exist!";
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        }

        Airport airport2 = new Airport(airportCode2, null);
        if (!databaseConnector.hasAirport(airport2)) {
            responseBody = "Airport with code " + airportCode2 + " does not exist!";
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        }

        Carrier carrier = new Carrier(carrierCode, null);
        if (!databaseConnector.hasCarrier(carrier)) {
            responseBody = "Carrier with code " + carrierCode + " does not exist!";
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        }

        ArrayList<Link> links = new ArrayList<>();

        String baseHref = URI + "airports/" + airportCode1 + "/" + airportCode2 + "/carriers/" + carrierCode + "/";
        links.add(new Link(baseHref + "extra-stats/", "extra-stats", "GET"));

        responseHeaders.set("Content-Type", "application/json");
        responseBody = jsonConverter.mergeLinksAndJson(links, null);

        return new ResponseEntity<>(responseBody, responseHeaders, HttpStatus.OK);
    }

    @GetMapping("/airports/{airportCode1}/{airportCode2}/carriers/extra-stats")
    public ResponseEntity<String> getExtraStats(@PathVariable String airportCode1,
                                                @PathVariable String airportCode2,
                                                @RequestHeader("Accept") String acceptHeader) {
        return createGetExtraStatisticsResponse(airportCode1, airportCode2, null, acceptHeader);
    }

    @GetMapping("/airports/{airportCode1}/{airportCode2}/carriers/{carrierCode}/extra-stats")
    public ResponseEntity<String> getExtraStats(@PathVariable String airportCode1,
                                                @PathVariable String airportCode2,
                                                @PathVariable(required = false) String carrierCode,
                                                @RequestHeader("Accept") String acceptHeader) {
        return createGetExtraStatisticsResponse(airportCode1, airportCode2, carrierCode, acceptHeader);
    }
}
