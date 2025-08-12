package com.biddy.biddy_api.domain.auction.controller;

import com.biddy.biddy_api.domain.auction.dto.AuctionDto;
import com.biddy.biddy_api.domain.auction.service.AuctionCommandService;
import com.biddy.biddy_api.global.RspTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auctions")
@RequiredArgsConstructor
@Tag(name = "Auction", description = "경매 게시글 API")
public class AuctionController {

    private final AuctionCommandService auctionCommandService;

    @PostMapping
    @Operation(summary = "경매 게시글 등록", description = "새로운 경매 게시글을 등록합니다.")
    public RspTemplate<Long> createAuction(@RequestBody @Valid AuctionDto.AuctionCreateRequest request) {

        Long auctionId = auctionCommandService.createAuction(request, 1L);
        return new RspTemplate<>(HttpStatus.CREATED, "경매가 등록되었습니다.", auctionId);
    }
}
