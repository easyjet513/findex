package com.findex.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "auto_sync_config")
public class AutoSyncConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "index_info_id", nullable = false, unique = true)
    private IndexInfo indexInfo;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    public AutoSyncConfig(IndexInfo indexInfo, Boolean isActive) {
        this.indexInfo = indexInfo;
        this.isActive = isActive != null ? isActive : false;
    }

    public void updateActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
