package org.ticktet_software.cw_20234071.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.ticktet_software.cw_20234071.dto.TicketDTO;
import org.ticktet_software.cw_20234071.dto.VendorDTO;
import org.ticktet_software.cw_20234071.entity.Ticket;
import org.ticktet_software.cw_20234071.entity.Vendor;
import org.ticktet_software.cw_20234071.mapper.TicketMapper;
import org.ticktet_software.cw_20234071.repository.TicketPoolRepository;
import org.ticktet_software.cw_20234071.repository.TicketRepository;
import org.ticktet_software.cw_20234071.repository.VendorRepository;
import org.ticktet_software.cw_20234071.util.Events;

import java.util.List;
import java.util.Optional;

import static org.ticktet_software.cw_20234071.util.ResponseHandler.generateResponse;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private VendorRepository vendorRepository;
    @Autowired
    private TicketMapper ticketMapper;

    private static final Logger logger = LogManager.getLogger(TicketService.class);
    @Autowired
    private TicketPoolRepository ticketPoolRepository;


    public ResponseEntity<Object> saveTicket(TicketDTO ticketDTO) {
        try {
            logger.info("Attempting to save a ticket.....");

            Ticket ticket = new Ticket();
            ticket.setSoldOut(false);
            ticket.setEvent(ticketDTO.getEvent());
            ticket.setPrice(ticketDTO.getPrice());

            ticketRepository.save(ticket);
            logger.info("Ticket saved successfully: {}", ticket);
            return generateResponse("Ticket saved.", HttpStatus.CREATED, ticketDTO);

        } catch (Exception e) {
            logger.error("Error saving ticket: {}", e.getMessage(), e);
            return generateResponse("Error occurred while saving ticket", HttpStatus.BAD_REQUEST, null);
        }
    }

    public ResponseEntity<Object> markAsSoldOut(Long id) {
        try {
            logger.info("Setting ticket ID {} as sold out.", id);

            Ticket existingTicket = ticketRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Ticket does not exist"));

            existingTicket.setSoldOut(true);
            ticketRepository.save(existingTicket);
            logger.info("Ticket ID {} marked as sold out.", id);

            return generateResponse("Ticket marked as sold out.", HttpStatus.OK, existingTicket);
        } catch (RuntimeException e) {
            logger.warn("Error: {}", e.getMessage(), e);
            return generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, null);
        } catch (Exception e) {
            logger.error("Unexpected error : {}", e.getMessage(), e);
            return generateResponse("Unexpected error", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public ResponseEntity<Object> getAllTickets() {
        try {
            logger.info("getAllTickets -> Selecting all Tickets");
            List<Ticket> tickets = ticketRepository.findAll();

            if (tickets.isEmpty()) {
                logger.info("getAllTickets -> Ticket list is empty");
                return generateResponse("Ticket list is empty", HttpStatus.NOT_FOUND, tickets);
            }
            else {
                logger.info("getAllVendors -> Vendor list is selected");
                return generateResponse("Showing all tickets.", HttpStatus.OK, ticketMapper.entityToDto(tickets));

            }
        } catch (Exception e) {
            logger.error("Error fetching tickets: {}", e.getMessage(), e);
            return generateResponse("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    public ResponseEntity<Object> isTicketAvailable(Long id) {
        try {
            logger.info("Searching for ticket: {}.", id);

            Ticket existingTicket = ticketRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Ticket does not exist"));

            if (existingTicket.isSoldOut()) {
                logger.info("Ticket is not available");
                return generateResponse("Ticket is not available", HttpStatus.OK, existingTicket);
            }

            logger.info("Ticket is available.");
            return generateResponse("Ticket is available.", HttpStatus.OK, existingTicket);
        } catch (RuntimeException e) {
            logger.warn("Ticket {} is not available : {}", id, e.getMessage(), e);
            return generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, null);
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage(), e);
            return generateResponse("Unexpected error", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public ResponseEntity<Object> deleteTicketByID(Long id) {
        try {
            logger.info("Attempting to delete ticket ID {}.", id);

            if (ticketRepository.existsById(id)) {
                ticketRepository.deleteById(id);
                logger.info("Ticket ID {} deleted successfully.", id);
                return generateResponse("Ticket ID deleted.", HttpStatus.OK, null);
            } else {
                logger.warn("Ticket ID {} does not exist.", id);
                return generateResponse("Ticket ID does not exist.", HttpStatus.NOT_FOUND, null);
            }
        } catch (Exception e) {
            logger.error("Error occurred while deleting ticket ID {}: {}", id, e.getMessage(), e);
            return generateResponse("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public Ticket createTicket(Vendor vendor) {
        try {
            Events event = Events.fromNumber(vendor.getEventNum());

            Optional<Vendor> vendorOptional = vendorRepository.findById(vendor.getVendorID());
            if (vendorOptional.isEmpty()) {
                logger.warn("Vendor does not exist.");
                throw new RuntimeException("Vendor does not exist.");
            }
            return putTicketDetails(vendorOptional, event);


        } catch (Exception e) {
            logger.error("Error occurred: {}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private Ticket putTicketDetails(Optional<Vendor> vendorOptional, Events event) {
        Vendor vendor = vendorOptional.get();

        Ticket ticket = new Ticket();
        ticket.setEvent(event);
        ticket.setTicketPool(ticketPoolRepository.findById(ticketPoolRepository.count()).get());
        ticket.setSoldOut(false);
        ticket.setVendor(vendor);
        ticketRepository.save(ticket);
        logger.info("Created a ticket for vendor ID: {}", ticket.getVendor().getVendorID());

        return ticket;
    }
}
