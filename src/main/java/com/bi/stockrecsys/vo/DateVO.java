package com.bi.stockrecsys.vo;

import lombok.*;

@AllArgsConstructor
@Builder
@Getter
public class DateVO {
    @NonNull
    private String year;
    @NonNull
    private String month;
    @NonNull
    private String date;
}
