package com.bi.stockrecsys.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StockRecommendVO {
    private double[] avgPriceWrapper;
    private double[] avgVolumeWrapper;
    private double[] avgSdWrapper;
    private double toCompare;
}
