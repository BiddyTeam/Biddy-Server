package com.biddy.biddy_api.domain.auction.dto;

import com.biddy.biddy_api.domain.auction.enums.BidStatus;
import lombok.Data;

@Data
public class PostBidRequest {
    Long auctionId;
    Long bidderId;
    Long amount;
    BidStatus status;
}
