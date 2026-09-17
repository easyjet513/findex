package com.findex.repository;

import com.findex.domain.IndexInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface IndexInfoRepository extends JpaRepository<IndexInfo, Long> {
    Optional<IndexInfo> findByIndexClassificationNameAndIndexName(String indexClassificationName, String indexName);
}
