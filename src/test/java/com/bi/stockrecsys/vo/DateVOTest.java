package com.bi.stockrecsys.vo;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class DateVOTest {

    DateVO dateVO = new DateVO(2022, 11, 27);

    @Test
    public void month_확인(){
        assertThat(dateVO.getMonth()).isEqualTo(11);
    }
}