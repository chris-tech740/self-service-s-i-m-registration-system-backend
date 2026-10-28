package com.example.self_service_s_i_m_registration_system.repository;

import com.example.self_service_s_i_m_registration_system.model.SimRegistration;
import com.example.self_service_s_i_m_registration_system.model.VerifiedDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;

public interface VerifiedDocumentRepository extends JpaRepository<VerifiedDocument, UUID> {

    @Query("SELECT d FROM VerifiedDocument d WHERE d.simRegistration.customer.nationalId = :nationalId ORDER BY d.verifiedDate DESC")
    List<VerifiedDocument> findByCustomerNationalIdOrderByVerifiedDateDesc(@Param("nationalId") String nationalId);

    boolean existsBySimRegistration(SimRegistration simRegistration);
}
