package com.loan.project.service;

import com.loan.project.dto.CustomerRequest;
import com.loan.project.dto.CustomerResponse;
import com.loan.project.entity.Customer;
import com.loan.project.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerResponse registerCustomer(CustomerRequest request) {
        //check ic dengan email dah wujud ke belum dalam db
        if (customerRepository.existsByIdentityNumber(request.getIdentityNumber())) {
            throw new RuntimeException("Identication Number already exists");
        }
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        //tukar dto kepada entity (guna builder)
        Customer customer =  Customer.builder()
                .fullName(request.getFullName())
                .identityNumber(request.getIdentityNumber())
                .netMonthlyIncome(request.getNetMonthlyIncome())
                .email(request.getEmail())
                .build();

        Customer savedCustomer = customerRepository.save(customer);

        //return response DTO
        return CustomerResponse.builder()
                .id(savedCustomer.getId())
                .fullName(savedCustomer.getFullName())
                .email(savedCustomer.getEmail())
                .build();
    }
}
