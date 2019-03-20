package models;

import java.time.YearMonth;

public class Statistic {
	private Airport airport;
	private Carrier carrier;
	private YearMonth yearMonth;
	
	// Statistics about flights.
	private int cancelledFlightCount;
	private int onTimeFlightCount;
	private int delayedFlightCount;
	private int divertedFlightCount;
	private int totalFlightCount;
	
	// Number of delays for each delay reason.
	private int lateAircraftDelayCount;
	private int carrierDelayCount;
	private int weatherDelayCount;
	private int securityDelayCount;
	private int nationalAviationSystemDelayCount;
	
	// Total delay time in minutes for each delay reason.
	private int lateAircraftDelayTime;
	private int carrierDelayTime;
	private int weatherDelayTime;
	private int securityDelayTime;
	private int nationalAviationSystemDelayTime;
	private int totalDelayTime;
	
	public Statistic (Airport airport, Carrier carrier, YearMonth yearMonth) {
		this.airport = airport;
		this.carrier = carrier;
		this.yearMonth = yearMonth;
	}
	
	public Airport getAirport() {
		return airport;
	}

	public Carrier getCarrier() {
		return carrier;
	}

	public YearMonth getYearMonth() {
		return yearMonth;
	}	

	public int getCancelledFlightCount() {
		return cancelledFlightCount;
	}

	public void setCancelledFlightCount(int cancelledFlightCount) {
		this.cancelledFlightCount = cancelledFlightCount;
	}

	public int getOnTimeFlightCount() {
		return onTimeFlightCount;
	}

	public void setOnTimeFlightCount(int onTimeFlightCount) {
		this.onTimeFlightCount = onTimeFlightCount;
	}

	public int getDelayedFlightCount() {
		return delayedFlightCount;
	}

	public void setDelayedFlightCount(int delayedFlightCount) {
		this.delayedFlightCount = delayedFlightCount;
	}

	public int getDivertedFlightCount() {
		return divertedFlightCount;
	}

	public void setDivertedFlightCount(int divertedFlightCount) {
		this.divertedFlightCount = divertedFlightCount;
	}

	public int getTotalFlightCount() {
		return totalFlightCount;
	}

	public void setTotalFlightCount(int totalFlightCount) {
		this.totalFlightCount = totalFlightCount;
	}

	public int getLateAircraftDelayCount() {
		return lateAircraftDelayCount;
	}

	public void setLateAircraftDelayCount(int lateAircraftDelayCount) {
		this.lateAircraftDelayCount = lateAircraftDelayCount;
	}

	public int getCarrierDelayCount() {
		return carrierDelayCount;
	}

	public void setCarrierDelayCount(int carrierDelayCount) {
		this.carrierDelayCount = carrierDelayCount;
	}

	public int getWeatherDelayCount() {
		return weatherDelayCount;
	}

	public void setWeatherDelayCount(int weatherDelayCount) {
		this.weatherDelayCount = weatherDelayCount;
	}

	public int getSecurityDelayCount() {
		return securityDelayCount;
	}

	public void setSecurityDelayCount(int securityDelayCount) {
		this.securityDelayCount = securityDelayCount;
	}

	public int getNationalAviationSystemDelayCount() {
		return nationalAviationSystemDelayCount;
	}

	public void setNationalAviationSystemDelayCount(int nationalAviationSystemDelayCount) {
		this.nationalAviationSystemDelayCount = nationalAviationSystemDelayCount;
	}

	public int getLateAircraftDelayTime() {
		return lateAircraftDelayTime;
	}

	public void setLateAircraftDelayTime(int lateAircraftDelayTime) {
		this.lateAircraftDelayTime = lateAircraftDelayTime;
	}

	public int getCarrierDelayTime() {
		return carrierDelayTime;
	}

	public void setCarrierDelayTime(int carrierDelayTime) {
		this.carrierDelayTime = carrierDelayTime;
	}

	public int getWeatherDelayTime() {
		return weatherDelayTime;
	}

	public void setWeatherDelayTime(int weatherDelayTime) {
		this.weatherDelayTime = weatherDelayTime;
	}

	public int getSecurityDelayTime() {
		return securityDelayTime;
	}

	public void setSecurityDelayTime(int securityDelayTime) {
		this.securityDelayTime = securityDelayTime;
	}

	public int getNationalAviationSystemDelayTime() {
		return nationalAviationSystemDelayTime;
	}

	public void setNationalAviationSystemDelayTime(int nationalAviationSystemDelayTime) {
		this.nationalAviationSystemDelayTime = nationalAviationSystemDelayTime;
	}

	public int getTotalDelayTime() {
		return totalDelayTime;
	}

	public void setTotalDelayTime(int totalDelayTime) {
		this.totalDelayTime = totalDelayTime;
	}
}