package com.biddy.biddy_api.domain.auction.repository;

import com.biddy.biddy_api.domain.auction.entity.AuctionKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionKeywordRepository extends JpaRepository<AuctionKeyword, Long> {
}
