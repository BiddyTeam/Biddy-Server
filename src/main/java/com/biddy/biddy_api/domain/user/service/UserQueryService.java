package com.biddy.biddy_api.domain.user.service;

import com.biddy.biddy_api.domain.auction.entity.Auction;
import com.biddy.biddy_api.domain.auction.entity.Bid;
import com.biddy.biddy_api.domain.auction.repository.BidRepository;
import com.biddy.biddy_api.domain.auction.repository.BookmarkRepository;
import com.biddy.biddy_api.domain.user.entity.User;
import com.biddy.biddy_api.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static com.biddy.biddy_api.domain.auction.enums.AuctionStatus.COMPLETED;
import static com.biddy.biddy_api.domain.auction.enums.BidStatus.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService {

    private final UserRepository userRepository;
    private final BidRepository bidRepository;

    public MyPageProfileDto getMyProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 비디코인 잔액
        BigDecimal bidCoinBalance = user.getWallet() != null ?
                user.getWallet().getBidCoin() : BigDecimal.ZERO;

        BigDecimal totalEarnings = calculateTotalEarnings(user);
        BigDecimal totalSpent = calculateTotalSpent(userId);

        return MyPageProfileDto.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .memberGrade(user.getGrade())
                .joinDate(user.getCreatedAt())
                .bidCoinBalance(bidCoinBalance)
                .totalEarnings(totalEarnings)
                .totalSpent(totalSpent)
                .build();
    }

    private BigDecimal calculateTotalEarnings(User user) {
        // 내가 판매자인 완료된 거래의 총 수익
        return user.getSellingAuctions().stream()
                .filter(auction -> auction.getStatus() == COMPLETED)
                .map(Auction::getCurrentPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateTotalSpent(Long userId) {
        // 내가 낙찰받은 경매의 총 지출
        return bidRepository.findAllByUserId(userId).stream()
                .filter(bid -> bid.getStatus() == WINNING)
                .map(Bid::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
