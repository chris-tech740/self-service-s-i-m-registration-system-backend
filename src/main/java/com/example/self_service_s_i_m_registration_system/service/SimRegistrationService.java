package com.example.self_service_s_i_m_registration_system.service;

import com.example.self_service_s_i_m_registration_system.model.SimRegistration;
import com.example.self_service_s_i_m_registration_system.repository.SimRegistrationRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SimRegistrationService {

    private final SimRegistrationRepository simRegistrationRepository;

    public SimRegistrationService(SimRegistrationRepository simRegistrationRepository) {
        this.simRegistrationRepository = simRegistrationRepository;
    }

    public String save(SimRegistration simRegistration) {
        simRegistrationRepository.save(simRegistration);
        return "Sim registration saved successfully";
    }

    public List<SimRegistration> getAll() {
        return simRegistrationRepository.findAll();
    }

    public Optional<SimRegistration> getById(UUID id) {
        return simRegistrationRepository.findById(id);
    }

    public String update(UUID id, SimRegistration simRegistration) {
        Optional<SimRegistration> existing = simRegistrationRepository.findById(id);
        if (existing.isPresent()) {
            SimRegistration found = existing.get();
            found.setRegistrationDate(simRegistration.getRegistrationDate());
            found.setStatus(simRegistration.getStatus());
            found.setSimNumber(simRegistration.getSimNumber());
            if (simRegistration.getCustomer() != null) {
                found.setCustomer(simRegistration.getCustomer());
            }
            if (simRegistration.getAgent() != null) {
                found.setAgent(simRegistration.getAgent());
            }
            simRegistrationRepository.save(found);
            return "Sim registration updated successfully";
        } else {
            return "The sim registration with that id does not exist";
        }
    }

    public String delete(UUID id) {
        if (simRegistrationRepository.existsById(id)) {
            simRegistrationRepository.deleteById(id);
            return "Sim registration deleted successfully";
        } else {
            return "The sim registration with that id does not exist";
        }
    }
}