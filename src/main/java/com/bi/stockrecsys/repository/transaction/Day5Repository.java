package com.bi.stockrecsys.repository.transaction;

import com.bi.stockrecsys.entity.transaction.Day5Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Day5Repository extends JpaRepository<Day5Entity, Long> {
    Day5Entity findByStockCode(String stockCode);
}
