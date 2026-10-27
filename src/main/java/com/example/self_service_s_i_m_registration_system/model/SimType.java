package com.example.self_service_s_i_m_registration_system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "sim_type")
public class SimType {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @ManyToMany(mappedBy = "simTypes")
    @JsonIgnore
    private List<RegistrationAgent> agents;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RegistrationAgent> getAgents() {
        return agents;
    }

    public void setAgents(List<RegistrationAgent> agents) {
        this.agents = agents;
    }
}