package com.biddy.biddy_api.domain.ai.service;

import com.biddy.biddy_api.domain.ai.dto.AutoWriteDto;
import com.biddy.biddy_api.domain.auction.enums.AuctionType;
import com.biddy.biddy_api.domain.product.enums.ProductCondition;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.content.Media;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiService {

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    /**
     * 이미지 기반 자동 글쓰기 기능
     * 이미지만으로 상품명과 설명을 자동 생성
     */
    public AutoWriteDto.Response generateAutoContent(List<MultipartFile> images) {
        try {
            // 이미지를 Media 객체로 변환
            List<Media> mediaList = convertImagesToMedia(images);
            Media[] mediaArray = mediaList.toArray(new Media[0]);

            String autoWritePrompt = buildAutoWritePrompt();

            String aiResponse = chatClient.prompt()
                    .user(userSpec -> userSpec
                            .text(autoWritePrompt)
                            .media(mediaArray))
                    .call()
                    .content();

            log.info("AI 자동 글쓰기 응답: {}", aiResponse);
            return parseAutoWriteResponse(aiResponse);

        } catch (Exception e) {
            log.error("AI 자동 글쓰기 실패: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 조건별 가격 책정 기능
     * 상품 상태와 선택한 경매 타입에 맞춘 가격 설정
     */
    public AutoWriteDto.PriceResponse generateSmartPricing(
            List<MultipartFile> images,
            String productTitle,
            String description,
            ProductCondition condition,
            AuctionType auctionType) {

        try {
            List<Media> mediaList = convertImagesToMedia(images);
            Media[] mediaArray = mediaList.toArray(new Media[0]);

            String pricingPrompt = buildSmartPricingPrompt(productTitle, description, condition, auctionType);

            String aiResponse = chatClient.prompt()
                    .user(userSpec -> userSpec
                            .text(pricingPrompt)
                            .media(mediaArray))
                    .call()
                    .content();

            log.info("AI 스마트 가격 책정 응답: {}", aiResponse);
            return parseSmartPricingResponse(aiResponse);

        } catch (Exception e) {
            log.error("AI 스마트 가격 책정 실패: {}", e.getMessage());
            return null;
        }
    }

    private List<Media> convertImagesToMedia(List<MultipartFile> images) {
        List<Media> mediaList = new ArrayList<>();

        for (MultipartFile image : images) {
            try {
                ByteArrayResource resource = new ByteArrayResource(image.getBytes());
                Media media = new Media(org.springframework.util.MimeTypeUtils.IMAGE_JPEG, resource);
                mediaList.add(media);
            } catch (IOException e) {
                log.warn("이미지 변환 실패: {}", e.getMessage());
            }
        }

        return mediaList;
    }

    private String buildAutoWritePrompt() {
        return """
                이미지를 보고 상품 정보를 JSON으로 작성해주세요.
                
                {
                  "title": "매력적인 상품 제목",
                  "description": "상품 설명 (200자 내외)",
                  "detectedCategory": "카테고리",
                  "detectedCondition": "NEW|LIKE_NEW|VERY_GOOD|GOOD|ACCEPTABLE",
                  "keyFeatures": ["특징1", "특징2"],
                  "estimatedValue": "예상가격"
                }
                """;
    }

    private String buildSmartPricingPrompt(String title, String description, ProductCondition condition, AuctionType auctionType) {
        return String.format("""
                당신은 한국 중고거래 시장의 전문 분석가입니다.
                제공된 상품 정보와 이미지를 분석하여 선택된 경매 타입에 최적화된 현실적인 가격을 책정해주세요.
                
                ## 상품 정보
                - 제목: %s
                - 설명: %s
                - 상품 상태: %s
                - 선택된 경매 타입: %s (%s)
                
                ## 분석 요청사항
                
                ### 1. 이미지 분석
                - 상품의 실제 상태를 세밀하게 분석
                - 브랜드, 모델, 색상, 크기 등 상품 정보 파악
                - 손상, 사용감, 부속품 여부 등 상태 평가
                
                ### 2. 시장가 분석
                - 중고나라, 당근마켓, 번개장터 등 주요 플랫폼 기준
                - 동일/유사 상품의 최근 실제 거래가 분석
                - 상품 상태, 브랜드 프리미엄 등을 고려한 시장가 추정
                
                ### 3. 경매 타입별 전략 분석
                선택된 경매 타입의 특성을 분석하여 최적 가격 책정:
                
                **%s (%s) 경매의 특성:**
                - 경매 기간이 구매자 심리에 미치는 영향 분석
                - 해당 기간 동안의 예상 입찰자 수와 경쟁 강도
                - 빠른 판매 vs 최고가 중 어느 쪽에 유리한지 판단
                - 유사 상품의 해당 기간 경매 성공 사례 분석
                
                ## 응답 형식 (반드시 JSON으로)
                {
                  "marketPrice": 분석된_실제_시장가_숫자만,
                  "selectedAuction": {
                    "type": "%s",
                    "duration": "%s", 
                    "startPrice": AI가_분석한_최적_시작가_숫자만,
                    "buyNowPrice": AI가_분석한_최적_즉시구매가_숫자만,
                    "strategy": "해당 경매 타입에 최적화된 가격 전략 설명",
                    "expectedResult": "예상되는 경매 결과와 최종 낙찰가 예측"
                  },
                  "analysis": {
                    "sellProbability": "HIGH|MEDIUM|LOW",
                    "reasoning": "가격 책정의 구체적 근거 (시장 데이터, 상품 상태, 경매 특성 등)",
                    "tips": "해당 경매에서 성공적 판매를 위한 구체적 조언",
                    "marketComparison": "유사 상품 거래 사례와의 비교 분석"
                  }
                }
                
                ## 중요한 분석 기준
                - 실제 한국 중고거래 시장의 거래 패턴 반영
                - 상품 카테고리별 수요와 공급 상황 고려
                - 계절성, 시기적 요인 (연말, 새학기 등) 반영
                - 경매 기간에 따른 구매자 행동 패턴 분석
                - 브랜드별 중고 시장에서의 가치 하락률 고려
                
                **절대 피해야 할 것:**
                - 단순한 퍼센트 계산이 아닌 실제 시장 분석 기반 가격 책정
                - 이론적 가격이 아닌 실제 팔릴 수 있는 현실적 가격
                - 상품 이미지에서 보이지 않는 내용 추측하지 말기
                """,
                title,
                description,
                condition.getDescription(),
                auctionType.getDisplayName(),
                auctionType.getDuration(),
                auctionType.getDisplayName(),
                auctionType.getDuration(),
                auctionType.name(),
                auctionType.getDuration()
        );
    }

    private AutoWriteDto.Response parseAutoWriteResponse(String aiResponse) {
        try {
            String jsonPart = extractJsonFromResponse(aiResponse);
            if (jsonPart == null) {
                return null;
            }

            JsonNode jsonNode = objectMapper.readTree(jsonPart);

            String title = jsonNode.path("title").asText("");
            String description = jsonNode.path("description").asText("");

            List<String> keyFeatures = new ArrayList<>();
            JsonNode featuresNode = jsonNode.path("keyFeatures");
            if (featuresNode.isArray()) {
                featuresNode.forEach(feature -> keyFeatures.add(feature.asText()));
            }

            if (title.isEmpty() || description.isEmpty()) {
                log.warn("AI 자동 글쓰기 응답 데이터 불완전");
                return null;
            }

            log.info("AI 자동 글쓰기 완료 - 제목: {}", title);

            return AutoWriteDto.Response.builder()
                    .title(title)
                    .description(description)
                    .keyFeatures(keyFeatures)
                    .build();

        } catch (Exception e) {
            log.error("자동 글쓰기 JSON 파싱 실패: {}", e.getMessage());
            return null;
        }
    }

    private AutoWriteDto.PriceResponse parseSmartPricingResponse(String aiResponse) {
        try {
            String jsonPart = extractJsonFromResponse(aiResponse);
            if (jsonPart == null) {
                return null;
            }

            JsonNode jsonNode = objectMapper.readTree(jsonPart);

            Integer marketPrice = jsonNode.path("marketPrice").asInt(0);
            JsonNode selectedAuctionNode = jsonNode.path("selectedAuction");
            JsonNode analysisNode = jsonNode.path("analysis");

            if (marketPrice == 0 || selectedAuctionNode.isMissingNode()) {
                log.warn("AI 스마트 가격 책정 응답 데이터 불완전");
                return null;
            }

            // 선택된 경매 정보 파싱
            AutoWriteDto.SelectedAuction selectedAuction = AutoWriteDto.SelectedAuction.builder()
                    .type(selectedAuctionNode.path("type").asText(""))
                    .duration(selectedAuctionNode.path("duration").asText(""))
                    .startPrice(selectedAuctionNode.path("startPrice").asInt(0))
                    .buyNowPrice(selectedAuctionNode.path("buyNowPrice").asInt(0))
                    .strategy(selectedAuctionNode.path("strategy").asText(""))
                    .expectedResult(selectedAuctionNode.path("expectedResult").asText(""))
                    .build();

            // 분석 정보 파싱
            AutoWriteDto.Analysis analysis = AutoWriteDto.Analysis.builder()
                    .sellProbability(analysisNode.path("sellProbability").asText(""))
                    .reasoning(analysisNode.path("reasoning").asText(""))
                    .tips(analysisNode.path("tips").asText(""))
                    .marketComparison(analysisNode.path("marketComparison").asText(""))
                    .build();

            log.info("AI 스마트 가격 책정 완료 - 타입: {}, 시작가: {}원, 즉시구매: {}원",
                    selectedAuction.getType(), selectedAuction.getStartPrice(), selectedAuction.getBuyNowPrice());

            return AutoWriteDto.PriceResponse.builder()
                    .marketPrice(marketPrice)
                    .selectedAuction(selectedAuction)
                    .analysis(analysis)
                    .build();

        } catch (Exception e) {
            log.error("스마트 가격 책정 JSON 파싱 실패: {}", e.getMessage());
            return null;
        }
    }

    private String extractJsonFromResponse(String response) {
        int startIndex = response.indexOf("{");
        int endIndex = response.lastIndexOf("}");

        if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
            return response.substring(startIndex, endIndex + 1);
        }

        log.warn("AI 응답에서 JSON을 찾을 수 없음: {}", response);
        return null;
    }
}