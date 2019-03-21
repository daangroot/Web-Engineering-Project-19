package rest;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    private Map<String, String> airportsMap;
    private Map<String, String> carriersMap;
    
    public MainRestController() {
        try {
            databaseConnector = new DatabaseConnector();
            
            airportsMap = new HashMap<>();
            List<Airport> airports = databaseConnector.getAirports();
            for (Airport airport : airports) {
                airportsMap.put(airport.getCode(), airport.getName());
            }
            
            carriersMap = new HashMap<>();
            List<Carrier> carriers = databaseConnector.getCarriers();
            for (Carrier carrier : carriers) {
                carriersMap.put(carrier.getCode(), carrier.getName());
            }
        } catch (PropertyVetoException | SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        
        jsonConverter = new JsonConverter();
        csvConverter  = new CsvConverter();
    }
    
    @GetMapping("/airports")
    public ResponseEntity<String> getAirports(@RequestHeader("Accept") String mediaType) {
        List<Airport> airports = new ArrayList<>();

        for (Map.Entry<String, String> item : airportsMap.entrySet()) {
            String code = item.getKey();
            String name = item.getValue();
            airports.add(new Airport(code, name));
        }
        
        HttpHeaders responseHeaders = new HttpHeaders();
        String responseBody;
        
        if (mediaType.equals("text/csv")) {
            responseHeaders.set("Content-Type", "text/csv");
            try {
                responseBody = csvConverter.AirportsToString(airports);
            } catch (Exception e) {
                e.printStackTrace();
                responseBody = "Something went wrong.";
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
        List<Carrier> carriers = new ArrayList<>();

        for (Map.Entry<String, String> item : carriersMap.entrySet()) {
            String code = item.getKey();
            String name = item.getValue();
            carriers.add(new Carrier(code, name));
        }
        
        HttpHeaders responseHeaders = new HttpHeaders();
        String responseBody;
        
        if (mediaType.equals("text/csv")) {
            responseHeaders.set("Content-Type", "text/csv");
            try {
                responseBody = csvConverter.CarriersToString(carriers);
            } catch (IOException e) {
                e.printStackTrace();
                responseBody = "Something went wrong.";
                return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            responseBody = jsonConverter.CarriersToString(carriers);
            responseHeaders.set("Content-Type", "application/json");
        }
       
        return new ResponseEntity<>(responseBody, responseHeaders, HttpStatus.OK);
    }
    
    @GetMapping("/airports/{airportCode}/carriers")
    public ResponseEntity<String> getCarriersAtAirport(@RequestHeader("Accept") String mediaType, @PathVariable String airportCode) {
        String responseBody;
        
        if (!airportsMap.containsKey(airportCode)) {
            responseBody = "Airport with code " + airportCode + " does not exist.";
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        }
        
        List<Carrier> carriers;
        
        try {
            carriers = databaseConnector.getCarriersAtAirport(new Airport(airportCode, null));
        } catch (SQLException e) {
            e.printStackTrace();
            responseBody = "Something went wrong.";
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }        
        
        HttpHeaders responseHeaders = new HttpHeaders();
        
        if (mediaType.equals("text/csv")) {
            try {
                responseBody = csvConverter.CarriersToString(carriers);
            } catch (IOException e) {
                e.printStackTrace();
                responseBody = "Something went wrong.";
                return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            responseHeaders.set("Content-Type", "text/csv");
        } else {
            responseBody = jsonConverter.CarriersToString(carriers);
            responseHeaders.set("Content-Type", "application/json");
        }
       
        return new ResponseEntity<>(responseBody, responseHeaders, HttpStatus.OK);
    }
    
    @GetMapping("/airports/{airportCode}/carriers/{carrierCode}/stats")
    public ResponseEntity<String> getStats(@RequestHeader("Accept") String mediaType, @PathVariable String airportCode, @PathVariable String carrierCode,
                                        @RequestParam(value="year", required=false) Integer year, @RequestParam(value="month", required=false) Integer month) {        
        String responseBody;
        
        if (!airportsMap.containsKey(airportCode)) {
            responseBody = "Airport with code " + airportCode + " does not exist.";
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        }
        
        if (!carriersMap.containsKey(carrierCode)) {
            responseBody = "Carrier with code " + carrierCode + " does not exist.";
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
        
        Airport airport = new Airport(airportCode, null);
        Carrier carrier = new Carrier(carrierCode, null);
        
        List<Statistic> statistics = null;
        Statistic statistic = null;
        
        try {
            if (year != null && month != null) {
                YearMonth yearMonth = YearMonth.of(year, month);
                statistic = databaseConnector.getStatistic(airport, carrier, yearMonth);
            }  else if (year != null) {
                statistics = databaseConnector.getStatisticsInYear(airport, carrier, year);
            } else if (month != null) {
                statistics = databaseConnector.getStatisticsInMonth(airport, carrier, month);
            } else {
                statistics = databaseConnector.getStatistics(airport, carrier);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            responseBody = "Something went wrong.";
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        HttpHeaders responseHeaders = new HttpHeaders();
        
        if (mediaType.equals("text/csv")) {
            try {
                if (statistics != null) {
                    responseBody = csvConverter.StatisticsToString(statistics, false, false, true);
                } else {
                    responseBody = csvConverter.StatisticToString(statistic, false, false, false);
                }
            } catch (IOException e) {
                e.printStackTrace();
                responseBody = "Something went wrong.";
                return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            responseHeaders.set("Content-Type", "text/csv");
        } else {
            if (statistics != null) {
                responseBody = jsonConverter.StatisticsToString(statistics, false, false, true);
            } else {
                responseBody = jsonConverter.StatisticToString(statistic, false, false, false);
            }
            responseHeaders.set("Content-Type", "application/json");
        }
        
        return new ResponseEntity<>(responseBody, responseHeaders, HttpStatus.OK);
    }
    
    @PostMapping("/airports/carriers/stats")
    public ResponseEntity<String> postStats(@RequestHeader("Content-Type") String mediaType, @RequestBody String data) {
        String responseBody;
        
        List<Statistic> statistics = null;
        
        if (mediaType.equals("text/csv")) {
            try {
                statistics = csvConverter.StringToStatistics(data);
            } catch (Exception e) {
                e.printStackTrace();
                responseBody = "There is a syntax error in the CSV string.";
                return new ResponseEntity<String>(responseBody, HttpStatus.BAD_REQUEST);
            }
        } else {
            try {
                statistics = jsonConverter.StringToStatistics(data);
            } catch (Exception e) {
                e.printStackTrace();
                responseBody = "There is a syntax error in the JSON string.";
                return new ResponseEntity<String>(responseBody, HttpStatus.BAD_REQUEST);
            }
        }
        
        try {
            databaseConnector.addStatistics(statistics);
        } catch (SQLException e) {
            e.printStackTrace();
            responseBody = "Something went wrong. Most likely cause: duplicate entry.";
            return new ResponseEntity<String>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return new ResponseEntity<String>(HttpStatus.OK);
    }
    
    @PutMapping("/airports/{airportCode}/carriers/{carrierCode}/stats")
    public ResponseEntity<String> putStats(@RequestHeader("Content-Type") String mediaType, @PathVariable String airportCode, @PathVariable String carrierCode,
                                           @RequestParam(value="year", required=false) Integer year, @RequestParam(value="month", required=false) Integer month, @RequestBody String data) {
        String responseBody;
        
        if (!airportsMap.containsKey(airportCode)) {
            responseBody = "Airport with code " + airportCode + " does not exist.";
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        }
        
        if (!carriersMap.containsKey(carrierCode)) {
            responseBody = "Carrier with code " + carrierCode + " does not exist.";
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
        
        List<Statistic> statistics = null;
        
        if (mediaType.equals("text/csv")) {
            try {
                statistics = csvConverter.StringToStatistics(data);
            } catch (Exception e) {
                e.printStackTrace();
                responseBody = "There is a syntax error in the CSV string.";
                return new ResponseEntity<String>(responseBody, HttpStatus.BAD_REQUEST);
            }
        } else {
            try {
                statistics = jsonConverter.StringToStatistics(data);
            } catch (Exception e) {
                e.printStackTrace();
                responseBody = "There is a syntax error in the JSON string.";
                return new ResponseEntity<String>(responseBody, HttpStatus.BAD_REQUEST);
            }
        }
        
        
        try {
            databaseConnector.updateStatistics(statistics);
        } catch (SQLException e) {
            e.printStackTrace();
            responseBody = "Something went wrong.";
            return new ResponseEntity<String>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return new ResponseEntity<String>(HttpStatus.OK);    
        
    }
    
    @DeleteMapping("/airports/{airportCode}/carriers/{carrierCode}/stats")
    public ResponseEntity<String> deleteStats(@RequestHeader("Content-Type") String mediaType, @PathVariable String airportCode, @PathVariable String carrierCode,
                                           @RequestParam(value="year", required=false) Integer year, @RequestParam(value="month", required=false) Integer month, @RequestBody String data) {
        String responseBody;
        
        if (!airportsMap.containsKey(airportCode)) {
            responseBody = "Airport with code " + airportCode + " does not exist.";
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        }
        
        if (!carriersMap.containsKey(carrierCode)) {
            responseBody = "Carrier with code " + carrierCode + " does not exist.";
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
        
        List<Statistic> statistics = null;
        
        if (mediaType.equals("text/csv")) {
            try {
                statistics = csvConverter.StringToStatistics(data);
            } catch (Exception e) {
                e.printStackTrace();
                responseBody = "There is a syntax error in the CSV string.";
                return new ResponseEntity<String>(responseBody, HttpStatus.BAD_REQUEST);
            }
        } else {
            try {
                statistics = jsonConverter.StringToStatistics(data);
            } catch (Exception e) {
                e.printStackTrace();
                responseBody = "There is a syntax error in the JSON string.";
                return new ResponseEntity<String>(responseBody, HttpStatus.BAD_REQUEST);
            }
        }
        
        
        try {
            databaseConnector.deleteStatistics(statistics);
        } catch (SQLException e) {
            e.printStackTrace();
            responseBody = "Something went wrong.";
            return new ResponseEntity<String>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return new ResponseEntity<String>(HttpStatus.OK);    
        
    }
}
