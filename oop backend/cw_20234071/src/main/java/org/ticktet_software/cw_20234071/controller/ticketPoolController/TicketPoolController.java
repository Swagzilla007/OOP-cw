package org.ticktet_software.cw_20234071.controller.ticketPoolController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ticktet_software.cw_20234071.dto.TicketDTO;
import java.util.List;

/**
 * REST Controller for managing ticket pool operations in the ticketing system.
 * Handles CRUD operations for tickets within pools.
 */
@CrossOrigin(origins = "http://localhost:3002")
@RequestMapping("/api/ticketsc")
public interface TicketPoolController {
    /**
     * Retrieves all tickets from the ticket pool.
     *
     * @return ResponseEntity containing a list of all tickets
     *         200 OK with list of tickets
     *         404 Not Found if no tickets exist
     */
    @GetMapping
    ResponseEntity<List<TicketDTO>> getAllTickets();

    /**
     * Retrieves a specific ticket from the pool by its ID.
     *
     * @param id the ID of the ticket to retrieve
     * @return ResponseEntity containing the ticket details
     *         200 OK with ticket data
     *         404 Not Found if ticket doesn't exist
     */
    @GetMapping("/{id}")
    ResponseEntity<TicketDTO> getTicketById(@PathVariable Long id);

    /**
     * Creates a new ticket in the pool.
     *
     * @param ticketDTO the ticket data transfer object containing ticket details
     * @return ResponseEntity containing the created ticket
     *         201 Created with the new ticket
     *         400 Bad Request if ticket data is invalid
     */
    @PostMapping
    ResponseEntity<TicketDTO> createTicket(@RequestBody TicketDTO ticketDTO);

    /**
     * Updates an existing ticket in the pool.
     *
     * @param id the ID of the ticket to update
     * @param ticketDTO the updated ticket information
     * @return ResponseEntity containing the updated ticket details
     *         200 OK if ticket is updated successfully
     *         404 Not Found if ticket doesn't exist
     *         400 Bad Request if update data is invalid
     */
    @PutMapping("/{id}")
    ResponseEntity<TicketDTO> updateTicket(@PathVariable Long id, @RequestBody TicketDTO ticketDTO);

    /**
     * Deletes a ticket from the pool.
     *
     * @param id the ID of the ticket to delete
     * @return ResponseEntity with no content
     *         204 No Content if ticket is deleted successfully
     *         404 Not Found if ticket doesn't exist
     */
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteTicket(@PathVariable Long id);
}
