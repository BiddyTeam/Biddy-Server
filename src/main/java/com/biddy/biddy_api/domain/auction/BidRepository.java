package com.biddy.biddy_api.domain.auction;

import com.biddy.biddy_api.domain.auction.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {

    @Query("select b from Bid b where b.auction.id = :auctionId")
    List<Bid> findAllByAuction(Long auctionId);

    @Query("select b from Bid b where b.bidder.id = :userId")
    List<Bid> findAllByUserId(Long userId);
}
