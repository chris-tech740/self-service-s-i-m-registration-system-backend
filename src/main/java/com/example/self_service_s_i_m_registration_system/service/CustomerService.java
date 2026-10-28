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
            Customer found = existing.get();
            found.setFirstName(customer.getFirstName());
            found.setLastName(customer.getLastName());
            found.setNationalId(customer.getNationalId());
            found.setPhone(customer.getPhone());
            found.setEmail(customer.getEmail());
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