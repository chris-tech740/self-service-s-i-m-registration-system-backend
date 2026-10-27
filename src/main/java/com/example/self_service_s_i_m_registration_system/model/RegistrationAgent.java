package com.example.self_service_s_i_m_registration_system.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "registration_agent")
public class RegistrationAgent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String firstName;

    private String lastName;

    private String staffNumber;

    @ManyToOne
    @JoinColumn(name = "service_desk_id")
    private ServiceDesk serviceDesk;

    @ManyToMany
    @JoinTable(
            name = "registration_agent_sim_type",
            joinColumns = @JoinColumn(name = "registration_agent_id"),
            inverseJoinColumns = @JoinColumn(name = "sim_type_id")
    )
    private List<SimType> simTypes;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStaffNumber() {
        return staffNumber;
    }

    public void setStaffNumber(String staffNumber) {
        this.staffNumber = staffNumber;
    }

    public ServiceDesk getServiceDesk() {
        return serviceDesk;
    }

    public void setServiceDesk(ServiceDesk serviceDesk) {
        this.serviceDesk = serviceDesk;
    }

    public List<SimType> getSimTypes() {
        return simTypes;
    }

    public void setSimTypes(List<SimType> simTypes) {
        this.simTypes = simTypes;
    }
}