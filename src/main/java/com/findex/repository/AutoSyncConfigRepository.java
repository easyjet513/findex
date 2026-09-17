package com.findex.repository;

import com.findex.domain.AutoSyncConfig;
import com.findex.domain.IndexInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AutoSyncConfigRepository extends JpaRepository<AutoSyncConfig, Long> {
    Optional<AutoSyncConfig> findByIndexInfo(IndexInfo indexInfo);
    List<AutoSyncConfig> findByIsActiveTrue();
}
