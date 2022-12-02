package com.bi.stockrecsys.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StockEntity {
    @Id // read-only, null 관련 설정 포함?
    private String code;

    @Column(nullable = false) // # read-only인데 이중 확인인가?
    private String name;

    @Column(nullable = false)
    private String market;

    @Column(name = "sector_name", nullable = false)
    private String sector; // # sector 테이블까지 만들 필요 없다고 생각해서 그냥 뺌.
}
