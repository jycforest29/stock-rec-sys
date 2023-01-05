package com.bi.stockrecsys.repository;

import com.bi.stockrecsys.entity.Pk;
import com.bi.stockrecsys.entity.RecordEntity;
import com.bi.stockrecsys.entity.StockEntity;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.*;

class RecordRepositoryTest {

    @Mock private StockRepository stockRepository;
    @Mock private RecordRepository recordRepository;

    @ParameterizedTest
    @CsvSource({"000150", "2022-09-14"})
    public void findByPk_정상_테스트(String stockCode, String inputDate){
        // given
        StockEntity stockEntity = stockRepository.findByCode(stockCode);
        Pk pk = new Pk(stockEntity, inputDate);

        // when
        RecordEntity recordEntity= recordRepository.findByPk(pk);

        // then
        assertThat(recordEntity.getPk().getDate()).isEqualTo(inputDate);
    }
}