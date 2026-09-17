package com.findex.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.findex.domain.*;
import com.findex.dto.PageResponse;
import com.findex.dto.SyncJobLogResponse;
import com.findex.repository.AutoSyncConfigRepository;
import com.findex.repository.IndexInfoRepository;
import com.findex.repository.SyncJobLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SyncJobService {

    private final SyncJobLogRepository syncJobLogRepository;
    private final IndexInfoRepository indexInfoRepository;
    private final AutoSyncConfigRepository autoSyncConfigRepository;
    private final IndexMarketDataService indexMarketDataService;
    private final OpenApiService openApiService;

    public PageResponse<SyncJobLogResponse> findAll() {
        List<SyncJobLogResponse> list = syncJobLogRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(SyncJobLogResponse::new)
                .toList();
        return new PageResponse<>(list);
    }

    @Transactional
    public int syncIndexInfosFromOpenApi(String clientIp) {
        int count = 0;
        try {
            JsonNode items = openApiService.fetchMarketIndexData("", "20260101");
            if (items.isArray()) {
                for (JsonNode item : items) {
                    String idxNm = item.path("idxNm").asText();
                    String idxClsNm = item.path("idxClsNm").asText("기타");
                    if (idxNm.isBlank()) continue;

                    IndexInfo info = indexInfoRepository.findByIndexClassificationNameAndIndexName(idxClsNm, idxNm)
                            .orElseGet(() -> {
                                IndexInfo newInfo = IndexInfo.builder()
                                        .indexClassificationName(idxClsNm)
                                        .indexName(idxNm)
                                        .sourceType(SourceType.OPEN_API)
                                        .isFavorite(false)
                                        .build();
                                IndexInfo saved = indexInfoRepository.save(newInfo);
                                autoSyncConfigRepository.save(new AutoSyncConfig(saved, false));
                                return saved;
                            });

                    info.update(
                            item.path("epyItmCnt").asInt(0),
                            item.path("basPntTm").asText(""),
                            new BigDecimal(item.path("basIdx").asText("0")),
                            null
                    );

                    // 지수별 이력 저장
                    syncJobLogRepository.save(SyncJobLog.builder()
                            .jobType(JobType.INDEX_INFO)
                            .indexInfo(info)
                            .targetDate(LocalDate.now())
                            .worker(clientIp)
                            .status(JobStatus.SUCCESS)
                            .build());
                    count++;
                }
            }
        } catch (Exception e) {
            syncJobLogRepository.save(SyncJobLog.builder()
                    .jobType(JobType.INDEX_INFO)
                    .targetDate(LocalDate.now())
                    .worker(clientIp)
                    .status(JobStatus.FAILURE)
                    .build());
            throw new RuntimeException("지수 정보 연동 실패", e);
        }
        return count;
    }

    @Transactional
    public int syncIndexDataFromOpenApi(Long indexInfoId, String beginBasDt, String clientIp) {
        return indexMarketDataService.syncFromOpenApi(indexInfoId, beginBasDt, clientIp);
    }
}