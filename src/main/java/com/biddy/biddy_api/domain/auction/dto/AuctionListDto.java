package com.biddy.biddy_api.domain.auction.dto;

import com.biddy.biddy_api.domain.auction.enums.AuctionStatus;
import com.biddy.biddy_api.domain.auction.enums.ProductCategory;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class AuctionListDto {
    private Long id;
    private String title;
    private BigDecimal startPrice;
    private BigDecimal currentPrice;
    private BigDecimal buyNowPrice;
    private LocalDateTime endTime;
    private AuctionStatus status;
    private ProductCategory category;
    private String thumbnailImage;
    private String sellerNickname;
    private Integer bidCount;
}
