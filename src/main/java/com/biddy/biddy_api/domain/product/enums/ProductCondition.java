package com.biddy.biddy_api.domain.product.enums;

public enum ProductCondition {
    NEW("새상품"),
    LIKE_NEW("거의새것"),
    VERY_GOOD("매우좋음"),
    GOOD("좋음"),
    ACCEPTABLE("보통");

    private final String description;

    ProductCondition(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
