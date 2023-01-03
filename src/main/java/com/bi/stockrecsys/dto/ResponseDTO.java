package com.bi.stockrecsys.dto;

import com.bi.stockrecsys.vo.DateVO;
import com.bi.stockrecsys.vo.RateVO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class ResponseDTO {
    @NonNull
    private DateVO start;
    @NonNull
    private DateVO end;
    @NonNull
    private String stockName;
    @NonNull
    private String stockCode;
    private double toCompare;
    private double better;
    @NonNull
    private RateVO rateVO;
}
