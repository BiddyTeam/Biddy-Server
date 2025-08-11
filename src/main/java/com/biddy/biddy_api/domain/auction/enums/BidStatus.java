package com.biddy.biddy_api.domain.auction.enums;

public enum BidStatus {
    ACTIVE("활성"),
    OUTBID("경합패배"),
    WINNING("낙찰"),
    CANCELLED("취소");

    private final String description;

    BidStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
