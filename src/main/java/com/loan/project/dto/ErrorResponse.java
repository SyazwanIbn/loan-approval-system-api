package com.loan.project.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // Kalau validationErrors null, dia takkan keluar kat JSON
public class ErrorResponse {

    private String timestamp;
    private int status;
    private String error;
    private String message;
    private Map<String, String> validationErrors;
}
