package model;

public class FeeCalculator {
    public static double calculateFee(int weight, int daysInDepot) {
        double baseFee = 5.0;
        double weightFee = weight * 0.5;
        double storageFee = daysInDepot * 0.2;
        return baseFee + weightFee + storageFee;
    }
}
