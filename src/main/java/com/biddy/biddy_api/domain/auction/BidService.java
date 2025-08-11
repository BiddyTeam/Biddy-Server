package com.biddy.biddy_api.domain.auction;

import com.biddy.biddy_api.domain.auction.dto.AuctionDto;
import com.biddy.biddy_api.domain.auction.dto.BidDto;
import com.biddy.biddy_api.domain.auction.entity.Bid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BidService {

    private final BidRepository bidRepository;

    public List<BidDto> getAuctionHistory(Long userId) {
        List<Bid> list = bidRepository.findAllByUserId(userId);
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
