package com.findex.controller;

import com.findex.dto.IndexInfoRequest;
import com.findex.dto.IndexInfoResponse;
import com.findex.dto.PageResponse;
import com.findex.service.IndexInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/index-infos")
@RequiredArgsConstructor
public class IndexInfoController {

    private final IndexInfoService indexInfoService;

    @PostMapping
    public ResponseEntity<IndexInfoResponse> create(@RequestBody IndexInfoRequest req) {
        return ResponseEntity.ok(indexInfoService.create(req));
    }

    @GetMapping
    public ResponseEntity<PageResponse<IndexInfoResponse>> findAll(
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) String sortDirection,
            @RequestParam(required = false) String indexClassification,
            @RequestParam(required = false) String indexName,
            @RequestParam(required = false) Boolean isFavorite) {
        return ResponseEntity.ok(new PageResponse<>(indexInfoService.findAll()));
    }

    @GetMapping("/summaries")
    public ResponseEntity<List<IndexInfoResponse>> getSummaries() {
        return ResponseEntity.ok(indexInfoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<IndexInfoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(indexInfoService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IndexInfoResponse> update(@PathVariable Long id, @RequestBody IndexInfoRequest req) {
        return ResponseEntity.ok(indexInfoService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        indexInfoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}