package com.example.self_service_s_i_m_registration_system.controller;

import com.example.self_service_s_i_m_registration_system.model.VerifiedDocument;
import com.example.self_service_s_i_m_registration_system.service.VerifiedDocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/verified-documents")
public class VerifiedDocumentController {

    private final VerifiedDocumentService verifiedDocumentService;

    public VerifiedDocumentController(VerifiedDocumentService verifiedDocumentService) {
        this.verifiedDocumentService = verifiedDocumentService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestParam UUID simRegistrationId, @RequestBody VerifiedDocument verifiedDocument) {
        String message = verifiedDocumentService.save(simRegistrationId, verifiedDocument);
        if (message.equals("Verified document saved successfully")) {
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        } else if (message.equals("The sim registration with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/customer/{nationalId}")
    public List<VerifiedDocument> getByCustomerNationalId(@PathVariable String nationalId) {
        return verifiedDocumentService.getByCustomerNationalId(nationalId);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = verifiedDocumentService.delete(id);
        if (message.equals("Verified document deleted successfully")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public List<VerifiedDocument> getAll() {
        return verifiedDocumentService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable UUID id) {
        Optional<VerifiedDocument> document = verifiedDocumentService.getById(id);
        if (document.isPresent()) {
            return new ResponseEntity<>(document.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("The verified document with that id does not exist", HttpStatus.NOT_FOUND);
        }
    }
}
