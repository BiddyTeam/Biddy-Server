package com.biddy.biddy_api.domain.auction.enums;

public enum ProductCondition {
    NEW("새상품"),
    USED("중고상품");

    private final String description;

    ProductCondition(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
