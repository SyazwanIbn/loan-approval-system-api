package com.loan.project.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class LoanApplicationRequest {

    @NotNull(message = "Customer ID is required")
    private UUID customerId;

    @NotNull(message = "Loan Amount is required")
    @Min(value =1000, message = "Min amount is RM 1,000")
    private BigDecimal loanAmount;

    @NotNull(message = "Tenure Months is required")
    @Min(value = 6, message = "Min tenure months is 6 months")
    private Integer tenureMonths;

}
