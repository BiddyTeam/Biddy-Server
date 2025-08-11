package com.biddy.biddy_api.domain.auction;

import com.biddy.biddy_api.domain.auction.dto.BidDto;
import com.biddy.biddy_api.global.RspTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BidController {

    private final BidService bidService;

    @GetMapping("auction/history")
    public RspTemplate<List<BidDto>> getAuctionHistory(
            @RequestParam Long userId
    ) {
        List<BidDto> list = bidService.getAuctionHistory(userId);
        return new RspTemplate<>(HttpStatus.FOUND, "입찰 이력을 조회합니다.", list);
    }
}
