package com.biddy.biddy_api.domain.auction.repository;

import com.biddy.biddy_api.domain.auction.entity.AuctionImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionImageRepository extends JpaRepository<AuctionImage, Long> {
}
