package com.biddy.biddy_api.domain.auction.enums;

public enum AuctionType {
    LIGHTNING("30분", "번개 경매", "초스피드 거래를 원하는 급매 상황"),
    QUICK("6시간", "퀵 경매", "당일 내 빠른 거래 희망"),
    HALF_DAY("12시간", "반일 경매", "반나절 동안 적당한 경매 진행"),
    DAILY("24시간", "데일리 경매", "하루 동안 안정적 경매 진행"),
    THREE_DAY("3일", "3일 경매", "주말 포함하여 충분한 입찰 기회"),
    WEEKLY("7일", "위클리 경매", "일주일간 여유롭게 최고가 추구");

    private final String duration;
    private final String displayName;
    private final String description;

    AuctionType(String duration, String displayName, String description) {
        this.duration = duration;
        this.displayName = displayName;
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    // 프론트엔드에서 사용할 수 있는 추가 정보
    public int getDurationInMinutes() {
        return switch (this) {
            case LIGHTNING -> 30;
            case QUICK -> 6 * 60;          // 6시간
            case HALF_DAY -> 12 * 60;      // 12시간
            case DAILY -> 24 * 60;         // 24시간
            case THREE_DAY -> 3 * 24 * 60; // 3일
            case WEEKLY -> 7 * 24 * 60;    // 7일
        };
    }

    // 경매 타입별 추천 상황
    public String getRecommendedFor() {
        return switch (this) {
            case LIGHTNING -> "급하게 현금이 필요한 경우";
            case QUICK -> "당일 거래를 원하는 경우";
            case HALF_DAY -> "적당한 시간 동안 경매를 진행하고 싶은 경우";
            case DAILY -> "안정적으로 하루 동안 경매를 진행하고 싶은 경우";
            case THREE_DAY -> "주말을 포함해 충분한 시간을 두고 싶은 경우";
            case WEEKLY -> "최고가를 받기 위해 여유롭게 진행하고 싶은 경우";
        };
    }

    // 경매 타입별 예상 경쟁 강도
    public String getCompetitionLevel() {
        return switch (this) {
            case LIGHTNING -> "낮음 (시간 부족으로 참여자 제한)";
            case QUICK -> "보통 (당일 관심자들만 참여)";
            case HALF_DAY -> "보통 (적당한 참여 시간)";
            case DAILY -> "높음 (충분한 참여 시간)";
            case THREE_DAY -> "매우 높음 (주말 포함 많은 참여자)";
            case WEEKLY -> "최고 (일주일간 최대 노출)";
        };
    }
}
