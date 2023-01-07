package com.bi.stockrecsys.repository;

import com.bi.stockrecsys.entity.Pk;
import com.bi.stockrecsys.entity.RecordEntity;
import com.bi.stockrecsys.entity.StockEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecordRepositoryTest {

    @Mock private RecordRepository recordRepository;

    @Test
    public void findByPk_정상_테스트(){
        // given
        Pk pk = new Pk(new StockEntity("테스트코드", "테스트종목", "Y", "테스트섹터"), "2022-09-14");
        RecordEntity record = new RecordEntity(pk, 0, 0);

        // when
        when(recordRepository.save(any())).thenReturn(record); // method stub
        RecordEntity returnedRecord = recordRepository.save(record);

        // then
        verify(recordRepository, times(1)).save(any());
        assertThat(returnedRecord.getPk().getDate()).isEqualTo("2022-09-14");
    }
}