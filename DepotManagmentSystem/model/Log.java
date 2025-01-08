package model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Log {
    private static Log instance;
    private StringBuilder logs = new StringBuilder();

    private Log() {}

    public static Log getInstance() {
        if (instance == null) {
            instance = new Log();
        }
        return instance;
    }

    public void addLog(String event) {
        logs.append(event).append("\n");
    }

    public void writeToFile(String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(logs.toString());
        }
    }

    public String getLogs() {
        return logs.toString();
    }
}
