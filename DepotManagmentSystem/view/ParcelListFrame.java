package view;

import model.CustomerManager;
import model.ParcelManager;
import model.Worker;
import model.Log;
import javax.swing.*;
import java.awt.*;

public class ParcelListFrame extends JFrame {
    private ParcelPanel parcelPanel;
    private ProcessingPanel processingPanel;
    private CustomerQueuePanel customerQueuePanel;
    private Worker worker;

    public ParcelListFrame(ParcelManager parcelManager, CustomerManager customerManager) {
        worker = new Worker(parcelManager, customerManager);

        setTitle("Parcel Management System");
        setSize(1200, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        parcelPanel = new ParcelPanel(parcelManager);
        processingPanel = new ProcessingPanel(worker);
        customerQueuePanel = new CustomerQueuePanel(customerManager);

        add(parcelPanel, BorderLayout.WEST);
        add(processingPanel, BorderLayout.CENTER);
        add(customerQueuePanel, BorderLayout.EAST);

        JPanel buttonPanel = new JPanel();
        JButton processButton = new JButton("Process Next Customer");
        processButton.addActionListener(e -> {
            worker.processNextCustomer();
            refreshPanels();
        });
        buttonPanel.add(processButton);

        JButton logButton = new JButton("View Log");
        logButton.addActionListener(e -> new LogPanel(Log.getInstance()).setVisible(true));
        buttonPanel.add(logButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void refreshPanels() {
        parcelPanel.updateDisplay();
        processingPanel.updateDisplay();
        customerQueuePanel.updateDisplay();
    }
}
