package com.biddy.biddy_api.domain.auth.controller;

import com.biddy.biddy_api.domain.auth.dto.KakaoLoginDTO;
import com.biddy.biddy_api.domain.auth.service.KakaoOauthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
@Validated
@Tag(name = "Auth", description = "인증 API")
public class AuthController {

    private final KakaoOauthService kakaoOauthService;

    @PostMapping("/login")
    @Operation(summary = "카카오 소셜 로그인 API",
            description = "카카오에서 인가코드를 발급받아 요청으로 넣어주세요. 기존 회원의 경우 로그인, 신규 회원의 경우 회원가입을 합니다.")
    public ResponseEntity<KakaoLoginDTO.Response> login(@RequestBody @Valid KakaoLoginDTO.Request requestDTO) {

        KakaoLoginDTO.Response response = kakaoOauthService.signup(requestDTO.code());

        ResponseCookie cookie = ResponseCookie.from("refreshToken", response.refreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofDays(7))
                .sameSite("None")
                .build();

        KakaoLoginDTO.Response body = KakaoLoginDTO.Response.builder()
                .accessToken(response.accessToken())
                .memberId(response.memberId())
                .nickname(response.nickname())
                .isNewUser(response.isNewUser())
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(body);
    }
}
