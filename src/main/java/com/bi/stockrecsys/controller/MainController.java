package com.bi.stockrecsys.controller;

import com.bi.stockrecsys.dto.RequestDTO;
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

    public MainController(MainService mainService){
        this.mainService = mainService;
    }

    @GetMapping
    public ResponseEntity<?> rebalance(@RequestBody List<RequestDTO> requestDTOList){

        List<ResponseDTO>[] responseDTOList = new ArrayList[requestDTOList.size()];
        for(int i = 0; i < requestDTOList.size(); i++){
            responseDTOList[i] = new ArrayList<ResponseDTO>();
        }

        int idx = 0;
        for(RequestDTO requestDTO : requestDTOList){
            validate(requestDTO);
            try{
                responseDTOList[idx] = mainService.recommend(requestDTO); // # 서비스인자로 dto 들어가도 되는거야? 아니. https://www.baeldung.com/java-dto-pattern. 근데 이게 가장 적당하지 않나?
                idx += 1;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return new ResponseEntity<>(requestDTOList, HttpStatus.OK);
    }

    public void validate(RequestDTO requestDTO){
        // # 현재 날짜로부터 1분기 <= start <= 현재 날짜
        // # 현재 날짜로부터 1분기 <= end <= 현재 날짜
        if (requestDTO.getStockName() == ""){
            throw new RuntimeException("종목 이름 빈칸");
        }
        // # 종목 이름 존재 안함
        // # 종목 코드 존재 안함
        if (requestDTO.getStockCode() == ""){
            throw new RuntimeException("종목 코드 빈칸");
        }
        if (requestDTO.getVolume() <= 0){
            throw new RuntimeException("거래량 <= 0");
        }
    }
}
