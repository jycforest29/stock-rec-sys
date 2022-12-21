package com.bi.stockrecsys.vo;

import lombok.*;

@AllArgsConstructor
@Setter
@Getter
public class DateVO { // 왜 캡슐화? 중복되는 코드 줄이려고 만듬
    @NonNull
    private String year;
    @NonNull
    private String month;
    @NonNull
    private String date;
}
