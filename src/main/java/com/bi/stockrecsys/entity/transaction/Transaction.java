package com.bi.stockrecsys.entity.transaction;

import com.bi.stockrecsys.entity.StockEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class Transaction {
    @Id
    private String stockCode;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private StockEntity stock;

    @Column(name = "start", nullable = false)
    private String start;

    @Column(name = "end", nullable = false)
    private String end;

    @Column(name = "avg_price", nullable = false)
    private int avgPrice;

    @Column(name = "avg_volume", nullable = false)
    private int avgVolume;

    @Column(name = "avg_sd_volume", nullable = false)
    private double avgVolumeSd;

    @Column(name = "avg_sd_price", nullable = false)
    private double avgPriceSd;
}
