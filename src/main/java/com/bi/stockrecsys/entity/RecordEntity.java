package com.bi.stockrecsys.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecordEntity {
    @Id
    @GeneratedValue
    private Long id; // 원래 stock과 date의 복합키가 pk임. 하지만 복잡함.

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL) // 다대일 단방향으로 설계
    private StockEntity stock;

    @Column(nullable = false)
    private String date;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int volume;
}
