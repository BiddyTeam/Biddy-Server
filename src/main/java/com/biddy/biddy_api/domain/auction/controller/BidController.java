package com.biddy.biddy_api.domain.auction.controller;

import com.biddy.biddy_api.domain.auction.service.BidService;
import com.biddy.biddy_api.domain.auction.dto.BidDto;
import com.biddy.biddy_api.domain.auction.dto.PostBidRequest;
import com.biddy.biddy_api.global.RspTemplate;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BidController {

    private final BidService bidService;

    @PostMapping("/auctions/{auctionId}/bids")
    @Operation(method = "Post", description = "입찰이 등록 되었습니다.")
    public RspTemplate<Long> postBid(
            @PathVariable Long auctionId,
            @RequestBody PostBidRequest request
    ) {
        Long id = bidService.postBidInAuction(auctionId, request);
        return new RspTemplate<>(HttpStatus.CREATED, "입찰이 등록 되었습니다.", id);
    }

    @GetMapping("/auctions/{auctionId}/bids")
    @Operation(method = "Get", description = "각 경매에 해당되는 입찰 이력을 조회합니다.")
    public RspTemplate<List<BidDto>> getBidListInAuction(
            @PathVariable Long auctionId
    ) {
        List<BidDto> list = bidService.getBidListInAuction(auctionId);
        return new RspTemplate<>(HttpStatus.FOUND, "경매에 해당되는 입찰 이력을 조회합니다.", list);
    }

    @GetMapping("/users/{userId}/bids")
    @Operation(method = "Get", description = "사용자가 입찰했던 이력을 조회합니다.")
    public RspTemplate<List<BidDto>> getBidHistory(
            @PathVariable Long userId
    ) {
        List<BidDto> list = bidService.getBidHistory(userId);
        return new RspTemplate<>(HttpStatus.FOUND, "입찰 이력을 조회합니다.", list);
    }

    @PostMapping("/immediately-purchase/{auctionId}")
    @Operation(method = "Post", description = "즉시 구매합니다.")
    public RspTemplate<Boolean> postImmediatelyPurchase(
            @PathVariable Long auctionId
    ) {
        bidService.postImmediatelyPurchase(auctionId);
        return new RspTemplate<>(HttpStatus.OK, "즉시 구매 완료", true);
    }
}
