package com.pos.repository;

import com.pos.model.Customer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * CustomerRepository
 *
 * An enhanced in-memory repository for managing Customer entities.
 * Ideal for development, prototyping, or lightweight deployments.
 * For production-grade applications, consider integrating with a persistent database.
 */
public class CustomerRepository {

    private final Map<String, Customer> customerStore = new ConcurrentHashMap<>();

    /**
     * Retrieves a customer by their ID.
     *
     * @param customerId ID of the customer
     * @return Optional containing the customer if found
     */
    public Optional<Customer> findById(String customerId) {
        return Optional.ofNullable(customerStore.get(customerId));
    }

    /**
     * Retrieves all customers in the repository.
     *
     * @return List of all customers
     */
    public List<Customer> findAll() {
        return new ArrayList<>(customerStore.values());
    }

    /**
     * Saves a customer. Assigns a new UUID if the ID is null or empty.
     *
     * @param customer The customer to save
     * @return The saved customer with an assigned ID
     */
    public Customer save(Customer customer) {
        if (customer.getCustomerId() == null || customer.getCustomerId().trim().isEmpty()) {
            customer.setCustomerId(UUID.randomUUID().toString());
        }
        customerStore.put(customer.getCustomerId(), customer);
        return customer;
    }

    /**
     * Updates an existing customer.
     *
     * @param customer The updated customer
     * @return true if update was successful, false otherwise
     */
    public boolean update(Customer customer) {
        String customerId = customer.getCustomerId();
        if (customerId == null || !customerStore.containsKey(customerId)) {
            return false;
        }
        customerStore.put(customerId, customer);
        return true;
    }

    /**
     * Checks if a customer exists by ID.
     *
     * @param customerId ID of the customer
     * @return true if the customer exists, false otherwise
     */
    public boolean existsById(String customerId) {
        return customerStore.containsKey(customerId);
    }

    /**
     * Deletes a customer by ID.
     *
     * @param customerId ID of the customer
     */
    public void deleteById(String customerId) {
        customerStore.remove(customerId);
    }

    /**
     * Deletes all customers.
     */
    public void deleteAll() {
        customerStore.clear();
    }

    /**
     * Finds customers by email address (case-insensitive).
     *
     * @param email The email to search for
     * @return List of matching customers
     */
    public List<Customer> findByEmail(String email) {
        return customerStore.values().stream()
                .filter(customer -> email != null && email.equalsIgnoreCase(customer.getEmail()))
                .collect(Collectors.toList());
    }

    /**
     * Finds customers by last name (case-insensitive).
     *
     * @param lastName Last name to search
     * @return List of matching customers
     */
    public List<Customer> findByLastName(String lastName) {
        return customerStore.values().stream()
                .filter(customer -> lastName != null && lastName.equalsIgnoreCase(customer.getLastName()))
                .collect(Collectors.toList());
    }

    /**
     * Finds customers by full name (case-insensitive).
     *
     * @param firstName First name
     * @param lastName Last name
     * @return List of matching customers
     */
    public List<Customer> findByFullName(String firstName, String lastName) {
        return customerStore.values().stream()
                .filter(customer -> firstName != null && lastName != null &&
                        firstName.equalsIgnoreCase(customer.getFirstName()) &&
                        lastName.equalsIgnoreCase(customer.getLastName()))
                .collect(Collectors.toList());
    }

    /**
     * Finds customers who registered after the specified date.
     *
     * @param since Date to compare against
     * @return List of customers registered after the specified date
     */
    public List<Customer> findRegisteredAfter(Date since) {
        return customerStore.values().stream()
                .filter(customer -> customer.getRegistrationDate() != null &&
                        customer.getRegistrationDate().after(since))
                .collect(Collectors.toList());
    }

    /**
     * Counts total number of customers.
     *
     * @return Total customer count
     */
    public int count() {
        return customerStore.size();
    }

    /**
     * Returns a map of email domains to the number of customers using each domain.
     * Useful for analytics and marketing.
     *
     * @return Map of email domain to customer count
     */
    public Map<String, Long> countByEmailDomain() {
        return customerStore.values().stream()
                .map(Customer::getEmail)
                .filter(email -> email != null && email.contains("@"))
                .map(email -> email.substring(email.indexOf("@") + 1).toLowerCase())
                .collect(Collectors.groupingBy(domain -> domain, Collectors.counting()));
    }

    /**
     * Searches customers by partial match on full name (case-insensitive).
     *
     * @param query Substring to search in full name
     * @return List of matching customers
     */
    public List<Customer> searchByName(String query) {
        String lowerQuery = query != null ? query.toLowerCase() : "";
        return customerStore.values().stream()
                .filter(customer -> {
                    String fullName = (customer.getFirstName() + " " + customer.getLastName()).toLowerCase();
                    return fullName.contains(lowerQuery);
                })
                .collect(Collectors.toList());
    }
}
