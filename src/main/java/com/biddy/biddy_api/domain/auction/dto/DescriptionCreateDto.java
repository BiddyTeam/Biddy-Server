package com.biddy.biddy_api.domain.auction.dto;

import com.biddy.biddy_api.domain.product.enums.ProductCategory;
import com.biddy.biddy_api.domain.product.enums.ProductCondition;
import io.swagger.v3.oas.annotations.media.Schema;

public class DescriptionCreateDto {

    @Schema(name = "DescritpionCreateRequest", description = "상품 설명 생성 요청")
    public static class Request {
        private String productName;
        private ProductCategory productCategory;
        private ProductCondition condition;
        private Integer auctionPeriod;
        private String userDescription;
    }

    public static class Response {
        private String description;
        private Integer startPrice;
        private Integer endPrice;
        private Integer buyNowPrice;
    }
}
