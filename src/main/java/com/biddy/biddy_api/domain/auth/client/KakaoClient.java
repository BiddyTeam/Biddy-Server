package com.biddy.biddy_api.domain.auth.client;

import com.biddy.biddy_api.domain.auth.properties.KakaoProperties;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Slf4j
@RequiredArgsConstructor
public class KakaoClient {

    private final WebClient webClient = WebClient.create();

    private final KakaoProperties kakaoProperties;

    // 인가코드로 AccessToken 요청하기
    public String getAccessToken(String code) {
        BodyInserters.FormInserter<String> formData = BodyInserters.fromFormData("grant_type", "authorization_code")
                .with("client_id", kakaoProperties.getClientId())
                .with("redirect_uri", kakaoProperties.getRedirectUri())
                .with("code", code);

        if (kakaoProperties.getClientSecret() != null && !kakaoProperties.getClientSecret().isBlank()) {
            formData.with("client_secret", kakaoProperties.getClientSecret());
        }

        log.info("Redirect URI used: {}", kakaoProperties.getRedirectUri());
        log.info("Auth Code: {}", code);
        JsonNode response = webClient.post()
                .uri(kakaoProperties.getTokenUri())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        return response.get("access_token").asText();
    }

    // access 토큰으로 사용자 정보 요청
    public KakaoClientInfo getClientInfo(String accessToken) {
        JsonNode response = webClient.get()
                .uri(kakaoProperties.getUserInfo())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        Long kakaoId = response.get("id").asLong();
        String nickname = response.path("properties").path("nickname").asText();

        return new KakaoClientInfo(kakaoId, nickname);
    }

    private record KakaoClientInfo(
            Long kakaoId,
            String nickname
    ) {
    }
}
