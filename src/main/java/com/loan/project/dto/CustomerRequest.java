package com.loan.project.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CustomerRequest {

    @NotNull(message = "Name cannot be blank")
    private String fullName;

    @NotNull(message = "IC cannot be blank")
    @Size(min = 12, max = 12, message = "IC must have 12 digits")
    @Pattern(regexp = "^[0-9]+$", message = "IC only contain numbers")
    private String identityNumber;

    @NotNull(message = "Salary cannot be blank")
    @Positive(message = "Salary must be positive")
    private BigDecimal netMonthlyIncome;

    @NotNull(message = "Email cannot be blank")
    @Email(message = "Email format must be name@email.com")
    private String email;

}
