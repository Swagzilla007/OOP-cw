package org.ticktet_software.cw_20234071.util;

import lombok.Getter;

import java.util.Scanner;

public class ConfigurationCLI {
    // Return the configuration instance
    @Getter
    private final Configuration configuration;
    private final Scanner scanner;

    public ConfigurationCLI() {
        this.configuration = new Configuration();
        this.scanner = new Scanner(System.in);
    }

    public void configureSystem() {
        System.out.println("Welcome to the Ticketing System Configuration");
        while (true) {
            try {
                System.out.print("Enter Total Tickets: ");
                configuration.setTotalTickets(Integer.parseInt(scanner.nextLine()));

                System.out.print("Enter Ticket Release Rate: ");
                configuration.setTicketReleaseRate(Integer.parseInt(scanner.nextLine()));

                System.out.print("Enter Customer Retrieval Rate: ");
                configuration.setCustomerRetrievalRate(Integer.parseInt(scanner.nextLine()));

                System.out.print("Enter Max Ticket Capacity: ");
                configuration.setMaxTicketCapacity(Integer.parseInt(scanner.nextLine()));

                // Validate input
                if (configuration.validateInput()) {
                    System.out.println("Configuration Complete. Starting system with the following settings:");
                    configuration.displayConfiguration(); // Check configuration fields here
                    break;
                } else {
                    System.out.println("Invalid configuration. Please re-enter values.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

}
