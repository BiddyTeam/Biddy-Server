package com.biddy.biddy_api.domain.auction.dto;

import com.biddy.biddy_api.domain.auction.entity.Auction;
import com.biddy.biddy_api.domain.auction.enums.AuctionStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BookmarkedAuctionDto {
    private Long auctionId;
    private String title;
    private String currentPrice;
    private LocalDateTime endTime;
    private AuctionStatus status;
    private String thumbnailUrl; // 경매 대표 이미지

    // Auction 엔티티를 이 DTO로 변환하는 정적 메서드
    public static BookmarkedAuctionDto from(Auction auction) {
        // 대표 이미지가 없을 경우를 대비한 null 처리
        String imageUrl = (auction.getAuctionImages() != null && !auction.getAuctionImages().isEmpty())
                ? auction.getAuctionImages().get(0).getImageUrl()
                : null; // 혹은 기본 이미지 URL

        return BookmarkedAuctionDto.builder()
                .auctionId(auction.getId())
                .title(auction.getTitle())
                .currentPrice(auction.getCurrentPrice().toString())
                .endTime(auction.getEndTime())
                .status(auction.getStatus())
                .thumbnailUrl(imageUrl)
                .build();
    }
}