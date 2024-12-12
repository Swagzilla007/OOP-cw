package org.ticktet_software.cw_20234071.runnable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ticktet_software.cw_20234071.dto.CustomerDTO;
import org.ticktet_software.cw_20234071.entity.Customer;
import org.ticktet_software.cw_20234071.repository.CustomerRepository;
import org.ticktet_software.cw_20234071.repository.TicketPoolRepository;
import org.ticktet_software.cw_20234071.service.CustomerService;
import org.ticktet_software.cw_20234071.service.TicketPoolService;

public class CustomerRunnable implements Runnable {

    private static final Logger logger = LogManager.getLogger(CustomerRunnable.class);
    private final CustomerDTO customerDTO;
    private final long id;
    private final TicketPoolService ticketPoolService;
    private final TicketPoolRepository ticketPoolRepository;
    private final CustomerRepository customerRepository;
    private final CustomerService customerService;

    public CustomerRunnable(CustomerDTO customerDTO, long id, TicketPoolService ticketPoolService, TicketPoolRepository ticketPoolRepository, CustomerRepository customerRepository, CustomerService customerService) {
        this.customerDTO = customerDTO;
        this.id = id;
        this.ticketPoolService = ticketPoolService;
        this.ticketPoolRepository = ticketPoolRepository;
        this.customerRepository = customerRepository;
        this.customerService = customerService;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        try {
            int retrievalRate = ticketPoolRepository.findById(ticketPoolRepository.count()).get().getCustomerRetrievalRate();

            int customerTickets = customerDTO.getTicketPerRetrieve();

            if (customerTickets > retrievalRate) {
                logger.error("CustomerRunnable -> Customer cant buy more then {} tickets.", retrievalRate);
                return;
            }

            if (!customerRepository.existsById(id)) {
                String errorMessage = String.format("VendorID {} not found", id);
                logger.error("VendorRunnable -> {}", errorMessage);
                return;
            }
            customerService.updateCustomer(id, customerDTO);

            Customer customer = customerRepository.findById(id).get();

            logger.info("CustomerRunnable -> Starting ticket selling for Customer ID: {}", customer.getCustomerID());
            int ticketAmount = customer.getTicketAmount();
            int ticketsPerRetrieve = customer.getTicketPerRetrieve();
            int sleepTime = customer.getRetrievalInterval() * 1000;
            int maxCap = ticketPoolRepository.findById(ticketPoolRepository.count()).get().getMaxTicketCapacity();
            int ticketsSold = ticketPoolRepository.findById(ticketPoolRepository.count()).get().getTicketsSold();

            if (ticketsSold >= maxCap) {
                logger.info("CustomerRunnable -> All of the tickets in the pool are sold out. ({}/{})",ticketsSold,maxCap);
                return;
            }

            while (ticketAmount > 0){

                int currentRelease = Math.min(ticketsPerRetrieve, ticketAmount);

                logger.info("CustomerRunnable -> CustomerID | {} Buying {} tickets this iteration.", customer.getCustomerID(), currentRelease);

                for (int i = 0; i < currentRelease; i++) {
                    ticketPoolService.deleteTicket(id, customer);
                    ticketAmount--; // Decrease the total ticket count
                }
                logger.info("CustomerRunnable -> customerID | {} Thread Sleeping for| {} Seconds", id, (sleepTime/1000));
                Thread.sleep(sleepTime);
                ticketsPerRetrieve = customerDTO.getTicketPerRetrieve();
            }
            logger.info("CustomerRunnable -> Ticket creation completed for Customer ID: {}.", id);
        }
        catch (Exception e) {
            logger.error("CustomerRunnable -> Error occurred for Customer ID: {}", id, e);
        }
        finally {
            long endTime = System.currentTimeMillis();
            logger.info("CustomerRunnable -> Completed in {} ms for vendor ID: {}",
                    (endTime - startTime), id);
        }

    }



}
