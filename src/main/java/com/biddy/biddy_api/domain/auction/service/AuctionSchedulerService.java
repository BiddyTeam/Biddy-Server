package com.biddy.biddy_api.domain.auction.service;

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
    private final AuctionProcessingService auctionProcessingService;

//    @Scheduled(fixedRate = 60000)
    public void updateAuctionStatuses() {
//        LocalDateTime now = LocalDateTime.now();
//
//        int startedCount = auctionRepository.activateScheduledAuctions(now);
//        List<Long> idsToProcess = auctionRepository.findIdsOfActiveAuctionsEndedBefore(now);
//
//        int endedCount = auctionRepository.endExpiredAuctions(now);
//        auctionProcessingService.processBidsForEndedAuctions(idsToProcess);
//
//        if (startedCount > 0 || endedCount > 0) {
//            log.info("경매 상태 업데이트 완료 - 시작: {}개, 종료: {}개 (후처리 대상)", startedCount, endedCount);
//        }
    }
}
