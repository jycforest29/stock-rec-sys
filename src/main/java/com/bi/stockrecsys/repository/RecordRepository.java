package com.bi.stockrecsys.repository;

import com.bi.stockrecsys.entity.RecordEntity;
import com.bi.stockrecsys.entity.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository extends JpaRepository<RecordEntity, Long> {
    RecordEntity findByStockAndDate(StockEntity stockEntity, String date); // pk라는 얘기
}
