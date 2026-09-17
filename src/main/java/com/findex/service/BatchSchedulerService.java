package com.findex.service;

import com.findex.domain.AutoSyncConfig;
import com.findex.repository.AutoSyncConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BatchSchedulerService {

    private final AutoSyncConfigRepository autoSyncConfigRepository;
    private final IndexMarketDataService indexMarketDataService;

    @Scheduled(cron = "${batch.sync.cron:0 0 6 * * *}")
    public void runDailyAutoSync() {
        log.info("=== [Batch] 매일 자동 연동 시작 ===");
        List<AutoSyncConfig> activeConfigs = autoSyncConfigRepository.findByIsActiveTrue();
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        for (AutoSyncConfig config : activeConfigs) {
            try {
                indexMarketDataService.syncFromOpenApi(config.getIndexInfo().getId(), today, "system");
            } catch (Exception e) {
                log.error("지수 [{}] 연동 실패: {}", config.getIndexInfo().getIndexName(), e.getMessage());
            }
        }
        log.info("=== [Batch] 매일 자동 연동 완료 ===");
    }
}
