package com.microdonation.repository;

import com.microdonation.model.Campaign;
import com.microdonation.model.Donation;
import com.microdonation.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {

    List<Donation> findByUser(User user);

    List<Donation> findByUserUserId(Long userId);

    List<Donation> findByCampaign(Campaign campaign);

    List<Donation> findByCampaignCampaignId(Long campaignId);

    List<Donation> findByPaymentStatus(String paymentStatus);

    @Query("SELECT SUM(d.amount) FROM Donation d WHERE d.campaign.campaignId = :campaignId AND d.paymentStatus = 'PAID'")
    BigDecimal getTotalDonationsByCampaign(@Param("campaignId") Long campaignId);

    @Query("SELECT COUNT(d) FROM Donation d WHERE d.campaign.campaignId = :campaignId AND d.paymentStatus = 'PAID'")
    Long getCountOfDonorsByCampaign(@Param("campaignId") Long campaignId);

    @Query("SELECT d FROM Donation d WHERE d.campaign.campaignId = :campaignId ORDER BY d.amount DESC")
    List<Donation> findTopDonorsByCampaign(@Param("campaignId") Long campaignId);
}
