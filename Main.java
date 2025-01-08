import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// ParcelData class to encapsulate parcel properties
class ParcelData {
    private String parcelId;
    private int dimensionL, dimensionW, dimensionH;
    private int weight;
    private int daysInDepot;
    private String status;

    public ParcelData(String parcelId, int dimensionL, int dimensionW, int dimensionH, int weight, int daysInDepot) {
        this.parcelId = parcelId;
        this.dimensionL = dimensionL;
        this.dimensionW = dimensionW;
        this.dimensionH = dimensionH;
        this.weight = weight;
        this.daysInDepot = daysInDepot;
        this.status = "Waiting";
    }

    public String getParcelId() { return parcelId; }
    public int getDimensionL() { return dimensionL; }
    public int getDimensionW() { return dimensionW; }
    public int getDimensionH() { return dimensionH; }
    public int getWeight() { return weight; }
    public int getDaysInDepot() { return daysInDepot; }
    public String getStatus() { return status; }
    public void updateStatus(String newStatus) { this.status = newStatus; }
}

class FeeCalculator {
    public static double calculateFee(ParcelData parcel) {
        double baseFee = 5.0;
        double weightFee = parcel.getWeight() * 0.5;
        double storageFee = parcel.getDaysInDepot() * 0.2;
        return baseFee + weightFee + storageFee;
    }
}

class Customer {
    private String name;
    private String parcelId;

    public Customer(String name, String parcelId) {
        this.name = name;
        this.parcelId = parcelId;
    }

    public String getName() { return name; }
    public String getParcelId() { return parcelId; }
}

class QueueOfCustomers {
    private Queue<Customer> queue = new LinkedList<>();
    private Log log = Log.getInstance();

    public void addCustomer(Customer customer) {
        queue.add(customer);
        log.addLog("Customer added to queue: " + customer.getName() + " with Parcel ID: " + customer.getParcelId());
    }

    public Customer removeCustomer() {
        Customer customer = queue.poll();
        if (customer != null) {
            log.addLog("Customer removed from queue: " + customer.getName() + " with Parcel ID: " + customer.getParcelId());
        }
        return customer;
    }

    public boolean isEmpty() { return queue.isEmpty(); }

    public Queue<Customer> getQueue() { return queue; }
}

class ParcelMap {
    private Map<String, ParcelData> parcels = new HashMap<>();
    private Log log = Log.getInstance();

    public void addParcel(ParcelData parcel) {
        parcels.put(parcel.getParcelId(), parcel);
        log.addLog("Parcel added: " + parcel.getParcelId());
    }

    public ParcelData getParcel(String parcelId) {
        ParcelData parcel = parcels.get(parcelId);
        if (parcel == null) {
            log.addLog("Error: Parcel with ID " + parcelId + " not found.");
        } else {
            log.addLog("Parcel retrieved: " + parcelId);
        }
        return parcel;
    }

    public Map<String, ParcelData> getParcels() { return parcels; }
}

class Log {
    private static Log instance;
    private StringBuilder logs = new StringBuilder();

    private Log() {}

    public static Log getInstance() {
        if (instance == null) { instance = new Log(); }
        return instance;
    }

    public void addLog(String event) { logs.append(event).append("\n"); }
    public void writeToFile(String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(logs.toString());
        }
    }
    public StringBuilder getLogs() { return logs; }
}

class Worker {
    private ParcelMap parcelMap;
    private Log log = Log.getInstance();

    public Worker(ParcelMap parcelMap) {
        this.parcelMap = parcelMap;
    }

    public void processCustomer(Customer customer) {
        ParcelData parcel = parcelMap.getParcel(customer.getParcelId());
        if (parcel == null) {
            log.addLog("Error: Parcel ID " + customer.getParcelId() + " not found for customer " + customer.getName());
            return;
        }
        if (parcel.getStatus().equals("Waiting")) {
            double fee = FeeCalculator.calculateFee(parcel);
            parcel.updateStatus("Collected");
            log.addLog("Customer " + customer.getName() + " collected Parcel " + parcel.getParcelId() + ". Fee: \u00a3" + String.format("%.2f", fee));
        } else {
            log.addLog("Parcel " + parcel.getParcelId() + " already collected or unavailable for customer " + customer.getName());
        }
    }
}

class DataLoader {
    public static QueueOfCustomers loadCustomers(String customerFile) throws IOException {
        QueueOfCustomers queue = new QueueOfCustomers();
        try (BufferedReader reader = new BufferedReader(new FileReader(customerFile))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                queue.addCustomer(new Customer(parts[0], parts[1]));
            }
        }
        return queue;
    }

    public static ParcelMap loadParcels(String parcelFile) throws IOException {
        ParcelMap parcelMap = new ParcelMap();
        try (BufferedReader reader = new BufferedReader(new FileReader(parcelFile))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                parcelMap.addParcel(new ParcelData(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), Integer.parseInt(parts[5])));
            }
        }
        return parcelMap;
    }
}

class SystemCoordinator {
    private QueueOfCustomers queue;
    private ParcelMap parcelMap;
    private Log log = Log.getInstance();

    public SystemCoordinator(String customerFile, String parcelFile) throws IOException {
        this.queue = DataLoader.loadCustomers(customerFile);
        this.parcelMap = DataLoader.loadParcels(parcelFile);
    }

    public void processQueue() {
        Worker worker = new Worker(parcelMap);
        if (!queue.isEmpty()) {
            Customer customer = queue.removeCustomer();
            worker.processCustomer(customer);
        }
    }

    public void writeLog(String fileName) throws IOException {
        log.writeToFile(fileName);
    }

    public QueueOfCustomers getQueue() { return queue; }
    public ParcelMap getParcelMap() { return parcelMap; }
}

abstract class BasePanel extends JPanel {
    public abstract void updateDisplay(SystemCoordinator coordinator);
}

class QueuePanel extends BasePanel {
    private JTextArea queueTextArea = new JTextArea(10, 20);

    public QueuePanel() {
        setLayout(new BorderLayout());
        queueTextArea.setEditable(false);
        add(new JScrollPane(queueTextArea), BorderLayout.CENTER);
    }

    @Override
    public void updateDisplay(SystemCoordinator coordinator) {
        StringBuilder queueDisplay = new StringBuilder("Queue:\n");
        for (Customer customer : coordinator.getQueue().getQueue()) {
            queueDisplay.append(customer.getName()).append(" - Parcel: ").append(customer.getParcelId()).append("\n");
        }
        queueTextArea.setText(queueDisplay.toString());
    }
}

class ParcelPanel extends BasePanel {
    private JTextArea parcelsTextArea = new JTextArea(10, 20);

    public ParcelPanel() {
        setLayout(new BorderLayout());
        parcelsTextArea.setEditable(false);
        add(new JScrollPane(parcelsTextArea), BorderLayout.CENTER);
    }

    @Override
    public void updateDisplay(SystemCoordinator coordinator) {
        StringBuilder parcelsDisplay = new StringBuilder("Parcels:\n");
        for (ParcelData parcel : coordinator.getParcelMap().getParcels().values()) {
            parcelsDisplay.append(parcel.getParcelId()).append(" - Status: ").append(parcel.getStatus())
                    .append(" - Dimensions: ").append(parcel.getDimensionL()).append("x")
                    .append(parcel.getDimensionW()).append("x")
                    .append(parcel.getDimensionH()).append("\n");
        }
        parcelsTextArea.setText(parcelsDisplay.toString());
    }
}

class LogPanel extends BasePanel {
    private JTextArea logTextArea = new JTextArea(10, 20);

    public LogPanel() {
        setLayout(new BorderLayout());
        logTextArea.setEditable(false);
        add(new JScrollPane(logTextArea), BorderLayout.CENTER);
    }

    @Override
    public void updateDisplay(SystemCoordinator coordinator) {
        logTextArea.setText(Log.getInstance().getLogs().toString());
    }
}

class ParcelGUI extends JFrame {
    private QueuePanel queuePanel;
    private ParcelPanel parcelPanel;
    private LogPanel logPanel;

    public ParcelGUI(SystemCoordinator coordinator) {
        setTitle("Parcel Depot Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        queuePanel = new QueuePanel();
        parcelPanel = new ParcelPanel();
        logPanel = new LogPanel();

        add(queuePanel, BorderLayout.WEST);
        add(parcelPanel, BorderLayout.CENTER);
        add(logPanel, BorderLayout.EAST);

        JPanel buttonsPanel = new JPanel();
        JButton processButton = new JButton("Process Next Customer");
        processButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                coordinator.processQueue();
                updateDisplays(coordinator);
            }
        });
        buttonsPanel.add(processButton);
        add(buttonsPanel, BorderLayout.SOUTH);

        updateDisplays(coordinator);
    }

    private void updateDisplays(SystemCoordinator coordinator) {
        queuePanel.updateDisplay(coordinator);
        parcelPanel.updateDisplay(coordinator);
        logPanel.updateDisplay(coordinator);
    }

    public static void main(String[] args) {
        try {
            SystemCoordinator coordinator = new SystemCoordinator("Custs.csv", "Parcels.csv");
            SwingUtilities.invokeLater(() -> new ParcelGUI(coordinator).setVisible(true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
