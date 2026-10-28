package com.example.self_service_s_i_m_registration_system.controller;

import com.example.self_service_s_i_m_registration_system.model.RegistrationStatus;
import com.example.self_service_s_i_m_registration_system.model.SimRegistration;
import com.example.self_service_s_i_m_registration_system.service.SimRegistrationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
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
        if ("Sim registration saved successfully".equals(message)) {
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        } else if ("Sim registration date cannot be in the past".equals(message)) {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        } else if ("Agent already has a registration to review on that date".equals(message)) {
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
        if ("Sim registration updated successfully".equals(message)) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else if ("The sim registration with that id does not exist".equals(message)) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = simRegistrationService.delete(id);
        if ("Sim registration deleted successfully".equals(message)) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else if ("That sim registration has verified documents, delete them first".equals(message)) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/by-status")
    public List<SimRegistration> getByStatus(@RequestParam RegistrationStatus status) {
        return simRegistrationService.getByStatus(status);
    }

    @GetMapping("/between")
    public List<SimRegistration> getBetween(@RequestParam String start, @RequestParam String end) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        return simRegistrationService.getBetween(startDate, endDate);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable UUID id, @RequestParam RegistrationStatus status) {
        String message = simRegistrationService.updateStatus(id, status);
        if ("Sim registration status updated successfully".equals(message)) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else if ("The sim registration with that id does not exist".equals(message)) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/stats/by-status")
    public List<Object[]> getStatsByStatus() {
        return simRegistrationService.getStatsByStatus();
    }

    @PatchMapping("/cancel-day")
    public ResponseEntity<String> cancelDay(@RequestParam UUID registrationAgentId, @RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        String message = simRegistrationService.cancelDay(registrationAgentId, localDate);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/page")
    public Page<SimRegistration> getPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "registrationDate,desc") String sort) {
        String[] sortParts = sort.split(",");
        String property = sortParts[0];
        Sort.Direction direction = Sort.Direction.ASC;
        if (sortParts.length > 1 && sortParts[1].equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, property));
        return simRegistrationService.getPage(pageable);
    }
}
