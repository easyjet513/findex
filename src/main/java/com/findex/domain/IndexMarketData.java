package com.findex.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
    name = "index_market_data",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_market_data_index_and_date", columnNames = {"index_info_id", "base_date"})
    }
)
public class IndexMarketData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "index_info_id", nullable = false)
    private IndexInfo indexInfo;

    @Column(name = "base_date", nullable = false)
    private LocalDate baseDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", nullable = false)
    private SourceType sourceType;

    @Column(name = "closing_price", precision = 19, scale = 4)
    private BigDecimal closingPrice;

    @Column(name = "opening_price", precision = 19, scale = 4)
    private BigDecimal openingPrice;

    @Column(name = "high_price", precision = 19, scale = 4)
    private BigDecimal highPrice;

    @Column(name = "low_price", precision = 19, scale = 4)
    private BigDecimal lowPrice;

    @Column(name = "price_change", precision = 19, scale = 4)
    private BigDecimal priceChange;

    @Column(name = "flt_rate", precision = 10, scale = 4)
    private BigDecimal fltRate;

    @Column(name = "trading_volume")
    private Long tradingVolume;

    @Column(name = "trading_value")
    private Long tradingValue;

    @Column(name = "market_total_amount")
    private Long marketTotalAmount;

    @Builder
    public IndexMarketData(IndexInfo indexInfo, LocalDate baseDate, SourceType sourceType,
                           BigDecimal closingPrice, BigDecimal openingPrice, BigDecimal highPrice,
                           BigDecimal lowPrice, BigDecimal priceChange, BigDecimal fltRate,
                           Long tradingVolume, Long tradingValue, Long marketTotalAmount) {
        this.indexInfo = indexInfo;
        this.baseDate = baseDate;
        this.sourceType = sourceType != null ? sourceType : SourceType.USER;
        this.closingPrice = closingPrice;
        this.openingPrice = openingPrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.priceChange = priceChange;
        this.fltRate = fltRate;
        this.tradingVolume = tradingVolume;
        this.tradingValue = tradingValue;
        this.marketTotalAmount = marketTotalAmount;
    }

    public void update(BigDecimal closingPrice, BigDecimal openingPrice, BigDecimal highPrice,
                       BigDecimal lowPrice, BigDecimal priceChange, BigDecimal fltRate,
                       Long tradingVolume, Long tradingValue, Long marketTotalAmount) {
        this.closingPrice = closingPrice;
        this.openingPrice = openingPrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.priceChange = priceChange;
        this.fltRate = fltRate;
        this.tradingVolume = tradingVolume;
        this.tradingValue = tradingValue;
        this.marketTotalAmount = marketTotalAmount;
    }
}
