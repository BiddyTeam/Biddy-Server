package com.biddy.biddy_api.domain.auction.utils;

import com.biddy.biddy_api.domain.auction.enums.AuctionType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AuctionTimeCalculator {

    public LocalDateTime calculateStartTime() {
        return LocalDateTime.now();
    }

    public LocalDateTime calculateEndTime(AuctionType auctionType) {
        LocalDateTime startTime = calculateStartTime();

        return switch (auctionType) {
            case LIGHTNING -> startTime.plusMinutes(30);        // 30분
            case QUICK -> startTime.plusHours(6);               // 6시간
            case HALF_DAY -> startTime.plusHours(12);           // 12시간
            case DAILY -> startTime.plusDays(1);                // 24시간
            case THREE_DAY -> startTime.plusDays(3);            // 3일
            case WEEKLY -> startTime.plusDays(7);               // 7일
        };
    }
}
