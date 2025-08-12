package com.biddy.biddy_api.domain.auction.entity;

import com.biddy.biddy_api.domain.auction.dto.AuctionCreateDto.AuctionCreateRequest;
import com.biddy.biddy_api.domain.auction.dto.AuctionDto;
import com.biddy.biddy_api.domain.auction.enums.AuctionStatus;
import com.biddy.biddy_api.domain.auction.enums.ProductCategory;
import com.biddy.biddy_api.domain.auction.enums.ProductCondition;
import com.biddy.biddy_api.domain.user.entity.User;
import com.biddy.biddy_api.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "auctions")
public class Auction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal startPrice;

    @Column(precision = 15, scale = 2)
    private BigDecimal buyNowPrice; // 즉시구매가

    @Setter
    @Column(precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal currentPrice = BigDecimal.ZERO;

    @Column(precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal bidIncrement = new BigDecimal("1000"); // 최소 입찰 단위

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Setter
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private AuctionStatus status = AuctionStatus.SCHEDULED;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductCategory category;

    @Enumerated(EnumType.STRING)
    private ProductCondition condition;

    @OneToMany(mappedBy = "auction", cascade = CascadeType.ALL)
    private List<Bid> bids;

    @OneToMany(mappedBy = "auction")
    private List<Bookmark> bookmarks;

    @OneToMany(mappedBy = "auction")
    private List<AuctionImage> auctionImages;

    public static Auction create(User seller, AuctionCreateRequest request, ProductCategory category) {
        return Auction.builder()
                .seller(seller)
                .title(request.getTitle())
                .description(request.getDescription())
                .startPrice(request.getStartPrice())
                .buyNowPrice(request.getBuyNowPrice())
                .currentPrice(request.getStartPrice())
                .bidIncrement(request.getBidIncrement() != null ? request.getBidIncrement() : new BigDecimal("1000"))
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .status(request.getStartTime().isAfter(LocalDateTime.now()) ?
                        AuctionStatus.SCHEDULED : AuctionStatus.ACTIVE)
                .category(category)
                .condition(request.getCondition() != null ?
                        ProductCondition.valueOf(request.getCondition().toUpperCase()) : null)
                .build();
    }

    public AuctionDto toDto(Long userId) {
        AuctionDto dto = new AuctionDto();
        dto.setSellerId(this.seller.getId());
        dto.setTitle(this.title);
        dto.setDescription(this.description);
        dto.setStartPrice(this.startPrice.toString());
        dto.setBuyNowPrice(this.buyNowPrice != null ? this.buyNowPrice.toString() : null);
        dto.setCurrentPrice(this.currentPrice.toString());
        dto.setBidIncrement(this.bidIncrement != null ? this.bidIncrement.toString() : null);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        dto.setStartTime(this.startTime.format(formatter));
        dto.setEndTime(this.endTime.format(formatter));

        dto.setAuctionStatus(this.status);
        dto.setProductCategory(this.category);
        dto.setProductCondition(this.condition);

        // Bid 리스트를 BidDto 리스트로 변환 (Bid 클래스에 toDto() 메서드가 있다고 가정)
        if (this.bids != null) {
            // dto.setBidDtoList(this.bids.stream().map(Bid::toDto).collect(Collectors.toList()));
        }

        // 현재 사용자가 북마크했는지 여부 확인
        if (this.bookmarks != null && userId != null) {
            dto.setIsBookmarks(this.bookmarks.stream().anyMatch(b -> b.getUser().getId().equals(userId)));
        } else {
            dto.setIsBookmarks(false);
        }

        return dto;
    }
}
