package models;

public class DelayData {
    // Each field represents the number of delays for a specific reason.
    private int lateAircraftCount;
    private int carrierCount;
    private int weatherCount;
    private int securityCount;
    private int nationalAviationSystemCount;

    public DelayData(int lateAircraftCount, int carrierCount, int weatherCount, int securityCount, int nationalAviationSystemCount) {
        this.lateAircraftCount = lateAircraftCount;
        this.carrierCount = carrierCount;
        this.weatherCount = weatherCount;
        this.securityCount = securityCount;
        this.nationalAviationSystemCount = nationalAviationSystemCount;
    }

    public int getLateAircraftCount() {
        return lateAircraftCount;
    }

    public int getCarrierCount() {
        return carrierCount;
    }

    public int getWeatherCount() {
        return weatherCount;
    }

    public int getSecurityCount() {
        return securityCount;
    }

    public int getNationalAviationSystemCount() {
        return nationalAviationSystemCount;
    }
}
