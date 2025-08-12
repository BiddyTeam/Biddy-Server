package com.biddy.biddy_api.domain.auction.controller;

import com.biddy.biddy_api.domain.auction.dto.DescriptionCreateDto;
import com.biddy.biddy_api.domain.auction.service.AuctionCommandService;
import com.biddy.biddy_api.global.RspTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/auction")
@RequiredArgsConstructor
@Validated
@Tag(name = "Auction", description = "경매 API")
public class AuctionController {

    private final AuctionCommandService auctionCommandService;

    @PostMapping("/product/description")
    @Operation(summary = "상품 설명 자동 생성", description = "상품 정보를 바탕으로 매력적인 설명을 AI가 생성합니다.")
    public RspTemplate<DescriptionCreateDto.Response> generateDescription(
            @RequestPart List<MultipartFile> images,
            @RequestPart DescriptionCreateDto.Request request
    ) {
        DescriptionCreateDto.Response response =
                auctionCommandService.generateDescription(images, request);
        return new RspTemplate<>(HttpStatus.CREATED, "상품 설명이 자동 생성되었습니다.", response);
    }
}
