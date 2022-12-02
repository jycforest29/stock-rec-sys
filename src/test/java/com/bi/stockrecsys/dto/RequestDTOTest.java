package com.bi.stockrecsys.dto;

import com.bi.stockrecsys.vo.DateVO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequestDTOTest {

    DateVO dateVO = new DateVO(2022, 11, 27);
    RequestDTO dto = new RequestDTO(dateVO, dateVO, "", "", 0, 0);
    @Test
    public void start_확인(){
        assertEquals(new DateVO(2022, 11, 27).getMonth(), dto.getStart().getMonth());
    }

    @Test
    public void stockName_확인(){
        assertEquals("", dto.getStockName());
    }

    @Test
    public void profit_확인(){
        assertEquals(0, dto.getProfit());
    }
}