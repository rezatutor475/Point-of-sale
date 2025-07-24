package com.pos.controller;

import com.pos.model.Customer;
import com.pos.service.CustomerService;

import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * CustomerController handles operations for customer management
 * such as creation, updates, deletion, searching, and toggling status.
 */
public class CustomerController {

    private static final Logger logger = Logger.getLogger(CustomerController.class.getName());

    private final CustomerService customerService;

    /**
     * Constructor injecting CustomerService.
     *
     * @param customerService the service layer for customer operations
     */
    public CustomerController(CustomerService customerService) {
        this.customerService = Objects.requireNonNull(customerService, "CustomerService must not be null");
    }

    /**
     * Creates a new customer.
     *
     * @param customer customer data
     * @return created customer
     */
    public Customer createCustomer(Customer customer) {
        validateCustomer(customer);
        try {
            logger.info("Creating new customer: " + customer.getName());
            return customerService.createCustomer(customer);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error creating customer", e);
            throw new RuntimeException("Customer creation failed", e);
        }
    }

    /**
     * Updates customer information.
     *
     * @param customer customer with updated fields
     * @return updated customer
     */
    public Customer updateCustomer(Customer customer) {
        validateCustomer(customer);
        try {
            logger.info("Updating customer with ID: " + customer.getId());
            return customerService.updateCustomer(customer);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error updating customer", e);
            throw new RuntimeException("Customer update failed", e);
        }
    }

    /**
     * Deletes a customer by ID.
     *
     * @param customerId ID of the customer
     * @return true if deletion is successful
     */
    public boolean deleteCustomer(String customerId) {
        validateCustomerId(customerId);
        try {
            logger.info("Deleting customer with ID: " + customerId);
            return customerService.deleteCustomer(customerId);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error deleting customer", e);
            throw new RuntimeException("Customer deletion failed", e);
        }
    }

    /**
     * Retrieves customer by ID.
     *
     * @param customerId customer ID
     * @return Customer or null if not found
     */
    public Customer getCustomerById(String customerId) {
        validateCustomerId(customerId);
        try {
            logger.info("Retrieving customer by ID: " + customerId);
            return customerService.getCustomerById(customerId);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error retrieving customer", e);
            throw new RuntimeException("Customer retrieval failed", e);
        }
    }

    /**
     * Searches for customers using a name or phone number.
     *
     * @param query the search term
     * @return list of matched customers
     */
    public List<Customer> searchCustomers(String query) {
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("Search query must not be null or empty");
        }
        try {
            logger.info("Searching customers with query: " + query);
            return customerService.searchCustomers(query);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error searching customers", e);
            throw new RuntimeException("Customer search failed", e);
        }
    }

    /**
     * Sets a customer's active status.
     *
     * @param customerId ID of the customer
     * @param active new active status
     * @return true if successful
     */
    public boolean setCustomerActiveStatus(String customerId, boolean active) {
        validateCustomerId(customerId);
        try {
            logger.info("Setting active status for customer ID: " + customerId + " to " + active);
            return customerService.setCustomerActiveStatus(customerId, active);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error setting active status", e);
            throw new RuntimeException("Setting customer active status failed", e);
        }
    }

    /**
     * Validates customer fields.
     *
     * @param customer the customer to validate
     */
    private void validateCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name is required");
        }
        if (customer.getPhoneNumber() == null || customer.getPhoneNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Customer phone number is required");
        }
    }

    /**
     * Validates customer ID.
     *
     * @param customerId ID to validate
     */
    private void validateCustomerId(String customerId) {
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID must not be null or empty");
        }
    }
}
