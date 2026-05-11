package com.loan.project.service;

import com.loan.project.dto.CustomerRequest;
import com.loan.project.dto.CustomerResponse;
import com.loan.project.entity.Customer;
import com.loan.project.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    //get all customers
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    //get customer by id
    public CustomerResponse getCustomerById(UUID id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return (mapToResponse(customer));
    }

    //update customer
    @Transactional
    public CustomerResponse updateCustomer(UUID id, CustomerRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        //update customer
        customer.setFullName(request.getFullName());
        customer.setNetMonthlyIncome(request.getNetMonthlyIncome());
        customer.setEmail(request.getEmail());

        return (mapToResponse(customer));
    }

    // DELETE: Panggil delete biasa, tapi Hibernate akan buat soft delete
    public void deleteCustomer(UUID id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        customerRepository.delete(customer); // 1 query je
    }

    //helper method untuk map entity ke dto
    private CustomerResponse mapToResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .fullName(customer.getFullName())
                .email(customer.getEmail())
                .build();
    }
}
