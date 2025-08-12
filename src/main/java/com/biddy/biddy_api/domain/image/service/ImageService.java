package com.biddy.biddy_api.domain.image.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {

    private final SupabaseStorageService supabaseStorageService;

    public List<String> uploadMultipleFiles(List<MultipartFile> files) {
        validateFiles(files);

        List<String> imageUrls = new ArrayList<>();
        List<String> failedFiles = new ArrayList<>();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    String imageUrl = supabaseStorageService.uploadFile(file);
                    imageUrls.add(imageUrl);
                    log.info("파일 업로드 성공: {}", file.getOriginalFilename());
                } catch (Exception e) {
                    failedFiles.add(file.getOriginalFilename());
                    log.error("파일 업로드 실패: {} - {}", file.getOriginalFilename(), e.getMessage());
                }
            }
        }

        if (!failedFiles.isEmpty()) {
            throw new RuntimeException("다음 파일들의 업로드에 실패했습니다: " + String.join(", ", failedFiles));
        }

        if (imageUrls.isEmpty()) {
            throw new RuntimeException("업로드할 수 있는 유효한 이미지가 없습니다.");
        }

        return imageUrls;
    }

    private void validateFiles(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new RuntimeException("이미지 파일이 필요합니다.");
        }

        // 파일 개수 제한 (예: 최대 10개)
        if (files.size() > 10) {
            throw new RuntimeException("최대 10개의 파일만 업로드할 수 있습니다.");
        }

        for (MultipartFile file : files) {
            validateSingleFile(file);
        }
    }

    private void validateSingleFile(MultipartFile file) {
        if (file.isEmpty()) {
            return;
        }

        // 파일 크기 제한 (예: 5MB)
        long maxSize = 5 * 1024 * 1024; // 5MB
        if (file.getSize() > maxSize) {
            throw new RuntimeException("파일 크기는 5MB를 초과할 수 없습니다: " + file.getOriginalFilename());
        }

        // 이미지 파일 형식 검증
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("이미지 파일만 업로드할 수 있습니다: " + file.getOriginalFilename());
        }
    }
}
