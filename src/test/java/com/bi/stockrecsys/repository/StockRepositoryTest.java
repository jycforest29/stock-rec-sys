package com.bi.stockrecsys.repository;

import com.bi.stockrecsys.entity.StockEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.Assert.*;

@SpringBootTest
class StockRepositoryTest {

    @Autowired
    private StockRepository stockRepository;

    @Test
    public void stockRepository_findBy_test(){

        // given, when
        StockEntity stock = stockRepository.findByCode("000150");

        // then
        assertEquals(stock.getName(), "두산");

    }

}