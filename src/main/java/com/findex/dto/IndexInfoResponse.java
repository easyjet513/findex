package com.findex.dto;

import com.findex.domain.IndexInfo;
import com.findex.domain.SourceType;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
public class IndexInfoResponse {
    private final Long id;
    private final String indexClassificationName;
    private final String indexName;
    private final Integer employedItemCount;
    private final String basePointInTime;
    private final BigDecimal baseIndex;
    private final SourceType sourceType;
    private final Boolean isFavorite;

    public IndexInfoResponse(IndexInfo entity) {
        this.id = entity.getId();
        this.indexClassificationName = entity.getIndexClassificationName();
        this.indexName = entity.getIndexName();
        this.employedItemCount = entity.getEmployedItemCount();
        this.basePointInTime = entity.getBasePointInTime();
        this.baseIndex = entity.getBaseIndex();
        this.sourceType = entity.getSourceType();
        this.isFavorite = entity.getIsFavorite();
    }
}
