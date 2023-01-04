package com.bi.stockrecsys.service;

import com.bi.stockrecsys.entity.Pk;
import com.bi.stockrecsys.entity.RecordEntity;
import com.bi.stockrecsys.entity.StockEntity;
import com.bi.stockrecsys.repository.RecordRepository;
import com.bi.stockrecsys.repository.StockRepository;
import com.bi.stockrecsys.repository.transaction.Day5Repository;
import com.bi.stockrecsys.repository.transaction.MonthRepository;
import com.bi.stockrecsys.repository.transaction.QuarterRepository;
import com.bi.stockrecsys.vo.DateVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Vector;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class MainServiceTest {

    @Mock private StockRepository stockRepository;
    @Mock private QuarterRepository quarterRepository;
    @Mock private MonthRepository monthRepository;
    @Mock private Day5Repository day5Repository;
    @Mock private RecordRepository recordRepository;
    private MainService mainService;

    @BeforeEach
    public void beforeEach(){
        this.mainService = new MainService(
                this.stockRepository,
                this.quarterRepository,
                this.monthRepository,
                this.day5Repository,
                this.recordRepository
        );
    }

    @Test
    public void getCosineSimilarity_예외_테스트(){
        // given


        // when


        // then

    }

    @Test
    public void isArraySizeEqual_예외_테스트(){
        // given
        double[] vectorA = new double[]{0.5, 1.1};
        double[] vectorB = new double[]{0.5};

        // when
        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            mainService.isArraySizeEqual(vectorA, vectorB);
        });

        // then
        assertThat(thrown.getMessage()).isEqualTo("두 벡터의 길이가 달라 코사인 유사도 계산 불가");
    }

    @Test
    public void toDate_정상_테스트(){
        // given
        DateVO dateVO = new DateVO("2022", "9", "14");

        // when
        String toDateResult = mainService.toDate(dateVO);

        // then
        assertThat(toDateResult).isEqualTo("2022-09-14");
    }

//    @Test
//    public void 메인서비스_테스트(){
//        // given
//        StockEntity stockEntity = stockRepository.findByCode("000150");
//        Pk pk = new Pk(stockEntity, mainService.toDate(new DateVO("2022", "9", "14")));
//        // Pk pk = new Pk(stockEntity, mainService.toDate(new DateVO("2022", "9", "14"))); -> DB의 형식과 다르므로 NULL Exception 발생.
//
//        // when
//        RecordEntity recordEntity= recordRepository.findByPk(pk);
//
//        // then
//        assertThat(recordEntity.getPk().getDate()).isEqualTo("2022-09-14");
//    }
//
//    @Test
//    public void 이익_및_손실_계산_테스트(){
//
//        // given
//        StockEntity stockEntity = stockRepository.findByCode("000150");
//        Pk pk = new Pk(stockEntity, mainService.toDate(new DateVO("2022", "11", "11")));
//
//        // when
//        double toCompare = (recordRepository.findByPk(pk).getPrice()) / 100 * 3.6;
//
//        // then
//        assertThat(3337 >= toCompare).isEqualTo(false);
//    }
}