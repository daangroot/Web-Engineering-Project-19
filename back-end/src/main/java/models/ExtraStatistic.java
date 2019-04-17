package models;

public class ExtraStatistic {
    private Airport airport1;
    private Airport airport2;
    private Carrier carrier;

    // Late aircraft delay time extra stats.
    private float lateAircraftTimeMean;
    private float lateAircraftTimeMedian;
    private float lateAircraftTimeSd;

    // Carrier delay time extra stats.
    private float carrierTimeMean;
    private float carrierTimeMedian;
    private float carrierTimeSd;

    public ExtraStatistic (Airport airport1, Airport airport2, Carrier carrier) {
        this.airport1 = airport1;
        this.airport2 = airport2;
        this.carrier = carrier;
    }

    public Airport getAirport1() {
        return airport1;
    }

    public Airport getAirport2() {
        return airport2;
    }

    public Carrier getCarrier() {
        return carrier;
    }

    public float getLateAircraftTimeMean() {
        return lateAircraftTimeMean;
    }

    public float getLateAircraftTimeMedian() {
        return lateAircraftTimeMedian;
    }

    public float getLateAircraftTimeSd() {
        return lateAircraftTimeSd;
    }

    public float getCarrierTimeMean() {
        return carrierTimeMean;
    }

    public float getCarrierTimeMedian() {
        return carrierTimeMedian;
    }

    public float getCarrierTimeSd() {
        return carrierTimeSd;
    }

    public void setLateAircraftTimeMean(float lateAircraftTimeMean) {
        this.lateAircraftTimeMean = lateAircraftTimeMean;
    }

    public void setLateAircraftTimeMedian(float lateAircraftTimeMedian) {
        this.lateAircraftTimeMedian = lateAircraftTimeMedian;
    }

    public void setLateAircraftTimeSd(float lateAircraftTimeSd) {
        this.lateAircraftTimeSd = lateAircraftTimeSd;
    }

    public void setCarrierTimeMean(float carrierTimeMean) {
        this.carrierTimeMean = carrierTimeMean;
    }

    public void setCarrierTimeMedian(float carrierTimeMedian) {
        this.carrierTimeMedian = carrierTimeMedian;
    }

    public void setCarrierTimeSd(float carrierTimeSd) {
        this.carrierTimeSd = carrierTimeSd;
    }
}