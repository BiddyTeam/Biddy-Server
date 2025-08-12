package com.biddy.biddy_api.global.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                .defaultSystem("당신은 경매 플랫폼의 전문 상품 설명 작성자입니다. " +
                        "매력적이고 신뢰할 수 있는 상품 설명을 작성해주세요.")
                .build();
    }
}
