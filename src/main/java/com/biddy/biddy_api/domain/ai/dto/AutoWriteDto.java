package com.biddy.biddy_api.domain.ai.dto;

import com.biddy.biddy_api.domain.auction.enums.AuctionType;
import com.biddy.biddy_api.domain.auction.enums.ProductCondition;
import lombok.Builder;
import lombok.Data;

import java.util.List;

public class AutoWriteDto {

    @Data
    public static class SmartPricingRequest {
        private String title;
        private String description;
        private ProductCondition condition;
        private AuctionType auctionType;
    }

    @Data
    @Builder
    public static class Response {
        private String title;                // AI가 생성한 상품 제목
        private String description;          // AI가 생성한 상품 설명
        private List<String> keyFeatures;   // 상품 주요 특징들
    }

    @Data
    @Builder
    public static class PriceResponse {
        private Integer marketPrice;                    // 시장 참고가
        private SelectedAuction selectedAuction;       // 선택된 경매 타입 정보
        private Analysis analysis;                      // 분석 정보
    }

    @Data
    @Builder
    public static class SelectedAuction {
        private String type;                           // 경매 타입 (LIGHTNING, QUICK 등)
        private String duration;                       // 경매 기간
        private Integer startPrice;                    // 시작가
        private Integer buyNowPrice;                   // 즉시구매가
        private String strategy;                       // 전략 설명
        private String expectedResult;                 // 예상 결과
    }

    @Data
    @Builder
    public static class Analysis {
        private String sellProbability;                // 판매 확률 (HIGH/MEDIUM/LOW)
        private String reasoning;                      // 분석 근거
        private String tips;                          // 판매 팁
        private String marketComparison;              // 시장 비교 분석
    }
}
