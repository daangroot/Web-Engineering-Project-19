package converters;

public class StatisticDataSelectorHelper {
    private boolean airport;
    private boolean carrier;
    private boolean year;
    private boolean month;

    private boolean flightData;
    private boolean cancelledCount;
    private boolean onTimeCount;
    private boolean delayedCount;
    private boolean divertedCount;
    private boolean totalCount;

    private boolean delayData;
    private boolean lateAircraftCount;
    private boolean carrierCount;
    private boolean weatherCount;
    private boolean securityCount;
    private boolean nationalAviationSystemCount;

    private boolean delayTimeData;
    private boolean lateAircraftTime;
    private boolean carrierTime;
    private boolean weatherTime;
    private boolean securityTime;
    private boolean nationalAviationSystemTime;
    private boolean totalTime;

    public StatisticDataSelectorHelper(boolean airport, boolean carrier, boolean year, boolean month) {
        this.airport = airport;
        this.carrier = carrier;
        this.year = year;
        this.month = month;
    }

    public void setFlightDataTrue() {
        flightData = true;

        cancelledCount = true;
        onTimeCount = true;
        delayedCount = true;
        divertedCount = true;
        totalCount = true;
    }

    public void setDelayDataTrue() {
        delayData = true;

        lateAircraftCount = true;
        carrierCount = true;
        weatherCount = true;
        securityCount = true;
        nationalAviationSystemCount = true;
    }

    public void setDelayDataTimeTrue() {
        delayTimeData = true;

        lateAircraftTime = true;
        carrierTime = true;
        weatherTime = true;
        securityTime = true;
        nationalAviationSystemTime = true;
        totalTime = true;
    }

    public void setFlightData(boolean cancelledCount, boolean onTimeCount, boolean delayedCount, boolean divertedCount,
                              boolean totalCount) {
        flightData = true;

        this.cancelledCount = cancelledCount;
        this.onTimeCount = onTimeCount;
        this.delayedCount = delayedCount;
        this.divertedCount = divertedCount;
        this.totalCount = totalCount;
    }

    public void setDelayData(boolean lateAircraftCount, boolean carrierCount, boolean weatherCount,
                             boolean securityCount, boolean nationalAviationSystemCount) {
        delayData = true;

        this.lateAircraftCount = lateAircraftCount;
        this.carrierCount = carrierCount;
        this.weatherCount = weatherCount;
        this.securityCount = securityCount;
        this.nationalAviationSystemCount = nationalAviationSystemCount;
    }

    public void setDelayTimeData(boolean lateAircraftTime, boolean carrierTime, boolean weatherTime,
                                 boolean securityTime, boolean nationalAviationSystemTime, boolean totalTime) {
        delayTimeData = true;

        this.lateAircraftTime = lateAircraftTime;
        this.carrierTime = carrierTime;
        this.weatherTime = weatherTime;
        this.securityTime = securityTime;
        this.nationalAviationSystemTime = nationalAviationSystemTime;
        this.totalTime = totalTime;
    }

    public boolean withAirport() {
        return airport;
    }

    public boolean withCarrier() {
        return carrier;
    }

    public boolean withYear() {
        return year;
    }

    public boolean withMonth() {
        return month;
    }

    public boolean withFlightData() {
        return flightData;
    }

    public boolean withCancelledCount() {
        return cancelledCount;
    }

    public boolean withOnTimeCount() {
        return onTimeCount;
    }

    public boolean withDelayedCount() {
        return delayedCount;
    }

    public boolean withDivertedCount() {
        return divertedCount;
    }

    public boolean withTotalCount() {
        return totalCount;
    }

    public boolean withDelayData() {
        return delayData;
    }

    public boolean withLateAircraftCount() {
        return lateAircraftCount;
    }

    public boolean withCarrierCount() {
        return carrierCount;
    }

    public boolean withWeatherCount() {
        return weatherCount;
    }

    public boolean withSecurityCount() {
        return securityCount;
    }

    public boolean withNationalAviationSystemCount() {
        return nationalAviationSystemCount;
    }

    public boolean withDelayTimeData() {
        return delayTimeData;
    }

    public boolean withLateAircraftTime() {
        return lateAircraftTime;
    }

    public boolean withCarrierTime() {
        return carrierTime;
    }

    public boolean withWeatherTime() {
        return weatherTime;
    }

    public boolean withSecurityTime() {
        return securityTime;
    }

    public boolean withNationalAviationSystemTime() {
        return nationalAviationSystemTime;
    }

    public boolean withTotalTime() {
        return totalTime;
    }
}
