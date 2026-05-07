package com.loan.project.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class LoanApplicationResponse {
    private UUID applicationId;
    private String customerName;
    private BigDecimal loanAmount;
    private Integer tenureMonths;
    private String status;
    private String createdAt;
}
