package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class CustomerManager {
    private Queue<Customer> customerQueue = new LinkedList<>();

    public void loadCustomers(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    customerQueue.add(new Customer(parts[0].trim(), parts[1].trim()));
                }
            }
        }
    }

    public Customer dequeueCustomer() {
        return customerQueue.poll();
    }

    public Queue<Customer> getCustomerQueue() {
        return customerQueue;
    }
}
