package database;

import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import models.Airport;
import models.Carrier;
import models.Statistic;

public class Test {
	public static void main(String[] args) {
		Airport airport1 = new Airport("ATL", "Atlanta, GA: Hartsfield-Jackson Atlanta International");
		Airport airport2 = new Airport("LAS", "Las Vegas, NV: McCarran International");
		
		Carrier carrier1 = new Carrier("AA", "American Airlines Inc.");
		Carrier carrier2 = new Carrier("B6", "JetBlue Airways");
		
		YearMonth yearMonth1 = YearMonth.of(2003, 6);
		
		
		/*Statistic stat1 = new Statistic(airport2, newCarrier, yearMonth);
		stat1.setCancelledFlightCount(0);
		stat1.setOnTimeFlightCount(1);
		stat1.setDelayedFlightCount(2);
		stat1.setDivertedFlightCount(3);
		stat1.setTotalFlightCount(4);
		
		stat1.setLateAircraftDelayCount(5);
		stat1.setCarrierDelayCount(6);
		stat1.setWeatherDelayCount(7);
		stat1.setSecurityDelayCount(8);
		stat1.setNationalAviationSystemDelayCount(10);
		
		stat1.setLateAircraftDelayTime(11);
		stat1.setCarrierDelayTime(12);
		stat1.setWeatherDelayTime(13);
		stat1.setSecurityDelayTime(14);
		stat1.setNationalAviationSystemDelayTime(15);
		stat1.setTotalDelayTime(16);
		
		Statistic stat = new Statistic(airport2, carrier, yearMonth);
		stat.setCancelledFlightCount(0);
		stat.setOnTimeFlightCount(1);
		stat.setDelayedFlightCount(2);
		stat.setDivertedFlightCount(3);
		stat.setTotalFlightCount(4);
		
		stat.setLateAircraftDelayCount(5);
		stat.setCarrierDelayCount(6);
		stat.setWeatherDelayCount(7);
		stat.setSecurityDelayCount(8);
		stat.setNationalAviationSystemDelayCount(10);
		
		stat.setLateAircraftDelayTime(11);
		stat.setCarrierDelayTime(12);
		stat.setWeatherDelayTime(13);
		stat.setSecurityDelayTime(14);
		stat.setNationalAviationSystemDelayTime(15);
		stat.setTotalDelayTime(16);
		
		List<Statistic> stats = new ArrayList<>();
		stats.add(stat1);
		stats.add(stat);*/
		
		List<Carrier> carriers = null;
		List<Statistic> stats = null;
		Statistic stat = null;
		
		try {
			DatabaseConnector dbconn = new DatabaseConnector();
			
			long startTime = System.nanoTime();
			stats = dbconn.getStatistics(airport1, carrier1);
			long endTime = System.nanoTime();

			long duration = (endTime - startTime);
			System.out.print("Query duration: " + duration);
			
			dbconn.close();
		} catch (PropertyVetoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (Statistic statistic : stats) {
			System.out.println(statistic.getCancelledFlightCount() + " " + statistic.getOnTimeFlightCount());
		}
		
		
	}
}
