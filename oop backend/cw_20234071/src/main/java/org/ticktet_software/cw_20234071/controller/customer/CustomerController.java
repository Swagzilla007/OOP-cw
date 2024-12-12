package org.ticktet_software.cw_20234071.controller.customer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ticktet_software.cw_20234071.dto.CustomerDTO;

/**
 * REST Controller for managing customer operations in the ticketing system.
 * Provides endpoints for creating, reading, updating, and deleting customers.
 */
@RequestMapping("/api/customers")
@RestController
@CrossOrigin(origins = "http://localhost:3002/")
public interface CustomerController {

    /**
     * Creates a new customer in the system.
     *
     * @param customerDTO the customer data transfer object containing customer details
     * @return ResponseEntity containing the result of the operation
     *         200 OK if customer is created successfully
     *         400 Bad Request if customer data is invalid
     */
    @PostMapping("/addCustomer")
    ResponseEntity<Object> postCustomer(@RequestBody CustomerDTO customerDTO);

    /**
     * Retrieves all customers from the system.
     *
     * @return ResponseEntity containing a list of all customers
     *         200 OK with list of customers
     *         404 Not Found if no customers exist
     */
    @GetMapping("/showCustomers")
    ResponseEntity<Object> getAllCustomers();

    /**
     * Retrieves a specific customer by their ID.
     *
     * @param id the ID of the customer to retrieve
     * @return ResponseEntity containing the customer details
     *         200 OK with customer data
     *         404 Not Found if customer doesn't exist
     */
    @GetMapping("/showCustomerByID/{id}")
    ResponseEntity<Object> getCustomerByID(@PathVariable Long id);

    /**
     * Deletes a customer from the system.
     *
     * @param id the ID of the customer to delete
     * @return ResponseEntity indicating the result of the operation
     *         200 OK if customer is deleted successfully
     *         404 Not Found if customer doesn't exist
     */
    @DeleteMapping("/deleteCustomerByID/{id}")
    ResponseEntity<Object> deleteCustomer(@PathVariable Long id);

    /**
     * Updates an existing customer's information.
     *
     * @param id the ID of the customer to update
     * @param customerDTO the updated customer information
     * @return ResponseEntity containing the updated customer details
     *         200 OK if customer is updated successfully
     *         404 Not Found if customer doesn't exist
     *         400 Bad Request if update data is invalid
     */
    @PutMapping("/updateCustomer/{id}")
    ResponseEntity<Object> updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO);

    /**
     * Deletes tickets associated with a customer.
     *
     * @param id the ID of the customer whose tickets are to be deleted
     * @param customerDTO customer data transfer object containing ticket information
     * @return ResponseEntity indicating the result of the operation
     *         200 OK if tickets are deleted successfully
     *         404 Not Found if customer or tickets don't exist
     */
    @DeleteMapping("/deleteTickets/{id}")
    ResponseEntity<Object> deleteTickets(@PathVariable Long id, @RequestBody CustomerDTO customerDTO);
}
