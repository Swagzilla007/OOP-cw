package org.ticktet_software.cw_20234071.util;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Configuration {
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;

    // Validate input values
    public boolean validateInput() {
        if (totalTickets <= 0 || ticketReleaseRate <= 0 || customerRetrievalRate <= 0 || maxTicketCapacity <= 0) {
            System.out.println("All values must be positive numbers.");
            return false;
        }
        if (maxTicketCapacity < totalTickets) {
            System.out.println("Max ticket capacity must be greater than or equal to total tickets.");
            return false;
        }
        if (ticketReleaseRate > totalTickets || customerRetrievalRate > totalTickets) {
            System.out.println("Ticket release rate must be greater than or equal to total tickets.");
            return false;
        }
        return true;
    }

    // Display configuration details
    public void displayConfiguration() {
        System.out.println("Current Configuration:");
        System.out.println("Total Tickets: " + totalTickets);
        System.out.println("Ticket Release Rate: " + ticketReleaseRate + " tickets per interval");
        System.out.println("Customer Retrieval Rate: " + customerRetrievalRate + " tickets per interval");
        System.out.println("Max Ticket Capacity: " + maxTicketCapacity);
    }
}
