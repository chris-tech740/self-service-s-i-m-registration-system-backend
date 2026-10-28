package com.example.self_service_s_i_m_registration_system.service;

import com.example.self_service_s_i_m_registration_system.model.ServiceDesk;
import com.example.self_service_s_i_m_registration_system.repository.ServiceDeskRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ServiceDeskService {

    private final ServiceDeskRepository serviceDeskRepository;

    public ServiceDeskService(ServiceDeskRepository serviceDeskRepository) {
        this.serviceDeskRepository = serviceDeskRepository;
    }

    public String save(ServiceDesk serviceDesk) {
        serviceDeskRepository.save(serviceDesk);
        return "Service desk saved successfully";
    }

    public List<ServiceDesk> getAll() {
        return serviceDeskRepository.findAll();
    }

    public Optional<ServiceDesk> getById(UUID id) {
        return serviceDeskRepository.findById(id);
    }

    public String update(UUID id, ServiceDesk serviceDesk) {
        Optional<ServiceDesk> existing = serviceDeskRepository.findById(id);
        if (existing.isPresent()) {
            ServiceDesk found = existing.get();
            found.setDeskNumber(serviceDesk.getDeskNumber());
            found.setName(serviceDesk.getName());
            serviceDeskRepository.save(found);
            return "Service desk updated successfully";
        } else {
            return "The service desk with that id does not exist";
        }
    }

    public String delete(UUID id) {
        if (serviceDeskRepository.existsById(id)) {
            serviceDeskRepository.deleteById(id);
            return "Service desk deleted successfully";
        } else {
            return "The service desk with that id does not exist";
        }
    }

    public List<ServiceDesk> getFreeServiceDesks() {
        return serviceDeskRepository.findFreeServiceDesks();
    }
}