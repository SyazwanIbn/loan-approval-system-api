package com.loan.project.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class LoanApplicationRequest {
    private UUID customerId;
    private BigDecimal loanAmount;
    private Integer tenureMonths;

}
