package com.findex.service;

import com.findex.domain.AutoSyncConfig;
import com.findex.dto.AutoSyncConfigResponse;
import com.findex.repository.AutoSyncConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AutoSyncConfigService {

    private final AutoSyncConfigRepository autoSyncConfigRepository;

    public List<AutoSyncConfigResponse> findAll() {
        return autoSyncConfigRepository.findAll().stream().map(AutoSyncConfigResponse::new).toList();
    }

    @Transactional
    public AutoSyncConfigResponse updateActive(Long id, Boolean isActive) {
        AutoSyncConfig config = autoSyncConfigRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("설정이 없습니다. ID: " + id));
        config.updateActive(isActive);
        return new AutoSyncConfigResponse(config);
    }
}
