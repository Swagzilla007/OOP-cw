package threads;

import core.AbstractTicketHandler;
import core.TicketPool;
import config.Configuration;
import java.util.Random;

/**
 * Represents a ticket vendor in the system.
 * Responsible for adding tickets to the pool at configured intervals.
 * Each vendor manages half of the total ticket allocation.
 */
public class Vendor extends AbstractTicketHandler {
    // Counter for tickets added by this vendor
    private int ticketsAdded;
    // Remaining tickets to be added
    private int remainingTickets;
    // Unique identifier for this vendor
    private final int vendorId;
    // Flag indicating if vendor is still operating
    private boolean active = true;

    /**
     * Initializes a vendor with specified parameters
     * @param pool Shared ticket pool
     * @param config System configuration
     * @param vendorId Unique identifier for this vendor
     */
    public Vendor(TicketPool pool, Configuration config, int vendorId) {
        super(pool, config);
        this.vendorId = vendorId;
        this.remainingTickets = config.getTotalTickets() / 2;
    }

    /**
     * Main execution loop for the vendor thread.
     * Continuously adds tickets to the pool in random batch sizes
     * until all allocated tickets are distributed.
     */
    @Override
    public void run() {
        while (running && remainingTickets > 0) {
            int batchSize = Math.min(new Random().nextInt(3) + 1, remainingTickets);
            int addedInBatch = 0;
            
            for (int i = 0; i < batchSize && pool.addTicket(); i++) {
                addedInBatch++;
                ticketsAdded++;
                remainingTickets--;
            }
            
            if (addedInBatch > 0) {
                logger.log("Vendor " + vendorId + " added batch of " + addedInBatch + 
                          " tickets. Remaining: " + remainingTickets);
            }
            
            sleep(config.getReleaseRate());
        }
        active = false;
    }

    /**
     * @return Total number of tickets added by this vendor
     */
    public int getTicketsAdded() { return ticketsAdded; }

    /**
     * @return true if vendor is still active and has remaining tickets
     */
    public boolean isActive() { return active && remainingTickets > 0; }
}