CREATE TABLE customers (
       id UUID PRIMARY KEY ,
       full_name VARCHAR(100) NOT NULL,
       identity_number VARCHAR(12) NOT NULL UNIQUE,
       net_monthly_income DECIMAL(15, 2) NOT NULL,
       email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE loan_applications (
       id UUID PRIMARY KEY,
       customer_id UUID NOT NULL,
       loan_amount DECIMAL(15, 2) NOT NULL,
       tenure_months INT NOT NULL,
       status VARCHAR(50) NOT NULL,
       created_at TIMESTAMP WITHOUT TIME ZONE,
       CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES customers (id) ON DELETE CASCADE
);