package com.biddy.biddy_api.global.jwt.domain;

import com.biddy.biddy_api.domain.user.entity.User;
import com.biddy.biddy_api.global.jwt.properties.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {

    private Key key;
    private final JwtProperties jwtProperties;
    private final UserRepository userRepository;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64URL.decode(jwtProperties.getSecret());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(Long userId, String nickname) {
        return createToken(userId, nickname, jwtProperties.getAccessTokenValidity());
    }

    public String createRefreshToken(Long userId, String nickname) {
        return createToken(userId, nickname, jwtProperties.getRefreshTokenValidity());
    }

    private String createToken(Long userId, String nickname, long validity) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(userId));

        if (nickname == null) {
            throw new RuntimeException("nickname is null");
        }

        claims.put("nickname", nickname);

        Date now = new Date();
        Date expiration = new Date(now.getTime() + validity);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (io.jsonwebtoken.security.SignatureException exception) {
            log.error("JWT signature validation fails");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        } catch (Exception exception) {
            log.error("JWT validation fails", exception);
        }
        return false;
    }

    public boolean isTokenExpiringSoon(String refreshToken, long thresholdMillis) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody();

            Date expiration = claims.getExpiration();
            long now = System.currentTimeMillis();

            return expiration.getTime() - now < thresholdMillis;
        } catch (JwtException e) {
            throw new RuntimeException();
        }
    }

    public Long getUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.valueOf(claims.getSubject());
    }

    public Authentication getAuthentication(String token) {
        Long userId = getUserId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(new RuntimeException());

        UserDetails userDetails = new CustomUserDetails(user.getId(), user.getNickname());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
