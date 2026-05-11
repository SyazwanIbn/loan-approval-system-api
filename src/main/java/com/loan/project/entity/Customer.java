package com.loan.project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "customers")
@SQLDelete(sql = "UPDATE customers SET deleted = true, deleted_at = NOW WHERE id = ?")
@SQLRestriction("deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false, unique = true, length = 12)
    private String identityNumber;

    @Column(nullable = false)
    private BigDecimal netMonthlyIncome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private boolean deleted = Boolean.FALSE;

    private LocalDateTime deletedAt;


}
