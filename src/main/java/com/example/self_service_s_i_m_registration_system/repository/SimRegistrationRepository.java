package com.example.self_service_s_i_m_registration_system.repository;

import com.example.self_service_s_i_m_registration_system.model.RegistrationAgent;
import com.example.self_service_s_i_m_registration_system.model.RegistrationStatus;
import com.example.self_service_s_i_m_registration_system.model.SimRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SimRegistrationRepository extends JpaRepository<SimRegistration, UUID> {

    List<SimRegistration> findByStatusOrderByRegistrationDateAsc(RegistrationStatus status);

    List<SimRegistration> findByRegistrationDateBetweenOrderByRegistrationDateAsc(LocalDate start, LocalDate end);

    boolean existsByAgentAndRegistrationDateAndStatusNot(RegistrationAgent agent, LocalDate registrationDate, RegistrationStatus status);

    @Query("SELECT r.status, COUNT(r) FROM SimRegistration r GROUP BY r.status")
    List<Object[]> countByStatus();

    @Modifying
    @Query("UPDATE SimRegistration r SET r.status = :rejectedStatus WHERE r.agent.id = :agentId AND r.registrationDate = :date AND r.status <> :approvedStatus")
    int cancelDayRegistrations(@Param("agentId") UUID agentId,
                               @Param("date") LocalDate date,
                               @Param("rejectedStatus") RegistrationStatus rejectedStatus,
                               @Param("approvedStatus") RegistrationStatus approvedStatus);

    long countByRegistrationDate(LocalDate registrationDate);

    long countByStatus(RegistrationStatus status);
}