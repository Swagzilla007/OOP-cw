package org.ticktet_software.cw_20234071;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.ticktet_software.cw_20234071.dto.CustomerDTO;
import org.ticktet_software.cw_20234071.dto.VendorDTO;
import org.ticktet_software.cw_20234071.entity.TicketPool;
import org.ticktet_software.cw_20234071.repository.CustomerRepository;
import org.ticktet_software.cw_20234071.repository.TicketPoolRepository;
import org.ticktet_software.cw_20234071.repository.VendorRepository;
import org.ticktet_software.cw_20234071.runnable.CustomerRunnable;
import org.ticktet_software.cw_20234071.runnable.VendorRunnable;
import org.ticktet_software.cw_20234071.service.CustomerService;
import org.ticktet_software.cw_20234071.service.TicketPoolService;
import org.ticktet_software.cw_20234071.service.VendorService;

import java.util.Random;

@SpringBootApplication
public class MainForSimulation {

    public static void main(String[] args) {
        // Start the Spring Boot application and get the ApplicationContext
        ApplicationContext context = SpringApplication.run(MainForSimulation.class, args);
        System.out.println("Spring Boot application is running...");

        // Retrieve services from the ApplicationContext
        TicketPoolService ticketPoolService = context.getBean(TicketPoolService.class);
        TicketPoolRepository ticketPoolRepository = context.getBean(TicketPoolRepository.class);
        VendorService vendorService = context.getBean(VendorService.class);
        CustomerService customerService = context.getBean(CustomerService.class);
        VendorRepository vendorRepository = context.getBean(VendorRepository.class);
        CustomerRepository customerRepository = context.getBean(CustomerRepository.class);

        // Initialize and save TicketPool
        TicketPool ticketPool = new TicketPool();
        ticketPool.setTotalTickets(10);
        ticketPool.setTicketReleaseRate(10);
        ticketPool.setCustomerRetrievalRate(10);
        ticketPool.setMaxTicketCapacity(20);
        ticketPool.setTicketsSold(0);
        ticketPoolRepository.save(ticketPool);
        System.out.println("TicketPool initialized: " + ticketPool.getTotalTickets() + " tickets available.");

        // Randomized Vendor creation and Runnable execution
        for (int i = 1; i <= 3; i++) {
            VendorDTO vendorDTO = new VendorDTO();
            vendorDTO.setVendorName("Vendor" + i);
            vendorDTO.setPassword("password" + i);
            vendorService.saveVendor(vendorDTO);

            vendorDTO.setTicketAmount(10);
            vendorDTO.setEventNum(1);

            vendorDTO.setTicketsPerRelease(10);
            vendorDTO.setReleaseInterval(5);
            System.out.println("Vendor saved: " + vendorDTO.getVendorName());

            Thread vendorThread = new Thread(new VendorRunnable(
                    (long)i, vendorDTO, ticketPoolService, ticketPoolRepository, vendorRepository, vendorService
            ));
            vendorThread.start();

            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setCustomerName("Customer" + i);
            customerDTO.setPassword("password" + i);
            customerService.saveCustomer(customerDTO);

            customerDTO.setTicketAmount(10);
            customerDTO.setEventNum(1);
            customerDTO.setTicketPerRetrieve(10);
            customerDTO.setRetrievalInterval(5);
            System.out.println("Customer saved: " + customerDTO.getCustomerName());

            Thread customerThread = new Thread(new CustomerRunnable(
                    customerDTO, i, ticketPoolService, ticketPoolRepository, customerRepository, customerService
            ));
            customerThread.start();
        }
    }
}
