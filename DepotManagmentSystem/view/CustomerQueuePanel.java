package view;

import model.CustomerManager;

import javax.swing.*;
import java.awt.*;

public class CustomerQueuePanel extends JPanel {
    private JTextArea queueTextArea;
    private CustomerManager customerManager;

    public CustomerQueuePanel(CustomerManager customerManager) {
        this.customerManager = customerManager;
        setLayout(new BorderLayout());
        queueTextArea = new JTextArea(20, 30);
        queueTextArea.setEditable(false);
        add(new JScrollPane(queueTextArea), BorderLayout.CENTER);
        updateDisplay();
    }

    public void updateDisplay() {
        StringBuilder sb = new StringBuilder("Customer Queue:\n");
        customerManager.getCustomerQueue().forEach(customer -> sb.append(customer.getName())
                .append(" - Parcel: ").append(customer.getParcelId()).append("\n"));
        queueTextArea.setText(sb.toString());
    }
}
