package com.bi.stockrecsys.repository.transaction;

import com.bi.stockrecsys.entity.StockEntity;
import com.bi.stockrecsys.entity.transaction.Day5Entity;
import com.bi.stockrecsys.repository.StockRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class Day5RepositoryTest {

    @Autowired
    private Day5Repository day5Repository;

    @Autowired
    private StockRepository stockRepository;

    @Test
    @Transactional
    @Rollback
    public void Day5Repository_테스트() throws Exception{

        // given
        List<StockEntity> stocks = stockRepository.saveAll(Arrays.asList(new StockEntity("테스트_code", "테스트_name", "테스트_market", "테스트_sector")));

        Day5Entity day5 = new Day5Entity();
        day5.setStock(stocks.get(0));
        day5.setStart("테스트_start");
        day5.setEnd("테스트_end");
        day5.setAvgPrice(0);
        day5.setAvgVolume(0);
        day5.setAvgSd(0.0);

        List<Day5Entity> day5s = day5Repository.saveAll(Arrays.asList(day5));

        // when
        Day5Entity day5Entity = day5Repository.findByStockCode(stocks.get(0).getCode());

        // then
        assertThat(day5Entity.getStock().getCode().equals("테스트_code"));
    }
}