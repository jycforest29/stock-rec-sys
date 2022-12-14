package com.bi.stockrecsys.vo;

import lombok.*;

@AllArgsConstructor
@Getter
public class RateVO {
    private double rate;
    private double priceRate;
    private double volumeRate;
    private double priceSdRate;
}
