package rest;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import converters.StatisticDataSelectorHelper;
import models.ExtraStatistic;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import converters.CsvConverter;
import converters.JsonConverter;
import models.Airport;
import models.Carrier;
import models.Statistic;
import database.DatabaseConnector;

@RestController
public class MainRestController {
    private DatabaseConnector databaseConnector;
    private JsonConverter jsonConverter;
    private CsvConverter csvConverter;

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
                                                               StatisticDataSelectorHelper dataSelector) {
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
                responseBody = csvConverter.StatisticsToString(statistics, dataSelector);
            } catch (IOException e) {
                e.printStackTrace();
                responseBody = "Something went wrong!";
                return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            responseHeaders.set("Content-Type", "application/json");
            responseBody = jsonConverter.StatisticsToString(statistics, dataSelector);
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
                responseBody = csvConverter.ExtraStatisticsToString(extraStatistics, carrierCode == null);
            } catch (IOException e) {
                e.printStackTrace();
                responseBody = "Something went wrong!";
                return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            responseHeaders.set("Content-Type", "application/json");
            responseBody = jsonConverter.ExtraStatisticsToString(extraStatistics, carrierCode == null);
        }

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
                responseBody = csvConverter.AirportsToString(airports);
            } catch (IOException e) {
                e.printStackTrace();
                responseBody = "Something went wrong!";
                return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            responseHeaders.set("Content-Type", "application/json");
            responseBody = jsonConverter.AirportsToString(airports);
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
                responseBody = csvConverter.CarriersToString(carriers);
            } catch (IOException e) {
                e.printStackTrace();
                responseBody = "Something went wrong!";
                return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            responseBody = jsonConverter.CarriersToString(carriers);
            responseHeaders.set("Content-Type", "application/json");
        }

        return new ResponseEntity<>(responseBody, responseHeaders, HttpStatus.OK);
    }

    @GetMapping("/airports/{airportCode}/carriers")
    public ResponseEntity<String> getCarriersAtAirport(@PathVariable String airportCode,
                                                       @RequestHeader("Accept") String mediaType) {
        HttpHeaders responseHeaders = new HttpHeaders();
        String responseBody;

        Airport airport = new Airport(airportCode, null);


        List<Carrier> carriers;

        if (!databaseConnector.hasAirport(airport)) {
            responseBody = "Airport with code " + airportCode + " does not exist!";
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        }

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
                responseBody = csvConverter.CarriersToString(carriers);
            } catch (IOException e) {
                e.printStackTrace();
                responseBody = "Something went wrong!";
                return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            responseHeaders.set("Content-Type", "application/json");
            responseBody = jsonConverter.CarriersToString(carriers);
        }

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

        return createGetStatisticsResponse(airportCode, carrierCode, year, month, acceptHeader, dataSelector);
    }


    @PostMapping("/airports/carriers/stats")
    public ResponseEntity<String> postStats(@RequestHeader("Content-Type") String mediaType, @RequestBody String data) {
        String responseBody;

        List<Statistic> statistics;

        if (mediaType.equals("text/csv")) {
            try {
                statistics = csvConverter.StringToStatistics(data, null, null, null, null);
            } catch (Exception e) {
                e.printStackTrace();
                responseBody = "Syntax error in CSV string.";
                return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
            }
        } else {
            try {
                statistics = jsonConverter.StringToStatistics(data, null, null, null, null);
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
                statistics = csvConverter.StringToStatistics(data, airport, carrier, year, month);
            } catch (Exception e) {
                e.printStackTrace();
                responseBody = "Syntax error in CSV string.";
                return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
            }
        } else {
            try {
                statistics = jsonConverter.StringToStatistics(data, airport, carrier, year, month);
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

        return createGetStatisticsResponse(airportCode, carrierCode, year, month, acceptHeader, dataSelector);
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

        return createGetStatisticsResponse(airportCode, carrierCode, year, month, acceptHeader, dataSelector);
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

        return createGetStatisticsResponse(null, carrierCode, year, month, acceptHeader, dataSelector);
    }

    @GetMapping("/airports/{airportCode1}/{airportCode2}/carriers/{carrierCode}/extra-stats")
    public ResponseEntity<String> getExtraStats(@PathVariable String airportCode1,
                                                @PathVariable String airportCode2,
                                                @PathVariable(required = false) String carrierCode,
                                                @RequestHeader("Accept") String acceptHeader) {
        return createGetExtraStatisticsResponse(airportCode1, airportCode2, carrierCode, acceptHeader);
    }

    @GetMapping("/airports/{airportCode1}/{airportCode2}/carriers/extra-stats")
    public ResponseEntity<String> getExtraStats(@PathVariable String airportCode1,
                                                @PathVariable String airportCode2,
                                                @RequestHeader("Accept") String acceptHeader) {
        return createGetExtraStatisticsResponse(airportCode1, airportCode2, null, acceptHeader);
    }
}
