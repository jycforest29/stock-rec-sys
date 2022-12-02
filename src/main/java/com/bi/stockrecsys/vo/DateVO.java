package com.bi.stockrecsys.vo;

import lombok.*;

@AllArgsConstructor
@Setter
@Getter
public class DateVO { // 왜 캡슐화? 중복되는 코드 줄이려고 만듬
    private int year;
    private int month;
    private int date;
}
