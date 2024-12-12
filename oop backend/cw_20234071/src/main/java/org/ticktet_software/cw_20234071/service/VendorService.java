package org.ticktet_software.cw_20234071.service;

import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.ticktet_software.cw_20234071.dto.VendorDTO;
import org.ticktet_software.cw_20234071.entity.Ticket;
import org.ticktet_software.cw_20234071.entity.TicketPool;
import org.ticktet_software.cw_20234071.entity.Vendor;
import org.ticktet_software.cw_20234071.mapper.UserMapper;
import org.ticktet_software.cw_20234071.repository.TicketPoolRepository;
import org.ticktet_software.cw_20234071.repository.TicketRepository;
import org.ticktet_software.cw_20234071.repository.VendorRepository;

import java.util.List;
import java.util.Objects;

import static org.ticktet_software.cw_20234071.util.ResponseHandler.generateResponse;


@Service
@Transactional
public class VendorService {

    private static final Logger logger = LogManager.getLogger(VendorService.class);

    @Autowired
    private VendorRepository vendorRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private TicketPoolRepository ticketPoolRepository;
    @Autowired
    private TicketPoolService ticketPoolService;
    @Autowired
    private TicketRepository ticketRepository;


    public ResponseEntity<Object> saveVendor(VendorDTO vendorDTO) {
        try {
            Vendor existingVendor = vendorRepository.findByVendorName(vendorDTO.getVendorName());
            if (existingVendor != null) {
                logger.error("saveVendor -> two users with the same vendor name cant exist. | {}", vendorDTO.getVendorName());
                return generateResponse("Vendor with this name already exists.", HttpStatus.CONFLICT, null);
            }
            else {
                logger.info("saveVendor -> Saving new vendor");

                Vendor vendor = userMapper.vendorDTOToVendor(vendorDTO);  // Use userMapper here
                vendorRepository.save(vendor);

                logger.info("saveVendor -> Vendor has been saved");
                return generateResponse("Vendor has been saved", HttpStatus.OK, vendor);
            }



        } catch (Exception e) {
            logger.error(e);
            return generateResponse("Error occurred", HttpStatus.BAD_REQUEST, e);
        }
    }

    public ResponseEntity<Object> getAllVendors() {
        try {
            logger.info("getAllVendors -> Selecting all vendors");
            List<Vendor> vendors = vendorRepository.findAll();
            if (vendors.isEmpty()) {
                logger.info("getAllVendors -> Vendor list is empty");
                return generateResponse("Vendor list is empty", HttpStatus.NOT_FOUND, vendors);
            } else {
                logger.info("getAllVendors -> Vendor list is selected");
                return generateResponse("Showing all vendors.", HttpStatus.OK, vendors);
            }
        } catch (Exception e) {
            logger.error("Error fetching Vendors: {}", e.getMessage(), e);
            return generateResponse("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    public ResponseEntity<Object> getVendorByID(Long id) {
        try {
            logger.info("Getting vendor by ID");
            Vendor vendor = vendorRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Vendor does not exist."));

            VendorDTO vendorDTO = userMapper.vendorToVendorDTO(vendor);
            logger.info("Vendor found and selected!");

            return generateResponse("Vendor selected.", HttpStatus.OK, vendorDTO);

        } catch (Exception e) {
            logger.error("Unexpected error occurred", e);
            return generateResponse("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public ResponseEntity<Object> deleteVendorById(Long id) {
        try {
            if (vendorRepository.existsById(id)) {
                vendorRepository.deleteById(id);
                logger.info("deleteVendorById -> Vendor deleted!");
                return generateResponse("Vendor Deleted.", HttpStatus.OK, null);
            }
            else {
                logger.warn("deleteVendorById -> Vendor with that id does not exist");
                return generateResponse("Vendor does not exist", HttpStatus.NOT_FOUND, null);
            }
        }
        catch (Exception e) {
            logger.error("deleteVendorById -> Error occurred: {}", e.getMessage());
            return generateResponse("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public ResponseEntity<Object> updateVendor(Long id, VendorDTO vendorDTO) {
        try {
            if (vendorRepository.existsById(id)) {
                Vendor existingVendor = vendorRepository.findById(id).orElse(null);

                if (existingVendor == null) {
                    logger.warn("updateVendorById -> Vendor with ID {} not found", id);
                    return generateResponse("Vendor not found", HttpStatus.NOT_FOUND, null);
                }

                if (vendorDTO != null) {
//                    if (vendorDTO.getVendorName()!= null) {
//                        existingVendor.setVendorName(vendorDTO.getVendorName());
//                    }
//                    if (vendorDTO.getPassword() != null) {
//                        existingVendor.setPassword(vendorDTO.getPassword());
//                    }

                    if (vendorDTO.getEventNum() > 0){
                        existingVendor.setEventNum(vendorDTO.getEventNum());
                    }
                    if (vendorDTO.getTicketAmount() > 0){
                        existingVendor.setTicketAmount(vendorDTO.getTicketAmount());
                    }
                    if (vendorDTO.getReleaseInterval() > 0){
                        existingVendor.setReleaseInterval(vendorDTO.getReleaseInterval());
                    }
                    if (vendorDTO.getTicketsPerRelease() > 0){
                        existingVendor.setTicketsPerRelease(vendorDTO.getTicketsPerRelease());
                    }

                    vendorRepository.save(existingVendor);

                    logger.info("updateVendorById -> Vendor with ID {} has been updated!", id);
                    return generateResponse("Vendor has been updated", HttpStatus.OK, vendorDTO);
                }
            }
            logger.warn("updateVendorById -> Vendor with ID {} does not exist", id);
            return generateResponse("Vendor does not exist.", HttpStatus.NOT_FOUND, null);
        }
        catch (Exception e) {
            logger.error("Error occurred while updating vendor: {}", e.getMessage());
            return generateResponse("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public ResponseEntity<Object> validateVendor(String vendorName, String password) {
        try {
            logger.info("Validating vendor with name: {}", vendorName);

            Vendor vendor = vendorRepository.findByVendorName(vendorName);
            if (vendor != null) {
                if (vendor.getPassword().equals(password)) {
                    logger.info("Vendor validation successful.");
                    return generateResponse("Vendor validated successfully.", HttpStatus.OK, true);
                } else {
                    logger.warn("Vendor validation failed: Incorrect password.");
                    return generateResponse("Incorrect password.", HttpStatus.UNAUTHORIZED, false);
                }
            } else {
                logger.warn("Vendor validation failed: Vendor not found.");
                return generateResponse("Vendor not found.", HttpStatus.NOT_FOUND, false);
            }
        }
        catch (Exception e) {
            logger.error("Error occurred during vendor validation: {}", e.getMessage());
            return generateResponse("Error occurred during validation.", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public int createTickets(int type, int event, int amount, int ticketsPerVendor, Long id){
        try {
            Vendor Vendor = vendorRepository.findById(id).get();
            long ticketsInPool = ticketRepository.countByVendor(Vendor);

            while (amount > ticketsPerVendor){
                amount -= ticketsPerVendor;
            }

            logger.info("TicketsInPool | {}", ticketsInPool);
            logger.info("TicketsPerVendor | {}", ticketsPerVendor);

            if (ticketsInPool < ticketsPerVendor){
                if (vendorRepository.existsById(id)) {
                    Vendor existingVendor = vendorRepository.findById(id).orElse(null);

                    if (existingVendor != null) {
                        VendorDTO vendorDTO = userMapper.vendorToVendorDTO(existingVendor);
                        vendorDTO.setEventNum(event);

                        vendorDTO.setTicketAmount(amount);
                        updateVendor(id, vendorDTO);

                        vendorRepository.save(existingVendor);
                        logger.info("Fetched details to create tickets.");
                        return amount;
                    }
                }
                logger.error("Vendor does not exist.");
                return amount;
            }
            else {
                logger.warn("Vendor ID | {} | is created the maximum amount of tickets available per vendor. |{}/{}|", id,ticketsInPool, amount);
                return 0;
            }

        }
        catch (Exception e) {
            logger.error(e);
            generateResponse("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            return -1;
        }
    }

    private ResponseEntity<Object> pushToPool(Long vendorID, int ticketReleseRate) {
        TicketPool ticketPool = ticketPoolRepository.findById(1L).get();
        int availableTickets = ticketPool.getTotalTickets() - ticketPool.getTicketInPool();
        logger.info("{} tickets available.", availableTickets);
        int addToPool = ticketReleseRate;
        long ticketAmount = ticketRepository.count();
        if (availableTickets > addToPool){
            addToPool = addToPool(ticketPool, addToPool, ticketAmount , vendorID);
        }
        else {
            addToPool = addToPool(ticketPool, availableTickets, ticketAmount , vendorID);
        }
        if (addToPool != 0){
            return generateResponse("added " + (ticketReleseRate - addToPool)+ " tickets to the pool", HttpStatus.OK, null);
        }

        return generateResponse("all 10 tickets added to the pool.", HttpStatus.OK, null);
    }

    private int addToPool(TicketPool ticketPool, int addToPool, long ticketAmount , Long vendorID) {
        for (long ticketID = 1; ticketID <= ticketAmount; ticketID++) {
            Ticket ticket = ticketRepository.findById(ticketID).orElse(null);
            assert ticket != null;
            Long vID = ticket.getVendor().getVendorID();

            if (ticket.getTicketPool() == null && Objects.equals(vID, vendorID) && addToPool > 0) {
                ticket.setTicketPool(ticketPool);
                logger.info("Added ticket {} to the pool", ticketID);
                ticketRepository.save(ticket);
                ticketPool.setTicketInPool(ticketPool.getTicketInPool() + 1);
                addToPool -= 1;
            }
        }
        ticketPoolRepository.save(ticketPool);
        return addToPool;
    }

    public int getAvailableCapacity() {
        TicketPool ticketPool = ticketPoolRepository.findById(1L).get();
        int currentPoolCapacity = ticketPool.getTotalTickets();
        int maxCapacity = ticketPool.getMaxTicketCapacity();
        return maxCapacity - currentPoolCapacity;
    }






}