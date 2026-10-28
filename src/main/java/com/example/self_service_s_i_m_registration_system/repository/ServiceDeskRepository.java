package com.example.self_service_s_i_m_registration_system.repository;

import com.example.self_service_s_i_m_registration_system.model.ServiceDesk;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ServiceDeskRepository extends JpaRepository<ServiceDesk, UUID> {
}