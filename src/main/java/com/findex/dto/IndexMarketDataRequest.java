package com.findex.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class IndexMarketDataRequest {
    private Long indexInfoId;
    private LocalDate baseDate;
    private BigDecimal closingPrice;
    private BigDecimal openingPrice;
    private BigDecimal highPrice;
    private BigDecimal lowPrice;
    private BigDecimal priceChange;
    private BigDecimal fltRate;
    private Long tradingVolume;
    private Long tradingValue;
    private Long marketTotalAmount;
}
