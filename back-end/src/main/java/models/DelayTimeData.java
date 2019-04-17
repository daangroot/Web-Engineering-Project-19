package models;

public class DelayTimeData {
    // Each field represents the total delay in minutes for a specific reason.
    private int lateAircraftTime;
    private int carrierTime;
    private int weatherTime;
    private int securityTime;
    private int nationalAviationSystemTime;
    private int totalTime;

    public DelayTimeData(int lateAircraftTime, int carrierTime, int weatherTime, int securityTime,
                         int nationalAviationSystemTime, int totalTime) {
        this.lateAircraftTime = lateAircraftTime;
        this.carrierTime = carrierTime;
        this.weatherTime = weatherTime;
        this.securityTime = securityTime;
        this.nationalAviationSystemTime = nationalAviationSystemTime;
        this.totalTime = totalTime;
    }

    public int getLateAircraftTime() {
        return lateAircraftTime;
    }

    public int getCarrierTime() {
        return carrierTime;
    }

    public int getWeatherTime() {
        return weatherTime;
    }

    public int getSecurityTime() {
        return securityTime;
    }

    public int getNationalAviationSystemTime() {
        return nationalAviationSystemTime;
    }

    public int getTotalTime() {
        return totalTime;
    }
}
