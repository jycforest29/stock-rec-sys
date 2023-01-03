package com.bi.stockrecsys.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
public class ResponseDTOWrapper {
    List<ResponseDTO> responseDTOs;
}
