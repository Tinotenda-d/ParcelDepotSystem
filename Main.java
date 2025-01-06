// Day 1: Core Functionality

// ParcelData class to encapsulate parcel properties (Single Responsibility Principle)
class ParcelData {
    private String parcelId; // Unique identifier for the parcel
    private int dimensionL, dimensionW, dimensionH; // Dimensions of the parcel
    private int weight; // Weight of the parcel
    private int daysInDepot; // Number of days in the depot
    private String status; // Parcel status: "Waiting" or "Collected"

    public ParcelData(String parcelId, int dimensionL, int dimensionW, int dimensionH, int weight, int daysInDepot) {
        this.parcelId = parcelId;
        this.dimensionL = dimensionL;
        this.dimensionW = dimensionW;
        this.dimensionH = dimensionH;
        this.weight = weight;
        this.daysInDepot = daysInDepot;
        this.status = "Waiting"; // Default status
    }

    public String getParcelId() { return parcelId; }
    public int getDimensionL() { return dimensionL; }
    public int getDimensionW() { return dimensionW; }
    public int getDimensionH() { return dimensionH; }
    public int getWeight() { return weight; }
    public int getDaysInDepot() { return daysInDepot; }
    public String getStatus() { return status; }
    public void updateStatus(String newStatus) { this.status = newStatus; }
}

// FeeCalculator class to handle fee calculation
class FeeCalculator {
    public static double calculateFee(ParcelData parcel) {
        double baseFee = 5.0;
        double weightFee = parcel.getWeight() * 0.5;
        double storageFee = parcel.getDaysInDepot() * 0.2;
        return baseFee + weightFee + storageFee;
    }
}


