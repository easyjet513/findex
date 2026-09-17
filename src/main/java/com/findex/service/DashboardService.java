package com.findex.service;

import com.findex.domain.IndexInfo;
import com.findex.domain.IndexMarketData;
import com.findex.dto.DashboardDto;
import com.findex.repository.IndexInfoRepository;
import com.findex.repository.IndexMarketDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final IndexInfoRepository indexInfoRepository;
    private final IndexMarketDataRepository marketDataRepository;

    public List<DashboardDto.FavoriteSummary> getFavoriteSummaries() {
        List<IndexInfo> favorites = indexInfoRepository.findAll().stream()
                .filter(IndexInfo::getIsFavorite)
                .toList();

        List<DashboardDto.FavoriteSummary> result = new ArrayList<>();
        for (IndexInfo info : favorites) {
            List<IndexMarketData> list = marketDataRepository.findByIndexInfoOrderByBaseDateDesc(info);
            if (!list.isEmpty()) {
                IndexMarketData latest = list.get(0);
                result.add(new DashboardDto.FavoriteSummary(
                        info.getId(), info.getIndexName(),
                        latest.getClosingPrice(), latest.getPriceChange(), latest.getFltRate()));
            }
        }
        return result;
    }

    public List<DashboardDto.ChartPoint> getChartWithMA(Long indexInfoId) {
        IndexInfo info = indexInfoRepository.findById(indexInfoId)
                .orElseThrow(() -> new IllegalArgumentException("지수가 없습니다."));
        List<IndexMarketData> list = marketDataRepository.findByIndexInfoOrderByBaseDateDesc(info)
                .stream().sorted(Comparator.comparing(IndexMarketData::getBaseDate)).toList();

        List<DashboardDto.ChartPoint> points = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            IndexMarketData current = list.get(i);
            BigDecimal ma5 = calculateAverage(list, i, 5);
            BigDecimal ma20 = calculateAverage(list, i, 20);

            points.add(DashboardDto.ChartPoint.builder()
                    .date(current.getBaseDate())
                    .closingPrice(current.getClosingPrice())
                    .ma5(ma5)
                    .ma20(ma20)
                    .build());
        }
        return points;
    }

    private BigDecimal calculateAverage(List<IndexMarketData> list, int currentIndex, int windowSize) {
        if (currentIndex < windowSize - 1) return null;
        BigDecimal sum = BigDecimal.ZERO;
        for (int j = currentIndex - windowSize + 1; j <= currentIndex; j++) {
            sum = sum.add(list.get(j).getClosingPrice());
        }
        return sum.divide(BigDecimal.valueOf(windowSize), 2, RoundingMode.HALF_UP);
    }

    public List<DashboardDto.RankItem> getPerformanceRankings(String period) {
        int offset = switch (period.toLowerCase()) {
            case "weekly" -> 5;
            case "monthly" -> 20;
            default -> 1;
        };

        List<IndexInfo> allIndices = indexInfoRepository.findAll();
        List<DashboardDto.RankItem> items = new ArrayList<>();

        for (IndexInfo info : allIndices) {
            List<IndexMarketData> list = marketDataRepository.findByIndexInfoOrderByBaseDateDesc(info);
            if (list.size() > offset) {
                BigDecimal current = list.get(0).getClosingPrice();
                BigDecimal past = list.get(offset).getClosingPrice();
                BigDecimal returnRate = current.subtract(past)
                        .divide(past, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));

                items.add(new DashboardDto.RankItem(0, info.getId(), info.getIndexName(), current, returnRate));
            }
        }

        items.sort((a, b) -> b.getRateOfReturn().compareTo(a.getRateOfReturn()));
        List<DashboardDto.RankItem> ranked = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            DashboardDto.RankItem it = items.get(i);
            ranked.add(new DashboardDto.RankItem(i + 1, it.getIndexId(), it.getIndexName(), it.getCurrentPrice(), it.getRateOfReturn()));
        }
        return ranked;
    }
}
