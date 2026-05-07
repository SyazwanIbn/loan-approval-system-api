package com.loan.project.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CustomerResponse {
    private UUID id;
    private String fullName;
    private String email;
}
