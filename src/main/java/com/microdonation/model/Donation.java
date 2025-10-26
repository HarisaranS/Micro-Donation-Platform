package com.microdonation.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "donations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "donation_id")
    private Long donationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id", nullable = false)
    private Campaign campaign;

    @NotNull(message = "Donation amount is required")
    @DecimalMin(value = "1.0", message = "Minimum donation amount is 1")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @CreationTimestamp
    @Column(name = "donation_date", nullable = false, updatable = false)
    private LocalDateTime donationDate;

    @Column(name = "payment_status", nullable = false, length = 20)
    private String paymentStatus = "PENDING"; // PAID, PENDING, FAILED

    @Column(name = "payment_mode", length = 50)
    private String paymentMode; // Credit Card, Debit Card, UPI, Net Banking

    @Column(name = "transaction_id", length = 100)
    private String transactionId;
}
