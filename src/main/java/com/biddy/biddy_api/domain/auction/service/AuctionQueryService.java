package com.biddy.biddy_api.domain.auction.service;

import com.biddy.biddy_api.domain.auction.dto.AuctionDto;
import com.biddy.biddy_api.domain.auction.entity.Auction;
import com.biddy.biddy_api.domain.auction.repository.AuctionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuctionQueryService {

    private final AuctionRepository auctionRepository;

    public AuctionDto getAuction(Long userId, Long auctionId) {
        Auction auction = auctionRepository.getAuctionById(auctionId).orElseThrow();
        return auction.toDto(userId);
    }
}
