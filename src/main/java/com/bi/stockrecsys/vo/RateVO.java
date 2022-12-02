package com.bi.stockrecsys.vo;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
public class RateVO {
    private double rate;
    private double priceRate;
    private double volumeRate;
    private double sdRate;
}
