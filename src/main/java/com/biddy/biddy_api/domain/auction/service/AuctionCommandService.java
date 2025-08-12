package com.biddy.biddy_api.domain.auction.service;

import com.biddy.biddy_api.domain.auction.dto.AuctionCreateDto;
import com.biddy.biddy_api.domain.auction.entity.Auction;
import com.biddy.biddy_api.domain.auction.entity.AuctionImage;
import com.biddy.biddy_api.domain.auction.entity.AuctionKeyword;
import com.biddy.biddy_api.domain.auction.enums.ProductCategory;
import com.biddy.biddy_api.domain.auction.repository.AuctionImageRepository;
import com.biddy.biddy_api.domain.auction.repository.AuctionKeywordRepository;
import com.biddy.biddy_api.domain.auction.repository.AuctionRepository;
import com.biddy.biddy_api.domain.auction.utils.AuctionTimeCalculator;
import com.biddy.biddy_api.domain.user.entity.User;
import com.biddy.biddy_api.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AuctionCommandService {

    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;
    private final AuctionImageRepository auctionImageRepository;
    private final AuctionKeywordRepository auctionKeywordRepository;
    private final AuctionTimeCalculator auctionTimeCalculator;

    public Long createAuction(AuctionCreateDto.AuctionCreateRequest request, Long userId) {
        User seller = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 카테고리 검증
        ProductCategory category;
        try {
            category = ProductCategory.fromString(request.getCategory());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("유효하지 않은 카테고리입니다: " + request.getCategory());
        }

        // 경매 유형에 따른 시간 계산
        LocalDateTime startTime = auctionTimeCalculator.calculateStartTime();
        LocalDateTime endTime = auctionTimeCalculator.calculateEndTime(request.getAuctionType());

        // 경매 생성
        Auction auction = Auction.create(seller, request, category, startTime, endTime);
        Auction savedAuction = auctionRepository.save(auction);

        // 이미지 저장
        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            List<AuctionImage> images = request.getImageUrls().stream()
                    .map(url -> AuctionImage.create(savedAuction, url))
                    .toList();
            auctionImageRepository.saveAll(images);
        }

        // 키워드 저장
        if (request.getKeywords() != null && !request.getKeywords().isEmpty()) {
            List<AuctionKeyword> keywords = request.getKeywords().stream()
                    .filter(keyword -> keyword != null && !keyword.trim().isEmpty())
                    .map(keyword -> AuctionKeyword.create(savedAuction, keyword))
                    .toList();
            auctionKeywordRepository.saveAll(keywords);
        }

        return savedAuction.getId();
    }
}
