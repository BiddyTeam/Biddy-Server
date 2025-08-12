package com.biddy.biddy_api.domain.image.controller;

import com.biddy.biddy_api.domain.image.service.ImageService;
import com.biddy.biddy_api.global.RspTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
@Tag(name = "Image", description = "이미지 업로드 API")
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    @Operation(summary = "여러 이미지 업로드", description = "Supabase Storage에 여러 이미지를 업로드하고 URL 리스트를 반환합니다.")
    public RspTemplate<List<String>> uploadMultipleImages(@RequestParam("files") List<MultipartFile> files) {

        try {
            List<String> imageUrls = imageService.uploadMultipleFiles(files);
            String message = String.format("%d개 이미지 업로드가 완료되었습니다.", imageUrls.size());
            return new RspTemplate<>(HttpStatus.OK, message, imageUrls);

        } catch (RuntimeException e) {
            return new RspTemplate<>(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return new RspTemplate<>(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드 중 오류가 발생했습니다.");
        }
    }
}
