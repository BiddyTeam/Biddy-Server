package com.biddy.biddy_api.domain.ai.controller;

import com.biddy.biddy_api.domain.ai.dto.AutoWriteDto;
import com.biddy.biddy_api.domain.ai.dto.SmartPricingDto;
import com.biddy.biddy_api.domain.ai.service.AiService;
import com.biddy.biddy_api.global.RspTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@Tag(name = "AI", description = "AI 기반 자동화 기능")
public class AiController {

    private final AiService aiService;

    @PostMapping("/auto-write")
    @Operation(
            summary = "이미지 기반 자동 글쓰기",
            description = "업로드된 이미지들을 분석하여 상품 제목과 설명을 자동으로 생성합니다."
    )
    public ResponseEntity<RspTemplate<AutoWriteDto.AutoWriteResponse>> autoWrite(
            @RequestParam("images") List<MultipartFile> images) {

        if (images == null || images.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new RspTemplate<>(HttpStatus.BAD_REQUEST, "이미지가 필요합니다."));
        }

        AutoWriteDto.AutoWriteResponse response = aiService.generateAutoContent(images);

        if (response == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RspTemplate<>(HttpStatus.INTERNAL_SERVER_ERROR, "AI 분석에 실패했습니다."));
        }

        return ResponseEntity.ok(
                new RspTemplate<>(HttpStatus.OK, "자동 글쓰기가 완료되었습니다.", response)
        );
    }

    @PostMapping(value = "/smart-pricing", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "스마트 가격 책정",
            description = "상품 정보와 원하는 판매 기간을 기반으로 최적의 경매 가격을 책정합니다."
    )
    public ResponseEntity<RspTemplate<SmartPricingDto.PriceResponse>> smartPricing(
            @RequestPart("images") List<MultipartFile> images,
            @RequestPart("request") SmartPricingDto.SmartPricingRequest request) {

        if (images == null || images.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new RspTemplate<>(HttpStatus.BAD_REQUEST, "이미지가 필요합니다."));
        }

        if (request.getAuctionType() == null) {
            return ResponseEntity.badRequest()
                    .body(new RspTemplate<>(HttpStatus.BAD_REQUEST, "경매 타입을 선택해주세요."));
        }

        SmartPricingDto.PriceResponse response = aiService.generateSmartPricing(
                images,
                request.getTitle(),
                request.getDescription(),
                request.getCondition(),
                request.getAuctionType()
        );

        if (response == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RspTemplate<>(HttpStatus.INTERNAL_SERVER_ERROR, "AI 가격 분석에 실패했습니다."));
        }

        return ResponseEntity.ok(
                new RspTemplate<>(HttpStatus.OK, "스마트 가격 책정이 완료되었습니다.", response)
        );
    }
}
