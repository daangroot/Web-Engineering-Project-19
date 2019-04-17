package converters;

import models.*;

import java.util.List;

public interface DataConverter {
    String airportsToString(List<Airport> airports) throws Exception;
    String carriersToString(List<Carrier> carriers) throws Exception;

    String statisticsToString(List<Statistic> statistics, StatisticDataSelectorHelper includedData) throws Exception;

    List<Statistic> stringToStatistics(String statisticsData, Airport airport, Carrier carrier, Integer year,
                                       Integer month) throws Exception;

    String extraStatisticsToString(List<ExtraStatistic> extraStatistics, boolean withCarrier) throws Exception;
}
