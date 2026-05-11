package com.loan.project.controller;

import com.loan.project.dto.LoanApplicationRequest;
import com.loan.project.dto.LoanApplicationResponse;
import com.loan.project.service.LoanApplicationService;
import com.loan.project.service.PdfGeneratorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/loans")
@RequiredArgsConstructor
@Tag(name = "2. Loan Application", description = "API for loan processing and DSR Calculation")
public class LoanApplicationController {

    private final LoanApplicationService loanApplicationService;
    private final PdfGeneratorService pdfGeneratorService;

    @PostMapping("/apply")
    public ResponseEntity<LoanApplicationResponse> applyForLoan(@Valid @RequestBody LoanApplicationRequest request)
    {
        LoanApplicationResponse response = loanApplicationService.applyForLoan(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<LoanApplicationResponse>> getAllLoans() {
        return ResponseEntity.ok(loanApplicationService.getAllLoanApplications());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanApplicationResponse> getLoanById(@PathVariable UUID id) {
        return ResponseEntity.ok(loanApplicationService.getLoanById(id));
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<List<LoanApplicationResponse>> getLoanByCustomerId(@PathVariable UUID id) {
        return ResponseEntity.ok(loanApplicationService.getAllLoanApplicationsByCustomerId(id));
    }

    @GetMapping("/{id}/download-approval-letter")
    public ResponseEntity<byte[]> downloadApprovalLetter(@PathVariable UUID id) {

        // fetch loan details
        LoanApplicationResponse loan = loanApplicationService.getLoanById(id);

        // generate pdf
        byte[] pdf = pdfGeneratorService.generateApprovalLetter(loan);

        // set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "approval_letter_" + id + ".pdf");
        headers.setContentLength(pdf.length);

        return ResponseEntity.ok().headers(headers).body(pdf);
    }
}
