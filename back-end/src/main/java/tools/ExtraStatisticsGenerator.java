package tools;

import java.sql.SQLException;
import java.util.*;

import converters.StatisticDataSelectorHelper;
import database.DatabaseConnector;
import models.*;

public class ExtraStatisticsGenerator {

    private static float computeMean(List<Integer> values) {
        int sum = 0;

        for (int value : values) {
            sum += value;
        }

        return sum / (float) values.size();
    }

    private static float computeMedian(List<Integer> values) {
        Collections.sort(values);

        float median;

        // get count of values
        int totalElements = values.size();

        // check if total number of scores is even
        if (totalElements % 2 == 0) {
            int sumOfMiddleElements = values.get(totalElements / 2) + values.get(totalElements / 2 - 1);
            // calculate average of middle elements
            median = sumOfMiddleElements / 2.f;
        } else {
            // get the middle element
            median = values.get(totalElements / 2);
        }

        return median;
    }

    private static float computeStandardDeviation(List<Integer> values, float mean) {
        float sum = 0;

        for (int value : values) {
            sum += (value - mean) * (value - mean);
        }

        float variance = sum / values.size();
        return (float) Math.sqrt(variance);
    }

    public static void main(String[] args) {
        DatabaseConnector databaseConnector = null;

        List<Airport> airports = null;
        List<Carrier> carriers = null;

        try {
            databaseConnector = new DatabaseConnector();

            airports = databaseConnector.getAirports();
            carriers = databaseConnector.getCarriers();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }

        List<ExtraStatistic> extraStatistics = new ArrayList<>();

        StatisticDataSelectorHelper dataSelector = new StatisticDataSelectorHelper(false, false, true, true);
        dataSelector.setDelayTimeData(true, true, false, false, false, false);

        for (Carrier carrier : carriers) {
            try {
                for (int i=0; i < airports.size(); i++) {
                    List<Statistic> statistics1 = databaseConnector.getStatistics(airports.get(i), carrier, null, null, dataSelector);
                    if (statistics1.isEmpty()) {
                        if (airports.get(i).getCode().equals("ATL")) System.out.println(carrier.getCode()+ "JOOOS");
                        continue;
                    }

                    for (int j=i+1; j <airports.size(); j++) {
                        List<Statistic> statistics2 = databaseConnector.getStatistics(airports.get(j), carrier, null, null, dataSelector);
                        if (statistics2.isEmpty()) {
                            continue;
                        }

                        List<Integer> lateAircraftTimes = new ArrayList<>();
                        List<Integer> carrierTimes = new ArrayList<>();

                        for (Statistic statistic : statistics1) {
                            lateAircraftTimes.add(statistic.getDelayTimeData().getLateAircraftTime());
                            carrierTimes.add(statistic.getDelayTimeData().getCarrierTime());
                        }

                        for (Statistic statistic : statistics2) {
                            lateAircraftTimes.add(statistic.getDelayTimeData().getLateAircraftTime());
                            carrierTimes.add(statistic.getDelayTimeData().getCarrierTime());
                        }

                        float lateAircraftTimeMean = computeMean(lateAircraftTimes);
                        float lateAircraftTimeMedian = computeMedian(lateAircraftTimes);
                        float lateAircraftTimeSd = computeStandardDeviation(lateAircraftTimes, lateAircraftTimeMean);

                        float carrierTimeMean = computeMean(carrierTimes);
                        float carrierTimeMedian = computeMedian(carrierTimes);
                        float carrierTimeSd = computeStandardDeviation(carrierTimes, carrierTimeMean);

                        ExtraStatistic extraStatistic = new ExtraStatistic(airports.get(i), airports.get(j), carrier);

                        extraStatistic.setLateAircraftTimeMean(lateAircraftTimeMean);
                        extraStatistic.setLateAircraftTimeMedian(lateAircraftTimeMedian);
                        extraStatistic.setLateAircraftTimeSd(lateAircraftTimeSd);

                        extraStatistic.setCarrierTimeMean(carrierTimeMean);
                        extraStatistic.setCarrierTimeMedian(carrierTimeMedian);
                        extraStatistic.setCarrierTimeSd(carrierTimeSd);

                        extraStatistics.add(extraStatistic);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

        System.out.println("Computed " + extraStatistics.size() + " extra statistics.");

        try {
            databaseConnector.addExtraStatistics(extraStatistics);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
