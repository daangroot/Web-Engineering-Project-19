package models;

public class Statistic {
	private Airport airport;
	private Carrier carrier;
	private Integer year;
	private Integer month;

	private FlightData flightData;
	private DelayData delayData;
	private DelayTimeData delayTimeData;

	public Statistic (Airport airport, Carrier carrier, Integer year, Integer month, FlightData flightData, DelayData delayData,
					  DelayTimeData delayTimeData) {
		this.airport = airport;
		this.carrier = carrier;
		this.year = year;
		this.month = month;

		this.flightData = flightData;
		this.delayData = delayData;
		this.delayTimeData = delayTimeData;
	}
	
	public Airport getAirport() {
		return airport;
	}

	public Carrier getCarrier() {
		return carrier;
	}

	public Integer getYear() {
		return year;
	}

	public Integer getMonth() {
		return month;
	}

	public FlightData getFlightData() {
		return flightData;
	}

	public DelayData getDelayData() {
		return delayData;
	}

	public DelayTimeData getDelayTimeData() {
		return delayTimeData;
	}
}