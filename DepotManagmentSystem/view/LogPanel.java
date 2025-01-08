package view;

import javax.swing.*;
import java.awt.*;
import model.Log;

public class LogPanel extends JFrame {
    private JTextArea logArea;

    public LogPanel(Log log) {
        setTitle("Log Viewer");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        logArea = new JTextArea(log.getLogs().toString());
        logArea.setEditable(false);

        add(new JScrollPane(logArea), BorderLayout.CENTER);
    }
}
