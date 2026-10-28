package com.example.self_service_s_i_m_registration_system.repository;

import com.example.self_service_s_i_m_registration_system.model.SimRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SimRegistrationRepository extends JpaRepository<SimRegistration, UUID> {
}