package com.biddy.biddy_api.domain.ai.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

public class AutoWriteDto {


    @Data
    @Builder
    public static class Response {
        private String title;                // AI가 생성한 상품 제목
        private String description;          // AI가 생성한 상품 설명
        private List<String> keyFeatures;   // 상품 주요 특징들
    }
}
