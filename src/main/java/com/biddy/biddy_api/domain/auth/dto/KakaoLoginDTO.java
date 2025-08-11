package com.biddy.biddy_api.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

public class KakaoLoginDTO {

    @Builder
    @Schema(name = "KakaoLoginRequest", description = "카카오 로그인 요청")
    public record Request(
            String code
    ){}

    @Builder
    public record Response(
            String accessToken,
            String refreshToken,
            Long memberId,
            String nickname,
            Boolean isNewUser
    ){}
}
