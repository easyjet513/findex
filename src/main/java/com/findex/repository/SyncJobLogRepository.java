package com.findex.repository;

import com.findex.domain.SyncJobLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SyncJobLogRepository extends JpaRepository<SyncJobLog, Long> {
    List<SyncJobLog> findAllByOrderByCreatedAtDesc();
}
