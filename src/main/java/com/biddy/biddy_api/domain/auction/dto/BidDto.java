package com.biddy.biddy_api.domain.auction.dto;

import com.biddy.biddy_api.domain.auction.enums.BidStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BidDto {
    private Long id;
    private AuctionDto auctionDto;
    private Long bidderId;
    private String bidderName;
    private BigDecimal amount;
    private BidStatus status;
    private Boolean isWinning;
}
