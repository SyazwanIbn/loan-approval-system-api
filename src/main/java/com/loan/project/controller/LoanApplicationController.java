package com.loan.project.controller;

import com.loan.project.dto.LoanApplicationRequest;
import com.loan.project.dto.LoanApplicationResponse;
import com.loan.project.service.LoanApplicationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/loans")
@RequiredArgsConstructor
@Tag(name = "2. Loan Application", description = "API for loan processing and DSR Calculation")
public class LoanApplicationController {

    private final LoanApplicationService loanApplicationService;

    @PostMapping("/apply")
    public ResponseEntity<LoanApplicationResponse> applyForLoan(@RequestBody LoanApplicationRequest request)
    {
        LoanApplicationResponse response = loanApplicationService.applyForLoan(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
