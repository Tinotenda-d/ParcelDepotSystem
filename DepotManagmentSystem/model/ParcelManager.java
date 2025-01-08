package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

public class ParcelManager {
    private Map<String, Parcel> parcelMap = new HashMap<>();

    public void loadParcels(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    parcelMap.put(parts[0].trim(),
                            new Parcel(parts[0].trim(),
                                    Integer.parseInt(parts[1].trim()),
                                    Integer.parseInt(parts[2].trim()),
                                    Integer.parseInt(parts[3].trim()),
                                    Integer.parseInt(parts[4].trim()),
                                    Integer.parseInt(parts[5].trim())));
                }
            }
        }
    }
    public void addParcel(Parcel parcel) {
        parcelMap.put(parcel.getParcelId(), parcel);
    }

    public Collection<Parcel> getAllParcels() {
        return parcelMap.values();
    }

    public Parcel getParcelById(String parcelId) {
        return parcelMap.get(parcelId);
    }
}
