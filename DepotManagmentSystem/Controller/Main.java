package Controller;

import model.CustomerManager;
import model.ParcelManager;
import view.ParcelListFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                ParcelManager parcelManager = new ParcelManager();
                CustomerManager customerManager = new CustomerManager();

                customerManager.loadCustomers("D:\\work\\src\\resources\\Custs.csv");
                parcelManager.loadParcels("D:\\work\\src\\resources\\Parcels.csv");

                new ParcelListFrame(parcelManager, customerManager).setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
