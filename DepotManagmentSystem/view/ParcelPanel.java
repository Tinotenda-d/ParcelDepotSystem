package view;

import model.Parcel;
import model.ParcelManager;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;

public class ParcelPanel extends JPanel {
    private JTextArea parcelTextArea;
    private ParcelManager parcelManager;

    public ParcelPanel(ParcelManager parcelManager) {
        this.parcelManager = parcelManager;
        setLayout(new BorderLayout());
        parcelTextArea = new JTextArea(20, 30);
        parcelTextArea.setEditable(false);
        add(new JScrollPane(parcelTextArea), BorderLayout.CENTER);
        updateDisplay();
    }

    public void updateDisplay() {
        StringBuilder sb = new StringBuilder("Parcels:\n");
        Collection<Parcel> parcels = parcelManager.getAllParcels();
        parcels.forEach(parcel -> sb.append(parcel.getParcelId())
                .append(" - Processed: ").append(parcel.isProcessed()).append("\n"));
        parcelTextArea.setText(sb.toString());
    }
}
