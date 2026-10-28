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
        if (serviceDesk == null) {
            return "Service desk data cannot be null";
        }
        if (serviceDesk.getDeskNumber() <= 0) {
            return "Service desk number must be greater than zero";
        }
        if (serviceDesk.getName() == null || serviceDesk.getName().trim().isEmpty()) {
            return "Service desk name is required";
        }
        if (serviceDeskRepository.existsByDeskNumber(serviceDesk.getDeskNumber())) {
            return "Service desk with that deskNumber already exists";
        }
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
            if (serviceDesk == null) {
                return "Service desk data cannot be null";
            }
            ServiceDesk found = existing.get();
            if (serviceDesk.getDeskNumber() > 0) {
                if (serviceDesk.getDeskNumber() != found.getDeskNumber() && serviceDeskRepository.existsByDeskNumber(serviceDesk.getDeskNumber())) {
                    return "Service desk with that deskNumber already exists";
                }
                found.setDeskNumber(serviceDesk.getDeskNumber());
            }
            if (serviceDesk.getName() != null && !serviceDesk.getName().trim().isEmpty()) {
                found.setName(serviceDesk.getName());
            }
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