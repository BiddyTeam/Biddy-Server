package com.biddy.biddy_api.domain.auction.service;

import com.biddy.biddy_api.domain.auction.entity.Auction;
import com.biddy.biddy_api.domain.auction.enums.AuctionStatus;
import com.biddy.biddy_api.domain.auction.repository.AuctionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuctionSchedulerService {

    private final AuctionRepository auctionRepository;

    // 매 1분마다 실행
    @Scheduled(fixedRate = 60000) // 60초 = 60000ms
    public void updateAuctionStatuses() {
        LocalDateTime now = LocalDateTime.now();

        int startedCount = auctionRepository.activateScheduledAuctions(now);
        int endedCount = auctionRepository.endExpiredAuctions(now);

        if (startedCount > 0 || endedCount > 0) {
            log.info("경매 상태 업데이트 완료 - 시작: {}개, 종료: {}개", startedCount, endedCount);
        }
    }
}
