package com.findex.controller;

import com.findex.dto.PageResponse;
import com.findex.dto.SyncJobLogResponse;
import com.findex.service.SyncJobService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sync-jobs")
@RequiredArgsConstructor
public class SyncJobController {

    private final SyncJobService syncJobService;

    // 연동 관리 목록 조회 (프론트엔드 페이징 대응)
    @GetMapping
    public ResponseEntity<PageResponse<SyncJobLogResponse>> findAll(
            @RequestParam(required = false) String jobType,
            @RequestParam(required = false) Long indexInfoId,
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(syncJobService.findAll());
    }

    // ⭐️ 1번 해결: 지수 정보 Open API 연동 실행
    @PostMapping("/index-infos")
    public ResponseEntity<String> syncIndexInfos(HttpServletRequest request) {
        String clientIp = request.getRemoteAddr();
        int count = syncJobService.syncIndexInfosFromOpenApi(clientIp);
        return ResponseEntity.ok("성공적으로 " + count + "건의 지수 정보를 연동했습니다.");
    }

    // 지수 시세 데이터 Open API 연동 실행
    @PostMapping("/index-data")
    public ResponseEntity<String> syncIndexData(
            @RequestParam(required = false) Long indexInfoId,
            @RequestParam(required = false, defaultValue = "20260101") String beginBasDt,
            HttpServletRequest request) {
        String clientIp = request.getRemoteAddr();
        int count = syncJobService.syncIndexDataFromOpenApi(indexInfoId, beginBasDt, clientIp);
        return ResponseEntity.ok("성공적으로 " + count + "건의 시세 데이터를 연동했습니다.");
    }
}