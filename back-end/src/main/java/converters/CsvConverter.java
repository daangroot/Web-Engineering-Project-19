package converters;

import java.util.ArrayList;
import java.util.List;

import models.Airport;
import models.Carrier;
import models.Statistic;

public class CsvConverter implements DataConverter {
    
    public CsvConverter() {
         
    }

    @Override
    public String AirportsToString(List<Airport> airports) {
        String csv;

        // Header record.
        csv = "code, name\n";

        for (Airport airport : airports) {
            csv += airport.getCode() + ", \"" + airport.getName() + "\"\n";
        }
        
        return csv;
    }

    @Override
    public String CarriersToString(List<Carrier> carriers) {
        String csv;

        // Header record.
        csv = "code, name\n";

        for (Carrier carrier : carriers) {
            csv += carrier.getCode() + ", \"" + carrier.getName() + "\"\n";
        }
        
        return csv;
    }
    
    @Override
    public String StatisticToString(Statistic statistic, boolean airport, boolean carrier, boolean yearMonth) {
        List <Statistic> statistics = new ArrayList<>();
        statistics.add(statistic);
        
        return StatisticsToString(statistics, airport, carrier, yearMonth);
    }

    @Override
    public String StatisticsToString(List<Statistic> statistics, boolean airport, boolean carrier, boolean yearMonth) {
        String csv = "";

        // Header record.
        if (airport) {
            csv += "airportCode, airportName ";
        }
        
        if (carrier) {
            csv += "carrierCode, carrierName, ";
        }
        
        if (yearMonth) {
            csv += "year, month, ";
        }
        
        csv += "cancelledFlightCount, onTimeFlightCount, delayedFlightCount, divertedFlightCount, totalFlightCount, "
             + "lateAircraftDelayCount, carrierDelayCount, weatherDelayCount, securityDelayCount, nationalAviationSystemDelayCount, "
             + "lateAircraftDelayTime, carrierDelayTime, weatherDelayTime, securityDelayTime, nationalAviationSystemDelayTime, totalDelayTime\n";
      
        // Records.
        for (Statistic statistic : statistics) {
            if (airport) {
                csv += statistic.getAirport().getCode() + ", " + statistic.getAirport().getName() + ", ";
            }
            
            if (carrier) {
                csv += statistic.getCarrier().getCode() + ", " + statistic.getCarrier().getName() + ", ";
            }
            
            if (yearMonth) {
                csv += statistic.getYearMonth().getYear() + ", " + statistic.getYearMonth().getMonthValue() + ", ";;
            }
            
            csv += statistic.getCancelledFlightCount() + ", " + statistic.getOnTimeFlightCount() + ", " + statistic.getDelayedFlightCount() + ", " + statistic.getDivertedFlightCount() + ", " + statistic.getTotalFlightCount() + ", "
                 + statistic.getLateAircraftDelayCount() + ", " + statistic.getCarrierDelayCount() + ", " + statistic.getWeatherDelayCount() + ", " + statistic.getSecurityDelayCount() + ", " 
                 + statistic.getNationalAviationSystemDelayCount() + ", " + statistic.getLateAircraftDelayTime() + ", " + statistic.getCarrierDelayTime() + ", " + statistic.getWeatherDelayTime() + ", "
                 + statistic.getSecurityDelayTime() + ", " + statistic.getNationalAviationSystemDelayTime() + ", " + statistic.getTotalDelayTime() + "\n";
        }
        
        return csv;
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
        List <Statistic> statistics = new ArrayList<>();
        statistics.add(statistic);
        
        return StatisticsToString(statistics, airport, carrier, yearMonth,lateAircraftDelayCount,carrierDelayCount,
        		weatherDelayCount,securityDelayCount,nationalAviationSystemDelayCount);
    }

    @Override
    public String StatisticsToDelayTimeString(List<Statistic> statistics, boolean airport, boolean carrier,
                                              boolean yearMonth, boolean lateAircraftDelayTime, boolean carrierDelayTime, boolean weatherDelayTime,
                                              boolean securityDelayTime, boolean nationalAviationSystemDelayTime, boolean totalDelayTime) {
        String csv = "";

        // Header record.
        if (airport) {
            csv += "airportCode, airportName ";
        }
        
        if (carrier) {
            csv += "carrierCode, carrierName, ";
        }
        
        if (yearMonth) {
            csv += "year, month, ";
        }
        
        csv += "lateAircraftDelayTime, carrierDelayTime, weatherDelayTime, securityDelayTime, nationalAviationSystemDelayTime, totalDelayTime\n";
      
        // Records.
        for (Statistic statistic : statistics) {
            if (airport) {
                csv += statistic.getAirport().getCode() + ", " + statistic.getAirport().getName() + ", ";
            }
            
            if (carrier) {
                csv += statistic.getCarrier().getCode() + ", " + statistic.getCarrier().getName() + ", ";
            }
            
            if (yearMonth) {
                csv += statistic.getYearMonth().getYear() + ", " + statistic.getYearMonth().getMonthValue() + ", ";;
            }
            
            csv += statistic.getLateAircraftDelayTime() + ", " + statistic.getCarrierDelayTime() + ", " + statistic.getWeatherDelayTime() + ", "
                 + statistic.getSecurityDelayTime() + ", " + statistic.getNationalAviationSystemDelayTime() + ", " + statistic.getTotalDelayTime() + "\n";
        }
        
        return csv;
    }
}
