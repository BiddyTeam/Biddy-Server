package com.biddy.biddy_api.domain.auction.controller;

import com.biddy.biddy_api.domain.auction.dto.BookmarkedAuctionDto;
import com.biddy.biddy_api.domain.auction.service.BookmarkService;
import com.biddy.biddy_api.global.RspTemplate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping
    public RspTemplate<Void> addBookmark(@RequestBody BookmarkRequest request) {
        bookmarkService.saveBookmark(request.getUserId(), request.getAuctionId());
        return new RspTemplate<>(HttpStatus.CREATED, "북마크가 추가되었습니다.");
    }

    @GetMapping("/users/{userId}")
    public RspTemplate<List<BookmarkedAuctionDto>> getBookmarksByUser(@PathVariable Long userId) {
        List<BookmarkedAuctionDto> bookmarks = bookmarkService.findBookmarksByUser(userId);
        return new RspTemplate<>(HttpStatus.OK, "북마크 목록이 조회되었습니다.", bookmarks);
    }

    @DeleteMapping
    public RspTemplate<Void> removeBookmark(@RequestParam Long userId, @RequestParam Long auctionId) {
        bookmarkService.deleteBookmark(userId, auctionId);
        return new RspTemplate<>(HttpStatus.OK, "북마크가 삭제되었습니다.");
    }

    @Getter
    public static class BookmarkRequest {
        private Long userId;
        private Long auctionId;
    }
}