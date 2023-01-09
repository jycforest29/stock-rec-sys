package com.bi.stockrecsys.controller;

import com.bi.stockrecsys.dto.RequestDTO;
import com.bi.stockrecsys.dto.RequestDTOWrapper;
import com.bi.stockrecsys.dto.ResponseDTO;
import com.bi.stockrecsys.service.MainService;
import com.bi.stockrecsys.vo.DateVO;
import com.bi.stockrecsys.vo.RateVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.nashorn.internal.runtime.logging.Logger;
import org.apache.commons.logging.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class MainControllerTest {

    private MockMvc mockMvc;
    private RequestDTO requestDTO;
    private ResponseDTO responseDTO;
    private RequestDTOWrapper requestDTOWrapper;

    @Mock
    MainService mainService;

    @InjectMocks
    MainController mainController;

    @BeforeEach
    public void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(mainController)
                .build();
    }

    public void rebalance_초기화(){
        requestDTO = RequestDTO.builder()
                .start(new DateVO("2022", "9", "14"))
                .end(new DateVO("2022", "11", "11"))
                .stockName("두산밥캣")
                .stockCode("241560")
                .volume(36)
                .profit(3.6)
                .build();
        responseDTO = ResponseDTO.builder()
                .start(new DateVO("2022", "9", "14"))
                .end(new DateVO("2022", "11", "11"))
                .stockName("두산밥캣")
                .stockCode("241560")
                .toCompare(0)
                .better(0)
                .rateVO(new RateVO(0, 0, 0, 0))
                .build();
        requestDTOWrapper = RequestDTOWrapper.builder()
                .requestDTOs(new ArrayList<>(Arrays.asList(requestDTO)))
                .build();
    }

    @Test
    public void rebalance_정상_테스트() throws Exception {
        rebalance_초기화();
        when(mainService.recommend(any())).thenReturn(new ArrayList<>(Arrays.asList(responseDTO)));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/rebalance").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDTOWrapper))).andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
        assertThat(mvcResult.getResponse().getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());

    }

}