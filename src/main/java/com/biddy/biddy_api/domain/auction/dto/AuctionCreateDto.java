package com.biddy.biddy_api.domain.auction.dto;

import com.biddy.biddy_api.domain.auction.enums.AuctionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

public class AuctionCreateDto {

    @Data
    @Builder
    @Schema(name = "AuctionCreateRequest", description = "경매 등록 요청")
    public static class AuctionCreateRequest {
        @NotBlank(message = "제목은 필수입니다")
        private String title;

        private String description;

        @NotNull(message = "시작가는 필수입니다")
        @Positive(message = "시작가는 0보다 커야 합니다")
        private BigDecimal startPrice;

        private BigDecimal buyNowPrice;

        @Positive(message = "입찰 단위는 0보다 커야 합니다")
        private BigDecimal bidIncrement;

        @NotBlank(message = "카테고리는 필수입니다")
        private String category;

        private String condition;

        private List<String> imageUrls;

        @NotNull(message = "경매 유형은 필수입니다")
        private AuctionType auctionType;

        private List<String> keywords;
    }
}
