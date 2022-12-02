package com.bi.stockrecsys.repository;

import com.bi.stockrecsys.entity.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, String> {
    StockEntity findByCode(String code);
    List<StockEntity> findBySectorAndMarket(String sector, String market);
}
