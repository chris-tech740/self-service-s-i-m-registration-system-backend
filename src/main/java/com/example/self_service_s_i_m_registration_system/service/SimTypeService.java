package com.example.self_service_s_i_m_registration_system.service;

import com.example.self_service_s_i_m_registration_system.model.SimType;
import com.example.self_service_s_i_m_registration_system.repository.SimTypeRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SimTypeService {

    private final SimTypeRepository simTypeRepository;

    public SimTypeService(SimTypeRepository simTypeRepository) {
        this.simTypeRepository = simTypeRepository;
    }

    public String save(SimType simType) {
        simTypeRepository.save(simType);
        return "Sim type saved successfully";
    }

    public List<SimType> getAll() {
        return simTypeRepository.findAll();
    }

    public Optional<SimType> getById(UUID id) {
        return simTypeRepository.findById(id);
    }

    public String update(UUID id, SimType simType) {
        Optional<SimType> existing = simTypeRepository.findById(id);
        if (existing.isPresent()) {
            SimType found = existing.get();
            found.setName(simType.getName());
            simTypeRepository.save(found);
            return "Sim type updated successfully";
        } else {
            return "The sim type with that id does not exist";
        }
    }

    public String delete(UUID id) {
        if (simTypeRepository.existsById(id)) {
            simTypeRepository.deleteById(id);
            return "Sim type deleted successfully";
        } else {
            return "The sim type with that id does not exist";
        }
    }
}