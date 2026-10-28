package com.example.self_service_s_i_m_registration_system.repository;

import com.example.self_service_s_i_m_registration_system.model.SimType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.UUID;

public interface SimTypeRepository extends JpaRepository<SimType, UUID> {
    @Query("SELECT s FROM SimType s WHERE s.agents IS EMPTY")
    List<SimType> findUnusedSimTypes();

    boolean existsByNameIgnoreCase(String name);
}