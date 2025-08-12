package com.biddy.biddy_api.domain.ai.dto;

import com.biddy.biddy_api.domain.auction.enums.ProductCategory;
import lombok.Builder;
import lombok.Data;

import java.util.List;

public class AutoWriteDto {

    @Data
    @Builder
    public static class AutoWriteResponse {
        private String title;
        private String description;
        private List<String> keyFeatures;
        private ProductCategory detectedCategory;
    }
}
