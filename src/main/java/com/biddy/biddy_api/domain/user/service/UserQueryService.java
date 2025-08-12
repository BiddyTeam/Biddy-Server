package com.biddy.biddy_api.domain.user.service;

import com.biddy.biddy_api.domain.auction.entity.Auction;
import com.biddy.biddy_api.domain.auction.entity.Bid;
import com.biddy.biddy_api.domain.auction.entity.Bookmark;
import com.biddy.biddy_api.domain.auction.repository.BidRepository;
import com.biddy.biddy_api.domain.auction.repository.BookmarkRepository;
import com.biddy.biddy_api.domain.user.dto.MyBookmarkDto;
import com.biddy.biddy_api.domain.user.dto.MyPageProfileDto;
import com.biddy.biddy_api.domain.user.dto.MyParticipatedAuctionDto;
import com.biddy.biddy_api.domain.user.entity.User;
import com.biddy.biddy_api.domain.user.repository.UserRepository;
import com.biddy.biddy_api.global.common.BaseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.biddy.biddy_api.domain.auction.enums.AuctionStatus.COMPLETED;
import static com.biddy.biddy_api.domain.auction.enums.BidStatus.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService {

    private final UserRepository userRepository;
    private final BidRepository bidRepository;
    private final BookmarkRepository bookmarkRepository;

    public MyPageProfileDto.MyPageProfileResponse getMyProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 비디코인 잔액
        BigDecimal bidCoinBalance = user.getWallet() != null ?
                user.getWallet().getBidCoin() : BigDecimal.ZERO;

        BigDecimal totalEarnings = calculateTotalEarnings(user);
        BigDecimal totalSpent = calculateTotalSpent(userId);

        return MyPageProfileDto.MyPageProfileResponse.builder()
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

    public List<MyParticipatedAuctionDto.MyParticipatedAuctionResponse> getMyParticipatedAuctions(Long userId) {
        List<Bid> myBids = bidRepository.findAllByUserId(userId);

        return myBids.stream()
                .collect(Collectors.groupingBy(bid -> bid.getAuction().getId()))
                .values()
                .stream()
                .map(bidsForAuction -> {
                    // 각 경매별로 최근 입찰만 가져오기
                    Bid latestBid = bidsForAuction.stream()
                            .max(Comparator.comparing(BaseEntity::getCreatedAt))
                            .orElse(bidsForAuction.get(0));

                    Auction auction = latestBid.getAuction();

                    String status = determineAuctionStatus(auction, latestBid);

                    return MyParticipatedAuctionDto.MyParticipatedAuctionResponse.builder()
                            .auctionId(auction.getId())
                            .title(auction.getTitle())
                            .thumbnailImage(getThumbnailImage(auction))
                            .myBidAmount(latestBid.getAmount())
                            .currentPrice(auction.getCurrentPrice())
                            .status(status)
                            .endTime(auction.getEndTime())
                            .isWinning(latestBid.getIsWinning())
                            .build();
                })
                .collect(Collectors.toList());
    }

    public List<MyBookmarkDto.MyBookmarkResponse> getMyBookmarks(Long userId) {

        List<Bookmark> bookmarks = bookmarkRepository.findByUserId(userId);

        return bookmarks.stream()
                .map(bookmark -> {
                    Auction auction = bookmark.getAuction();
                    return MyBookmarkDto.MyBookmarkResponse.builder()
                            .auctionId(auction.getId())
                            .title(auction.getTitle())
                            .thumbnailImage(getThumbnailImage(auction))
                            .currentPrice(auction.getCurrentPrice())
                            .endTime(auction.getEndTime())
                            .status(auction.getStatus().getDescription())
                            .bidCount(auction.getBids() != null ? auction.getBids().size() : 0)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private String getThumbnailImage(Auction auction) {
        return auction.getAuctionImages() != null && !auction.getAuctionImages().isEmpty()
                ? auction.getAuctionImages().get(0).getImageUrl()
                : null;
    }

    private String determineAuctionStatus(Auction auction, Bid myBid) {
        switch (auction.getStatus()) {
            case ACTIVE:
                return myBid.getIsWinning() ? "선두" : "입찰중";
            case ENDED:
                return myBid.getIsWinning() ? "낙찰" : "마감";
            case SCHEDULED:
                return "예정";
            default:
                return "기타";
        }
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
