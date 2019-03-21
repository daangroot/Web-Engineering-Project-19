package converters;

import java.io.IOException;
import java.io.StringWriter;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import models.Airport;
import models.Carrier;
import models.Statistic;

public class CsvConverter implements DataConverter {
    
    public CsvConverter() {
         
    }

    @Override
    public String AirportsToString(List<Airport> airports) throws IOException {
        StringWriter stringWriter = new StringWriter();
        CSVPrinter printer = new CSVPrinter(stringWriter, CSVFormat.DEFAULT.withHeader("code", "name"));
            
        for (Airport airport : airports) {
            printer.printRecord(airport.getCode(), airport.getName());
        }
        
        printer.close();
       
        return stringWriter.toString();
    }

    @Override
    public String CarriersToString(List<Carrier> carriers) throws IOException {
        StringWriter stringWriter = new StringWriter();
        CSVPrinter printer = new CSVPrinter(stringWriter, CSVFormat.DEFAULT.withHeader("code", "name"));
            
        for (Carrier carrier : carriers) {
            printer.printRecord(carrier.getCode(), carrier.getName());
        }

        printer.close();
        
        return stringWriter.toString();
    }
    
    @Override
    public String StatisticToString(Statistic statistic, boolean airport, boolean carrier, boolean yearMonth) throws IOException {
        List <Statistic> statistics = new ArrayList<>();
        statistics.add(statistic);
        
        return StatisticsToString(statistics, airport, carrier, yearMonth);
    }

    @Override
    public String StatisticsToString(List<Statistic> statistics, boolean airport, boolean carrier, boolean yearMonth) throws IOException {
        StringWriter stringWriter = new StringWriter();
        
        List<String> headerColumns = new ArrayList<>();
        
        if (airport) {
            headerColumns.add("airportCode");
            headerColumns.add("airportName");
        }
        
        if (carrier) {
            headerColumns.add("carrierCode");
            headerColumns.add("carrierName");
        }
        
        if (yearMonth) {
            headerColumns.add("year");
            headerColumns.add("month");
        }
        
        headerColumns.add("cancelledFlightCount");
        headerColumns.add("onTimeFlightCount");
        headerColumns.add("delayedFlightCount");
        headerColumns.add("divertedFlightCount");
        headerColumns.add("totalFlightCount");
        headerColumns.add("lateAircraftDelayCount");
        headerColumns.add("carrierDelayCount");
        headerColumns.add("weatherDelayCount");
        headerColumns.add("securityDelayCount");
        headerColumns.add("nationalAviationSystemDelayCount");
        headerColumns.add("lateAircraftDelayTime");
        headerColumns.add("carrierDelayTime");
        headerColumns.add("weatherDelayTime");
        headerColumns.add("securityDelayTime");
        headerColumns.add("nationalAviationSystemDelayTime");
        headerColumns.add("totalDelayTime");

        String[] header = headerColumns.toArray(new String[0]);
        
        CSVPrinter printer = new CSVPrinter(stringWriter, CSVFormat.DEFAULT.withHeader(header));
        
        for (Statistic statistic : statistics) {
            if (airport) {
                printer.print(statistic.getAirport().getCode());
                printer.print(statistic.getAirport().getName());
            }
            
            if (carrier) {
                printer.print(statistic.getCarrier().getCode());
                printer.print(statistic.getCarrier().getName());
            }
            
            if (yearMonth) {
                printer.print(statistic.getYearMonth().getYear());
                printer.print(statistic.getYearMonth().getMonthValue());
            }
            
            if (airport || carrier || yearMonth) {
                // Write comma after last column.
                stringWriter.write(",");
            }
            
            printer.printRecord(statistic.getCancelledFlightCount(), statistic.getOnTimeFlightCount(), statistic.getDelayedFlightCount(), statistic.getDivertedFlightCount(), statistic.getTotalFlightCount(),
                                statistic.getLateAircraftDelayCount(), statistic.getCarrierDelayCount(), statistic.getWeatherDelayCount(), statistic.getSecurityDelayCount(), statistic.getNationalAviationSystemDelayCount(),
                                statistic.getLateAircraftDelayTime(), statistic.getCarrierDelayTime(), statistic.getWeatherDelayTime(), statistic.getSecurityDelayTime(), statistic.getNationalAviationSystemDelayTime(),
                                statistic.getTotalDelayTime());
        }
        
        printer.close();
        
        return stringWriter.toString();
    }
    
    @Override
    public List<Statistic> StringToStatistics(String statisticData) throws Exception {
        List<Statistic> statistics = new ArrayList<>();
        
        CSVParser csvParser = CSVParser.parse(statisticData, CSVFormat.DEFAULT.withHeader());
        List<CSVRecord> records = csvParser.getRecords();
        
        for (CSVRecord record : records) {
            Airport airport = new Airport(record.get("airportCode"), record.get("airportName"));
            Carrier carrier = new Carrier(record.get("carrierCode"), record.get("carrierName"));
            YearMonth yearMonth = YearMonth.of(Integer.parseInt(record.get("year")), Integer.parseInt(record.get("month")));
            
            Statistic statistic = new Statistic(airport, carrier, yearMonth);
            
            statistic.setCancelledFlightCount(Integer.parseInt(record.get("cancelledFlightCount")));
            statistic.setOnTimeFlightCount(Integer.parseInt(record.get("onTimeFlightCount")));
            statistic.setDelayedFlightCount(Integer.parseInt(record.get("delayedFlightCount")));
            statistic.setDivertedFlightCount(Integer.parseInt(record.get("divertedFlightCount")));
            statistic.setTotalFlightCount(Integer.parseInt(record.get("totalFlightCount")));

            statistic.setLateAircraftDelayCount(Integer.parseInt(record.get("lateAircraftDelayCount")));
            statistic.setWeatherDelayCount(Integer.parseInt(record.get("weatherDelayCount")));
            statistic.setSecurityDelayCount(Integer.parseInt(record.get("securityDelayCount")));
            statistic.setNationalAviationSystemDelayCount(Integer.parseInt(record.get("nationalAviationSystemDelayCount")));
            statistic.setCarrierDelayCount(Integer.parseInt(record.get("carrierDelayCount")));

            statistic.setLateAircraftDelayTime(Integer.parseInt(record.get("lateAircraftDelayTime")));
            statistic.setWeatherDelayTime(Integer.parseInt(record.get("weatherDelayTime")));
            statistic.setSecurityDelayTime(Integer.parseInt(record.get("securityDelayTime")));
            statistic.setNationalAviationSystemDelayTime(Integer.parseInt(record.get("nationalAviationSystemDelayTime")));
            statistic.setCarrierDelayTime(Integer.parseInt(record.get("carrierDelayTime")));
            statistic.setTotalDelayTime(Integer.parseInt(record.get("totalDelayTime")));
            
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
