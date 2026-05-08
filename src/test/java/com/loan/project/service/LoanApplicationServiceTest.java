package com.loan.project.service;

import com.loan.project.dto.LoanApplicationRequest;
import com.loan.project.dto.LoanApplicationResponse;
import com.loan.project.entity.Customer;
import com.loan.project.entity.LoanApplication;
import com.loan.project.repository.CustomerRepository;
import com.loan.project.repository.LoanApplicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanApplicationServiceTest {

    @Mock
    private CustomerRepository customerRepository; //robot database pelanggan

    @Mock
    private LoanApplicationRepository loanApplicationRepository; //robot database loanApplication

    @InjectMocks
    private LoanApplicationService loanApplicationService; // service yang nak di test

    private Customer mockCustomer;
    private LoanApplicationRequest mockRequest;

    @BeforeEach
    void SetUp() {
        // data palsu bawah ni akan di reset setiap kali sebelum test berjalan
        mockCustomer = Customer.builder()
                .id(UUID.randomUUID())
                .fullName("Ahmad Burhan")
                .identityNumber("970921234321")
                .netMonthlyIncome(new BigDecimal("4500.00"))
                .email("ahmad@gmail.com")
                .build();

        mockRequest = new LoanApplicationRequest();
        mockRequest.setCustomerId(mockCustomer.getId());
        mockRequest.setTenureMonths(60);
    }

    @Test
    @DisplayName("Test 1: Application Approve if DSR below 60%")
    void shouldAproveLoan_whenDSRIsBelowThreshold() {
        // given situation (situasi yang kita tetapkan)
        mockRequest.setLoanAmount(new BigDecimal("50000.00")); // so ansuran rm833.33, DSR 18.5%(833.33/4500)

        // Bgtau mock database, kalau service cari ID, kita bagi dia id mockCustomer
        when(customerRepository.findById(mockRequest.getCustomerId())).thenReturn(Optional.of(mockCustomer));

        //Bgtau mock database, kalau service nak save, return je balik object tu
        when(loanApplicationRepository.save(any(LoanApplication.class))).thenAnswer(invocation -> {
            LoanApplication savedLoan = invocation.getArgument(0);
            savedLoan.setId(UUID.randomUUID());
            return savedLoan;
        });

        //WHEN (apabila kita panggil fungsi sebenar
        LoanApplicationResponse response = loanApplicationService.applyForLoan(mockRequest);

        // THEN (Apakah keputusan yang kita harapkan?)
        assertNotNull(response);
        assertEquals("APPROVED", response.getStatus()); // Wajib lulus!
        verify(customerRepository, times(1)).findById(any()); // Pastikan database dipanggil sekali
        verify(loanApplicationRepository, times(1)).save(any());

        }

    @Test
    @DisplayName("Test 2: Permohonan REJECTED jika DSR lebih 60%")
    void shouldRejectLoan_WhenDsrIsAboveThreshold() {
        // GIVEN (Situasi yang kita tetapkan)
        // Gaji mockCustomer kita ialah RM 4,500.
        // Kita cuba pinjam RM 300,000 untuk tempoh 60 bulan.
        // Ansuran bulanan = RM 5,000. DSR = 5000 / 4500 = 1.11 (111%)
        // Memandangkan 111% > 60%, sistem patut reject.
        mockRequest.setLoanAmount(new BigDecimal("300000.00"));

        when(customerRepository.findById(mockRequest.getCustomerId())).thenReturn(Optional.of(mockCustomer));
        when(loanApplicationRepository.save(any(LoanApplication.class))).thenAnswer(invocation -> {
            LoanApplication savedLoan = invocation.getArgument(0);
            savedLoan.setId(UUID.randomUUID());
            return savedLoan;
        });

        // WHEN (Apabila kita panggil fungsi sebenar)
        LoanApplicationResponse response = loanApplicationService.applyForLoan(mockRequest);

        // THEN (Apakah keputusan yang kita harapkan?)
        assertNotNull(response);
        assertEquals("REJECTED", response.getStatus()); // Kita paksa JUnit semak adakah statusnya REJECTED
        verify(customerRepository, times(1)).findById(any());
        verify(loanApplicationRepository, times(1)).save(any());
    }


}
