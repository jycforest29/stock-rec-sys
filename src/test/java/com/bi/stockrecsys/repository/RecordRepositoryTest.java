package com.bi.stockrecsys.repository;

import com.bi.stockrecsys.entity.RecordEntity;
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
class RecordRepositoryTest {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private RecordRepository recordRepository;


    @Test
    @Transactional
    @Rollback
    public void RecordRepository_test(){
        // given
        List<StockEntity> stocks = stockRepository.saveAll(Arrays.asList(new StockEntity("레코드_테스트_code", "테스트_name", "테스트_market", "테스트_sector")));

        RecordEntity toInsert = new RecordEntity();
        toInsert.setStock(stocks.get(0));
        toInsert.setDate("테스트_start");
        toInsert.setPrice(0);
        toInsert.setVolume(0);

        List<RecordEntity> recordEntities = recordRepository.saveAll(Arrays.asList(toInsert));

        // when
        RecordEntity recordEntity= recordRepository.findByStockAndDate(stocks.get(0), "테스트_start");

        // then
        assertThat(recordEntity.getPrice() == 0);
        assertThat(recordEntity.getVolume() == 0);
    }
}