package org.ticktet_software.cw_20234071.controller.vendor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.ticktet_software.cw_20234071.dto.VendorDTO;
import org.ticktet_software.cw_20234071.entity.Vendor;
import org.ticktet_software.cw_20234071.mapper.UserMapper;
import org.ticktet_software.cw_20234071.repository.TicketPoolRepository;
import org.ticktet_software.cw_20234071.repository.TicketRepository;
import org.ticktet_software.cw_20234071.repository.VendorRepository;
import org.ticktet_software.cw_20234071.runnable.VendorRunnable;
import org.ticktet_software.cw_20234071.service.TicketPoolService;
import org.ticktet_software.cw_20234071.service.TicketService;
import org.ticktet_software.cw_20234071.service.VendorService;


import java.util.Map;

import static org.ticktet_software.cw_20234071.util.ResponseHandler.generateResponse;

@Component
public class VendorControllerImpl implements VendorController {

    private static final Logger logger = LogManager.getLogger(VendorControllerImpl.class);

    @Autowired
    private VendorService vendorService;
    @Autowired
    private TicketPoolRepository ticketPoolRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private TicketPoolService ticketPoolService;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private VendorRepository vendorRepository;
    @Autowired
    private UserMapper userMapper;

    public ResponseEntity<Object> postVendor(VendorDTO vendorDTO) {
        try {
            ResponseEntity<Object> response = vendorService.saveVendor(vendorDTO);
            Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
            VendorDTO vendor = objectMapper.convertValue(responseBody.get("data"), VendorDTO.class);
            return response;
        }
        catch (Exception e) {
            logger.error(e);
            return generateResponse("Error occurred!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public ResponseEntity<Object> getAllVendors() {
        try {
            ResponseEntity<Object> response = vendorService.getAllVendors();
            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("getAllVendors -> Fetched all vendors successfully");
            }
            else {
                logger.warn("getAllVendors -> No vendors found.");
            }
            return response;
        }
        catch (Exception e) {
            logger.error("getAllVendors -> Exception: {}", e.getMessage());
            return generateResponse("Error occurred!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public ResponseEntity<Object> getVendorByID(Long id) {
        try {
            ResponseEntity<Object> response = vendorService.getVendorByID(id);
            logger.info("getVendorByID -> Vendor found: ");
            if (response != null) {
                logger.info("getVendorByID -> Vendor found: ");
            }
            else {
                logger.warn("getVendorByID -> No vendor found.");
            }
            return response;
        }
        catch (Exception e){
            logger.error(e);
            return generateResponse("Ërror Occurred", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public ResponseEntity<Object> deleteVendor(Long id) {
        try {
            logger.info("deleteVendor -> Deleting Vendor");
            return vendorService.deleteVendorById(id);
            }
        catch (Exception e) {
            logger.error("deleteVendor -> Error occurred: {}", e.getMessage());
            return generateResponse("Error Occurred", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public ResponseEntity<Object> updateVendor(Long id, VendorDTO vendorDTO) {
        try{
            ResponseEntity<Object> response = vendorService.updateVendor(id, vendorDTO);
            return generateResponse("Vendor updated", HttpStatus.OK, response);
        }
        catch (Exception e){
            logger.error(e);
            return generateResponse("Error occurred!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public ResponseEntity<Object> createTickets(Long id, VendorDTO vendorDTO) {
        try {
            logger.info("createTickets -> Starting ticket creation process for vendor ID: {}", id);
            VendorRunnable vendorRunnable = new VendorRunnable(id, vendorDTO, ticketPoolService, ticketPoolRepository, vendorRepository, vendorService);
            vendorRunnable.run();
            return generateResponse("Ticket creation started", HttpStatus.ACCEPTED, null);
        } catch (Exception e) {
            logger.error("createTickets -> Error occurred while creating tickets: {}", e.getMessage());
            return generateResponse("Error occurred!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }





}
