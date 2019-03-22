package models;


public class ExtraStatistic {
    private Airport airport1;
    private Airport airport2;
    private Carrier carrier;

    // Extra stats
    private double lateAircraftDelaysTimedMean;
    private double lateAircraftDelaysTimedMed;
    private double lateAircraftDelaysTimedSd;
    private double carrierAircraftDelaysTimedMean;
    private double carrierAircraftDelaysTimedMed;
    private double carrierAircraftDelaysTimedSd;


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

    public double getLateAircraftDelaysTimedMean() {
        return lateAircraftDelaysTimedMean;
    }

    public void setLateAircraftDelaysTimedMean(double lateAircraftDelaysTimedMean) {
        this.lateAircraftDelaysTimedMean = lateAircraftDelaysTimedMean;
    }

    public double getLateAircraftDelaysTimedMed() {
        return lateAircraftDelaysTimedMed;
    }

    public void setLateAircraftDelaysTimedMed(double lateAircraftDelaysTimedMed) {
        this.lateAircraftDelaysTimedMed = lateAircraftDelaysTimedMed;
    }

    public double getLateAircraftDelaysTimedSd() {
        return lateAircraftDelaysTimedSd;
    }

    public void setLateAircraftDelaysTimedSd(double lateAircraftDelaysTimedSd) {
        this.lateAircraftDelaysTimedSd = lateAircraftDelaysTimedSd;
    }

    public double getCarrierAircraftDelaysTimedMean() {
        return carrierAircraftDelaysTimedMean;
    }

    public void setCarrierAircraftDelaysTimedMean(double carrierAircraftDelaysTimedMean) {
        this.carrierAircraftDelaysTimedMean = carrierAircraftDelaysTimedMean;
    }

    public double getCarrierAircraftDelaysTimedMed() {
        return carrierAircraftDelaysTimedMed;
    }

    public void setCarrierAircraftDelaysTimedMed(double carrierAircraftDelaysTimedMed) {
        this.carrierAircraftDelaysTimedMed = carrierAircraftDelaysTimedMed;
    }

    public double getCarrierAircraftDelaysTimedSd() {
        return carrierAircraftDelaysTimedSd;
    }

    public void setCarrierAircraftDelaysTimedSd(double carrierAircraftDelaysTimedSd) {
        this.carrierAircraftDelaysTimedSd = carrierAircraftDelaysTimedSd;
    }
}