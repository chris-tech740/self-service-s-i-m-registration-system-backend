package com.example.self_service_s_i_m_registration_system.service;

import com.example.self_service_s_i_m_registration_system.model.RegistrationAgent;
import com.example.self_service_s_i_m_registration_system.repository.RegistrationAgentRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RegistrationAgentService {

    private final RegistrationAgentRepository registrationAgentRepository;

    public RegistrationAgentService(RegistrationAgentRepository registrationAgentRepository) {
        this.registrationAgentRepository = registrationAgentRepository;
    }

    public String save(RegistrationAgent registrationAgent) {
        registrationAgentRepository.save(registrationAgent);
        return "Registration agent saved successfully";
    }

    public List<RegistrationAgent> getAll() {
        return registrationAgentRepository.findAll();
    }

    public Optional<RegistrationAgent> getById(UUID id) {
        return registrationAgentRepository.findById(id);
    }

    public String update(UUID id, RegistrationAgent registrationAgent) {
        Optional<RegistrationAgent> existing = registrationAgentRepository.findById(id);
        if (existing.isPresent()) {
            RegistrationAgent found = existing.get();
            found.setFirstName(registrationAgent.getFirstName());
            found.setLastName(registrationAgent.getLastName());
            found.setStaffNumber(registrationAgent.getStaffNumber());
            if (registrationAgent.getServiceDesk() != null) {
                found.setServiceDesk(registrationAgent.getServiceDesk());
            }
            if (registrationAgent.getSimTypes() != null) {
                found.setSimTypes(registrationAgent.getSimTypes());
            }
            registrationAgentRepository.save(found);
            return "Registration agent updated successfully";
        } else {
            return "The registration agent with that id does not exist";
        }
    }

    public String delete(UUID id) {
        if (registrationAgentRepository.existsById(id)) {
            registrationAgentRepository.deleteById(id);
            return "Registration agent deleted successfully";
        } else {
            return "The registration agent with that id does not exist";
        }
    }

    public List<RegistrationAgent> getBySimType(String name) {
        return registrationAgentRepository.findBySimTypeName(name);
    }
}