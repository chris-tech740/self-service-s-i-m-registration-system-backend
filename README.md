# Self Service SIM Registration System Backend

**Academic Year:** 2026-2027 (1)  
**Course Code & Name:** INSY 8322 WEB TECHNOLOGY AND INTERNET  
**Lecturer:** Patrick DUSHIMIMANA  
**Student Name:** IMANZI Chris  
**Student ID:** 27545  
**Project Name:** Self Service SIM Registration System  

---

## 1. Project Overview
The **Self Service SIM Registration System** is an enterprise-grade backend developed with Spring Boot, Spring Data JPA, and PostgreSQL. It allows telecommunication customers to register SIM cards online. Submissions are reviewed by registration agents stationed at specific service desks, cleared for specific SIM types (e.g., Prepaid, Postpaid, Corporate). Verified identity documents are recorded once a registration moves to `APPROVED`.

---

## 2. Technology Stack
- **Language:** Java 21
- **Framework:** Spring Boot (Spring Web MVC, Spring Data JPA)
- **Database:** PostgreSQL
- **Persistence:** Hibernate 7 / JPA
- **Build Tool:** Apache Maven (with Maven Wrapper `mvnw`)
- **API Testing:** Postman & JUnit 5 / MockMvc

---

## 3. Database & Architecture Requirements

### Entities & Relationships
1. **SimType (`sim_type`):** `id` (UUID), `name`. `@ManyToMany(mappedBy = "simTypes") List<RegistrationAgent> agents` marked `@JsonIgnore`.
2. **ServiceDesk (`service_desk`):** `id` (UUID), `int deskNumber`, `name`.
3. **RegistrationAgent (`registration_agent`):** `id` (UUID), `firstName`, `lastName`, `staffNumber`.
   - `@ManyToOne ServiceDesk serviceDesk` (`service_desk_id`, nullable).
   - `@ManyToMany List<SimType> simTypes` with `@JoinTable(name = "registration_agent_sim_type")`.
4. **Customer (`customer`):** `id` (UUID), `firstName`, `lastName`, `nationalId` (unique business key), `phone`, `email`.
5. **SimRegistration (`sim_registration`):** `id` (UUID), `registrationDate`, `status` (`RegistrationStatus` enum), `simNumber`.
   - `@ManyToOne Customer customer` (`customer_id`).
   - `@ManyToOne RegistrationAgent agent` (`agent_id`).
6. **VerifiedDocument (`verified_document`):** `id` (UUID), `documentType`, `remarks`, `int pages`, `verifiedDate`.
   - `@ManyToOne SimRegistration simRegistration` (`sim_registration_id`).

### Registration Status Lifecycle
```
SUBMITTED ─────────► UNDER_REVIEW ─────────► APPROVED (Final)
    │                     │
    └─────────────────────┴────────────────► REJECTED (Final)
```

---

## 4. API Endpoints Reference

### Part A: Domain Model & CRUD (15 Marks)
| Requirement | Method | Endpoint | Expected Status | Expected Behavior |
| :--- | :--- | :--- | :--- | :--- |
| **A1** | - | - | - | Entities, relationships, and tables created in PostgreSQL |
| **A2** | `POST` | `/api/{path}/save` | `201 CREATED` | Save entity ("Sim registration saved successfully", etc.) |
| **A2** | `GET` | `/api/{path}/all` | `200 OK` | Return all entities in a list |
| **A2** | `GET` | `/api/{path}/{id}` | `200 OK` / `404 NOT FOUND` | Get entity by ID or 404 message |
| **A2** | `PUT` | `/api/{path}/update/{id}` | `200 OK` / `404 NOT FOUND` | Update entity or 404 message |
| **A2** | `DELETE` | `/api/{path}/delete/{id}` | `200 OK` / `404 NOT FOUND` | Delete entity or 404 message |
*`{path}` can be: `sim-types`, `service-desks`, `registration-agents`, `customers`, `sim-registrations`.*

---

### Part B: Derived Queries (15 Marks)
| ID | Method | Endpoint | Query Type | Description |
| :--- | :--- | :--- | :--- | :--- |
| **B1** | `GET` | `/api/customers/by-last-name?lastName=mukamana` | **DERIVED** | Case-insensitive lookup, ordered by firstName ASC |
| **B2** | `GET` | `/api/sim-registrations/by-status?status=SUBMITTED` | **DERIVED** | Returns registrations in status, earliest registrationDate first |
| **B3** | `GET` | `/api/sim-registrations/between?start=2026-10-01&end=2026-10-31` | **DERIVED** | Inclusive date range query ordered by registrationDate ASC |

---

### Part C: JPQL with Joins (20 Marks)
| ID | Method | Endpoint | Query Type | Description |
| :--- | :--- | :--- | :--- | :--- |
| **C1** | `GET` | `/api/registration-agents/by-sim-type?name=prepaid` | **JPQL** | Finds agents holding that SIM type (case-insensitive join) |
| **C2** | `GET` | `/api/service-desks/free` | **JPQL** | Returns desks unoccupied by agents, sorted by deskNumber ASC |
| **C3** | `GET` | `/api/sim-types/unused` | **JPQL** | Returns SIM types that no agent holds (`IS EMPTY`) |
| **C4** | `GET` | `/api/customers/of-registration-agent/{id}` | **JPQL** | Returns distinct customers registered with agent; 404 if agent not found |

---

### Part D: Business Rules (25 Marks)
| ID | Method | Endpoint | Description |
| :--- | :--- | :--- | :--- |
| **D1** | `POST` | `/api/sim-registrations/save` | Rejects past dates (400) and duplicate review conflicts on same day (409). Force starts as `SUBMITTED`. |
| **D2** | `PATCH` | `/api/sim-registrations/{id}/status?status=...` | Strict state transitions; general PUT does not alter status. |
| **D3** | `PUT` | `/api/registration-agents/{agentId}/sim-types/{simTypeId}` | Adds SIM type to agent's list; 409 if already held, 404 if not found. |
| **D4** | `PATCH` | `/api/registration-agents/{agentId}/service-desk?deskNumber=...` | Reassigns agent to a new desk; 409 if desk taken, 404 if not found. |

---

### Part E: VerifiedDocument Entity (15 Marks)
| ID | Method | Endpoint | Description |
| :--- | :--- | :--- | :--- |
| **E1** | `POST` | `/api/verified-documents/save?simRegistrationId=...` | Writes verified document only for `APPROVED` registrations (400 if not). Date set to today. |
| **E2** | `GET` | `/api/verified-documents/customer/{nationalId}` | Returns customer's verified documents across all SIMs, newest first. |
| **E3** | `DELETE` | `/api/verified-documents/delete/{id}` | Deletes verified document. |
| **E3** | `DELETE` | `/api/sim-registrations/delete/{id}` | Protected delete: blocks deletion if verified documents exist (409). |

---

### Part F: Reports & Bulk Updates (10 Marks)
| ID | Method | Endpoint | Description |
| :--- | :--- | :--- | :--- |
| **F1** | `GET` | `/api/sim-registrations/stats/by-status` | **JPQL GROUP BY**: Returns status counts as `List<Object[]>`. |
| **F2** | `PATCH` | `/api/sim-registrations/cancel-day?registrationAgentId=...&date=...` | **JPQL UPDATE**: Bulk cancels agent's registrations on date to `REJECTED` (excluding `APPROVED`). |

---

### Bonus: Dashboard & Pagination (+10 Marks)
| Feature | Method | Endpoint | Description |
| :--- | :--- | :--- | :--- |
| **Dashboard** | `GET` | `/api/dashboard/summary` | Returns a JSON map: `{"customers":5, "agents":3, "freeDesks":1, "registrationsToday":0, "submitted":4, "documents":2}` |
| **Pagination** | `GET` | `/api/sim-registrations/page?page=0&size=5&sort=registrationDate,desc` | Returns paginated `Page<SimRegistration>` with totalElements and totalPages. |

---

## 5. Setup and Execution Steps

### 1. Database Configuration
Ensure PostgreSQL is running on `localhost:5432`. Create the database and configure privileges:
```sql
CREATE DATABASE self_service_sim_registration_system;
GRANT ALL ON SCHEMA public TO postgres;
```
Configure your credentials in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/self_service_sim_registration_system
spring.datasource.username=postgres
spring.datasource.password=123123
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 2. Build the Project
Run Maven compiler via the provided wrapper:
```bash
./mvnw clean compile
```

### 3. Run the Automated Test Suite
Execute the 18 automated integration tests covering all requirements:
```bash
./mvnw test
```

### 4. Run the API Server
Start the Spring Boot application:
```bash
./mvnw spring-boot:run
```
The application will listen on `http://localhost:8080`.

---

## 6. Postman Collection
The collection is included at the root of the project:
`self-service-s-i-m-registration-system-midterm.postman_collection.json`

Import this file into Postman to run and demo all endpoints for each requirement (`A2`, `B1`, `B2`, `B3`, `C1`, `C2`, `C3`, `C4`, `D1`, `D2`, `D3`, `D4`, `E1`, `E2`, `E3`, `F1`, `F2`, `Bonus`).
