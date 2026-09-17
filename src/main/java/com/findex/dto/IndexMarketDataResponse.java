package com.findex.dto;

import com.findex.domain.IndexMarketData;
import com.findex.domain.SourceType;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public class IndexMarketDataResponse {
    private final Long id;
    private final Long indexInfoId;
    private final LocalDate baseDate;
    private final SourceType sourceType;
    private final BigDecimal closingPrice;
    private final BigDecimal openingPrice;
    private final BigDecimal highPrice;
    private final BigDecimal lowPrice;
    private final BigDecimal priceChange;
    private final BigDecimal fltRate;
    private final Long tradingVolume;
    private final Long tradingValue;
    private final Long marketTotalAmount;

    public IndexMarketDataResponse(IndexMarketData entity) {
        this.id = entity.getId();
        this.indexInfoId = entity.getIndexInfo().getId();
        this.baseDate = entity.getBaseDate();
        this.sourceType = entity.getSourceType();
        this.closingPrice = entity.getClosingPrice();
        this.openingPrice = entity.getOpeningPrice();
        this.highPrice = entity.getHighPrice();
        this.lowPrice = entity.getLowPrice();
        this.priceChange = entity.getPriceChange();
        this.fltRate = entity.getFltRate();
        this.tradingVolume = entity.getTradingVolume();
        this.tradingValue = entity.getTradingValue();
        this.marketTotalAmount = entity.getMarketTotalAmount();
    }
}
