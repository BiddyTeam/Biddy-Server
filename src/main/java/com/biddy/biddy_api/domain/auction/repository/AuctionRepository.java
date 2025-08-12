package com.biddy.biddy_api.domain.auction.repository;

import com.biddy.biddy_api.domain.auction.entity.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {
    Optional<Auction> getAuctionById(Long auctionId);

    @Modifying
    @Query("UPDATE Auction a SET a.status = 'ACTIVE' WHERE a.startTime <= :now AND a.status = 'SCHEDULED'")
    int activateScheduledAuctions(@Param("now") LocalDateTime now);

    @Modifying
    @Query("UPDATE Auction a SET a.status = 'ENDED' WHERE a.endTime <= :now AND a.status = 'ACTIVE'")
    int endExpiredAuctions(@Param("now") LocalDateTime now);
}
