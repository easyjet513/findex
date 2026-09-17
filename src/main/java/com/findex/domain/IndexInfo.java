package com.findex.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
    name = "index_info",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_index_cls_nm_and_idx_nm", columnNames = {"index_classification_name", "index_name"})
    }
)
public class IndexInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "index_classification_name", nullable = false)
    private String indexClassificationName;

    @Column(name = "index_name", nullable = false)
    private String indexName;

    @Column(name = "employed_item_count")
    private Integer employedItemCount;

    @Column(name = "base_point_in_time")
    private String basePointInTime;

    @Column(name = "base_index", precision = 19, scale = 4)
    private BigDecimal baseIndex;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", nullable = false)
    private SourceType sourceType;

    @Column(name = "is_favorite", nullable = false)
    private Boolean isFavorite;

    @OneToMany(mappedBy = "indexInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IndexMarketData> marketDataList = new ArrayList<>();

    @Builder
    public IndexInfo(String indexClassificationName, String indexName, Integer employedItemCount,
                     String basePointInTime, BigDecimal baseIndex, SourceType sourceType, Boolean isFavorite) {
        this.indexClassificationName = indexClassificationName;
        this.indexName = indexName;
        this.employedItemCount = employedItemCount;
        this.basePointInTime = basePointInTime;
        this.baseIndex = baseIndex;
        this.sourceType = sourceType != null ? sourceType : SourceType.USER;
        this.isFavorite = isFavorite != null ? isFavorite : false;
    }

    public void update(Integer employedItemCount, String basePointInTime, BigDecimal baseIndex, Boolean isFavorite) {
        this.employedItemCount = employedItemCount;
        this.basePointInTime = basePointInTime;
        this.baseIndex = baseIndex;
        if (isFavorite != null) {
            this.isFavorite = isFavorite;
        }
    }
}
