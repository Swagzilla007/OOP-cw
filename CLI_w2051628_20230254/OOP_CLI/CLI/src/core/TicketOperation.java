package core;

/**
 * Defines the contract for ticket operations in the system.
 * Provides methods for basic ticket management operations.
 */
public interface TicketOperation {
    /**
     * Attempts to add a new ticket to the system
     * @return true if ticket was successfully added, false otherwise
     */
    boolean addTicket();

    /**
     * Retrieves a ticket from the system
     * @return ticket ID if available, null if no tickets exist
     */
    Integer getTicket();

    /**
     * Returns the current count of available tickets
     * @return number of available tickets
     */
    int getAvailableTickets();
}