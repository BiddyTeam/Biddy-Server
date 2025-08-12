package com.biddy.biddy_api.domain.user.controller;

import com.biddy.biddy_api.domain.user.service.UserQueryService;
import com.biddy.biddy_api.global.RspTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
@Tag(name = "User", description = "회원 API")
public class UserController {

    private final UserQueryService userQueryService;

    @GetMapping("/profile")
    @Operation(summary = "마이페이지 프로필 조회",
            description = "사용자 기본 정보, 비디코인 잔액, 수익/지출 정보를 조회합니다.")
    public RspTemplate<MyPageProfileDto> getMyProfile() {
        Long userId = 1L;
        MyPageProfileDto profile = userQueryService.getMyProfile(userId);
        return new RspTemplate<>(HttpStatus.OK, "프로필 정보를 조회했습니다.", profile);
    }
}
