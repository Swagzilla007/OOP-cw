package org.ticktet_software.cw_20234071.controller.ticket;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ticktet_software.cw_20234071.dto.TicketDTO;

/**
 * REST Controller for managing ticket operations in the ticketing system.
 * Handles ticket creation, updates, queries, and deletions.
 */
@RequestMapping("/api/tickets")
@RestController
@CrossOrigin(origins = "http://localhost:3002/")
public interface TicketController {

    /**
     * Creates a new ticket in the system.
     *
     * @param ticketDTO the ticket data transfer object containing ticket details
     * @return ResponseEntity containing the created ticket
     *         200 OK if ticket is created successfully
     *         400 Bad Request if ticket data is invalid
     */
    @PostMapping("/SaveTicket")
    ResponseEntity<Object> postTicket(@RequestBody TicketDTO ticketDTO);

    /**
     * Marks a ticket as sold out.
     *
     * @param id the ID of the ticket to mark as sold
     * @return ResponseEntity indicating the result of the operation
     *         200 OK if ticket is marked as sold successfully
     *         404 Not Found if ticket doesn't exist
     */
    @PutMapping("/TicketSold/{id}")
    ResponseEntity<Object> soldOut(@PathVariable Long id);

    /**
     * Retrieves all tickets from the system.
     *
     * @return ResponseEntity containing a list of all tickets
     *         200 OK with list of tickets
     *         404 Not Found if no tickets exist
     */
    @GetMapping("/ShowAllTickets")
    ResponseEntity<Object> showAllTickets();

    /**
     * Checks if a specific ticket is available for purchase.
     *
     * @param id the ID of the ticket to check
     * @return ResponseEntity containing the availability status
     *         200 OK with availability status
     *         404 Not Found if ticket doesn't exist
     */
    @GetMapping("/isAvailable/{id}")
    ResponseEntity<Object> isAvailable(@PathVariable Long id);

    /**
     * Deletes a ticket from the system.
     *
     * @param id the ID of the ticket to delete
     * @return ResponseEntity indicating the result of the operation
     *         200 OK if ticket is deleted successfully
     *         404 Not Found if ticket doesn't exist
     */
    @DeleteMapping("/DeleteTicket/{id}")
    ResponseEntity<Object> deleteTicket(@PathVariable Long id);
}
