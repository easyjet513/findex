package com.findex.repository;

import com.findex.domain.IndexInfo;
import com.findex.domain.IndexMarketData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IndexMarketDataRepository extends JpaRepository<IndexMarketData, Long> {
    Optional<IndexMarketData> findByIndexInfoAndBaseDate(IndexInfo indexInfo, LocalDate baseDate);
    List<IndexMarketData> findByIndexInfoOrderByBaseDateDesc(IndexInfo indexInfo);
}
