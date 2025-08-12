package com.biddy.biddy_api.domain.auction.service;

import com.biddy.biddy_api.domain.auction.dto.AuctionCreateDto;
import com.biddy.biddy_api.domain.auction.entity.Auction;
import com.biddy.biddy_api.domain.auction.entity.AuctionImage;
import com.biddy.biddy_api.domain.auction.enums.AuctionStatus;
import com.biddy.biddy_api.domain.auction.enums.ProductCategory;
import com.biddy.biddy_api.domain.auction.enums.ProductCondition;
import com.biddy.biddy_api.domain.auction.repository.AuctionImageRepository;
import com.biddy.biddy_api.domain.auction.repository.AuctionRepository;
import com.biddy.biddy_api.domain.user.entity.User;
import com.biddy.biddy_api.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AuctionCommandService {

    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;
    private final AuctionImageRepository auctionImageRepository;

    public Long createAuction(AuctionCreateDto.AuctionCreateRequest request, Long userId) {
        User seller = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 시간 검증
        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new RuntimeException("시작 시간은 종료 시간보다 이전이어야 합니다.");
        }

        if (request.getStartTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("시작 시간은 현재 시간보다 이후여야 합니다.");
        }

        // 카테고리 검증
        ProductCategory category;
        try {
            category = ProductCategory.fromString(request.getCategory());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("유효하지 않은 카테고리입니다: " + request.getCategory());
        }

        // 경매 생성 (상품 정보 통합)
        Auction auction = Auction.builder()
                .seller(seller)
                .title(request.getTitle())
                .description(request.getDescription())
                .startPrice(request.getStartPrice())
                .buyNowPrice(request.getBuyNowPrice())
                .currentPrice(request.getStartPrice())
                .bidIncrement(request.getBidIncrement() != null ? request.getBidIncrement() : new BigDecimal("1000"))
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .status(request.getStartTime().isAfter(LocalDateTime.now()) ?
                        AuctionStatus.SCHEDULED : AuctionStatus.ACTIVE)
                .category(category)
                .condition(request.getCondition() != null ?
                        ProductCondition.valueOf(request.getCondition().toUpperCase()) : null)
                .build();

        Auction savedAuction = auctionRepository.save(auction);

        // 이미지 저장
        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            List<AuctionImage> images = request.getImageUrls().stream()
                    .map(url -> AuctionImage.builder()
                            .auction(savedAuction)
                            .imageUrl(url)
                            .build())
                    .toList();
            auctionImageRepository.saveAll(images);
        }

        return savedAuction.getId();
    }
}
