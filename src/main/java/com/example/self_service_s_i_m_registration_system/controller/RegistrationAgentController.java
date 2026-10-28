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
        return new ResponseEntity<>(message, HttpStatus.CREATED);
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
        if (message.equals("Registration agent updated successfully")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
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
}