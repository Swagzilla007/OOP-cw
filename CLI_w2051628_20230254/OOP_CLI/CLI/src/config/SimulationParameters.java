package config;

import java.io.*;
import java.util.Properties;

public class SimulationParameters {
    private static final String PARAM_FILE = "simulation_params.txt";
    private Properties properties;

    public SimulationParameters() {
        properties = new Properties();
        loadParameters();
    }

    public void saveParameters(int releaseRate, int retrievalRate, int maxCapacity, int totalTickets) {
        properties.setProperty("releaseRate", String.valueOf(releaseRate));
        properties.setProperty("retrievalRate", String.valueOf(retrievalRate));
        properties.setProperty("maxCapacity", String.valueOf(maxCapacity));
        properties.setProperty("totalTickets", String.valueOf(totalTickets));

        try (OutputStream output = new FileOutputStream(PARAM_FILE)) {
            properties.store(output, "Simulation Parameters");
        } catch (IOException e) {
            System.out.println("Failed to save parameters: " + e.getMessage());
        }
    }

    public boolean hasStoredParameters() {
        return !properties.isEmpty();
    }

    public int getStoredReleaseRate() {
        return Integer.parseInt(properties.getProperty("releaseRate", "0"));
    }

    public int getStoredRetrievalRate() {
        return Integer.parseInt(properties.getProperty("retrievalRate", "0"));
    }

    public int getStoredMaxCapacity() {
        return Integer.parseInt(properties.getProperty("maxCapacity", "0"));
    }

    public int getStoredTotalTickets() {
        return Integer.parseInt(properties.getProperty("totalTickets", "0"));
    }

    private void loadParameters() {
        File file = new File(PARAM_FILE);
        if (file.exists()) {
            try (InputStream input = new FileInputStream(PARAM_FILE)) {
                properties.load(input);
            } catch (IOException e) {
                System.out.println("Failed to load parameters: " + e.getMessage());
                properties = new Properties(); // Reset on error
            }
        }
    }
}
