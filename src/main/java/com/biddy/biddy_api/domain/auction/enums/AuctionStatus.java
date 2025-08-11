package com.biddy.biddy_api.domain.auction.enums;

public enum AuctionStatus {
    SCHEDULED("예정"),
    ACTIVE("진행중"),
    ENDED("종료"),
    CANCELLED("취소"),
    COMPLETED("완료");

    private final String description;

    AuctionStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
