package com.bi.stockrecsys.service;

import com.bi.stockrecsys.entity.Pk;
import com.bi.stockrecsys.entity.RecordEntity;
import com.bi.stockrecsys.entity.StockEntity;
import com.bi.stockrecsys.repository.RecordRepository;
import com.bi.stockrecsys.repository.StockRepository;
import com.bi.stockrecsys.vo.DateVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class MainServiceTest {
    @Autowired MainService mainService;

    @Autowired StockRepository stockRepository;

    @Autowired RecordRepository recordRepository;

    @Test
    public void toDate_메서드_테스트(){
        // given
        DateVO dateVO = new DateVO("2022", "9", "14");

        // when
        String toDateResult = mainService.toDate(dateVO);

        // then
        assertThat("2022-09-14").isEqualTo(toDateResult);
    }

    @Test
    public void 메인서비스_테스트(){

        // given
        StockEntity stockEntity = stockRepository.findByCode("000150");
        Pk pk = new Pk(stockEntity, mainService.toDate(new DateVO("2022", "9", "14")));
        // Pk pk = new Pk(stockEntity, mainService.toDate(new DateVO("2022", "9", "14"))); -> DB의 형식과 다르므로 NULL Exception 발생.

        // when
        RecordEntity recordEntity= recordRepository.findByPk(pk);

        // then
        assertThat(recordEntity.getPk().getDate()).isEqualTo("2022-09-14");
    }

    @Test
    public void 이익_및_손실_계산_테스트(){

        // given
        StockEntity stockEntity = stockRepository.findByCode("000150");
        Pk pk = new Pk(stockEntity, mainService.toDate(new DateVO("2022", "11", "11")));

        // when
        double toCompare = (recordRepository.findByPk(pk).getPrice()) / 100 * 3.6;

        // then
        assertThat(3337 >= toCompare).isEqualTo(false);
    }
}