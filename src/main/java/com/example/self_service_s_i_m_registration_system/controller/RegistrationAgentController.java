package com.example.self_service_s_i_m_registration_system.controller;

import com.example.self_service_s_i_m_registration_system.model.RegistrationAgent;
import com.example.self_service_s_i_m_registration_system.service.RegistrationAgentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/registration-agents")
public class RegistrationAgentController {

    private final RegistrationAgentService registrationAgentService;

    public RegistrationAgentController(RegistrationAgentService registrationAgentService) {
        this.registrationAgentService = registrationAgentService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody RegistrationAgent registrationAgent) {
        String message = registrationAgentService.save(registrationAgent);
        if ("Registration agent saved successfully".equals(message)) {
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        } else if ("Agent with that staffNumber already exists".equals(message)) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all")
    public List<RegistrationAgent> getAll() {
        return registrationAgentService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable UUID id) {
        Optional<RegistrationAgent> registrationAgent = registrationAgentService.getById(id);
        if (registrationAgent.isPresent()) {
            return new ResponseEntity<>(registrationAgent.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("The registration agent with that id does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody RegistrationAgent registrationAgent) {
        String message = registrationAgentService.update(id, registrationAgent);
        if ("Registration agent updated successfully".equals(message)) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else if ("The registration agent with that id does not exist".equals(message)) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else if ("Agent with that staffNumber already exists".equals(message)) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = registrationAgentService.delete(id);
        if (message.equals("Registration agent deleted successfully")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/by-sim-type")
    public List<RegistrationAgent> getBySimType(@RequestParam String name) {
        return registrationAgentService.getBySimType(name);
    }

    @PutMapping("/{registrationAgentId}/sim-types/{simTypeId}")
    public ResponseEntity<String> addSimType(@PathVariable UUID registrationAgentId, @PathVariable UUID simTypeId) {
        String message = registrationAgentService.addSimType(registrationAgentId, simTypeId);
        if (message.equals("Sim type added to the registration agent")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else if (message.equals("The registration agent already has that sim type")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{registrationAgentId}/service-desk")
    public ResponseEntity<String> changeServiceDesk(@PathVariable UUID registrationAgentId, @RequestParam int deskNumber) {
        String message = registrationAgentService.changeServiceDesk(registrationAgentId, deskNumber);
        if (message.equals("Service desk changed successfully")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else if (message.equals("That service desk is taken by another registration agent")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }
}