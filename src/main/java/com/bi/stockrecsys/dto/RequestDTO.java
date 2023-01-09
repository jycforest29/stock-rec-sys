package com.bi.stockrecsys.dto;

import com.bi.stockrecsys.vo.DateVO;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class RequestDTO {
    @NonNull
    private DateVO start; // 매수
    @NonNull
    private DateVO end; // 매도
    @NonNull
    private String stockName;
    @NonNull
    private String stockCode;
    private int volume;
    private double profit;
}
