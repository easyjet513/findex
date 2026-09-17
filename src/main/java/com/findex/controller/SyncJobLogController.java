package com.findex.controller;

import com.findex.dto.SyncJobLogResponse;
import com.findex.repository.SyncJobLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sync-logs")
@RequiredArgsConstructor
public class SyncJobLogController {

    private final SyncJobLogRepository syncJobLogRepository;

    @GetMapping
    public ResponseEntity<List<SyncJobLogResponse>> findAll() {
        List<SyncJobLogResponse> list = syncJobLogRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(SyncJobLogResponse::new)
                .toList();
        return ResponseEntity.ok(list);
    }
}
