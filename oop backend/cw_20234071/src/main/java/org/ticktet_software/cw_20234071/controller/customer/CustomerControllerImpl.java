package org.ticktet_software.cw_20234071.controller.customer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.ticktet_software.cw_20234071.dto.CustomerDTO;
import org.ticktet_software.cw_20234071.repository.CustomerRepository;
import org.ticktet_software.cw_20234071.repository.TicketPoolRepository;
import org.ticktet_software.cw_20234071.runnable.CustomerRunnable;
import org.ticktet_software.cw_20234071.service.CustomerService;
import org.ticktet_software.cw_20234071.service.TicketPoolService;

import static org.ticktet_software.cw_20234071.util.ResponseHandler.generateResponse;

@Component
public class CustomerControllerImpl implements CustomerController {

    private static final Logger logger = LogManager.getLogger(CustomerControllerImpl.class);

    @Autowired
    private CustomerService customerService;
    @Autowired
    private TicketPoolRepository ticketPoolRepository;
    @Autowired
    private TicketPoolService ticketPoolService;
    @Autowired
    private CustomerRepository customerRepository;

    public ResponseEntity<Object> postCustomer(CustomerDTO customerDTO) {
        try {
            ResponseEntity<Object> response = customerService.saveCustomer(customerDTO);
            logger.info("addVendor -> Vendor added: {}", customerDTO);
            return response;
        } catch (Exception e) {
            logger.error(e);
            return generateResponse("Error occurred!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public ResponseEntity<Object> getAllCustomers() {
        try {
            ResponseEntity<Object> response = customerService.getAllCustomers();
            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("getAllCustomers -> Fetched all Customers successfully");
            } else {
                logger.warn("getAllCustomers -> No Customers found.");
            }
            return response;
        } catch (Exception e) {
            logger.error("getAllVendors -> Exception: {}", e.getMessage());
            return generateResponse("Error occurred!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public ResponseEntity<Object> getCustomerByID(Long id) {
        try {
            ResponseEntity<Object> response = customerService.getCustomerByID(id);
            logger.info("getCustomerByID -> Customer found: ");
            if (response != null) {
                logger.info("getCustomerByID -> Customer found: ");
            } else {
                logger.warn("getCustomerByID -> No Customer found.");
            }
            return response;
        } catch (Exception e) {
            logger.error(e);
            return generateResponse("Ërror Occurred", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public ResponseEntity<Object> deleteCustomer(Long id) {
        try {
            logger.info("deleteCustomer -> Deleting Customer");
            return customerService.deleteCustomerById(id);
        } catch (Exception e) {
            logger.error("deleteCustomer -> Error occurred: {}", e.getMessage());
            return generateResponse("Error Occurred", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public ResponseEntity<Object> updateCustomer(Long id, CustomerDTO customerDTO) {
        try {
            ResponseEntity<Object> response = customerService.updateCustomer(id, customerDTO);
            return generateResponse("Customer updated", HttpStatus.OK, response);
        } catch (Exception e) {
            logger.error(e);
            return generateResponse("Error occurred!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public ResponseEntity<Object> deleteTickets(Long id, CustomerDTO customerDTO) {
        try {
            logger.info("deleteTickets -> Starting ticket selling process for Customer ID: {}", id);
            CustomerRunnable customerRunnable = new CustomerRunnable(customerDTO, id, ticketPoolService,
                    ticketPoolRepository, customerRepository, customerService);
            customerRunnable.run();

            logger.info("createTickets -> Ticket creation process initiated for vendor ID: {}", id);
            return generateResponse("Ticket creation started", HttpStatus.ACCEPTED, null);
        } catch (Exception e) {
            logger.error("createTickets -> Error occurred while creating tickets: {}", e.getMessage());
            return generateResponse("Error occurred!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
