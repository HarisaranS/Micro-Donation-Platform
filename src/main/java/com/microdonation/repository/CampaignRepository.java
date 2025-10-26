package com.microdonation.repository;

import com.microdonation.model.Campaign;
import com.microdonation.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {

    List<Campaign> findByStatus(String status);

    List<Campaign> findByCreator(User creator);

    List<Campaign> findByCreatorUserId(Long userId);

    @Query("SELECT c FROM Campaign c WHERE c.status = 'ACTIVE' ORDER BY c.createdAt DESC")
    List<Campaign> findActiveCampaigns();

    @Query("SELECT c FROM Campaign c ORDER BY c.raisedAmount DESC")
    List<Campaign> findTopFundedCampaigns();
}
