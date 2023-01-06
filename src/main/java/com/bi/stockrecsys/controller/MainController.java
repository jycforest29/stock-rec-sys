package com.bi.stockrecsys.controller;

import com.bi.stockrecsys.dto.RequestDTO;
import com.bi.stockrecsys.dto.RequestDTOWrapper;
import com.bi.stockrecsys.dto.ResponseDTO;
import com.bi.stockrecsys.service.MainService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MainController {

    private final MainService mainService;
    private List<RequestDTO> requestDTOs;
    private List<ResponseDTO>[] responseDTOs;

    public MainController(MainService mainService){
        this.mainService = mainService;
    }

    @GetMapping("/rebalance")
    public ResponseEntity<?> rebalance(@RequestBody RequestDTOWrapper requestDTOWrapper){
        requestDTOs = requestDTOWrapper.getRequestDTOs();
        responseDTOs = new ArrayList[requestDTOs.size()];
        for(int i = 0; i < requestDTOs.size(); i++){
            responseDTOs[i] = new ArrayList<ResponseDTO>();
        }

        int idx = 0;
        for(RequestDTO requestDTO : requestDTOs){
            validate(requestDTO);
            try{
                responseDTOs[idx] = mainService.recommend(requestDTO);
                idx += 1;
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(responseDTOs);
    }

    // [ToDo - refactor] 분기문이 너무 많음. 더 깔끔히 처리할 수 없을까?
    public void validate(RequestDTO requestDTO){
        // [ToDo - feat] 거래 날짜가 DB상의 범위를 벗어나는 경우에 대해 예외처리 안함.
        if (requestDTO.getStart().toString().isEmpty()){ // [To - feat]각 년도, 월, 일이 빈칸일 때 예외 세부적 처리 안함.
            throw new RuntimeException("거래시작 기간 빈칸");
        }
        if (requestDTO.getEnd().toString().equals("")){ // [To - feat]각 년도, 월, 일이 빈칸일 때 예외 세부적 처리 안함.
            throw new RuntimeException("거래종료 기간 빈칸");
        }
        if (requestDTO.getStockName().isEmpty()){
            throw new RuntimeException("종목 이름 빈칸");
        }
        if (requestDTO.getStockCode().isEmpty()){
            throw new RuntimeException("종목 코드 빈칸");
        }
        if (requestDTO.getVolume() <= 0){
            throw new RuntimeException("거래량 <= 0");
        }
    }

    public void setUp(){

    }
}
