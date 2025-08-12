package com.biddy.biddy_api.domain.auction.entity;

import com.biddy.biddy_api.domain.user.entity.User;
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
@Table(name = "bookmarks")
public class Bookmark extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_id", nullable = false)
    private Auction auction;

    public Bookmark(User user, Auction auction) {
        this.user = user;
        this.auction = auction;
    }

    public void setOwner(User user, Auction auction) {
        // 1. 기존 연관관계가 있다면 제거 (안전성을 위해)
        if (this.user != null) {
            this.user.getBookmarks().remove(this);
        }
        if (this.auction != null) {
            this.auction.getBookmarks().remove(this);
        }

        // 2. 새로운 연관관계 설정
        this.user = user;
        this.auction = auction;

        // 3. 반대편 엔티티의 리스트에도 자신을 추가
        if (!user.getBookmarks().contains(this)) {
            user.getBookmarks().add(this);
        }
        if (!auction.getBookmarks().contains(this)) {
            auction.getBookmarks().add(this);
        }
    }
}
