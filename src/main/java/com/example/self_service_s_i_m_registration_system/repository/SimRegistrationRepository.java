package com.example.self_service_s_i_m_registration_system.repository;

import com.example.self_service_s_i_m_registration_system.model.RegistrationStatus;
import com.example.self_service_s_i_m_registration_system.model.SimRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SimRegistrationRepository extends JpaRepository<SimRegistration, UUID> {
    List<SimRegistration> findByStatusOrderByRegistrationDateAsc(RegistrationStatus status);
    List<SimRegistration> findByRegistrationDateBetweenOrderByRegistrationDateAsc(LocalDate start, LocalDate end);
}