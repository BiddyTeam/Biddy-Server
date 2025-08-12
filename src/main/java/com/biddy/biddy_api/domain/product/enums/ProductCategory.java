package com.biddy.biddy_api.domain.product.enums;

public enum ProductCategory {
    ELECTRONICS("전자기기"),
    INTERIOR("인테리어"),
    BOOK("도서"),
    FASHION("패션"),
    BEAUTY("뷰티"),
    WATCH("시계"),
    LIFESTYLE("생활용품"),
    PET("반려동물"),
    GIFT("교환권"),
    FOOD("직물");

    private final String description;

    ProductCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
