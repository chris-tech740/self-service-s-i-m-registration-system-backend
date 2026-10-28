package com.example.self_service_s_i_m_registration_system.service;

import com.example.self_service_s_i_m_registration_system.model.Customer;
import com.example.self_service_s_i_m_registration_system.repository.CustomerRepository;
import com.example.self_service_s_i_m_registration_system.repository.RegistrationAgentRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final RegistrationAgentRepository registrationAgentRepository;

    public CustomerService(CustomerRepository customerRepository, RegistrationAgentRepository registrationAgentRepository) {
        this.customerRepository = customerRepository;
        this.registrationAgentRepository = registrationAgentRepository;
    }


    public String save(Customer customer) {
        if (customer == null) {
            return "Customer data cannot be null";
        }
        if (customer.getNationalId() == null || customer.getNationalId().trim().isEmpty()) {
            return "Customer nationalId is required";
        }
        if (customer.getFirstName() == null || customer.getFirstName().trim().isEmpty()) {
            return "Customer firstName is required";
        }
        if (customer.getLastName() == null || customer.getLastName().trim().isEmpty()) {
            return "Customer lastName is required";
        }
        if (customerRepository.existsByNationalId(customer.getNationalId())) {
            return "Customer with that nationalId already exists";
        }
        customerRepository.save(customer);
        return "Customer saved successfully";
    }

    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getById(UUID id) {
        return customerRepository.findById(id);
    }

    public String update(UUID id, Customer customer) {
        Optional<Customer> existing = customerRepository.findById(id);
        if (existing.isPresent()) {
            if (customer == null) {
                return "Customer data cannot be null";
            }
            Customer found = existing.get();
            if (customer.getNationalId() != null && !customer.getNationalId().trim().isEmpty()) {
                if (!customer.getNationalId().equals(found.getNationalId()) && customerRepository.existsByNationalId(customer.getNationalId())) {
                    return "Customer with that nationalId already exists";
                }
                found.setNationalId(customer.getNationalId());
            }
            if (customer.getFirstName() != null && !customer.getFirstName().trim().isEmpty()) {
                found.setFirstName(customer.getFirstName());
            }
            if (customer.getLastName() != null && !customer.getLastName().trim().isEmpty()) {
                found.setLastName(customer.getLastName());
            }
            if (customer.getPhone() != null) {
                found.setPhone(customer.getPhone());
            }
            if (customer.getEmail() != null) {
                found.setEmail(customer.getEmail());
            }
            customerRepository.save(found);
            return "Customer updated successfully";
        } else {
            return "The customer with that id does not exist";
        }
    }

    public String delete(UUID id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return "Customer deleted successfully";
        } else {
            return "The customer with that id does not exist";
        }
    }

    public List<Customer> getByLastName(String lastName) {
        return customerRepository.findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);
    }

    public Optional<List<Customer>> getCustomersOfRegistrationAgent(UUID registrationAgentId) {
        if (!registrationAgentRepository.existsById(registrationAgentId)) {
            return Optional.empty();
        }
        return Optional.of(customerRepository.findByRegistrationAgentId(registrationAgentId));
    }
}