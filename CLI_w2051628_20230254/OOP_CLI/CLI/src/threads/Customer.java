package threads;

import core.AbstractTicketHandler;
import core.TicketPool;
import config.Configuration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a customer in the ticket system.
 * Attempts to purchase a random number of tickets (1-4)
 * using an atomic transaction approach.
 */
public class Customer extends AbstractTicketHandler {
    // List of successfully purchased ticket IDs
    private final List<Integer> purchasedTickets = new ArrayList<>();
    // Customer identification and ticket request parameters
    private final int customerId, requestedTickets;
    // Flag indicating if customer is still attempting to purchase
    private boolean active = true;

    /**
     * Initializes a customer with specified parameters
     * @param pool Shared ticket pool
     * @param config System configuration
     * @param customerId Unique identifier for this customer
     */
    public Customer(TicketPool pool, Configuration config, int customerId) {
        super(pool, config);
        this.customerId = customerId;
        this.requestedTickets = new Random().nextInt(4) + 1;
        logger.log("Customer " + customerId + " requesting " + requestedTickets + " tickets");
    }

    /**
     * Main execution loop for the customer thread.
     * Implements an atomic transaction pattern for ticket purchases:
     * - Attempts to acquire all requested tickets
     * - Rolls back (returns tickets) if unable to complete the full purchase
     * - Terminates when either successful or no more tickets available
     */
    @Override
    public void run() {
        while (running) {
            List<Integer> tempTickets = new ArrayList<>();
            boolean success = true;

            for (int i = 0; i < requestedTickets && success; i++) {
                Integer ticket = pool.removeTicket();
                if (ticket != null) tempTickets.add(ticket);
                else {
                    success = false;
                    for (Integer t : tempTickets) {
                        pool.returnTicket(t);
                    }
                    logger.log("Customer " + customerId + " returning ticket due to incomplete purchase");
                }
            }
                
            if (success) {
                purchasedTickets.addAll(tempTickets);
                logger.log("Customer " + customerId + " purchased " + requestedTickets + " tickets");
                break;
            }

            if (pool.getAvailableTickets() == 0 && !hasActiveVendors()) break;
            sleep(config.getRetrievalRate());
        }
        active = false;
    }

    /**
     * Checks if any vendors are still active in the system
     * @return true if at least one vendor is still active
     */
    private boolean hasActiveVendors() {
        return pool.getHandlers().stream()
                .filter(h -> h instanceof Vendor)
                .map(h -> (Vendor)h)
                .anyMatch(Vendor::isActive);
    }

    /**
     * @return List of tickets successfully purchased by this customer
     */
    public List<Integer> getPurchasedTickets() { return purchasedTickets; }

    /**
     * @return Customer's unique identifier
     */
    public int getCustomerId() { return customerId; }

    /**
     * @return true if customer is still attempting to purchase tickets
     */
    public boolean isActive() { return active; }
}