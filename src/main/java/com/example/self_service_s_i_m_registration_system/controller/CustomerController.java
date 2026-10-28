package com.example.self_service_s_i_m_registration_system.controller;

import com.example.self_service_s_i_m_registration_system.model.Customer;
import com.example.self_service_s_i_m_registration_system.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Customer customer) {
        String message = customerService.save(customer);
        if ("Customer saved successfully".equals(message)) {
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        } else if ("Customer with that nationalId already exists".equals(message)) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all")
    public List<Customer> getAll() {
        return customerService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable UUID id) {
        Optional<Customer> customer = customerService.getById(id);
        if (customer.isPresent()) {
            return new ResponseEntity<>(customer.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("The customer with that id does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody Customer customer) {
        String message = customerService.update(id, customer);
        if ("Customer updated successfully".equals(message)) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else if ("The customer with that id does not exist".equals(message)) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else if ("Customer with that nationalId already exists".equals(message)) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = customerService.delete(id);
        if (message.equals("Customer deleted successfully")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/by-last-name")
    public List<Customer> getByLastName(@RequestParam String lastName) {
        return customerService.getByLastName(lastName);
    }

    @GetMapping("/of-registration-agent/{registrationAgentId}")
    public ResponseEntity<Object> getCustomersOfRegistrationAgent(@PathVariable UUID registrationAgentId) {
        Optional<List<Customer>> customers = customerService.getCustomersOfRegistrationAgent(registrationAgentId);
        if (customers.isPresent()) {
            return new ResponseEntity<>(customers.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("The registration agent with that id does not exist", HttpStatus.NOT_FOUND);
        }
    }
}