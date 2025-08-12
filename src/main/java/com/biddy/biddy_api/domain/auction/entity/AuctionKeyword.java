package com.biddy.biddy_api.domain.auction.entity;

import com.biddy.biddy_api.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "auction_keywords")
public class AuctionKeyword extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_id", nullable = false)
    private Auction auction;

    @Column(nullable = false)
    private String keyword;

    public static AuctionKeyword create(Auction auction, String keyword) {
        return AuctionKeyword.builder()
                .auction(auction)
                .keyword(keyword.trim())
                .build();
    }
}
