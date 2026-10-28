package com.example.self_service_s_i_m_registration_system.controller;

import com.example.self_service_s_i_m_registration_system.model.SimType;
import com.example.self_service_s_i_m_registration_system.service.SimTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/sim-types")
public class SimTypeController {

    private final SimTypeService simTypeService;

    public SimTypeController(SimTypeService simTypeService) {
        this.simTypeService = simTypeService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody SimType simType) {
        String message = simTypeService.save(simType);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public List<SimType> getAll() {
        return simTypeService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable UUID id) {
        Optional<SimType> simType = simTypeService.getById(id);
        if (simType.isPresent()) {
            return new ResponseEntity<>(simType.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("The sim type with that id does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody SimType simType) {
        String message = simTypeService.update(id, simType);
        if (message.equals("Sim type updated successfully")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = simTypeService.delete(id);
        if (message.equals("Sim type deleted successfully")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }
}