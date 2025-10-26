package com.microdonation.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "campaigns")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "campaign_id")
    private Long campaignId;

    @NotBlank(message = "Campaign title is required")
    @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters")
    @Column(nullable = false, length = 100)
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 20, message = "Description must be at least 20 characters")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Goal amount is required")
    @DecimalMin(value = "100.0", message = "Goal amount must be at least 100")
    @Column(name = "goal_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal goalAmount;

    @Column(name = "raised_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal raisedAmount = BigDecimal.ZERO;

    @NotNull(message = "Start date is required")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(nullable = false, length = 20)
    private String status = "ACTIVE"; // ACTIVE, COMPLETED, CANCELLED

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User creator;

    @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Donation> donations = new ArrayList<>();

    // Helper method to calculate progress percentage
    @Transient
    public double getProgressPercentage() {
        if (goalAmount.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        return raisedAmount.divide(goalAmount, 4, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal("100"))
                .doubleValue();
    }

    // Helper method to check if campaign is active
    @Transient
    public boolean isActive() {
        return "ACTIVE".equals(status) && LocalDate.now().isBefore(endDate.plusDays(1));
    }
}
