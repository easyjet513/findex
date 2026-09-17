package com.findex.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "sync_job_log")
public class SyncJobLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_type", nullable = false)
    private JobType jobType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "index_info_id")
    private IndexInfo indexInfo;

    @Column(name = "target_date")
    private LocalDate targetDate;

    @Column(name = "worker", nullable = false)
    private String worker;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private JobStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public SyncJobLog(JobType jobType, IndexInfo indexInfo, LocalDate targetDate,
                      String worker, JobStatus status) {
        this.jobType = jobType;
        this.indexInfo = indexInfo;
        this.targetDate = targetDate;
        this.worker = worker;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }
}
