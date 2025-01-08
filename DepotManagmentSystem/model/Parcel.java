package model;

import java.io.Serializable;

public class Parcel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String parcelId;
    private int length;
    private int width;
    private int height;
    private int weight;
    private int daysInDepot;
    private boolean processed;

    public Parcel(String parcelId, int length, int width, int height, int weight, int daysInDepot) {
        this.parcelId = parcelId;
        this.length = length;
        this.width = width;
        this.height = height;
        this.weight = weight;
        this.daysInDepot = daysInDepot;
        this.processed = false;
    }

    public String getParcelId() {
        return parcelId;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public int getWeight() {
        return weight;
    }

    public int getDaysInDepot() {
        return daysInDepot;
    }

    @Override
    public String toString() {
        return String.format("Parcel ID: %s, Dimensions: %dx%dx%d, Weight: %d, Days in Depot: %d",
                parcelId, length, width, height, weight, daysInDepot);
    }
}
