package com.bi.stockrecsys.repository;

import com.bi.stockrecsys.entity.Pk;
import com.bi.stockrecsys.entity.RecordEntity;
import com.bi.stockrecsys.entity.StockEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RecordRepositoryTest {

    @Mock private StockRepository stockRepository;
    @Mock private RecordRepository recordRepository;

//    @BeforeEach
//    public void setUp(){
//        StockEntity stockEntity = new StockEntity("테스트코드", "테스트종목", "Y", "테스트섹터");
//        Pk pk = new Pk(stockEntity, "2022-09-14");
//        RecordEntity recordEntity = new RecordEntity(pk, 0, 0);
//
//        stockRepository.save(stockEntity);
//        recordRepository.save(recordEntity);
//    }
//
//    @AfterEach
//    public void clear(){
//        stockRepository.deleteAll();
//        recordRepository.deleteAll();
//    }
//
//    @ParameterizedTest
//    @CsvSource({"테스트코드", "2022-09-14"})
//    @Test
//    public void findByPk_정상_테스트(String stockCode, String inputDate){
//        // given
//        StockEntity stockEntity = stockRepository.findByCode(stockCode);
//        Pk pk = new Pk(stockEntity, inputDate);
//
//        // when
//        RecordEntity recordEntity= recordRepository.findByPk(pk);
//
//        // then
//        assertThat(recordEntity.getPk().getDate()).isEqualTo(inputDate);
//    }
}