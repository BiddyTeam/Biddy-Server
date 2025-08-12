package com.biddy.biddy_api.domain.auction.service;

import com.biddy.biddy_api.domain.auction.dto.DescriptionCreateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class AuctionCommandService {

    private final AiService aiService;

    public DescriptionCreateDto.Response generateDescription(
            List<MultipartFile> images,
            DescriptionCreateDto.Request request) {

        log.info("AI 상품 설명 생성 요청 - 상품명: {}, 카테고리: {}",
                request.getProductName(), request.getProductCategory());

        // AI 서비스를 통해 설명 생성
        DescriptionCreateDto.Response response = aiService.generateProductDescription(images, request);

        log.info("AI 상품 설명 생성 완료 - 시작가: {}, 즉시구매가: {}",
                response.getStartPrice(), response.getBuyNowPrice());

        return response;
    }
}
