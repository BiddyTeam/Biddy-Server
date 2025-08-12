package com.biddy.biddy_api.domain.user.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MyParticipatedAuctionDto {

    @Data
    @Builder
    public static class MyParticipatedAuctionResponse {
        private Long auctionId;
        private String title;
        private String thumbnailImage;
        private BigDecimal myBidAmount; // 내 입찰가
        private BigDecimal currentPrice; // 현재가
        private String status; // 마감, 입찰중, 낙찰 등
        private LocalDateTime endTime;
        private Boolean isWinning; // 현재 내가 최고가인지
        private String badgeColor; // 입찰중, 선두, 마감 등에 따른 뱃지 색상
    }
}
