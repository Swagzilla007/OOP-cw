package org.ticktet_software.cw_20234071.runnable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.ticktet_software.cw_20234071.dto.VendorDTO;
import org.ticktet_software.cw_20234071.entity.TicketPool;
import org.ticktet_software.cw_20234071.entity.Vendor;
import org.ticktet_software.cw_20234071.repository.TicketPoolRepository;
import org.ticktet_software.cw_20234071.repository.VendorRepository;
import org.ticktet_software.cw_20234071.service.TicketPoolService;
import org.ticktet_software.cw_20234071.service.VendorService;

import static org.ticktet_software.cw_20234071.util.ResponseHandler.generateResponse;
public class VendorRunnable implements Runnable {

    private static final Logger logger = LogManager.getLogger(VendorRunnable.class);

    private final TicketPoolService ticketPoolService;
    private final VendorDTO vendorDTO;
    private final TicketPoolRepository ticketPoolRepository;
    private final VendorRepository vendorRepository;
    private final VendorService vendorService;

    private final Long id;

    public VendorRunnable(Long id, VendorDTO vendorDTO, TicketPoolService ticketPoolService, TicketPoolRepository ticketPoolRepository, VendorRepository vendorRepository, VendorService vendorService) {
        this.vendorDTO = vendorDTO;
        this.id = id;
        this.ticketPoolService = ticketPoolService;
        this.ticketPoolRepository = ticketPoolRepository;
        this.vendorRepository = vendorRepository;
        this.vendorService = vendorService;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        try {
            int releaseRate = ticketPoolRepository.findById(ticketPoolRepository.count()).get().getTicketReleaseRate();

            int vendorTickets = vendorDTO.getTicketAmount();
            if (vendorTickets > releaseRate) {
                logger.error("VendorRunnable -> Vendor cant sell more then {} tickets.", releaseRate);
                return;
            }

            if (!vendorRepository.existsById(id)) {
                String errorMessage = String.format("VendorID {} not found", id);
                logger.error("VendorRunnable -> {}", errorMessage);
                return;
            }

            vendorService.updateVendor(id, vendorDTO);

            Vendor vendor = vendorRepository.findById(id).get();

            logger.info("VendorRunnable -> Starting ticket creation for vendor ID: {}", id);

            int ticketAmount = vendor.getTicketAmount();
            int ticketsPerRelease = vendor.getTicketsPerRelease();
            int sleepTime = vendor.getReleaseInterval() * 1000;
            int maxCap = ticketPoolRepository.findById(ticketPoolRepository.count()).get().getMaxTicketCapacity();
            int ticketsInPool = ticketPoolRepository.findById(ticketPoolRepository.count()).get().getTicketInPool();

            if(ticketsInPool >= maxCap){
                logger.info("VendorRunnable -> Pools total cap is filled cant add more. ({}/{})",ticketsInPool,maxCap);
                return;
            }

            while (ticketAmount > 0) {
                // Adjust ticketsPerRelease dynamically
                int currentRelease = Math.min(ticketsPerRelease, ticketAmount);

                logger.info("VendorRunnable -> VendorID | {} Adding {} tickets this iteration.", vendor.getVendorID(), currentRelease);

                // Add tickets based on the current release value
                for (int i = 0; i < currentRelease; i++) {
                    ticketPoolService.addTicket(id, vendor);
                    ticketAmount--;

                }

                logger.info("VendorRunnable -> VendorID | {} Thread Sleeping for | {} Seconds", vendor.getVendorID(), (sleepTime / 1000));

                // Sleep before the next iteration
                Thread.sleep(sleepTime);
            }
            logger.info("VendorRunnable -> Ticket creation completed for vendor ID: {}.", vendorDTO.getVendorID());

        } catch (InterruptedException e) {
            logger.error("VendorRunnable -> Thread interrupted for vendor ID: {}", vendorDTO.getVendorID(), e);
            Thread.currentThread().interrupt(); // Restore interrupt status
        } catch (Exception e) {
            logger.error("VendorRunnable -> Error occurred for vendor ID: {}", id, e);
        } finally {
            long endTime = System.currentTimeMillis();
            logger.info("VendorRunnable -> Completed in {} ms for vendor ID: {}",
                    (endTime - startTime), id);
        }
    }

}
