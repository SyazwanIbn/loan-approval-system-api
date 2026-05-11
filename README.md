# 🏦 Loan Application & Approval Management System

## 📖 Overview
This project is an Enterprise-grade backend REST API built to manage customer profiles and automate the loan approval process. It demonstrates the seamless translation of strict business rules—specifically the Debt Service Ratio (DSR) evaluation—into functional, scalable, and secure technical specifications.

Designed with a focus on clean architecture, this system serves as a bridge between operational business requirements and robust backend execution.

## 🚀 Tech Stack & Tools
* **Language:** Java 21
* **Framework:** Spring Boot 3.x (Spring Web, Spring Data JPA)
* **Database:** PostgreSQL
* **Database Migration:** Flyway (Version Control for Database)
* **API Documentation:** Swagger UI / OpenAPI 3
* **Document Generation:** OpenPDF
* **Environment:** Windows / Docker Ready

## ✨ Key Features & Business Value

### 1. Automated Business Rule Engine (Loan Approval)
* Evaluates loan applications dynamically based on the applicant's **Debt Service Ratio (DSR)**.
* Automatically calculates monthly installments and compares them against the net monthly income.
* Rejects applications exceeding the strictly configured DSR threshold, ensuring risk compliance without manual intervention.

### 2. Enterprise Data Management (Soft Delete)
* Implements `@SQLRestriction` and `@SQLDelete` for the Customer module.
* Ensures data integrity and audit compliance by preventing "hard deletes" from the database, retaining historical records.

### 3. Data Encapsulation & Security (DTO Pattern)
* Strict separation between Persistence layer (`Entities`) and Presentation layer (`DTOs`).
* Prevents sensitive data leakage (e.g., net income) in API responses.

### 4. Global Exception Handling
* Centralized `@RestControllerAdvice` to intercept errors.
* Translates technical stack traces into clean, standardized, and human-readable JSON error responses for frontend consumers.

### 5. Dynamic Document Generation
* Automatically generates an Official Approval Letter (PDF format) upon successful loan application.

## ⚙️ Setup & Installation

**Prerequisites:**
* Java 21 installed.
* PostgreSQL running locally (or via Docker) on port `5432` (or your configured port).
* Maven (`mvnw`) included in the wrapper.

**1. Clone the repository**
```bash
git clone <your-repo-url>
cd loan-project
```

**2. Configure the Database**
* Ensure your src/main/resources/application.yml has the correct PostgreSQL credentials. Flyway will automatically create the tables and apply the schema upon startup.

**3. Run the Application**
```bash
./mvnw spring-boot:run
```

## 📚 API Documentation (Swagger UI)
Once the application is running, the interactive API documentation is automatically generated and accessible via:

URL: http://localhost:8080/swagger-ui/index.html

**Core Endpoints:**
Customer Management:

* POST /api/v1/customers - Register a new customer.

* GET /api/v1/customers - Retrieve active customers (filters soft-deleted).

* PUT /api/v1/customers/{id} - Update customer details.

* DELETE /api/v1/customers/{id} - Safely soft-delete a customer.

Loan Operations:

* POST /api/v1/loans/apply - Submit application & trigger DSR evaluation.

* GET /api/v1/loans/{id}/download-letter - Download generated PDF offer letter.

## 🧠 Architecture Approach
This system utilizes a layered architecture (Controller -> Service -> Repository) to maintain a separation of concerns. It heavily relies on Hibernate's Dirty Checking mechanism within @Transactional boundaries for optimized database updates, reducing redundant query executions.