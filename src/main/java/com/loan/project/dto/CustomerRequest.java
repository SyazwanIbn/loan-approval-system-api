package com.loan.project.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CustomerRequest {
    private String fullName;
    private String identityNumber;
    private BigDecimal netMonthlyIncome;
    private String email;

}
