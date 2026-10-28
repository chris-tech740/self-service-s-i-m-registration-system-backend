package com.example.self_service_s_i_m_registration_system.repository;

import com.example.self_service_s_i_m_registration_system.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    List<Customer> findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName);

    @Query("SELECT DISTINCT s.customer FROM SimRegistration s WHERE s.agent.id = :registrationAgentId")
    List<Customer> findByRegistrationAgentId(@Param("registrationAgentId") UUID registrationAgentId);
}