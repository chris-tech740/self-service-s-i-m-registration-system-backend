package com.example.self_service_s_i_m_registration_system.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "verified_document")
public class VerifiedDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String documentType;

    private String remarks;

    private int pages;

    private LocalDate verifiedDate;

    @ManyToOne
    @JoinColumn(name = "sim_registration_id")
    private SimRegistration simRegistration;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public LocalDate getVerifiedDate() {
        return verifiedDate;
    }

    public void setVerifiedDate(LocalDate verifiedDate) {
        this.verifiedDate = verifiedDate;
    }

    public SimRegistration getSimRegistration() {
        return simRegistration;
    }

    public void setSimRegistration(SimRegistration simRegistration) {
        this.simRegistration = simRegistration;
    }
}
