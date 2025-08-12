package com.biddy.biddy_api.domain.auction.service;

import com.biddy.biddy_api.domain.auction.dto.BookmarkedAuctionDto;
import com.biddy.biddy_api.domain.auction.entity.Auction;
import com.biddy.biddy_api.domain.auction.entity.Bookmark;
import com.biddy.biddy_api.domain.auction.repository.AuctionRepository;
import com.biddy.biddy_api.domain.auction.repository.BookmarkRepository;
import com.biddy.biddy_api.domain.user.entity.User;
import com.biddy.biddy_api.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BookmarkService {

    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;
    private final BookmarkRepository bookmarkRepository;

    public void saveBookmark(Long userId, Long auctionId) {
        User user = userRepository.findById(userId).orElseThrow();
        Auction auction = auctionRepository.getAuctionById(auctionId).orElseThrow();

        Bookmark bookmark = new Bookmark(user, auction);
        bookmark.setOwner(user, auction);
        bookmarkRepository.save(bookmark);
    }

    public void deleteBookmark(Long userId, Long auctionId) {
        Bookmark bookmark = bookmarkRepository.findByUserIdAndAuctionId(userId, auctionId)
                .orElseThrow(() -> new IllegalArgumentException("해당 북마크를 찾을 수 없습니다."));

        bookmark.getUser().getBookmarks().remove(bookmark);
        bookmark.getAuction().getBookmarks().remove(bookmark);

        bookmarkRepository.delete(bookmark);
    }

    public List<BookmarkedAuctionDto> findBookmarksByUser(Long userId) {
        List<Bookmark> bookmarks = bookmarkRepository.findByUserId(userId);

        return bookmarks.stream()
                .map(bookmark -> BookmarkedAuctionDto.from(bookmark.getAuction())) // 각 북마크에서 Auction 정보를 DTO로 변환
                .collect(Collectors.toList());
    }
}
