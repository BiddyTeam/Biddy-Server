package com.biddy.biddy_api.domain.auction.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class AuctionCreateDto {

    @Data
    @Builder
    @Schema(name = "AuctionCreateRequest", description = "경매 등록 요청")
    public class AuctionCreateRequest {
        @NotBlank(message = "제목은 필수입니다")
        private String title;

        private String description;

        @NotNull(message = "시작가는 필수입니다")
        @Positive(message = "시작가는 0보다 커야 합니다")
        private BigDecimal startPrice;

        private BigDecimal buyNowPrice;

        @Positive(message = "입찰 단위는 0보다 커야 합니다")
        private BigDecimal bidIncrement;

        @NotNull(message = "시작 시간은 필수입니다")
        private LocalDateTime startTime;

        @NotNull(message = "종료 시간은 필수입니다")
        private LocalDateTime endTime;

        @NotBlank(message = "카테고리는 필수입니다")
        private String category;

        private String condition;

        private List<String> imageUrls;
    }
}
