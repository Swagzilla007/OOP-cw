package org.ticktet_software.cw_20234071.controller.ticket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.ticktet_software.cw_20234071.dto.TicketDTO;
import org.ticktet_software.cw_20234071.service.TicketService;

import static org.ticktet_software.cw_20234071.util.ResponseHandler.generateResponse;

@Component
public class TicketControllerImpl implements TicketController {

    private static final Logger logger = LogManager.getLogger(TicketControllerImpl.class);

    @Autowired
    private TicketService ticketService;

    public ResponseEntity<Object> postTicket(TicketDTO ticketDTO) {
        try {
            return ticketService.saveTicket(ticketDTO);
        }
        catch (Exception e) {
            logger.error(e);
            return generateResponse("Error occurred!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public ResponseEntity<Object> soldOut(Long id){
        try {
            return ticketService.markAsSoldOut(id);
        }
        catch (Exception e) {
            logger.error(e);
            return generateResponse("Error occurred!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public ResponseEntity<Object> showAllTickets() {
        try {
            return ticketService.getAllTickets();
        }
        catch (Exception e) {
            logger.error(e);
            return generateResponse("Error occurred!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public ResponseEntity<Object> deleteTicket(Long id) {
        try {
            return ticketService.deleteTicketByID(id);
        }
        catch (Exception e) {
            logger.error(e);
            return generateResponse("Error occurred!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public ResponseEntity<Object> isAvailable(Long id) {
        try {
            return ticketService.isTicketAvailable(id);
        }
        catch (Exception e) {
            logger.error(e);
            return generateResponse("Error occurred!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


}
