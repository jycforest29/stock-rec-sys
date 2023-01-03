package com.bi.stockrecsys.repository;

import com.bi.stockrecsys.entity.Pk;
import com.bi.stockrecsys.entity.RecordEntity;
import com.bi.stockrecsys.entity.StockEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.Assert.*;

@SpringBootTest
class RecordRepositoryTest {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private RecordRepository recordRepository;

    @Test
    @Transactional
    public void RecordRepository_test(){
        // given
        StockEntity stockEntity = stockRepository.findByCode("000150");
        Pk pk = new Pk(stockEntity, "2022-09-14");

        // when
        RecordEntity recordEntity= recordRepository.findByPk(pk);

        // then
        assertEquals(recordEntity.getPk().getDate(), "2022-09-14");
    }
}