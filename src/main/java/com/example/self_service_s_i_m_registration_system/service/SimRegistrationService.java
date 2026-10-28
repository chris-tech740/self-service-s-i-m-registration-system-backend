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
        if (simRegistration.getRegistrationDate() != null && simRegistration.getRegistrationDate().isBefore(java.time.LocalDate.now())) {
            return "Sim registration date cannot be in the past";
        }
        if (simRegistration.getAgent() != null && simRegistration.getAgent().getId() != null) {
            if (simRegistrationRepository.existsByAgentAndRegistrationDateAndStatusNot(simRegistration.getAgent(), simRegistration.getRegistrationDate(), com.example.self_service_s_i_m_registration_system.model.RegistrationStatus.REJECTED)) {
                return "Agent already has a registration to review on that date";
            }
        }
        simRegistration.setStatus(com.example.self_service_s_i_m_registration_system.model.RegistrationStatus.SUBMITTED);
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

    public String updateStatus(UUID id, com.example.self_service_s_i_m_registration_system.model.RegistrationStatus status) {
        Optional<SimRegistration> existing = simRegistrationRepository.findById(id);
        if (existing.isEmpty()) {
            return "The sim registration with that id does not exist";
        }
        SimRegistration found = existing.get();
        com.example.self_service_s_i_m_registration_system.model.RegistrationStatus current = found.getStatus();
        boolean allowed = false;
        if (current == com.example.self_service_s_i_m_registration_system.model.RegistrationStatus.SUBMITTED &&
                (status == com.example.self_service_s_i_m_registration_system.model.RegistrationStatus.UNDER_REVIEW || status == com.example.self_service_s_i_m_registration_system.model.RegistrationStatus.REJECTED)) {
            allowed = true;
        } else if (current == com.example.self_service_s_i_m_registration_system.model.RegistrationStatus.UNDER_REVIEW &&
                (status == com.example.self_service_s_i_m_registration_system.model.RegistrationStatus.APPROVED || status == com.example.self_service_s_i_m_registration_system.model.RegistrationStatus.REJECTED)) {
            allowed = true;
        }

        if (!allowed) {
            return "Cannot change status from " + current + " to " + status;
        }

        found.setStatus(status);
        simRegistrationRepository.save(found);
        return "Sim registration status updated successfully";
    }


    public String delete(UUID id) {
        if (simRegistrationRepository.existsById(id)) {
            simRegistrationRepository.deleteById(id);
            return "Sim registration deleted successfully";
        } else {
            return "The sim registration with that id does not exist";
        }
    }

    public List<SimRegistration> getByStatus(com.example.self_service_s_i_m_registration_system.model.RegistrationStatus status) {
        return simRegistrationRepository.findByStatusOrderByRegistrationDateAsc(status);
    }

    public List<SimRegistration> getBetween(java.time.LocalDate start, java.time.LocalDate end) {
        return simRegistrationRepository.findByRegistrationDateBetweenOrderByRegistrationDateAsc(start, end);
    }
}