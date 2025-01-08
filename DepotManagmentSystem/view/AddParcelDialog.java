package view;

import model.Parcel;
import model.ParcelManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddParcelDialog extends JDialog {
    private JTextField parcelIdField;
    private JTextField lengthField;
    private JTextField widthField;
    private JTextField heightField;
    private JTextField weightField;
    private JTextField daysInDepotField;
    private JButton addButton;

    public AddParcelDialog(Frame owner, ParcelManager parcelManager) {
        super(owner, "Add Parcel", true);
        setLayout(new GridLayout(7, 2)); // Adjust grid layout for six fields

        add(new JLabel("Parcel ID:"));
        parcelIdField = new JTextField();
        add(parcelIdField);

        add(new JLabel("Length:"));
        lengthField = new JTextField();
        add(lengthField);

        add(new JLabel("Width:"));
        widthField = new JTextField();
        add(widthField);

        add(new JLabel("Height:"));
        heightField = new JTextField();
        add(heightField);

        add(new JLabel("Weight:"));
        weightField = new JTextField();
        add(weightField);

        add(new JLabel("Days in Depot:"));
        daysInDepotField = new JTextField();
        add(daysInDepotField);

        addButton = new JButton("Add Parcel");
        add(addButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String parcelId = parcelIdField.getText();
                int length = Integer.parseInt(lengthField.getText());
                int width = Integer.parseInt(widthField.getText());
                int height = Integer.parseInt(heightField.getText());
                int weight = Integer.parseInt(weightField.getText());
                int daysInDepot = Integer.parseInt(daysInDepotField.getText());

                Parcel parcel = new Parcel(parcelId, length, width, height, weight, daysInDepot);
                parcelManager.addParcel(parcel);
                dispose();
            }
        });

        pack();
        setLocationRelativeTo(owner);
    }
}
