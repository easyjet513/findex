package com.findex.controller;

import com.findex.dto.IndexMarketDataRequest;
import com.findex.dto.IndexMarketDataResponse;
import com.findex.dto.PageResponse;
import com.findex.service.IndexMarketDataService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/index-data")
@RequiredArgsConstructor
public class IndexMarketDataController {

    private final IndexMarketDataService marketDataService;

    @PostMapping
    public ResponseEntity<IndexMarketDataResponse> create(@RequestBody IndexMarketDataRequest req) {
        return ResponseEntity.ok(marketDataService.create(req));
    }

    @GetMapping
    public ResponseEntity<PageResponse<IndexMarketDataResponse>> findAll(
            @RequestParam(required = false) Long indexInfoId,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) String sortDirection) {
        List<IndexMarketDataResponse> list = (indexInfoId != null)
                ? marketDataService.findByIndexId(indexInfoId)
                : List.of();
        return ResponseEntity.ok(new PageResponse<>(list));
    }

    @GetMapping("/index/{indexId}")
    public ResponseEntity<List<IndexMarketDataResponse>> findByIndex(@PathVariable Long indexId) {
        return ResponseEntity.ok(marketDataService.findByIndexId(indexId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        marketDataService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/export/{indexId}")
    public void exportCsv(@PathVariable Long indexId, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"market_data_" + indexId + ".csv\"");
        marketDataService.exportCsv(indexId, response.getWriter());
    }
}