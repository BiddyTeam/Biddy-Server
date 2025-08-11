package com.biddy.biddy_api.domain.transaction.enums;

public enum TransactionStatus {
    PENDING("결제대기"),
    PAID("결제완료"),
    SHIPPED("배송중"),
    DELIVERED("배송완료"),
    COMPLETED("거래완료"),
    CANCELLED("취소"),
    REFUNDED("환불완료");

    private final String description;

    TransactionStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
