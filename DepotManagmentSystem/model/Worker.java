package model;

public class Worker {
    private ParcelManager parcelManager;
    private CustomerManager customerManager;
    private Parcel currentlyProcessingParcel;

    public Worker(ParcelManager parcelManager, CustomerManager customerManager) {
        this.parcelManager = parcelManager;
        this.customerManager = customerManager;
    }

    public void processNextCustomer() {
        Customer customer = customerManager.dequeueCustomer();
        if (customer != null) {
            currentlyProcessingParcel = parcelManager.getParcelById(customer.getParcelId());
            if (currentlyProcessingParcel != null) {
                currentlyProcessingParcel.setProcessed(true);
                Log.getInstance().addLog("Processed Parcel: " + currentlyProcessingParcel.getParcelId());
            } else {
                Log.getInstance().addLog("Parcel not found for Customer: " + customer.getName());
            }
        } else {
            Log.getInstance().addLog("No customers in the queue.");
        }
    }

    public Parcel getCurrentlyProcessingParcel() {
        return currentlyProcessingParcel;
    }
}
