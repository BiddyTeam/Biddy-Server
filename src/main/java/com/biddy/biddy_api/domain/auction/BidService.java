package com.biddy.biddy_api.domain.auction;

import com.biddy.biddy_api.domain.auction.dto.AuctionDto;
import com.biddy.biddy_api.domain.auction.dto.BidDto;
import com.biddy.biddy_api.domain.auction.dto.PostBidRequest;
import com.biddy.biddy_api.domain.auction.entity.Auction;
import com.biddy.biddy_api.domain.auction.entity.Bid;
import com.biddy.biddy_api.domain.auction.enums.BidStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BidService {

    private final BidRepository bidRepository;
    private final AuctionRepository auctionRepository;

    public Long postBidInAuction(PostBidRequest request) throws RuntimeException {
        Auction auction = auctionRepository.getAuctionById(request.getAuctionId()).orElseThrow();
        Bid bid = Bid.builder()
                .auction(auction)
                .status(BidStatus.ACTIVE)
                .amount(BigDecimal.valueOf(request.getAmount()))
                .bidder(null) // 수정 필요
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
                    .auctionDto(new AuctionDto()) // 수정 필요
                    .bidderId(bid.getBidder().getId())
                    .bidderName(bid.getBidder().getNickname())
                    .amount(bid.getAmount())
                    .status(bid.getStatus())
                    .isWinning(bid.getIsWinning())
                    .build();
            resultList.add(dto);
        }
        return resultList;
    }
}
