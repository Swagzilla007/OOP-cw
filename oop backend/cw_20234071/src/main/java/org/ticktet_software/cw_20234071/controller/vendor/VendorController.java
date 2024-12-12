package org.ticktet_software.cw_20234071.controller.vendor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ticktet_software.cw_20234071.dto.VendorDTO;

/**
 * REST Controller for managing vendor operations in the ticketing system.
 * Handles vendor registration, updates, queries, and ticket creation by vendors.
 */
@RequestMapping("/api/vendors")
@RestController
public interface VendorController {
    /**
     * Registers a new vendor in the system.
     *
     * @param vendorDTO the vendor data transfer object containing vendor details
     * @return ResponseEntity containing the created vendor
     *         200 OK if vendor is registered successfully
     *         400 Bad Request if vendor data is invalid
     */
    @PostMapping("/addVendor")
    ResponseEntity<Object> postVendor(@RequestBody VendorDTO vendorDTO);

    /**
     * Retrieves all vendors from the system.
     *
     * @return ResponseEntity containing a list of all vendors
     *         200 OK with list of vendors
     *         404 Not Found if no vendors exist
     */
    @GetMapping("/showAllVendors")
    ResponseEntity<Object> getAllVendors();

    /**
     * Retrieves a specific vendor by their ID.
     *
     * @param id the ID of the vendor to retrieve
     * @return ResponseEntity containing the vendor details
     *         200 OK with vendor data
     *         404 Not Found if vendor doesn't exist
     */
    @GetMapping("/showVendorByID/{id}")
    ResponseEntity<Object> getVendorByID(@PathVariable Long id);

    /**
     * Deletes a vendor from the system.
     *
     * @param id the ID of the vendor to delete
     * @return ResponseEntity indicating the result of the operation
     *         200 OK if vendor is deleted successfully
     *         404 Not Found if vendor doesn't exist
     */
    @DeleteMapping("/DeleteVendor/{id}")
    ResponseEntity<Object> deleteVendor(@PathVariable Long id);

    /**
     * Updates an existing vendor's information.
     *
     * @param id the ID of the vendor to update
     * @param vendorDTO the updated vendor information
     * @return ResponseEntity containing the updated vendor details
     *         200 OK if vendor is updated successfully
     *         404 Not Found if vendor doesn't exist
     *         400 Bad Request if update data is invalid
     */
    @PutMapping("/UpdateVendor/{id}")
    ResponseEntity<Object> updateVendor(@PathVariable Long id, @RequestBody VendorDTO vendorDTO);

    /**
     * Creates tickets for a specific vendor.
     *
     * @param id the ID of the vendor creating tickets
     * @param vendorDTO vendor data transfer object containing ticket creation details
     * @return ResponseEntity containing the created tickets information
     *         200 OK if tickets are created successfully
     *         404 Not Found if vendor doesn't exist
     *         400 Bad Request if ticket creation data is invalid
     */
    @PostMapping("/createTickets/{id}")
    ResponseEntity<Object> createTickets(@PathVariable Long id, @RequestBody VendorDTO vendorDTO);
}
