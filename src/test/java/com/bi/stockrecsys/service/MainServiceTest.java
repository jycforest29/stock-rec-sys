package com.bi.stockrecsys.service;

import com.bi.stockrecsys.repository.RecordRepository;
import com.bi.stockrecsys.repository.StockRepository;
import com.bi.stockrecsys.repository.transaction.Day5Repository;
import com.bi.stockrecsys.repository.transaction.MonthRepository;
import com.bi.stockrecsys.repository.transaction.QuarterRepository;
import com.bi.stockrecsys.vo.DateVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class MainServiceTest {

    @Mock private StockRepository stockRepository;
    @Mock private QuarterRepository quarterRepository;
    @Mock private MonthRepository monthRepository;
    @Mock private Day5Repository day5Repository;
    @Mock private RecordRepository recordRepository;
    private MainService mainService;

    @BeforeEach
    public void BeforeEach(){
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
        double[] vectorA = new double[]{0};
        double[] vectorB = new double[]{0};

        // when, then
        assertThrows(ArithmeticException.class, () -> {
            mainService.getCosineSimilarity(vectorA, vectorB);
        });
    }

    @Test
    public void isArraySizeEqual_예외_테스트(){
        // given
        double[] vectorA = new double[]{0.5};
        double[] vectorB = new double[]{0.5};

        // when
        Exception thrown = assertThrows(RuntimeException.class, () -> {
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


}