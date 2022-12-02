package com.bi.stockrecsys.vo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RateVOTest {

    RateVO rateVO = new RateVO(1.0, 2.0, 3.0, 4.0);

    @Test
    public void rate_확인_with_double(){
        assertEquals(1.0 ,rateVO.getRate());
    }

    @Test
    public void rate_확인_with_int(){
        assertEquals(1 ,rateVO.getRate());
    }
}