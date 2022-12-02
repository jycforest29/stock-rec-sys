package com.bi.stockrecsys;

import com.bi.stockrecsys.vo.DateVO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class StockRecsysApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void check(){
		DateVO dateVO = new DateVO(2022, 11, 27);
		assertEquals(dateVO.getMonth(), 11);
	}

}
