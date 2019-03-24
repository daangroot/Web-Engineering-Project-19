package converters;

import models.*;

import java.io.IOException;
import java.util.List;

public interface DataConverter {
    String AirportsToString(List<Airport> airports) throws Exception;
    String CarriersToString(List<Carrier> carriers) throws Exception;

    String StatisticsToString(List<Statistic> statistics, StatisticDataSelectorHelper includedData) throws Exception;

    List<Statistic> StringToStatistics(String statisticsData, Airport airport, Carrier carrier, Integer year,
                                       Integer month) throws Exception;

    String ExtraStatisticsToString(List<ExtraStatistic> extraStatistics, boolean withCarrier) throws Exception;
}
