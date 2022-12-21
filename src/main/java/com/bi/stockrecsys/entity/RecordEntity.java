package com.bi.stockrecsys.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "record")
public class RecordEntity {

    @EmbeddedId
    private Pk pk;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int volume;

}
