package com.findex.dto;

import com.findex.domain.AutoSyncConfig;
import lombok.Getter;

@Getter
public class AutoSyncConfigResponse {
    private final Long id;
    private final Long indexInfoId;
    private final String indexName;
    private final Boolean isActive;

    public AutoSyncConfigResponse(AutoSyncConfig entity) {
        this.id = entity.getId();
        this.indexInfoId = entity.getIndexInfo().getId();
        this.indexName = entity.getIndexInfo().getIndexName();
        this.isActive = entity.getIsActive();
    }
}
