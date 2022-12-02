package com.bi.stockrecsys.repository.transaction;

import com.bi.stockrecsys.entity.transaction.MonthEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonthRepository extends JpaRepository<MonthEntity, Long> {
    MonthEntity findByStockCode(String stockCode);
}
