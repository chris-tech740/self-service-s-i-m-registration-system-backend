package com.example.self_service_s_i_m_registration_system;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class MidtermRequirementsTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private com.example.self_service_s_i_m_registration_system.repository.CustomerRepository customerRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        customerRepository.findByNationalId("1199000012345678").ifPresent(customerRepository::delete);
    }


    @Test
    void testB1_FindCustomersByLastName() throws Exception {
        mockMvc.perform(get("/api/customers/by-last-name?lastName=mukamana"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$[0].firstName").value("Aline"))
                .andExpect(jsonPath("$[1].firstName").value("Beata"));
    }

    @Test
    void testB2_SimRegistrationsByStatus() throws Exception {
        mockMvc.perform(get("/api/sim-registrations/by-status?status=SUBMITTED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }

    @Test
    void testB3_SimRegistrationsBetween() throws Exception {
        mockMvc.perform(get("/api/sim-registrations/between?start=2026-10-01&end=2026-10-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }

    @Test
    void testC1_RegistrationAgentsBySimType() throws Exception {
        mockMvc.perform(get("/api/registration-agents/by-sim-type?name=prepaid"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].staffNumber").value("STAFF001"));
    }

    @Test
    void testC2_FreeServiceDesks() throws Exception {
        mockMvc.perform(get("/api/service-desks/free"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].deskNumber").value(4));
    }

    @Test
    void testC3_UnusedSimTypes() throws Exception {
        mockMvc.perform(get("/api/sim-types/unused"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Tourist"));
    }

    @Test
    void testC4_CustomersOfRegistrationAgent() throws Exception {
        mockMvc.perform(get("/api/customers/of-registration-agent/33333333-3333-3333-3333-333333333301"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))));

        mockMvc.perform(get("/api/customers/of-registration-agent/00000000-0000-0000-0000-000000000000"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("The registration agent with that id does not exist"));
    }

    @Test
    void testD1_SafeSavingPastDate() throws Exception {
        String body = "{\"registrationDate\": \"2025-01-01\", \"simNumber\": \"0788112233\"}";
        mockMvc.perform(post("/api/sim-registrations/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Sim registration date cannot be in the past"));
    }

    @Test
    void testD2_StatusFlow() throws Exception {
        mockMvc.perform(patch("/api/sim-registrations/55555555-5555-5555-5555-555555555503/status?status=SUBMITTED"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Cannot change status from APPROVED to SUBMITTED"));
    }

    @Test
    void testD3_AddSimTypeConflict() throws Exception {
        mockMvc.perform(put("/api/registration-agents/33333333-3333-3333-3333-333333333301/sim-types/11111111-1111-1111-1111-111111111101"))
                .andExpect(status().isConflict())
                .andExpect(content().string("The registration agent already has that sim type"));
    }

    @Test
    void testD4_ChangeServiceDeskConflict() throws Exception {
        mockMvc.perform(patch("/api/registration-agents/33333333-3333-3333-3333-333333333301/service-desk?deskNumber=2"))
                .andExpect(status().isConflict())
                .andExpect(content().string("That service desk is taken by another registration agent"));
    }

    @Test
    void testE1_WriteVerifiedDocumentToNonApproved() throws Exception {
        String body = "{\"documentType\": \"National ID\", \"remarks\": \"Test\", \"pages\": 2}";
        mockMvc.perform(post("/api/verified-documents/save?simRegistrationId=55555555-5555-5555-5555-555555555502")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Verified documents are only for APPROVED sim registrations"));
    }

    @Test
    void testE2_CustomerVerifiedDocumentHistory() throws Exception {
        mockMvc.perform(get("/api/verified-documents/customer/1199880012345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    void testE3_DeleteProtectedSimRegistration() throws Exception {
        mockMvc.perform(delete("/api/sim-registrations/delete/55555555-5555-5555-5555-555555555503"))
                .andExpect(status().isConflict())
                .andExpect(content().string("That sim registration has verified documents, delete them first"));
    }

    @Test
    void testF1_SimRegistrationsStatsByStatus() throws Exception {
        mockMvc.perform(get("/api/sim-registrations/stats/by-status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    void testBonus_DashboardSummary() throws Exception {
        mockMvc.perform(get("/api/dashboard/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customers").value(5))
                .andExpect(jsonPath("$.agents").value(3))
                .andExpect(jsonPath("$.freeDesks").value(1));
    }

    @Test
    void testBonus_Pagination() throws Exception {
        mockMvc.perform(get("/api/sim-registrations/page?page=0&size=5&sort=registrationDate,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(5)))
                .andExpect(jsonPath("$.totalElements").value(10))
                .andExpect(jsonPath("$.totalPages").value(2));
    }

    @Test
    void testValidation_CustomerMissingNationalId() throws Exception {
        String body = "{\"firstName\": \"Test\", \"lastName\": \"User\"}";
        mockMvc.perform(post("/api/customers/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Customer nationalId is required"));
    }

    @Test
    void testValidation_CustomerDuplicateNationalId() throws Exception {
        String body = "{\"firstName\": \"Test\", \"lastName\": \"User\", \"nationalId\": \"1199880012345678\"}";
        mockMvc.perform(post("/api/customers/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict())
                .andExpect(content().string("Customer with that nationalId already exists"));
    }

    @Test
    void testValidation_ServiceDeskInvalidNumber() throws Exception {
        String body = "{\"deskNumber\": 0, \"name\": \"Desk Zero\"}";
        mockMvc.perform(post("/api/service-desks/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Service desk number must be greater than zero"));
    }

    @Test
    void testValidation_SimTypeBlankName() throws Exception {
        String body = "{\"name\": \"\"}";
        mockMvc.perform(post("/api/sim-types/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Sim type name is required"));
    }
}
