package org.ticktet_software.cw_20234071.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ticktet_software.cw_20234071.dto.CustomerDTO;
import org.ticktet_software.cw_20234071.entity.Customer;
import org.ticktet_software.cw_20234071.entity.Ticket;
import org.ticktet_software.cw_20234071.entity.TicketPool;
import org.ticktet_software.cw_20234071.mapper.UserMapper;
import org.ticktet_software.cw_20234071.repository.CustomerRepository;
import org.ticktet_software.cw_20234071.repository.TicketPoolRepository;
import org.ticktet_software.cw_20234071.repository.TicketRepository;
import org.ticktet_software.cw_20234071.util.Events;

import java.util.List;
import java.util.Objects;

import static org.ticktet_software.cw_20234071.util.ResponseHandler.generateResponse;

@Service
@Transactional
public class CustomerService {

    private static final Logger logger = LogManager.getLogger(CustomerService.class);

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private TicketPoolRepository ticketPoolRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private UserMapper userMapper;

    public ResponseEntity<Object> saveCustomer(CustomerDTO customerDTO) {
        try {
            logger.info("saveCustomer -> Saving new Customer");

            Customer customer = userMapper.CustomerDTOToCustomer(customerDTO);  // Use userMapper here
            customerRepository.save(customer);

            logger.info("saveCustomer -> Customer has been saved");
            return generateResponse("Customer has been saved", HttpStatus.OK, customer);

        } catch (Exception e) {
            logger.error(e);
            return generateResponse("Error occurred", HttpStatus.BAD_REQUEST, e);
        }
    }

    public ResponseEntity<Object> getAllCustomers() {
        try {
            logger.info("getAllCustomers -> Selecting all Customers");
            List<Customer> customers = customerRepository.findAll();
            if (customers.isEmpty()) {
                logger.info("getAllVendors -> Vendor list is empty");
                return generateResponse("Vendor list is empty", HttpStatus.NOT_FOUND, customers);
            } else {
                logger.info("getAllVendors -> Vendor list is selected");
                return generateResponse("Showing all vendors.", HttpStatus.OK, customers);
            }
        } catch (Exception e) {
            logger.error(e);
            return generateResponse("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    public ResponseEntity<Object> getCustomerByID(Long id) {
        try {
            logger.info("Getting customer by ID");
            Customer customer = customerRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("customer does not exist."));

            CustomerDTO customerDTO = userMapper.CustomerToCustomerDTO(customer);
            logger.info("Customer found and selected!");

            return generateResponse("Vendor selected.", HttpStatus.OK, customerDTO);

        } catch (Exception e) {
            logger.error("Unexpected error occurred", e);
            return generateResponse("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public ResponseEntity<Object> deleteCustomerById(Long id) {
        try {
            if (customerRepository.existsById(id)) {
                customerRepository.deleteById(id);
                logger.info("deleteCustomerById -> Customer deleted!");
                return generateResponse("customer Deleted.", HttpStatus.OK, null);
            }
            else {
                logger.warn("deleteCustomerById -> Customer with that id does not exist");
                return generateResponse("Customer does not exist", HttpStatus.NOT_FOUND, null);
            }
        }
        catch (Exception e) {
            logger.error("deleteCustomerById -> Error occurred: {}", e.getMessage());
            return generateResponse("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public ResponseEntity<Object> updateCustomer(Long id, CustomerDTO customerDTO) {
        try {
            if (customerRepository.existsById(id)) {
                Customer existingCustomer = customerRepository.findById(id).orElse(null);

                if (existingCustomer == null) {
                    logger.warn("updateCustomer -> Customer with ID {} not found", id);
                    return generateResponse("Vendor not found", HttpStatus.NOT_FOUND, null);
                }

                if (customerDTO != null) {
//                    if (customerDTO.getCustomerName() != null) {
//                        existingCustomer.setCustomerName(customerDTO.getCustomerName());
//                        logger.info(existingCustomer.getCustomerName());
//                    }
//                    if (customerDTO.getPassword() != null) {
//                        existingCustomer.setPassword(customerDTO.getPassword());
//                        logger.info(existingCustomer.getPassword());
//                    }
//                    if (customerDTO.getTypeNum() >= 0){
//                        existingCustomer.setTypeNum(customerDTO.getTypeNum());
//                    }
                    if (customerDTO.getEventNum() >= 0){
                        existingCustomer.setEventNum(customerDTO.getEventNum());
                    }
                    if (customerDTO.getTicketAmount() >= 0){
                        existingCustomer.setTicketAmount(customerDTO.getTicketAmount());
                    }

                    customerRepository.save(existingCustomer);

                    logger.info("updateCustomer -> Customer with ID {} has been updated!", id);
                    return generateResponse("Customer has been updated", HttpStatus.OK, customerDTO);
                }
            }
            logger.warn("updateCustomer -> Vendor with ID {} does not exist", id);
            return generateResponse("Customer does not exist.", HttpStatus.NOT_FOUND, null);
        }
        catch (Exception e) {
            logger.error("Error occurred while updating Customer: {}", e.getMessage());
            return generateResponse("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public ResponseEntity<Object> validateCustomer(String customerName, String password) {
        try {
            logger.info("Validating Customer with name: {}", customerName);

            Customer customer = customerRepository.findByCustomerName(customerName);
            if (customer != null) {
                if (customer.getPassword().equals(password)) {
                    logger.info("Customer validation successful.");
                    return generateResponse("Customer validated successfully.", HttpStatus.OK, true);
                } else {
                    logger.warn("Customer validation failed: Incorrect password.");
                    return generateResponse("Incorrect password.", HttpStatus.UNAUTHORIZED, false);
                }
            } else {
                logger.warn("Customer validation failed: Vendor not found.");
                return generateResponse("Customer not found.", HttpStatus.NOT_FOUND, false);
            }
        } catch (Exception e) {
            logger.error("Error occurred during vendor validation: {}", e.getMessage());
            return generateResponse("Error occurred during validation.", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public ResponseEntity<Object> PullFromPool(long customerID,long vendorID,int eventNum,int ticketsToRelese){
        try {
            TicketPool ticketPool = ticketPoolRepository.findById(1L).get();

            Events event = Events.fromNumber(eventNum);


            long tickets = ticketRepository.count();
            logger.info("{} total Tickets available", tickets);

            buyTickets(customerID, vendorID, tickets, ticketsToRelese, event, ticketPool);

            long numOfTickets = 10 - ticketsToRelese;
            return generateResponse("Brought" + numOfTickets + "tickets to the Customer", HttpStatus.OK, null);

        }
        catch (Exception e) {
            logger.error(e.getMessage());
            return generateResponse("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private int buyTickets(Long customerID, Long vendorID,  Long tickets,int buyingAmount, Events event, TicketPool ticketPool) {
        for (long ticketID = 1; ticketID <= tickets; ticketID++){
            Ticket ticket = ticketRepository.findById(ticketID).get();
            Customer customer = customerRepository.findById(customerID).get();
            Long vID = ticket.getVendor().getVendorID();


            if (ticket.getTicketPool() != null && Objects.equals(vID, vendorID) && buyingAmount > 0 && ticket.getEvent() == event){
                ticket.setSoldOut(true);
                ticket.setCustomer(customer);
                buyingAmount -= 1;
                ticketRepository.save(ticket);
                logger.info(ticket.getCustomer());
                ticketPool.setTicketsSold(ticketPool.getTicketsSold() + 1);
                ticketPoolRepository.save(ticketPool);

            }
            else {
                if (ticket.getTicketPool() == null && !(Objects.equals(vID, vendorID)) && !(ticket.getEvent() == event)){
                    logger.warn("Ticket is already taken or not matching attributes ID: {} - Ticket Event {} : Needed Event {} | Ticket pool {}", ticket.getTicketId(), ticket.getEvent(), event, ticket.getTicketPool());
                    return buyingAmount;
                }
                if (buyingAmount <= 0){
                    return buyingAmount;
                }
            }
        }
        return buyingAmount;
    }
}


