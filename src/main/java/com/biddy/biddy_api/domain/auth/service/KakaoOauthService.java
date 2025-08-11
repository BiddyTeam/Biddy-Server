package com.biddy.biddy_api.domain.auth.service;

import com.biddy.biddy_api.domain.auth.client.KakaoClient;
import com.biddy.biddy_api.domain.user.entity.User;
import com.biddy.biddy_api.global.jwt.domain.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class KakaoOauthService {

    private final KakaoClient kakaoClient;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    private static final long EXPIRED = 3 * 24 * 60 * 60 * 1000L;

    public KakaoLoginResponseDTO signup(String code) {
        String kakaoAccessToken = kakaoClient.getAccessToken(code);
        KakaoClient.KakaoClientInfo info = kakaoClient.getClientInfo(kakaoAccessToken);

        Optional<User> optionalUser = userRepository.findByKakaoId(info.kakaoId());
        boolean isNewUser = optionalUser.isEmpty();

        User user = optionalUser.orElseGet(() ->
                userRepository.save(User.builder()
                        .kakaoId(info.kakaoId())
                        .nickname(info.nickname())
                        .build())
        );

        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getNickname());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId(), user.getNickname());

        // 5. refresh는 db에 저장
        user.setRefreshToken(refreshToken);

        // jwt개발할 때 넣기
        return new KakaoLoginResponseDTO(accessToken, refreshToken, user.getId(), user.getNickname(), newuser);
    }
}
