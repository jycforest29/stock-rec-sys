package com.bi.stockrecsys.repository;

import com.bi.stockrecsys.entity.StockEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class StockRepositoryTest {

    @Autowired
    private StockRepository stockRepository;

    @Test
    @Transactional
    @Rollback
    public void stockRepository_test(){
        // given
        List<StockEntity> stocks = stockRepository.saveAll(Arrays.asList(new StockEntity("테스트_code", "테스트_name", "테스트_market", "테스트_sector")));

        // when
        StockEntity stock = stockRepository.findByCode("테스트_code");

        // then
        assertThat(stock.getCode().equals("테스트_code"));
    }
}