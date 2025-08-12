package com.biddy.biddy_api.domain.auction.service;

import com.biddy.biddy_api.domain.auction.dto.AuctionDto;
import com.biddy.biddy_api.domain.auction.converter.AuctionConverter;
import com.biddy.biddy_api.domain.auction.dto.AuctionListDto;
import com.biddy.biddy_api.domain.auction.entity.Auction;
import com.biddy.biddy_api.domain.auction.repository.AuctionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuctionQueryService {

    private final AuctionRepository auctionRepository;
    private final AuctionConverter auctionConverter;

    public AuctionDto getAuction(Long userId, Long auctionId) {
        Auction auction = auctionRepository.getAuctionById(auctionId).orElseThrow();
        return auction.toDto(userId);
    }

    public List<AuctionListDto> getAllAuctions() {
        List<Auction> auctions = auctionRepository.findAll();
        return auctionConverter.toListDtos(auctions);
    }
}
