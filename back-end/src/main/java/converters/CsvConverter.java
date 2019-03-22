package converters;

import java.util.ArrayList;
import java.util.List;

import models.Airport;
import models.Carrier;
import models.ExtraStatistic;
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
        List <Statistic> statistics = new ArrayList<>();
        statistics.add(statistic);

        return StatisticsToFlightString(statistics, airport, carrier, yearMonth);
        return null;
    }

    @Override
    public String StatisticsToFlightString(List<Statistic> statistics, boolean airport, boolean carrier,
                                           boolean yearMonth, boolean cancelledFlightCount, boolean onTimeFlightCount, boolean delayedFlightCount,
                                           boolean divertedFlightCount, boolean totalFlightCount) {
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

        csv += "cancelledFlightCount, onTimeFlightCount, delayedFlightCount, divertedFlightCount, totalFlightCount\n";

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

            csv += statistic.getCancelledFlightCount() + ", " + statistic.getOnTimeFlightCount() + ", " + statistic.getDelayedFlightCount() + ", "
                    + statistic.getDivertedFlightCount() + ", " + statistic.getTotalFlightCount() + "\n";
        }

        return csv;

    }

    @Override
    public String StatisticToDelayString(Statistic statistic, boolean airport, boolean carrier, boolean yearMonth,
                                         boolean lateAircraftDelayCount, boolean carrierDelayCount, boolean weatherDelayCount,
                                         boolean securityDelayCount, boolean nationalAviationSystemDelayCount) {
        List <Statistic> statistics = new ArrayList<>();
        statistics.add(statistic);

        return StatisticsToDelayString(statistics, airport, carrier, yearMonth);

    }

    @Override
    public String StatisticsToDelayString(List<Statistic> statistics, boolean airport, boolean carrier,
                                          boolean yearMonth, boolean lateAircraftDelayCount, boolean carrierDelayCount, boolean weatherDelayCount,
                                          boolean securityDelayCount, boolean nationalAviationSystemDelayCount) {
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

        csv += "lateAircraftDelayCount, carrierDelayCount, weatherDelayCount, securityDelayCount, nationalAviationSystemDelayCount\n";

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

            csv += statistic.getLateAircraftDelayCount() + ", " + statistic.getCarrierDelayCount() + ", " + statistic.getWeatherDelayCount() + ", "
                    + statistic.getSecurityDelayCount() + ", " + statistic.getNationalAviationSystemDelayCount() + "\n";
        }

        return csv;
    	

    }

    @Override
    public String StatisticToDelayTimeString(Statistic statistic, boolean airport, boolean carrier, boolean yearMonth,
                                             boolean lateAircraftDelayTime, boolean carrierDelayTime, boolean weatherDelayTime,
                                             boolean securityDelayTime, boolean nationalAviationSystemDelayTime, boolean totalDelayTime) {
        List <Statistic> statistics = new ArrayList<>();
        statistics.add(statistic);
        
        return StatisticsToDelayTimeString(statistics, airport, carrier, yearMonth);
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

    @Override
    public String ExtraStatisticsToString(List<ExtraStatistic> extraStatistics, boolean airport1, boolean airport2, boolean carrier) {
        String csv = "";

        // Header record.
        if (airport1) {
            csv += "airportCode, airportName ";
        }

        if (airport2) {
            csv += "airportCode, airportName ";
        }

        if (carrier) {
            csv += "carrierCode, carrierName, ";
        }

        csv += "lateAircraftDelaysTimedMean, lateAircraftDelaysTimedMed, lateAircraftDelaysTimedSd, carrierAircraftDelaysTimedMean, carrierAircraftDelaysTimedMed, carrierAircraftDelaysTimedSd\n";

        // Records.
        for (ExtraStatistic  extraStatistic : extraStatistics) {
            if (airport1) {
                csv += extraStatistic.getAirport1().getCode() + ", " + extraStatistic.getAirport1().getName() + ", ";
            }

            if (airport2) {
                csv += extraStatistic.getAirport2().getCode() + ", " + extraStatistic.getAirport2().getName() + ", ";
            }

            if (carrier) {
                csv += extraStatistic.getCarrier().getCode() + ", " + extraStatistic.getCarrier().getName() + ", ";
            }

            csv += extraStatistic.getLateAircraftDelaysTimedMean() + ", " + extraStatistic.getLateAircraftDelaysTimedMed() + ", " + extraStatistic.getLateAircraftDelaysTimedSd() + ", "
                    + extraStatistic.getCarrierAircraftDelaysTimedMean() + ", " + extraStatistic.getCarrierAircraftDelaysTimedMed() + "," + extraStatistic.getCarrierAircraftDelaysTimedSd() + "\n";
        }

        return csv;


    }
}
