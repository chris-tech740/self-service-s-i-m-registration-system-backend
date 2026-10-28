package com.example.self_service_s_i_m_registration_system.service;

import com.example.self_service_s_i_m_registration_system.model.RegistrationStatus;
import com.example.self_service_s_i_m_registration_system.model.SimRegistration;
import com.example.self_service_s_i_m_registration_system.model.VerifiedDocument;
import com.example.self_service_s_i_m_registration_system.repository.SimRegistrationRepository;
import com.example.self_service_s_i_m_registration_system.repository.VerifiedDocumentRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VerifiedDocumentService {

    private final VerifiedDocumentRepository verifiedDocumentRepository;
    private final SimRegistrationRepository simRegistrationRepository;

    public VerifiedDocumentService(VerifiedDocumentRepository verifiedDocumentRepository,
                                   SimRegistrationRepository simRegistrationRepository) {
        this.verifiedDocumentRepository = verifiedDocumentRepository;
        this.simRegistrationRepository = simRegistrationRepository;
    }

    public String save(UUID simRegistrationId, VerifiedDocument verifiedDocument) {
        if (verifiedDocument == null) {
            return "Verified document data cannot be null";
        }
        Optional<SimRegistration> regOpt = simRegistrationRepository.findById(simRegistrationId);
        if (regOpt.isEmpty()) {
            return "The sim registration with that id does not exist";
        }
        SimRegistration simRegistration = regOpt.get();
        if (simRegistration.getStatus() != RegistrationStatus.APPROVED) {
            return "Verified documents are only for APPROVED sim registrations";
        }
        if (verifiedDocument.getDocumentType() == null || verifiedDocument.getDocumentType().trim().isEmpty()) {
            return "Document type is required";
        }
        if (verifiedDocument.getPages() <= 0) {
            return "Number of pages must be at least 1";
        }
        verifiedDocument.setVerifiedDate(LocalDate.now());
        verifiedDocument.setSimRegistration(simRegistration);
        verifiedDocumentRepository.save(verifiedDocument);
        return "Verified document saved successfully";
    }

    public List<VerifiedDocument> getByCustomerNationalId(String nationalId) {
        return verifiedDocumentRepository.findByCustomerNationalIdOrderByVerifiedDateDesc(nationalId);
    }

    public String delete(UUID id) {
        if (verifiedDocumentRepository.existsById(id)) {
            verifiedDocumentRepository.deleteById(id);
            return "Verified document deleted successfully";
        } else {
            return "The verified document with that id does not exist";
        }
    }

    public List<VerifiedDocument> getAll() {
        return verifiedDocumentRepository.findAll();
    }

    public Optional<VerifiedDocument> getById(UUID id) {
        return verifiedDocumentRepository.findById(id);
    }
}
