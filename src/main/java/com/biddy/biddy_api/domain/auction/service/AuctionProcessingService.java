package com.biddy.biddy_api.domain.auction.service;

import com.biddy.biddy_api.domain.auction.entity.Auction;
import com.biddy.biddy_api.domain.auction.entity.Bid;
import com.biddy.biddy_api.domain.auction.enums.BidStatus;
import com.biddy.biddy_api.domain.auction.repository.AuctionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuctionProcessingService {

    private final AuctionRepository auctionRepository;

    public void processBidsForEndedAuctions(List<Long> auctionIds) {
        if (auctionIds == null || auctionIds.isEmpty()) {
            return; // 처리할 ID가 없으면 즉시 종료
        }

        // 1. ID 목록으로 경매와 관련 입찰 정보를 한 번에 조회 (N+1 문제 방지)
        List<Auction> endedAuctions = auctionRepository.findAllById(auctionIds);

        // 2. 각 경매를 순회하며 낙찰/패찰 처리 진행 (이 로직은 이전과 동일)
        for (Auction auction : endedAuctions) {
            List<Bid> bids = auction.getBids();

            if (bids == null || bids.isEmpty()) {
                continue;
            }

            Optional<Bid> winningBidOptional = bids.stream()
                    .max(Comparator.comparing(Bid::getAmount));

            winningBidOptional.ifPresent(winningBid -> {
                for (Bid bid : bids) {
                    bid.setStatus(bid.equals(winningBid) ? BidStatus.WINNING : BidStatus.OUTBID);
                }
            });
        }
    }
}