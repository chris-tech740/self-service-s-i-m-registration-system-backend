package com.example.self_service_s_i_m_registration_system.service;

import com.example.self_service_s_i_m_registration_system.model.RegistrationAgent;
import com.example.self_service_s_i_m_registration_system.model.ServiceDesk;
import com.example.self_service_s_i_m_registration_system.model.SimType;
import com.example.self_service_s_i_m_registration_system.repository.RegistrationAgentRepository;
import com.example.self_service_s_i_m_registration_system.repository.ServiceDeskRepository;
import com.example.self_service_s_i_m_registration_system.repository.SimTypeRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RegistrationAgentService {

    private final RegistrationAgentRepository registrationAgentRepository;
    private final SimTypeRepository simTypeRepository;
    private final ServiceDeskRepository serviceDeskRepository;

    public RegistrationAgentService(RegistrationAgentRepository registrationAgentRepository,
                                    SimTypeRepository simTypeRepository,
                                    ServiceDeskRepository serviceDeskRepository) {
        this.registrationAgentRepository = registrationAgentRepository;
        this.simTypeRepository = simTypeRepository;
        this.serviceDeskRepository = serviceDeskRepository;
    }


    public String save(RegistrationAgent registrationAgent) {
        if (registrationAgent == null) {
            return "Registration agent data cannot be null";
        }
        if (registrationAgent.getStaffNumber() == null || registrationAgent.getStaffNumber().trim().isEmpty()) {
            return "Agent staffNumber is required";
        }
        if (registrationAgent.getFirstName() == null || registrationAgent.getFirstName().trim().isEmpty()) {
            return "Agent firstName is required";
        }
        if (registrationAgent.getLastName() == null || registrationAgent.getLastName().trim().isEmpty()) {
            return "Agent lastName is required";
        }
        if (registrationAgentRepository.existsByStaffNumber(registrationAgent.getStaffNumber())) {
            return "Agent with that staffNumber already exists";
        }
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
            if (registrationAgent == null) {
                return "Registration agent data cannot be null";
            }
            RegistrationAgent found = existing.get();
            if (registrationAgent.getStaffNumber() != null && !registrationAgent.getStaffNumber().trim().isEmpty()) {
                if (!registrationAgent.getStaffNumber().equals(found.getStaffNumber()) && registrationAgentRepository.existsByStaffNumber(registrationAgent.getStaffNumber())) {
                    return "Agent with that staffNumber already exists";
                }
                found.setStaffNumber(registrationAgent.getStaffNumber());
            }
            if (registrationAgent.getFirstName() != null && !registrationAgent.getFirstName().trim().isEmpty()) {
                found.setFirstName(registrationAgent.getFirstName());
            }
            if (registrationAgent.getLastName() != null && !registrationAgent.getLastName().trim().isEmpty()) {
                found.setLastName(registrationAgent.getLastName());
            }
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

    public String addSimType(UUID registrationAgentId, UUID simTypeId) {
        Optional<RegistrationAgent> agentOpt = registrationAgentRepository.findById(registrationAgentId);
        if (agentOpt.isEmpty()) {
            return "The registration agent with that id does not exist";
        }
        Optional<SimType> simTypeOpt = simTypeRepository.findById(simTypeId);
        if (simTypeOpt.isEmpty()) {
            return "That sim type does not exist";
        }
        RegistrationAgent agent = agentOpt.get();
        SimType simType = simTypeOpt.get();
        if (agent.getSimTypes() == null) {
            agent.setSimTypes(new ArrayList<>());
        }
        boolean alreadyHas = agent.getSimTypes().stream()
                .anyMatch(st -> st.getId().equals(simType.getId()));
        if (alreadyHas) {
            return "The registration agent already has that sim type";
        }
        agent.getSimTypes().add(simType);
        registrationAgentRepository.save(agent);
        return "Sim type added to the registration agent";
    }

    public String changeServiceDesk(UUID registrationAgentId, int deskNumber) {
        Optional<RegistrationAgent> agentOpt = registrationAgentRepository.findById(registrationAgentId);
        if (agentOpt.isEmpty()) {
            return "The registration agent with that id does not exist";
        }
        Optional<ServiceDesk> deskOpt = serviceDeskRepository.findByDeskNumber(deskNumber);
        if (deskOpt.isEmpty()) {
            return "The service desk with that deskNumber does not exist";
        }
        ServiceDesk desk = deskOpt.get();
        if (registrationAgentRepository.existsByServiceDesk(desk)) {
            return "That service desk is taken by another registration agent";
        }
        RegistrationAgent agent = agentOpt.get();
        agent.setServiceDesk(desk);
        registrationAgentRepository.save(agent);
        return "Service desk changed successfully";
    }
}