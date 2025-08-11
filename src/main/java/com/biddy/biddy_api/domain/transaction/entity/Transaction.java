package com.biddy.biddy_api.domain.transaction.entity;

import com.biddy.biddy_api.domain.auction.entity.Auction;
import com.biddy.biddy_api.domain.payment.entity.Payment;
import com.biddy.biddy_api.domain.transaction.enums.TransactionStatus;
import com.biddy.biddy_api.domain.user.entity.User;
import com.biddy.biddy_api.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transaction")
public class Transaction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "auction_id", nullable = false)
    private Auction auction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private User buyer;

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal finalPrice;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private TransactionStatus status = TransactionStatus.PENDING;

    @OneToOne(mappedBy = "transaction", cascade = CascadeType.ALL)
    private Payment payment;

    private String deliveryTrackingNumber;
    private String deliveryCompany;
}
