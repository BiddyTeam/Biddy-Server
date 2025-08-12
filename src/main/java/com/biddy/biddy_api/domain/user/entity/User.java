package com.biddy.biddy_api.domain.user.entity;

import com.biddy.biddy_api.domain.auction.entity.Auction;
import com.biddy.biddy_api.domain.auction.entity.Bookmark;
import com.biddy.biddy_api.domain.auction.entity.Bid;
import com.biddy.biddy_api.domain.user.enums.Grade;
import com.biddy.biddy_api.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @Enumerated(EnumType.STRING)
    private Grade grade;

    private String nickname;

    private Long kakaoId;

    private String refreshToken;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserAddress> addresses;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Wallet wallet;

    @OneToMany(mappedBy = "seller")
    private List<Auction> sellingAuctions;

    @OneToMany(mappedBy = "bidder")
    private List<Bid> bids;

    @OneToMany(mappedBy = "user")
    private List<Bookmark> bookmarks;

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
