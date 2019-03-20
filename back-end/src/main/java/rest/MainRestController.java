package rest;

import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    
    public MainRestController() {
        try {
            databaseConnector = new DatabaseConnector();
        } catch (PropertyVetoException | SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        
        jsonConverter = new JsonConverter();
        csvConverter  = new CsvConverter();
    }
    
    @GetMapping("/airports")
    public ResponseEntity<String> getAirports(@RequestHeader("Accept") String mediaType) {
        List<Airport> airports = null;
        try {
            airports = databaseConnector.getAirports();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        HttpHeaders responseHeaders = new HttpHeaders();
        String responseBody;
        
        if (mediaType.equals("text/csv")) {
            responseHeaders.set("Content-Type", "text/csv");
            responseBody = csvConverter.AirportsToString(airports);
        } else {
            responseHeaders.set("Content-Type", "application/json");
            responseBody = jsonConverter.AirportsToString(airports);
        }
       
        return new ResponseEntity<String>(responseBody, responseHeaders, HttpStatus.OK);
    }
    
    @GetMapping("/carriers")
    public ResponseEntity<String> getCarriers(@RequestHeader("Accept") String mediaType) {
        List<Carrier> carriers = null;
        try {
            carriers = databaseConnector.getCarriers();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        HttpHeaders responseHeaders = new HttpHeaders();
        String responseBody;
        
        if (mediaType.equals("text/csv")) {
            responseHeaders.set("Content-Type", "text/csv");
            responseBody = csvConverter.CarriersToString(carriers);
        } else {
            responseBody = jsonConverter.CarriersToString(carriers);
            responseHeaders.set("Content-Type", "application/json");
        }
       
        return new ResponseEntity<String>(responseBody, responseHeaders, HttpStatus.OK);
    }
    
    @GetMapping("/airports/{airportCode}/carriers")
    public ResponseEntity<String> getCarriersAtAirport(@RequestHeader("Accept") String mediaType, @PathVariable String airportCode) {
        List<Carrier> carriers = null;
        try {
            carriers = databaseConnector.getCarriersAtAirport(new Airport(airportCode, null));
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        HttpHeaders responseHeaders = new HttpHeaders();
        String responseBody;
        
        if (mediaType.equals("text/csv")) {
            responseBody = csvConverter.CarriersToString(carriers);
            responseHeaders.set("Content-Type", "text/csv");
        } else {
            responseBody = jsonConverter.CarriersToString(carriers);
            responseHeaders.set("Content-Type", "application/json");
        }
       
        return new ResponseEntity<String>(responseBody, responseHeaders, HttpStatus.OK);
    }
    
    @GetMapping("/airports/{airportCode}/carriers/{carrierCode}/stats")
    public ResponseEntity<String> getStats(@RequestHeader("Accept") String mediaType, @PathVariable String airportCode, @PathVariable String carrierCode,
                                        @RequestParam(value="year", required=false) Integer year, @RequestParam(value="month", required=false) Integer month) {
        Airport airport = new Airport(airportCode, null);
        Carrier carrier = new Carrier(carrierCode, null);
        
        List<Statistic> statistics = null;
        Statistic statistic = null;
        
        if ((year != null && year < 0) || (month != null && month < 1)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
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
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        HttpHeaders responseHeaders = new HttpHeaders();
        String responseBody;
        
        if (mediaType.equals("text/csv")) {
            if (statistics != null) {
                responseBody = csvConverter.StatisticsToString(statistics, false, false, true);
            } else {
                responseBody = csvConverter.StatisticToString(statistic, false, false, false);
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
        
        return new ResponseEntity<String>(responseBody, responseHeaders, HttpStatus.OK);
    }
    /*
    public ResponseEntity<String> postStats(@RequestHeader("Content-Type") String mediaType, @PathVariable String airportCode, @PathVariable String carrierCode,
            @RequestParam(value="year", required=false) Integer year, @RequestParam(value="month", required=false) Integer month) {
        Airport airport = new Airport(airportCode, null);
        Carrier carrier = new Carrier(carrierCode, null);
        
        List<Statistic> statistics = null;
        Statistic statistic = null;
        
        if ((year != null && year < 0) || (month != null && month < 1)) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
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
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        HttpHeaders responseHeaders = new HttpHeaders();
        String responseBody;
        
        if (mediaType.equals("text/csv")) {
        responseHeaders.set("Content-Type", "text/csv");
        if (statistics != null) {
        responseBody = csvConverter.StatisticsToString(statistics, false, false, true);
        } else {
        responseBody = csvConverter.StatisticToString(statistic, false, false, false);
        }
        } else {
        if (statistics != null) {
        responseBody = jsonConverter.StatisticsToString(statistics, false, false, true);
        } else {
        responseBody = jsonConverter.StatisticToString(statistic, false, false, false);
        }
        }
        
        return new ResponseEntity<String>(responseBody, responseHeaders, HttpStatus.OK);
        }*/
}
