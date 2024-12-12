package org.ticktet_software.cw_20234071.service;

import jakarta.transaction.Transactional;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ticktet_software.cw_20234071.dto.CustomerDTO;
import org.ticktet_software.cw_20234071.dto.VendorDTO;
import org.ticktet_software.cw_20234071.entity.Customer;
import org.ticktet_software.cw_20234071.entity.Ticket;
import org.ticktet_software.cw_20234071.entity.TicketPool;
import org.ticktet_software.cw_20234071.entity.Vendor;
import org.ticktet_software.cw_20234071.repository.CustomerRepository;
import org.ticktet_software.cw_20234071.repository.TicketPoolRepository;
import org.ticktet_software.cw_20234071.repository.TicketRepository;
import org.ticktet_software.cw_20234071.repository.VendorRepository;
import org.ticktet_software.cw_20234071.util.Events;

import java.util.ArrayList;

@Getter
@Service
public class TicketPoolService {

    private static final Logger logger = LogManager.getLogger(TicketPoolService.class);

    @Autowired
    private TicketPoolRepository ticketPoolRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private TicketService ticketService;

    private final TicketPool ticketPool;
    @Autowired
    private VendorRepository vendorRepository;
    @Autowired
    private CustomerRepository customerRepository;

    public TicketPoolService(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }

    ArrayList<Ticket> tickets = new ArrayList<>();
    int ticketsAdded = 0;
    int ticketsSold = 0;


    public synchronized void addTicket(Long vendorID, Vendor vendor) {
        TicketPool ticketPool = ticketPoolRepository.findById(ticketPoolRepository.count()).orElseThrow(
                () -> new RuntimeException("TicketPool not found")
        );

        int maxCap = ticketPool.getMaxTicketCapacity();
        int totalTickets = ticketPool.getTotalTickets();

        // While loop to enforce waiting condition
        while (tickets.size() >= totalTickets && !(ticketsAdded >= maxCap)) {
            logger.info("Waiting: ticketsAdded={}, totalTickets={}, maxCap={}", ticketsAdded, totalTickets, maxCap);
            try {
                wait(); // Release the lock and wait until notified
            } catch (InterruptedException e) {
                logger.error("Thread interrupted: {}", e.getMessage());
                Thread.currentThread().interrupt(); // Restore interrupt status
            }
        }

        // Add a ticket to the pool

        Ticket ticket = ticketService.createTicket(vendor);
        tickets.add(ticket);

        ticketsAdded++;
        ticketPool.setTicketInPool(ticketsAdded);
        ticketPoolRepository.save(ticketPool);


        logger.info("Ticket added: ticketsAdded={}, totalTickets={}, maxCap={}", ticketsAdded, totalTickets, maxCap);

        // Notify all waiting threads to re-check conditions
        notifyAll();
    }

    @Transactional
    public synchronized void deleteTicket(Long customerID, Customer customer) {
        TicketPool ticketPool = ticketPoolRepository.findById(ticketPoolRepository.count()).orElseThrow(
                () -> new RuntimeException("TicketPool not found")
        );

        int totalTickets = ticketPool.getTotalTickets();

        // While loop to ensure tickets are available to delete
        while (tickets.isEmpty()) {
            logger.info("No tickets available.");
            try {
                wait(); // Release the lock and wait until notified
            } catch (InterruptedException e) {
                logger.error("Thread interrupted: {}", e.getMessage());
                Thread.currentThread().interrupt(); // Restore interrupt status
            }
        }

        // Find a ticket to delete based on vendorID and customer info
        Events event = Events.fromNumber(customer.getEventNum());

        Ticket ticketToRemove = tickets.stream()
                .filter(ticket -> ticket.getVendor().getVendorID().equals(customerID) &&
                        ticket.getEvent().equals(event))
                .findFirst()
                .orElse(null); // Return null if no matching ticket is found

        if (ticketToRemove == null) {
            logger.warn("No matching ticket found for VendorID: {} and Event: {}", customerID, event);
            return; // Exit the method without breaking the thread
        }

        Ticket ticket = ticketRepository.getReferenceById(ticketToRemove.getTicketId());
        ticket.setSoldOut(true);
        ticket.setCustomer(customerRepository.getReferenceById(customer.getCustomerID()));
        ticketRepository.save(ticket);

        // Remove the ticket
        tickets.remove(ticketToRemove);
        ticketsSold++;
        ticketPool.setTicketsSold(ticketsSold);
        ticketPoolRepository.save(ticketPool);

        logger.info("Ticket removed: vendorID={}, customerID={}, ticketsSold={}", customerID, customer.getCustomerID(), ticketsSold);

        // Notify all waiting threads to re-check conditions
        notifyAll();
    }


}