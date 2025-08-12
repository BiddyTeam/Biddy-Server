package com.biddy.biddy_api.domain.auction.controller;

import com.biddy.biddy_api.domain.auction.dto.AuctionCreateDto;
import com.biddy.biddy_api.domain.auction.dto.AuctionDto;
import com.biddy.biddy_api.domain.auction.dto.AuctionListDto;
import com.biddy.biddy_api.domain.auction.service.AuctionCommandService;
import com.biddy.biddy_api.domain.auction.service.AuctionQueryService;
import com.biddy.biddy_api.global.RspTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auctions")
@RequiredArgsConstructor
@Tag(name = "Auction", description = "경매 게시글 API")
public class AuctionController {

    private final AuctionCommandService auctionCommandService;
    private final AuctionQueryService auctionQueryService;

    @PostMapping
    @Operation(summary = "경매 게시글 등록", description = "새로운 경매 게시글을 등록합니다.")
    public RspTemplate<Long> createAuction(@RequestBody @Valid AuctionCreateDto.AuctionCreateRequest request) {
        Long auctionId = auctionCommandService.createAuction(request, 1L);
        return new RspTemplate<>(HttpStatus.CREATED, "경매가 등록되었습니다.", auctionId);
    }

    @GetMapping("/{auctionId}/member/{memberId}")
    @Operation(summary = "경매 상세 조회", description = "경매 상세 정보를 조회합니다.")
    public RspTemplate<AuctionDto> getAuction(
            @PathVariable Long memberId,
            @PathVariable Long auctionId
    ) {
        AuctionDto auction = auctionQueryService.getAuction(memberId, auctionId);
        return new RspTemplate<>(HttpStatus.FOUND, "경매 상세 정보 조회", auction);
    }
  
    @GetMapping
    @Operation(summary = "경매 목록 조회", description = "모든 경매 목록을 조회합니다.")
    public RspTemplate<List<AuctionListDto>> getAllAuctions() {
        List<AuctionListDto> auctions = auctionQueryService.getAllAuctions();
        return new RspTemplate<>(HttpStatus.OK, "경매 목록을 조회했습니다.", auctions);
    }
}
