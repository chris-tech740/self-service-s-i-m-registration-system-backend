package com.example.self_service_s_i_m_registration_system.controller;

import com.example.self_service_s_i_m_registration_system.model.ServiceDesk;
import com.example.self_service_s_i_m_registration_system.service.ServiceDeskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/service-desks")
public class ServiceDeskController {

    private final ServiceDeskService serviceDeskService;

    public ServiceDeskController(ServiceDeskService serviceDeskService) {
        this.serviceDeskService = serviceDeskService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody ServiceDesk serviceDesk) {
        String message = serviceDeskService.save(serviceDesk);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public List<ServiceDesk> getAll() {
        return serviceDeskService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable UUID id) {
        Optional<ServiceDesk> serviceDesk = serviceDeskService.getById(id);
        if (serviceDesk.isPresent()) {
            return new ResponseEntity<>(serviceDesk.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("The service desk with that id does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody ServiceDesk serviceDesk) {
        String message = serviceDeskService.update(id, serviceDesk);
        if (message.equals("Service desk updated successfully")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = serviceDeskService.delete(id);
        if (message.equals("Service desk deleted successfully")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/free")
    public List<ServiceDesk> getFree() {
        return serviceDeskService.getFreeServiceDesks();
    }
}