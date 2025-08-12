package com.biddy.biddy_api.domain.auction.repository;

import com.biddy.biddy_api.domain.auction.entity.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {
    Optional<Auction> getAuctionById(Long auctionId);
}
