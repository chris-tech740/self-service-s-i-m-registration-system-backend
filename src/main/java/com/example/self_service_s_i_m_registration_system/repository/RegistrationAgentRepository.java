package com.example.self_service_s_i_m_registration_system.repository;

import com.example.self_service_s_i_m_registration_system.model.RegistrationAgent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;

public interface RegistrationAgentRepository extends JpaRepository<RegistrationAgent, UUID> {
    @Query("SELECT r FROM RegistrationAgent r JOIN r.simTypes s WHERE LOWER(s.name) = LOWER(:name)")
    List<RegistrationAgent> findBySimTypeName(@Param("name") String name);
}