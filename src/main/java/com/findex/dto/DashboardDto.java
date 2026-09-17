package com.findex.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DashboardDto {

    @Getter
    @AllArgsConstructor
    public static class FavoriteSummary {
        private Long indexId;
        private String indexName;
        private BigDecimal currentPrice;
        private BigDecimal priceChange;
        private BigDecimal fltRate;
    }

    @Getter
    @Builder
    public static class ChartPoint {
        private LocalDate date;
        private BigDecimal closingPrice;
        private BigDecimal ma5;
        private BigDecimal ma20;
    }

    @Getter
    @AllArgsConstructor
    public static class RankItem {
        private int rank;
        private Long indexId;
        private String indexName;
        private BigDecimal currentPrice;
        private BigDecimal rateOfReturn;
    }
}
