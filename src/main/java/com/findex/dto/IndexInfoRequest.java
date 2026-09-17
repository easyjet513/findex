package com.findex.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class IndexInfoRequest {
    private String indexClassificationName;
    private String indexName;
    private Integer employedItemCount;
    private String basePointInTime;
    private BigDecimal baseIndex;
    private Boolean isFavorite;
}
