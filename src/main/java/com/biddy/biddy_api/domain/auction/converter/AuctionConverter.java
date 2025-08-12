package com.biddy.biddy_api.domain.auction.converter;

import com.biddy.biddy_api.domain.auction.dto.AuctionListDto;
import com.biddy.biddy_api.domain.auction.entity.Auction;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuctionConverter {

    public List<AuctionListDto> toListDtos(List<Auction> auctions) {
        return auctions.stream()
                .map(this::toListDto)
                .collect(Collectors.toList());
    }

    public AuctionListDto toListDto(Auction auction) {
        // 첫 번째 이미지를 썸네일로 사용
        String thumbnailImage = auction.getAuctionImages() != null && !auction.getAuctionImages().isEmpty()
                ? auction.getAuctionImages().get(0).getImageUrl()
                : null;

        // 입찰 수 계산
        Integer bidCount = auction.getBids() != null ? auction.getBids().size() : 0;

        return AuctionListDto.builder()
                .id(auction.getId())
                .title(auction.getTitle())
                .startPrice(auction.getStartPrice())
                .currentPrice(auction.getCurrentPrice())
                .buyNowPrice(auction.getBuyNowPrice())
                .endTime(auction.getEndTime())
                .status(auction.getStatus())
                .category(auction.getCategory())
                .thumbnailImage(thumbnailImage)
                .sellerNickname(auction.getSeller().getNickname())
                .bidCount(bidCount)
                .build();
    }
}
