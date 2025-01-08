package view;

import model.Worker;

import javax.swing.*;
import java.awt.*;

public class ProcessingPanel extends JPanel {
    private JTextArea processingTextArea;
    private Worker worker;

    public ProcessingPanel(Worker worker) {
        this.worker = worker;
        setLayout(new BorderLayout());
        processingTextArea = new JTextArea(10, 30);
        processingTextArea.setEditable(false);
        add(new JScrollPane(processingTextArea), BorderLayout.CENTER);
        updateDisplay();
    }

    public void updateDisplay() {
        if (worker.getCurrentlyProcessingParcel() != null) {
            processingTextArea.setText("Processing: " + worker.getCurrentlyProcessingParcel().getParcelId());
        } else {
            processingTextArea.setText("No parcel currently being processed.");
        }
    }
}
