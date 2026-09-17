package com.findex.controller;

import com.findex.dto.AutoSyncConfigResponse;
import com.findex.service.AutoSyncConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auto-sync-configs")
@RequiredArgsConstructor
public class AutoSyncConfigController {

    private final AutoSyncConfigService autoSyncConfigService;

    @GetMapping
    public ResponseEntity<List<AutoSyncConfigResponse>> findAll() {
        return ResponseEntity.ok(autoSyncConfigService.findAll());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AutoSyncConfigResponse> updateActive(
            @PathVariable Long id,
            @RequestParam Boolean isActive) {
        return ResponseEntity.ok(autoSyncConfigService.updateActive(id, isActive));
    }
}
