package com.bi.stockrecsys.entity;

import lombok.Getter;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
@Getter
public class Pk implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL) // 다대일 단방향으로 설계
    private StockEntity stock;

    @Column(nullable = false)
    private String date;

    public Pk(StockEntity stock, String date){
        this.stock = stock;
        this.date = date;
    }

    private Pk(){}
}
