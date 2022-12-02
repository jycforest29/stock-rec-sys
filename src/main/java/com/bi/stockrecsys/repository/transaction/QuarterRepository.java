package com.bi.stockrecsys.repository.transaction;

import com.bi.stockrecsys.entity.transaction.QuarterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuarterRepository extends JpaRepository<QuarterEntity, Long> {
    QuarterEntity findByStockCode(String stockCode);
}
