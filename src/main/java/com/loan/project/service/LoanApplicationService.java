package com.loan.project.service;

import com.loan.project.dto.LoanApplicationRequest;
import com.loan.project.dto.LoanApplicationResponse;
import com.loan.project.entity.Customer;
import com.loan.project.entity.LoanApplication;
import com.loan.project.enums.LoanStatus;
import com.loan.project.repository.CustomerRepository;
import com.loan.project.repository.LoanApplicationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanApplicationService {

    private final LoanApplicationRepository loanRepository;
    private final CustomerRepository customerRepository;

    // set had maksimun dsr ke 60%
    private static final BigDecimal MAX_DSR_THRESHOLD = new BigDecimal("0.60");

    @Transactional
    public LoanApplicationResponse applyForLoan(LoanApplicationRequest request) {
        // 1. find customer in database
        Customer customer  =customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer Not Found"));

        //2. kira anggaran ansuran bulanan pinjaman (jumlah pinjaman / tempoh bulan)
        BigDecimal monthlyInstallment = request.getLoanAmount().divide(
                BigDecimal.valueOf(request.getTenureMonths()),2, RoundingMode.HALF_UP
        );

        //3. kira DSR (ansuran bulanan / gaji bersih bulanan)
        BigDecimal dsr = monthlyInstallment.divide(
                customer.getNetMonthlyIncome(),4, RoundingMode.HALF_UP
        );

        //4. Logik kelulusan based on DSR
        LoanStatus finalStatus;
        if (dsr.compareTo(MAX_DSR_THRESHOLD) > 0) {
            finalStatus = LoanStatus.REJECTED;
        } else {
            finalStatus = LoanStatus.APPROVED;
        }

        //5. tukar dto ke entity loanApplication
        LoanApplication loanApplication = LoanApplication.builder()
                .customer(customer)
                .loanAmount(request.getLoanAmount())
                .tenureMonths(request.getTenureMonths())
                .status(finalStatus)
                .build();

        LoanApplication savedLoan = loanRepository.save(loanApplication);

        //6. tukar dto ke entity balik
        return LoanApplicationResponse.builder()
                .applicationId(savedLoan.getId())
                .customerName(customer.getFullName())
                .loanAmount(savedLoan.getLoanAmount())
                .tenureMonths(savedLoan.getTenureMonths())
                .status(savedLoan.getStatus().name())
                .createdAt(savedLoan.getCreatedAt() != null ? savedLoan.getCreatedAt().toString() : "TBD")
                .build();
    }

    //get all loan
    public List<LoanApplicationResponse> getAllLoanApplications() {
        return loanRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    //get loan by id
    public LoanApplicationResponse getLoanById(UUID id) {
        LoanApplication loan = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan Application Not Found"));
        return mapToResponse(loan);
    }

    //get loan by customer id
    public List<LoanApplicationResponse> getAllLoanApplicationsByCustomerId(UUID id) {
        //check customer wujud ke tak
        if(!customerRepository.existsById(id)) {
            throw new RuntimeException("Customer Not Found");
        }

        return loanRepository.findByCustomerId(id)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    //helper method
    private LoanApplicationResponse mapToResponse(LoanApplication loan) {
        return LoanApplicationResponse.builder()
                .applicationId(loan.getId())
                .customerName(loan.getCustomer().getFullName())
                .loanAmount(loan.getLoanAmount())
                .tenureMonths(loan.getTenureMonths())
                .status(loan.getStatus().name())
                .createdAt(loan.getCreatedAt() != null ? loan.getCreatedAt().toString() : "TBD")
                .build();
    }





}
