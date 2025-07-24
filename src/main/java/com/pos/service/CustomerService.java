package com.pos.service;

import com.pos.model.Customer;
import com.pos.repository.CustomerRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * CustomerService
 *
 * Provides business logic and operations related to customers.
 */
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = Objects.requireNonNull(customerRepository, "CustomerRepository cannot be null");
    }

    public Customer saveCustomer(Customer customer) {
        validateCustomer(customer);
        return customerRepository.save(customer);
    }

    public Optional<Customer> getCustomerById(String customerId) {
        if (isNullOrEmpty(customerId)) return Optional.empty();
        return customerRepository.findById(customerId);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public boolean deleteCustomerById(String customerId) {
        if (isNullOrEmpty(customerId) || !customerRepository.existsById(customerId)) {
            return false;
        }
        customerRepository.deleteById(customerId);
        return true;
    }

    public boolean updateCustomer(String customerId, Customer updatedCustomer) {
        if (isNullOrEmpty(customerId) || updatedCustomer == null) return false;

        return customerRepository.findById(customerId)
                .map(existingCustomer -> {
                    existingCustomer.setName(updatedCustomer.getName());
                    existingCustomer.setEmail(updatedCustomer.getEmail());
                    existingCustomer.setPhone(updatedCustomer.getPhone());
                    existingCustomer.setAddress(updatedCustomer.getAddress());
                    return customerRepository.save(existingCustomer) != null;
                })
                .orElse(false);
    }

    public boolean exists(String customerId) {
        return !isNullOrEmpty(customerId) && customerRepository.existsById(customerId);
    }

    public long countCustomers() {
        return customerRepository.count();
    }

    public List<Customer> findCustomersByName(String nameFragment) {
        if (isNullOrEmpty(nameFragment)) return Collections.emptyList();

        String lowerCaseFragment = nameFragment.toLowerCase();
        return customerRepository.findAll().stream()
                .filter(c -> hasText(c.getName()) && c.getName().toLowerCase().contains(lowerCaseFragment))
                .collect(Collectors.toList());
    }

    public List<Customer> findCustomersByEmailDomain(String domain) {
        if (isNullOrEmpty(domain)) return Collections.emptyList();

        String normalizedDomain = "@" + domain.toLowerCase();
        return customerRepository.findAll().stream()
                .filter(c -> hasText(c.getEmail()) && c.getEmail().toLowerCase().endsWith(normalizedDomain))
                .collect(Collectors.toList());
    }

    public void deleteAllCustomers() {
        customerRepository.deleteAll();
    }

    public Map<String, Long> countCustomersByDomain() {
        return customerRepository.findAll().stream()
                .filter(c -> hasText(c.getEmail()) && c.getEmail().contains("@"))
                .collect(Collectors.groupingBy(
                        c -> c.getEmail().substring(c.getEmail().indexOf("@") + 1).toLowerCase(),
                        Collectors.counting()
                ));
    }

    public List<Customer> findCustomersWithPhonePrefix(String prefix) {
        if (isNullOrEmpty(prefix)) return Collections.emptyList();

        return customerRepository.findAll().stream()
                .filter(c -> hasText(c.getPhone()) && c.getPhone().startsWith(prefix))
                .collect(Collectors.toList());
    }

    public Optional<Customer> findCustomerByExactEmail(String email) {
        if (isNullOrEmpty(email)) return Optional.empty();

        return customerRepository.findAll().stream()
                .filter(c -> email.equalsIgnoreCase(c.getEmail()))
                .findFirst();
    }

    public List<Customer> findCustomersRegisteredAfter(Date date) {
        if (date == null) return Collections.emptyList();

        return customerRepository.findAll().stream()
                .filter(c -> c.getCreatedAt() != null && c.getCreatedAt().after(date))
                .collect(Collectors.toList());
    }

    public List<Customer> findCustomersRegisteredBefore(Date date) {
        if (date == null) return Collections.emptyList();

        return customerRepository.findAll().stream()
                .filter(c -> c.getCreatedAt() != null && c.getCreatedAt().before(date))
                .collect(Collectors.toList());
    }

    public List<Customer> findCustomersWithIncompleteData() {
        return customerRepository.findAll().stream()
                .filter(c -> !hasText(c.getName()) || !hasText(c.getEmail())
                        || !hasText(c.getPhone()) || !hasText(c.getAddress()))
                .collect(Collectors.toList());
    }

    public Map<String, List<Customer>> groupCustomersByEmailDomain() {
        return customerRepository.findAll().stream()
                .filter(c -> hasText(c.getEmail()) && c.getEmail().contains("@"))
                .collect(Collectors.groupingBy(
                        c -> c.getEmail().substring(c.getEmail().indexOf("@") + 1).toLowerCase()
                ));
    }

    public Map<String, List<Customer>> groupCustomersByPhonePrefix(int prefixLength) {
        if (prefixLength <= 0) return Collections.emptyMap();

        return customerRepository.findAll().stream()
                .filter(c -> hasText(c.getPhone()) && c.getPhone().length() >= prefixLength)
                .collect(Collectors.groupingBy(
                        c -> c.getPhone().substring(0, prefixLength)
                ));
    }

    private void validateCustomer(Customer customer) {
        if (customer == null) throw new IllegalArgumentException("Customer cannot be null");
        if (!hasText(customer.getName())) {
            throw new IllegalArgumentException("Customer name cannot be empty");
        }
        if (!hasText(customer.getEmail())) {
            throw new IllegalArgumentException("Customer email cannot be empty");
        }
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
