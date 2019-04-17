package converters;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import models.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

public class CsvConverter implements DataConverter {
    
    public CsvConverter() {
    }

    @Override
    public String airportsToString(List<Airport> airports) throws IOException {
        StringWriter stringWriter = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT.withHeader("airportCode", "airportName"));

        for (Airport airport : airports) {
            csvPrinter.printRecord(airport.getCode(), airport.getName());
        }

        csvPrinter.close();

        return stringWriter.toString();
    }

    @Override
    public String carriersToString(List<Carrier> carriers) throws IOException {
        StringWriter stringWriter = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT.withHeader("carrierCode", "carrierName"));

        for (Carrier carrier : carriers) {
            csvPrinter.printRecord(carrier.getCode(), carrier.getName());
        }

        csvPrinter.close();

        return stringWriter.toString();
    }

    @Override
    public String statisticsToString(List<Statistic> statistics, StatisticDataSelectorHelper includedData) throws IOException {
        StringWriter stringWriter = new StringWriter();

        List<String> headerColumns = new ArrayList<>();

        if (includedData.withAirport()) {
            headerColumns.add("airportCode");
            headerColumns.add("airportName");
        }

        if (includedData.withCarrier()) {
            headerColumns.add("carrierCode");
            headerColumns.add("carrierName");
        }

        if (includedData.withYear()) {
            headerColumns.add("year");
        }

        if (includedData.withMonth()) {
            headerColumns.add("month");
        }

        if (includedData.withFlightData()) {
            if (includedData.withCancelledCount()) {
                headerColumns.add("cancelledCount");
            }

            if (includedData.withOnTimeCount()) {
                headerColumns.add("onTimeCount");
            }

            if (includedData.withDelayedCount()) {
                headerColumns.add("delayedCount");
            }

            if (includedData.withDivertedCount()) {
                headerColumns.add("divertedCount");
            }

            if (includedData.withTotalCount()) {
                headerColumns.add("totalCount");
            }
        }

        if (includedData.withDelayData()) {
            if (includedData.withLateAircraftCount()) {
                headerColumns.add("lateAircraftCount");
            }

            if (includedData.withCarrierCount()) {
                headerColumns.add("carrierCount");
            }

            if (includedData.withWeatherCount()) {
                headerColumns.add("weatherCount");
            }

            if (includedData.withSecurityCount()) {
                headerColumns.add("securityCount");
            }

            if (includedData.withNationalAviationSystemCount()) {
                headerColumns.add("nationalAviationSystemCount");
            }
        }

        if (includedData.withDelayTimeData()) {
            if (includedData.withLateAircraftTime()) {
                headerColumns.add("lateAircraftTime");
            }

            if (includedData.withCarrierTime()) {
                headerColumns.add("carrierTime");
            }

            if (includedData.withWeatherTime()) {
                headerColumns.add("weatherTime");
            }

            if (includedData.withSecurityTime()) {
                headerColumns.add("securityTime");
            }

            if (includedData.withNationalAviationSystemTime()) {
                headerColumns.add("nationalAviationSystemTime");
            }

            if (includedData.withTotalTime()) {
                headerColumns.add("totalTime");
            }
        }

        String[] header = headerColumns.toArray(new String[0]);

        CSVPrinter csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT.withHeader(header));

        for (Statistic statistic : statistics) {
            if (includedData.withAirport()) {
                csvPrinter.print(statistic.getAirport().getCode());
                csvPrinter.print(statistic.getAirport().getName());
            }

            if (includedData.withCarrier()) {
                csvPrinter.print(statistic.getCarrier().getCode());
                csvPrinter.print(statistic.getCarrier().getName());
            }

            if (includedData.withYear()) {
                csvPrinter.print(statistic.getYear());
            }

            if (includedData.withMonth()) {
                csvPrinter.print(statistic.getMonth());
            }

            if (includedData.withFlightData()) {
                FlightData flightData = statistic.getFlightData();

                if (includedData.withCancelledCount()) {
                    csvPrinter.print(flightData.getCancelledCount());
                }

                if (includedData.withOnTimeCount()) {
                    csvPrinter.print(flightData.getOnTimeCount());
                }

                if (includedData.withDelayedCount()) {
                    csvPrinter.print(flightData.getDelayedCount());
                }

                if (includedData.withDivertedCount()) {
                    csvPrinter.print(flightData.getDivertedCount());
                }

                if (includedData.withTotalCount()) {
                    csvPrinter.print(flightData.getTotalCount());
                }
            }

            if (includedData.withDelayData()) {
                DelayData delayData = statistic.getDelayData();

                if (includedData.withLateAircraftCount()) {
                    csvPrinter.print(delayData.getLateAircraftCount());
                }

                if (includedData.withCarrierCount()) {
                    csvPrinter.print(delayData.getCarrierCount());
                }

                if (includedData.withWeatherCount()) {
                    csvPrinter.print(delayData.getWeatherCount());
                }

                if (includedData.withSecurityCount()) {
                    csvPrinter.print(delayData.getSecurityCount());
                }

                if (includedData.withNationalAviationSystemCount()) {
                    csvPrinter.print(delayData.getNationalAviationSystemCount());
                }
            }

            if (includedData.withDelayTimeData()) {
                DelayTimeData delayTimeData = statistic.getDelayTimeData();

                if (includedData.withLateAircraftTime()) {
                    csvPrinter.print(delayTimeData.getLateAircraftTime());
                }

                if (includedData.withCarrierTime()) {
                    csvPrinter.print(delayTimeData.getCarrierTime());
                }

                if (includedData.withWeatherTime()) {
                    csvPrinter.print(delayTimeData.getWeatherTime());
                }

                if (includedData.withSecurityTime()) {
                    csvPrinter.print(delayTimeData.getSecurityTime());
                }

                if (includedData.withNationalAviationSystemTime()) {
                    csvPrinter.print(delayTimeData.getNationalAviationSystemTime());
                }

                if (includedData.withTotalTime()) {
                    csvPrinter.print(delayTimeData.getTotalTime());
                }
            }

            csvPrinter.println();
        }

        csvPrinter.close();

        return stringWriter.toString();
    }

    @Override
    public List<Statistic> stringToStatistics(String statisticData, Airport airport, Carrier carrier, Integer year,
                                              Integer month) throws Exception {
        List<Statistic> statistics = new ArrayList<>();

        CSVParser csvParser = CSVParser.parse(statisticData, CSVFormat.DEFAULT.withHeader());
        List<CSVRecord> records = csvParser.getRecords();

        Airport tempAirport = airport;
        Carrier tempCarrier = carrier;
        Integer tempYear = year;
        Integer tempMonth = month;

        for (CSVRecord record : records) {
            if (airport == null) {
                tempAirport = new Airport(record.get("airportCode"), null);
            }

            if (carrier == null) {
                tempCarrier = new Carrier(record.get("carrierCode"), null);
            }

            if (year == null) {
                tempYear = Integer.parseInt(record.get("year"));
            }

            if (month == null) {
                tempMonth = Integer.parseInt(record.get("month"));
            }

            int flightCount = Integer.parseInt(record.get("cancelledCount"));
            int onTimeCount = Integer.parseInt(record.get("onTimeCount"));
            int delayedCount = Integer.parseInt(record.get("delayedCount"));
            int divertedCount = Integer.parseInt(record.get("divertedCount"));
            int totalCount = Integer.parseInt(record.get("totalCount"));
            FlightData flightData = new FlightData(flightCount, onTimeCount, delayedCount, divertedCount, totalCount);

            int lateAircraftCount = Integer.parseInt(record.get("lateAircraftCount"));
            int carrierCount = Integer.parseInt(record.get("carrierCount"));
            int weatherCount = Integer.parseInt(record.get("weatherCount"));
            int securityCount = Integer.parseInt(record.get("securityCount"));
            int nationalAviationSystemCount = Integer.parseInt(record.get("nationalAviationSystemCount"));
            DelayData delayData = new DelayData(lateAircraftCount, carrierCount, weatherCount, securityCount,
                    nationalAviationSystemCount);

            int lateAircraftTime = Integer.parseInt(record.get("lateAircraftTime"));
            int carrierTime = Integer.parseInt(record.get("carrierTime"));
            int weatherTime = Integer.parseInt(record.get("weatherTime"));
            int securityTime = Integer.parseInt(record.get("securityTime"));
            int nationalAviationSystemTime = Integer.parseInt(record.get("nationalAviationSystemTime"));
            int totalTime = Integer.parseInt(record.get("totalTime"));
            DelayTimeData delayTimeData = new DelayTimeData(lateAircraftTime, carrierTime, weatherTime, securityTime,
                    nationalAviationSystemTime, totalTime);

            Statistic statistic = new Statistic(tempAirport, tempCarrier, tempYear, tempMonth, flightData, delayData,
                    delayTimeData);

            statistics.add(statistic);
        }

        return statistics;
    }

    @Override
    public String extraStatisticsToString(List<ExtraStatistic> extraStatistics, boolean withCarrier)
            throws IOException {
        StringWriter stringWriter = new StringWriter();

        List<String> headerColumns = new ArrayList<>();

        if (withCarrier) {
            headerColumns.add("carrierCode");
            headerColumns.add("carrierName");
        }

        headerColumns.add("lateAircraftTimeMean");
        headerColumns.add("lateAircraftTimeMedian");
        headerColumns.add("lateAircraftTimeSd");

        headerColumns.add("carrierTimeMean");
        headerColumns.add("carrierTimeMedian");
        headerColumns.add("carrierTimeSd");

        String[] header = headerColumns.toArray(new String[0]);

        CSVPrinter csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT.withHeader(header));

        for (ExtraStatistic extraStatistic : extraStatistics) {
            if (withCarrier) {
                csvPrinter.print(extraStatistic.getCarrier().getCode());
                csvPrinter.print(extraStatistic.getCarrier().getName());
            }

            csvPrinter.print(extraStatistic.getLateAircraftTimeMean());
            csvPrinter.print(extraStatistic.getLateAircraftTimeMedian());
            csvPrinter.print(extraStatistic.getLateAircraftTimeSd());

            csvPrinter.print(extraStatistic.getCarrierTimeMean());
            csvPrinter.print(extraStatistic.getCarrierTimeMedian());
            csvPrinter.print(extraStatistic.getCarrierTimeSd());

            csvPrinter.println();
        }

        csvPrinter.close();

        return stringWriter.toString();
    }
}
