package com.biddy.biddy_api.domain.auction.dto;

import com.biddy.biddy_api.domain.auction.enums.AuctionStatus;
import com.biddy.biddy_api.domain.auction.enums.AuctionType;
import com.biddy.biddy_api.domain.auction.enums.ProductCategory;
import com.biddy.biddy_api.domain.auction.enums.ProductCondition;
import lombok.Data;

import java.util.List;

@Data
public class AuctionDto {
    public Long sellerId;

    public String title;
    public String description;
    public String startPrice;
    public String buyNowPrice;
    public String currentPrice;
    public String bidIncrement;
    public String startTime;
    public String endTime;

    public AuctionType auctionType;
    public AuctionStatus auctionStatus;

    public List<String> prouductImageUrls;
    public ProductCategory productCategory;
    public ProductCondition productCondition;

    public List<BidDto> bidDtoList;

    public Boolean isBookmarks;
}
