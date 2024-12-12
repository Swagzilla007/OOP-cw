package core;

import config.Configuration;
import java.util.*;

/**
 * Manages the pool of available tickets in the system.
 * Provides thread-safe operations for adding and removing tickets,
 * maintaining the integrity of ticket distribution.
 */
public class TicketPool {
    // Set of currently available tickets
    private final Set<Integer> availableTickets = new HashSet<>();
    // List of registered ticket handlers
    private final List<AbstractTicketHandler> handlers = new ArrayList<>();
    // Maximum capacity of the ticket pool
    private final int maxCapacity;
    // Generator for unique ticket IDs
    private int nextTicketId = 1;

    /**
     * Initializes the ticket pool with specified capacity
     * @param config Configuration containing pool parameters
     */
    public TicketPool(Configuration config) {
        this.maxCapacity = config.getMaxCapacity();
    }

    /**
     * Adds a new ticket to the pool if capacity allows
     * @return true if ticket was added, false if pool is at capacity
     */
    public synchronized boolean addTicket() {
        if (availableTickets.size() >= maxCapacity) return false;
        availableTickets.add(nextTicketId++);
        return true;
    }

    /**
     * Returns a ticket to the pool
     * @param ticketId ID of the ticket being returned
     */
    public synchronized void returnTicket(int ticketId) {
        availableTickets.add(ticketId);
    }

    /**
     * Removes and returns a ticket from the pool
     * @return ticket ID if available, null if pool is empty
     */
    public synchronized Integer removeTicket() {
        if (availableTickets.isEmpty()) return null;
        Integer ticket = availableTickets.iterator().next();
        availableTickets.remove(ticket);
        return ticket;
    }

    /**
     * Returns the current number of available tickets
     */
    public synchronized int getAvailableTickets() { return availableTickets.size(); }
    
    /**
     * Returns the list of registered handlers
     */
    public List<AbstractTicketHandler> getHandlers() { return handlers; }
    
    /**
     * Registers a new ticket handler
     */
    public void registerHandler(AbstractTicketHandler handler) { handlers.add(handler); }
}