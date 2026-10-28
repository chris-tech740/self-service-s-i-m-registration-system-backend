package com.example.self_service_s_i_m_registration_system.controller;

import com.example.self_service_s_i_m_registration_system.model.SimRegistration;
import com.example.self_service_s_i_m_registration_system.service.SimRegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/sim-registrations")
public class SimRegistrationController {

    private final SimRegistrationService simRegistrationService;

    public SimRegistrationController(SimRegistrationService simRegistrationService) {
        this.simRegistrationService = simRegistrationService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody SimRegistration simRegistration) {
        String message = simRegistrationService.save(simRegistration);
        if (message.equals("Sim registration saved successfully")) {
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        } else if (message.equals("Sim registration date cannot be in the past")) {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        } else if (message.equals("Agent already has a registration to review on that date")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/all")
    public List<SimRegistration> getAll() {
        return simRegistrationService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable UUID id) {
        Optional<SimRegistration> simRegistration = simRegistrationService.getById(id);
        if (simRegistration.isPresent()) {
            return new ResponseEntity<>(simRegistration.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("The sim registration with that id does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody SimRegistration simRegistration) {
        String message = simRegistrationService.update(id, simRegistration);
        if (message.equals("Sim registration updated successfully")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = simRegistrationService.delete(id);
        if (message.equals("Sim registration deleted successfully")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/by-status")
    public List<SimRegistration> getByStatus(@RequestParam com.example.self_service_s_i_m_registration_system.model.RegistrationStatus status) {
        return simRegistrationService.getByStatus(status);
    }

    @GetMapping("/between")
    public List<SimRegistration> getBetween(@RequestParam String start, @RequestParam String end) {
        java.time.LocalDate startDate = java.time.LocalDate.parse(start);
        java.time.LocalDate endDate = java.time.LocalDate.parse(end);
        return simRegistrationService.getBetween(startDate, endDate);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable UUID id, @RequestParam com.example.self_service_s_i_m_registration_system.model.RegistrationStatus status) {
        String message = simRegistrationService.updateStatus(id, status);
        if (message.equals("Sim registration status updated successfully")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else if (message.equals("The sim registration with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
    }
}