package com.findex.service;

import com.findex.domain.AutoSyncConfig;
import com.findex.domain.IndexInfo;
import com.findex.domain.SourceType;
import com.findex.dto.IndexInfoRequest;
import com.findex.dto.IndexInfoResponse;
import com.findex.repository.AutoSyncConfigRepository;
import com.findex.repository.IndexInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IndexInfoService {

    private final IndexInfoRepository indexInfoRepository;
    private final AutoSyncConfigRepository autoSyncConfigRepository;

    @Transactional
    public IndexInfoResponse create(IndexInfoRequest req) {
        IndexInfo indexInfo = IndexInfo.builder()
                .indexClassificationName(req.getIndexClassificationName())
                .indexName(req.getIndexName())
                .employedItemCount(req.getEmployedItemCount())
                .basePointInTime(req.getBasePointInTime())
                .baseIndex(req.getBaseIndex())
                .sourceType(SourceType.USER)
                .isFavorite(req.getIsFavorite())
                .build();
        IndexInfo saved = indexInfoRepository.save(indexInfo);

        autoSyncConfigRepository.save(new AutoSyncConfig(saved, false));

        return new IndexInfoResponse(saved);
    }

    public List<IndexInfoResponse> findAll() {
        return indexInfoRepository.findAll().stream().map(IndexInfoResponse::new).toList();
    }

    public IndexInfoResponse findById(Long id) {
        return indexInfoRepository.findById(id).map(IndexInfoResponse::new)
                .orElseThrow(() -> new IllegalArgumentException("지수 ID가 없습니다: " + id));
    }

    @Transactional
    public IndexInfoResponse update(Long id, IndexInfoRequest req) {
        IndexInfo entity = indexInfoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("지수 ID가 없습니다: " + id));
        entity.update(req.getEmployedItemCount(), req.getBasePointInTime(), req.getBaseIndex(), req.getIsFavorite());
        return new IndexInfoResponse(entity);
    }

    @Transactional
    public void delete(Long id) {
        indexInfoRepository.deleteById(id);
    }
}
