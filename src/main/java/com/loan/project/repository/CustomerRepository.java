package com.loan.project.repository;

import com.loan.project.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    //auto generate SQL based on method bawah ni
    Optional<Customer> findByIdentityNumber(String identityNumber);
    boolean existsByIdentityNumber(String identityNumber);
    boolean existsByEmail(String email);
}
