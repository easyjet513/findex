package com.findex.dto;

import com.findex.domain.JobStatus;
import com.findex.domain.JobType;
import com.findex.domain.SyncJobLog;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class SyncJobLogResponse {
    private final Long id;
    private final JobType jobType;
    private final Long indexInfoId;
    private final String indexName;
    private final LocalDate targetDate;
    private final String worker;
    private final JobStatus status;
    private final LocalDateTime createdAt;

    public SyncJobLogResponse(SyncJobLog entity) {
        this.id = entity.getId();
        this.jobType = entity.getJobType();
        this.indexInfoId = entity.getIndexInfo() != null ? entity.getIndexInfo().getId() : null;
        this.indexName = entity.getIndexInfo() != null ? entity.getIndexInfo().getIndexName() : null;
        this.targetDate = entity.getTargetDate();
        this.worker = entity.getWorker();
        this.status = entity.getStatus();
        this.createdAt = entity.getCreatedAt();
    }
}
