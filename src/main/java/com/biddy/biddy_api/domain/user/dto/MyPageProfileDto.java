package com.biddy.biddy_api.domain.user.dto;

import com.biddy.biddy_api.domain.user.enums.Grade;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public class MyPageProfileDto {

    @Data
    @Builder
    public static class MyPageProfileResponse {
        private Long userId;
        private String nickname;
        private String email;
        private Grade memberGrade;
        private LocalDateTime joinDate;
        private BigDecimal bidCoinBalance;
        private BigDecimal totalEarnings;
        private BigDecimal totalSpent;
    }
}
