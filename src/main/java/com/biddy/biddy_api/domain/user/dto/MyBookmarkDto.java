package com.biddy.biddy_api.domain.user.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MyBookmarkDto {

    @Data
    @Builder
    public static class MyBookmarkResponse {
        private Long auctionId;
        private String title;
        private String thumbnailImage;
        private BigDecimal currentPrice;
        private LocalDateTime endTime;
        private String status; // 진행중, 마감 등
        private Integer bidCount; // 입찰 수
    }
}
