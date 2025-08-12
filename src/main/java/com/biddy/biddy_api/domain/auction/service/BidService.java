package com.biddy.biddy_api.domain.auction.service;

import com.biddy.biddy_api.domain.auction.dto.BidDto;
import com.biddy.biddy_api.domain.auction.dto.PostBidRequest;
import com.biddy.biddy_api.domain.auction.entity.Auction;
import com.biddy.biddy_api.domain.auction.entity.Bid;
import com.biddy.biddy_api.domain.auction.enums.BidStatus;
import com.biddy.biddy_api.domain.auction.repository.AuctionRepository;
import com.biddy.biddy_api.domain.auction.repository.BidRepository;
import com.biddy.biddy_api.domain.user.entity.User;
import com.biddy.biddy_api.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BidService {

    private final BidRepository bidRepository;
    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;

    public Long postBidInAuction(PostBidRequest request) throws RuntimeException {
        Auction auction = auctionRepository.getAuctionById(request.getAuctionId()).orElseThrow();
        long requestPriceLong = request.getAmount();
        BigDecimal requestPrice = BigDecimal.valueOf(requestPriceLong);
        BigDecimal currentPrice = auction.getCurrentPrice();

        // 요청된 가격이 현재가보다 낮을 때
        if (requestPrice.compareTo(currentPrice) <= 0) {
            throw new RuntimeException();
        }

        User user = userRepository.findById(request.getBidderId()).orElseThrow();
        auction.setCurrentPrice(requestPrice);

        Bid bid = Bid.builder()
                .auction(auction)
                .status(BidStatus.ACTIVE)
                .amount(BigDecimal.valueOf(request.getAmount()))
                .bidder(user)
                .isWinning(false)
                .build();

        Bid savedBid = bidRepository.save(bid);
        return savedBid.getId();
    }

    public  List<BidDto> getBidListInAuction(Long auctionId) {
        List<Bid> list = bidRepository.findAllByAuction(auctionId);
        return toDtos(list);
    }

    public List<BidDto> getBidHistory(Long userId) {
        List<Bid> list = bidRepository.findAllByUserId(userId);
        return toDtos(list);
    }

    private List<BidDto> toDtos(List<Bid> list) {
        List<BidDto> resultList = new ArrayList<>();
        for (Bid bid: list) {
            BidDto dto = BidDto.builder()
                    .id(bid.getId())
                    .bidderId(bid.getBidder().getId())
                    .bidderName(bid.getBidder().getNickname())
                    .amount(bid.getAmount())
                    .build();
            resultList.add(dto);
        }
        return resultList;
    }
}
