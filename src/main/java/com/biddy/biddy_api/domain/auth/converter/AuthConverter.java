package com.biddy.biddy_api.domain.auth.converter;

import com.biddy.biddy_api.domain.auth.dto.KakaoLoginDTO;
import org.springframework.stereotype.Component;

@Component
public class AuthConverter {

    public KakaoLoginDTO.Response toKakaoLoginResponse(
            String accessToken, String refreshToken, Long id, String nickname, boolean isNewUser){
        return KakaoLoginDTO.Response.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .memberId(id)
                .nickname(nickname)
                .isNewUser(isNewUser)
                .build();
    }
}
