package com.example.self_service_s_i_m_registration_system.service;

import com.example.self_service_s_i_m_registration_system.model.RegistrationStatus;
import com.example.self_service_s_i_m_registration_system.repository.CustomerRepository;
import com.example.self_service_s_i_m_registration_system.repository.RegistrationAgentRepository;
import com.example.self_service_s_i_m_registration_system.repository.ServiceDeskRepository;
import com.example.self_service_s_i_m_registration_system.repository.SimRegistrationRepository;
import com.example.self_service_s_i_m_registration_system.repository.VerifiedDocumentRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class DashboardService {

    private final CustomerRepository customerRepository;
    private final RegistrationAgentRepository registrationAgentRepository;
    private final ServiceDeskRepository serviceDeskRepository;
    private final SimRegistrationRepository simRegistrationRepository;
    private final VerifiedDocumentRepository verifiedDocumentRepository;

    public DashboardService(CustomerRepository customerRepository,
                            RegistrationAgentRepository registrationAgentRepository,
                            ServiceDeskRepository serviceDeskRepository,
                            SimRegistrationRepository simRegistrationRepository,
                            VerifiedDocumentRepository verifiedDocumentRepository) {
        this.customerRepository = customerRepository;
        this.registrationAgentRepository = registrationAgentRepository;
        this.serviceDeskRepository = serviceDeskRepository;
        this.simRegistrationRepository = simRegistrationRepository;
        this.verifiedDocumentRepository = verifiedDocumentRepository;
    }

    public Map<String, Long> getSummary() {
        Map<String, Long> summary = new LinkedHashMap<>();
        summary.put("customers", customerRepository.count());
        summary.put("agents", registrationAgentRepository.count());
        summary.put("freeDesks", (long) serviceDeskRepository.findFreeServiceDesks().size());
        summary.put("registrationsToday", simRegistrationRepository.countByRegistrationDate(LocalDate.now()));
        summary.put("submitted", simRegistrationRepository.countByStatus(RegistrationStatus.SUBMITTED));
        summary.put("documents", verifiedDocumentRepository.count());
        return summary;
    }
}
