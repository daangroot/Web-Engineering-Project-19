package models;

public class FlightData {
    private int cancelledCount;
    private int onTimeCount;
    private int delayedCount;
    private int divertedCount;
    private int totalCount;

    public FlightData(int cancelledCount, int onTimeCount, int delayedCount, int divertedCount, int totalCount) {
        this.cancelledCount = cancelledCount;
        this.onTimeCount = onTimeCount;
        this.delayedCount = delayedCount;
        this.divertedCount = divertedCount;
        this.totalCount = totalCount;
    }

    public int getCancelledCount() {
        return cancelledCount;
    }

    public int getOnTimeCount() {
        return onTimeCount;
    }

    public int getDelayedCount() {
        return delayedCount;
    }

    public int getDivertedCount() {
        return divertedCount;
    }

    public int getTotalCount() {
        return totalCount;
    }
}
