package converters;

import java.util.List;

import models.Airport;
import models.Carrier;
import models.Statistic;

public interface DataConverter {
    public String AirportsToString(List<Airport> airports);
    public String CarriersToString(List<Carrier> carriers);
    
    public String StatisticToString(Statistic statistic, boolean airport, boolean carrier, boolean yearMonth);
    public String StatisticsToString(List<Statistic> statistics, boolean airport, boolean carrier, boolean yearMonth);
    
    public String StatisticToFlightString(Statistic statistic, boolean airport, boolean carrier, boolean yearMonth,
                                          boolean cancelledFlightCount, boolean onTimeFlightCount, boolean delayedFlightCount,
                                          boolean divertedFlightCount, boolean totalFlightCount);
    public String StatisticsToFlightString(List<Statistic> statistics, boolean airport, boolean carrier, boolean yearMonth,
                                           boolean cancelledFlightCount, boolean onTimeFlightCount, boolean delayedFlightCount,
                                           boolean divertedFlightCount, boolean totalFlightCount);
    
    public String StatisticToDelayString(Statistic statistic, boolean airport, boolean carrier, boolean yearMonth,
                                         boolean lateAircraftDelayCount, boolean carrierDelayCount, boolean weatherDelayCount,
                                         boolean securityDelayCount, boolean nationalAviationSystemDelayCount);
    public String StatisticsToDelayString(List<Statistic> statistics, boolean airport, boolean carrier, boolean yearMonth,
                                          boolean lateAircraftDelayCount, boolean carrierDelayCount, boolean weatherDelayCount,
                                          boolean securityDelayCount, boolean nationalAviationSystemDelayCount);
    
    public String StatisticToDelayTimeString(Statistic statistic, boolean airport, boolean carrier, boolean yearMonth,
                                             boolean lateAircraftDelayTime, boolean carrierDelayTime, boolean weatherDelayTime,
                                             boolean securityDelayTime, boolean nationalAviationSystemDelayTime, boolean totalDelayTime);
    public String StatisticsToDelayTimeString(List<Statistic> statistics, boolean airport, boolean carrier, boolean yearMonth,
                                              boolean lateAircraftDelayTime, boolean carrierDelayTime, boolean weatherDelayTime,
                                              boolean securityDelayTime, boolean nationalAviationSystemDelayTime, boolean totalDelayTime);
}
