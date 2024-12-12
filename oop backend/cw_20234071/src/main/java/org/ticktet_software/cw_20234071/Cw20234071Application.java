package org.ticktet_software.cw_20234071;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.ticktet_software.cw_20234071.dto.VendorDTO;
import org.ticktet_software.cw_20234071.entity.Customer;
import org.ticktet_software.cw_20234071.entity.TicketPool;
import org.ticktet_software.cw_20234071.entity.Vendor;
import org.ticktet_software.cw_20234071.repository.TicketPoolRepository;
import org.ticktet_software.cw_20234071.service.TicketPoolService;
import org.ticktet_software.cw_20234071.service.VendorService;
import org.ticktet_software.cw_20234071.util.ConfigurationCLI;

import java.util.Scanner;

/**
 * Main Application Class for the Ticketing System
 * 
 * Architectural Decisions:
 * 1. Layered Architecture:
 *    - Presentation Layer (Controllers)
 *    - Business Layer (Services)
 *    - Data Access Layer (Repositories)
 *    This separation allows for better maintainability and testability
 * 
 * 2. Dependency Injection:
 *    - Using Spring's IoC container for loose coupling
 *    - Components are easily replaceable and testable
 * 
 * 3. Repository Pattern:
 *    - Abstracts data persistence operations
 *    - Makes it easier to switch between different database implementations
 * 
 * 4. DTO Pattern:
 *    - Separates domain models from API contracts
 *    - Provides data validation and transformation layer
 */
@SpringBootApplication
public class Cw20234071Application {

    private final TicketPoolService ticketPoolService;
    private final TicketPoolRepository ticketPoolRepository;

    public Cw20234071Application(TicketPoolService ticketPoolService, TicketPoolRepository ticketPoolRepository) {
        this.ticketPoolService = ticketPoolService;
        this.ticketPoolRepository = ticketPoolRepository;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ConfigurableApplicationContext context = null;
        boolean running = true;

        while (running) {
            System.out.println("Enter a command (start/end/simulate):");
            String input = scanner.nextLine().trim().toLowerCase();

            switch (input) {
                case "start":
                    if (context == null || !context.isActive()) {
                        SpringApplication app = new SpringApplication(Cw20234071Application.class);
                        app.setWebApplicationType(WebApplicationType.SERVLET); // Set as needed
                        context = app.run(args);
                        System.out.println("Spring Boot application started.");

                        // Run the logic only when starting the application
                        Cw20234071Application mainApp = context.getBean(Cw20234071Application.class);
                        mainApp.initializeTicketPool();
                    } else {
                        System.out.println("Application is already running.");
                    }
                    break;
                case "end":
                    if (context != null && context.isActive()) {
                        context.close();
                        System.out.println("Spring Boot application stopped and context closed.");
                    } else {
                        System.out.println("Application is not running.");
                    }
                    running = false; // Exit the loop
                    break;

                default:
                    System.out.println("Unknown command. Please enter 'start' or 'end'.");
                    break;
            }
        }

        System.out.println("Exiting application.");
        System.exit(0); // Ensure the program exits after the loop ends
    }

    private void initializeTicketPool() {
        ConfigurationCLI configurationCLI = new ConfigurationCLI();
        configurationCLI.configureSystem();

        TicketPool ticketPool = ticketPoolService.getTicketPool();

        // Initialize the TicketPool properties
        ticketPool.setTotalTickets(configurationCLI.getConfiguration().getTotalTickets());
        ticketPool.setTicketReleaseRate(configurationCLI.getConfiguration().getTicketReleaseRate());
        ticketPool.setCustomerRetrievalRate(configurationCLI.getConfiguration().getCustomerRetrievalRate());
        ticketPool.setMaxTicketCapacity(configurationCLI.getConfiguration().getMaxTicketCapacity());
        ticketPool.setTicketsSold(0);
        ticketPoolRepository.save(ticketPool);

        System.out.println("TicketPool initialized: " + ticketPool.getTotalTickets() + " tickets available.");
    }

    public void deleteTicketPool() {
        ticketPoolRepository.deleteAll(); // Deletes all TicketPool entries
        System.out.println("All TicketPools have been deleted from the repository.");
    }
}
