package com.findex.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.findex.domain.*;
import com.findex.dto.IndexMarketDataRequest;
import com.findex.dto.IndexMarketDataResponse;
import com.findex.repository.IndexInfoRepository;
import com.findex.repository.IndexMarketDataRepository;
import com.findex.repository.SyncJobLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IndexMarketDataService {

    private final IndexMarketDataRepository marketDataRepository;
    private final IndexInfoRepository indexInfoRepository;
    private final SyncJobLogRepository syncJobLogRepository;
    private final OpenApiService openApiService;

    @Transactional
    public IndexMarketDataResponse create(IndexMarketDataRequest req) {
        IndexInfo indexInfo = indexInfoRepository.findById(req.getIndexInfoId())
                .orElseThrow(() -> new IllegalArgumentException("지수 정보가 없습니다. ID: " + req.getIndexInfoId()));

        IndexMarketData data = IndexMarketData.builder()
                .indexInfo(indexInfo)
                .baseDate(req.getBaseDate())
                .sourceType(SourceType.USER)
                .closingPrice(req.getClosingPrice())
                .openingPrice(req.getOpeningPrice())
                .highPrice(req.getHighPrice())
                .lowPrice(req.getLowPrice())
                .priceChange(req.getPriceChange())
                .fltRate(req.getFltRate())
                .tradingVolume(req.getTradingVolume())
                .tradingValue(req.getTradingValue())
                .marketTotalAmount(req.getMarketTotalAmount())
                .build();

        return new IndexMarketDataResponse(marketDataRepository.save(data));
    }

    public List<IndexMarketDataResponse> findByIndexId(Long indexInfoId) {
        IndexInfo indexInfo = indexInfoRepository.findById(indexInfoId)
                .orElseThrow(() -> new IllegalArgumentException("지수 정보가 없습니다. ID: " + indexInfoId));
        return marketDataRepository.findByIndexInfoOrderByBaseDateDesc(indexInfo).stream()
                .map(IndexMarketDataResponse::new)
                .toList();
    }

    @Transactional
    public void delete(Long id) {
        marketDataRepository.deleteById(id);
    }

    public void exportCsv(Long indexInfoId, PrintWriter writer) {
        IndexInfo indexInfo = indexInfoRepository.findById(indexInfoId)
                .orElseThrow(() -> new IllegalArgumentException("지수 정보가 없습니다. ID: " + indexInfoId));
        List<IndexMarketData> list = marketDataRepository.findByIndexInfoOrderByBaseDateDesc(indexInfo);

        writer.println("날짜,종가,시가,고가,저가,전일대비,등락률,거래량,거래대금,시가총액,출처");
        for (IndexMarketData item : list) {
            writer.printf("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                    item.getBaseDate(), item.getClosingPrice(), item.getOpeningPrice(),
                    item.getHighPrice(), item.getLowPrice(), item.getPriceChange(),
                    item.getFltRate(), item.getTradingVolume(), item.getTradingValue(),
                    item.getMarketTotalAmount(), item.getSourceType());
        }
        writer.flush();
    }

    @Transactional
    public int syncFromOpenApi(Long indexInfoId, String beginBasDt, String worker) {
        IndexInfo indexInfo = indexInfoRepository.findById(indexInfoId)
                .orElseThrow(() -> new IllegalArgumentException("지수 정보가 없습니다. ID: " + indexInfoId));

        int count = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        try {
            JsonNode items = openApiService.fetchMarketIndexData(indexInfo.getIndexName(), beginBasDt);
            if (items.isArray()) {
                for (JsonNode item : items) {
                    LocalDate date = LocalDate.parse(item.path("basDt").asText(), formatter);
                    IndexMarketData marketData = marketDataRepository.findByIndexInfoAndBaseDate(indexInfo, date)
                            .orElseGet(() -> IndexMarketData.builder()
                                    .indexInfo(indexInfo)
                                    .baseDate(date)
                                    .sourceType(SourceType.OPEN_API)
                                    .build());

                    marketData.update(
                            new BigDecimal(item.path("clpr").asText("0")),
                            new BigDecimal(item.path("mkp").asText("0")),
                            new BigDecimal(item.path("hipr").asText("0")),
                            new BigDecimal(item.path("lopr").asText("0")),
                            new BigDecimal(item.path("vs").asText("0")),
                            new BigDecimal(item.path("fltRt").asText("0")),
                            item.path("trqu").asLong(0),
                            item.path("trPrc").asLong(0),
                            item.path("mrktTotAmt").asLong(0)
                    );
                    marketDataRepository.save(marketData);
                    count++;
                }
            }

            syncJobLogRepository.save(SyncJobLog.builder()
                    .jobType(JobType.INDEX_MARKET_DATA)
                    .indexInfo(indexInfo)
                    .targetDate(LocalDate.now())
                    .worker(worker)
                    .status(JobStatus.SUCCESS)
                    .build());

        } catch (Exception e) {
            syncJobLogRepository.save(SyncJobLog.builder()
                    .jobType(JobType.INDEX_MARKET_DATA)
                    .indexInfo(indexInfo)
                    .targetDate(LocalDate.now())
                    .worker(worker)
                    .status(JobStatus.FAILURE)
                    .build());
            throw e;
        }

        return count;
    }
}
