package core;

import config.Configuration;
import logging.Logger;

/**
 * Abstract base class for ticket handling operations.
 * Provides common functionality for both vendors and customers
 * in the ticket distribution system.
 */
public abstract class AbstractTicketHandler implements Runnable {
    // Reference to the shared ticket pool
    protected final TicketPool pool;
    // Configuration settings for the handler
    protected final Configuration config;
    // Logger instance for operation tracking
    protected final Logger logger;
    // Status flag for handler operation
    protected volatile boolean running;

    /**
     * Initializes a ticket handler with necessary components
     * @param pool The ticket pool for operations
     * @param config System configuration parameters
     */
    public AbstractTicketHandler(TicketPool pool, Configuration config) {
        this.pool = pool;
        this.config = config;
        this.logger = Logger.getInstance();
        this.running = true;
    }

    /**
     * Signals the handler to stop operations
     */
    public void stop() {
        running = false;
    }

    /**
     * Implements thread sleep with interrupt handling
     * @param millis Duration to sleep in milliseconds
     */
    protected void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            running = false;
            Thread.currentThread().interrupt();
        }
    }
}