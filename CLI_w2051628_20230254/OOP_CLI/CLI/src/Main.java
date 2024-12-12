import config.Configuration;
import config.SimulationParameters;
import core.TicketPool;
import threads.Customer;
import threads.Vendor;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Main controller class for the Ticket Distribution System.
 * Manages the simulation of ticket distribution between vendors and customers
 * in a multi-threaded environment.
 */
public class Main {
    /** System configuration instance */
    private static Configuration config;
    /** Central ticket pool for the distribution system */
    private static TicketPool ticketPool;
    /** Set of currently active customers */
    private static Set<Customer> activeCustomers = new HashSet<>();
    /** Set of customer threads */
    private static Set<Thread> customerThreadSet = new HashSet<>();
    /** Set of active vendor instances */
    private static Set<Vendor> activeVendors = new HashSet<>();
    /** Set of vendor threads */
    private static Set<Thread> vendorThreadSet = new HashSet<>();
    /** Generator for unique customer IDs */
    private static AtomicInteger customerIdGenerator = new AtomicInteger(1);
    /** Control flag for simulation state */
    private static volatile boolean isSimulationActive = true;

    /**
     * Entry point for the ticket distribution simulation.
     * Initializes the system, starts the simulation, and manages the lifecycle
     * of vendors and customers.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SimulationParameters params = new SimulationParameters();
        int[] parameters = getParameters(scanner, params);
        initializeSystem(parameters[0], parameters[1], parameters[2], parameters[3]);
        
        System.out.println("\nPress Enter to start simulation process...");
        scanner.nextLine();

        try {
            startSimulation();
            monitorAndCreateCustomers();
            waitForCompletion();
            printResults();
        } catch (InterruptedException e) {
            System.out.println("Simulation interrupted");
            Thread.currentThread().interrupt();
        }
        scanner.close();
    }

    /**
     * Retrieves simulation parameters either from stored values or user input.
     *
     * @param scanner Scanner for user input
     * @param params SimulationParameters instance for parameter storage
     * @return Array containing [releaseInterval, retrievalInterval, poolCapacity, totalTickets]
     */
    private static int[] getParameters(Scanner scanner, SimulationParameters params) {
        int totalTicketCount, poolCapacity, ticketReleaseInterval, ticketRetrievalInterval;
        
        if (params.hasStoredParameters() && shouldUseStoredParameters(scanner, params)) {
            totalTicketCount = params.getStoredTotalTickets();
            poolCapacity = params.getStoredMaxCapacity();
            ticketReleaseInterval = params.getStoredReleaseRate();
            ticketRetrievalInterval = params.getStoredRetrievalRate();
        } else {
            totalTicketCount = promptForPositiveInteger(scanner, "Enter total number of tickets:");
            poolCapacity = promptForPositiveInteger(scanner, "Enter maximum ticket pool capacity:");
            ticketReleaseInterval = promptForPositiveInteger(scanner, "Enter ticket release interval (milliseconds):");
            ticketRetrievalInterval = promptForPositiveInteger(scanner, "Enter ticket retrieval interval (milliseconds):");
            params.saveParameters(ticketReleaseInterval, ticketRetrievalInterval, poolCapacity, totalTicketCount);
        }
        return new int[]{ticketReleaseInterval, ticketRetrievalInterval, poolCapacity, totalTicketCount};
    }

    /**
     * Prompts user to decide whether to use stored parameters.
     *
     * @param scanner Scanner for user input
     * @param params SimulationParameters containing stored values
     * @return true if stored parameters should be used
     */
    private static boolean shouldUseStoredParameters(Scanner scanner, SimulationParameters params) {
        System.out.println("\nPast workflow found:");
        System.out.printf("Total Tickets: %d%nRelease Rate: %d ms%nRetrieval Rate: %d ms%nMax Capacity: %d%n",
                        params.getStoredTotalTickets(), params.getStoredReleaseRate(),
                        params.getStoredRetrievalRate(), params.getStoredMaxCapacity());
        System.out.println("\nDo you want to work with past workflow? (y/n)");
        return scanner.nextLine().trim().toLowerCase().equals("y");
    }

    /**
     * Initializes the system with specified parameters.
     *
     * @param releaseInterval Interval between ticket releases
     * @param retrievalInterval Interval between ticket retrieval attempts
     * @param maxCapacity Maximum capacity of ticket pool
     * @param totalTickets Total number of tickets in system
     */
    private static void initializeSystem(int releaseInterval, int retrievalInterval, int maxCapacity, int totalTickets) {
        config = new Configuration(releaseInterval, retrievalInterval, maxCapacity, totalTickets);
        ticketPool = new TicketPool(config);
        displaySystemConfiguration(totalTickets, releaseInterval, retrievalInterval, maxCapacity);
    }

    /**
     * Displays the current system configuration.
     *
     * @param totalTickets Total number of tickets
     * @param releaseInterval Ticket release interval
     * @param retrievalInterval Ticket retrieval interval
     * @param maxCapacity Maximum pool capacity
     */
    private static void displaySystemConfiguration(int totalTickets, int releaseInterval, int retrievalInterval, int maxCapacity) {
        System.out.printf("%nSystem initialized with:%nTotal Tickets: %d%nRelease Interval: %d ms%n" +
                         "Retrieval Interval: %d ms%nMaximum Capacity: %d%n",
                         totalTickets, releaseInterval, retrievalInterval, maxCapacity);
    }

    /**
     * Prompts for and validates positive integer input.
     *
     * @param scanner Scanner for user input
     * @param prompt Message to display to user
     * @return Valid positive integer input
     */
    private static int promptForPositiveInteger(Scanner scanner, String prompt) {
        while (true) {
            System.out.println(prompt);
            if (scanner.hasNextInt()) {
                int value = scanner.nextInt();
                scanner.nextLine();
                if (value > 0) return value;
            } else scanner.nextLine();
            System.out.println("Invalid input, try working again with positive numbers");
        }
    }

    /**
     * Initializes and starts the simulation by creating vendors and customers.
     */
    private static void startSimulation() {
        initializeVendors();
        initializeCustomers(5);
    }

    /**
     * Initializes vendor threads for ticket distribution.
     * Creates two vendors, each responsible for half of total tickets.
     */
    private static void initializeVendors() {
        for (int i = 1; i <= 2; i++) {
            Vendor vendor = new Vendor(ticketPool, config, i);
            Thread vendorThread = new Thread(vendor);
            activeVendors.add(vendor);
            vendorThreadSet.add(vendorThread);
            vendorThread.start();
        }
    }

    /**
     * Creates initial set of customer threads.
     *
     * @param count Number of initial customers to create
     */
    private static void initializeCustomers(int count) {
        for (int i = 0; i < count; i++) {
            createNewCustomer();
        }
    }

    /**
     * Creates and starts a new customer thread with unique ID.
     */
    private static void createNewCustomer() {
        Customer customer = new Customer(ticketPool, config, customerIdGenerator.getAndIncrement());
        Thread customerThread = new Thread(customer);
        activeCustomers.add(customer);
        customerThreadSet.add(customerThread);
        customerThread.start();
    }

    /**
     * Monitors customer activity and maintains optimal customer count.
     * Creates new customers when tickets are available and count is below threshold.
     */
    private static void monitorAndCreateCustomers() {
        new Thread(() -> {
            while (isSimulationActive) {
                if (activeVendors.stream().noneMatch(Vendor::isActive)) break;
                
                activeCustomers.removeIf(customer -> !customer.isActive());
                
                if (ticketPool.getAvailableTickets() > 0) {
                    while (activeCustomers.size() < 5) {
                        createNewCustomer();
                    }
                }
                
                try {
                    Thread.sleep(config.getRetrievalRate());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
    }

    /**
     * Waits for all vendor and customer threads to complete.
     * Performs cleanup and ensures orderly shutdown.
     *
     * @throws InterruptedException if thread is interrupted during wait
     */
    private static void waitForCompletion() throws InterruptedException {
        while (activeVendors.stream().anyMatch(Vendor::isActive)) {
            Thread.sleep(config.getRetrievalRate());
        }
        isSimulationActive = false;
        activeVendors.forEach(Vendor::stop);
        
        for (Thread thread : vendorThreadSet) {
            thread.join();
        }
        for (Thread thread : customerThreadSet) {
            thread.join();
        }
    }

    /**
     * Displays final simulation statistics including tickets processed
     * and customer count.
     */
    private static void printResults() {
        System.out.println("\n=== Simulation Summary ===");
        int totalTicketsProcessed = 0;
        
        for (Vendor vendor : activeVendors) {
            int vendorTickets = vendor.getTicketsAdded();
            System.out.printf("Vendor %d Statistics:%n  Tickets Processed: %d%n", 
                            vendor.hashCode() % 100, vendorTickets);
            totalTicketsProcessed += vendorTickets;
        }
        
        System.out.printf("%nFinal Statistics:%n");
        System.out.printf("Total Tickets Processed: %d%n", totalTicketsProcessed);
        System.out.printf("Total Customers Served: %d%n", customerIdGenerator.get() - 1);
    }
}