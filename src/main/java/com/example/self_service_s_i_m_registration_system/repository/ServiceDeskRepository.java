package com.example.self_service_s_i_m_registration_system.repository;

import com.example.self_service_s_i_m_registration_system.model.ServiceDesk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.UUID;

public interface ServiceDeskRepository extends JpaRepository<ServiceDesk, UUID> {
    @Query("SELECT d FROM ServiceDesk d WHERE NOT EXISTS (SELECT r FROM RegistrationAgent r WHERE r.serviceDesk = d) ORDER BY d.deskNumber ASC")
    List<ServiceDesk> findFreeServiceDesks();
}