package com.biddy.biddy_api.domain.image.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupabaseStorageService {

    private final WebClient supabaseWebClient;

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.storage.bucket}")
    private String bucketName;

    public String uploadFile(MultipartFile file) {
        try {
            String fileName = generateFileName(file.getOriginalFilename());
            String uploadPath = "/storage/v1/object/" + bucketName + "/" + fileName;

            // Supabase Storage에 파일 업로드
            String response = supabaseWebClient.post()
                    .uri(uploadPath)
                    .contentType(MediaType.parseMediaType(file.getContentType()))
                    .body(BodyInserters.fromValue(file.getBytes()))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // 공개 URL 생성
            String publicUrl = supabaseUrl + "/storage/v1/object/public/" + bucketName + "/" + fileName;

            log.info("파일 업로드 성공: {}", publicUrl);
            return publicUrl;

        } catch (IOException e) {
            log.error("파일 업로드 실패: {}", e.getMessage());
            throw new RuntimeException("파일 업로드에 실패했습니다.");
        }
    }

    public void deleteFile(String fileName) {
        try {
            String deletePath = "/storage/v1/object/" + bucketName + "/" + fileName;

            supabaseWebClient.delete()
                    .uri(deletePath)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.info("파일 삭제 성공: {}", fileName);
        } catch (Exception e) {
            log.error("파일 삭제 실패: {}", e.getMessage());
        }
    }

    private String generateFileName(String originalFilename) {
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return "auction/" + UUID.randomUUID() + extension;
    }
}
