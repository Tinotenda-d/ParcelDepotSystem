import java.io.*;
import java.util.*;


// Day 1: Core Functionality

// ParcelData class to encapsulate parcel properties (Single Responsibility Principle)
class ParcelData {
    private String parcelId; // Unique identifier for the parcel
    private int dimensionL, dimensionW, dimensionH; // Dimensions of the parcel
    private int weight; // Weight of the parcel
    private int daysInDepot; // Number of days in the depot
    private String status; // Parcel status: "Waiting" or "Collected"

    public ParcelData(String parcelId, int dimensionL, int dimensionW, int dimensionH, int weight, int daysInDepot) {
        this.parcelId = parcelId;
        this.dimensionL = dimensionL;
        this.dimensionW = dimensionW;
        this.dimensionH = dimensionH;
        this.weight = weight;
        this.daysInDepot = daysInDepot;
        this.status = "Waiting"; // Default status
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

// FeeCalculator class to handle fee calculation
class FeeCalculator {
    public static double calculateFee(ParcelData parcel) {
        double baseFee = 5.0;
        double weightFee = parcel.getWeight() * 0.5;
        double storageFee = parcel.getDaysInDepot() * 0.2;
        return baseFee + weightFee + storageFee;
    }
}

// Day 2: Queue and Map Management

// Customer class to encapsulate customer-related data
class Customer {
    private String name; // Name of the customer
    private String parcelId; // Parcel ID for the customer

    public Customer(String name, String parcelId) {
        this.name = name;
        this.parcelId = parcelId;
    }

    public String getName() { return name; }
    public String getParcelId() { return parcelId; }
}

// QueueOfCustomers class for queue management
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

// ParcelMap class for managing parcels
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

// Day 3: Logging and Processing

// Log class (Singleton pattern) for event logging
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

// Worker class for processing customers
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

// Day 4: Data Loading and Coordination

// DataLoader class to load data from files
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

// SystemCoordinator class for main processing logic
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
