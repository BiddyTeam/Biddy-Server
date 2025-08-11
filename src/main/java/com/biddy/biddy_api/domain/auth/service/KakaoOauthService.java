package com.biddy.biddy_api.domain.auth.service;

import com.biddy.biddy_api.domain.auth.client.KakaoClient;
import com.biddy.biddy_api.domain.auth.converter.AuthConverter;
import com.biddy.biddy_api.domain.auth.dto.KakaoLoginDTO;
import com.biddy.biddy_api.domain.user.entity.User;
import com.biddy.biddy_api.domain.user.repository.UserRepository;
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
    private final JwtTokenProvider jwtTokenProvider;

    private final UserRepository userRepository;

    private final AuthConverter authConverter;

    private static final long EXPIRED = 3 * 24 * 60 * 60 * 1000L;

    public KakaoLoginDTO.Response signup(String code) {
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

        user.setRefreshToken(refreshToken);

        return authConverter.toKakaoLoginResponse(accessToken, refreshToken, user.getId(), user.getNickname(), isNewUser);
    }
}
