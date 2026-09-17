package com.findex.controller;

import com.findex.dto.DashboardDto;
import com.findex.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/favorites")
    public ResponseEntity<List<DashboardDto.FavoriteSummary>> getFavorites() {
        return ResponseEntity.ok(dashboardService.getFavoriteSummaries());
    }

    @GetMapping("/charts/{indexId}")
    public ResponseEntity<List<DashboardDto.ChartPoint>> getChart(@PathVariable Long indexId) {
        return ResponseEntity.ok(dashboardService.getChartWithMA(indexId));
    }

    @GetMapping("/rankings")
    public ResponseEntity<List<DashboardDto.RankItem>> getRankings(
            @RequestParam(defaultValue = "daily") String period) {
        return ResponseEntity.ok(dashboardService.getPerformanceRankings(period));
    }
}
