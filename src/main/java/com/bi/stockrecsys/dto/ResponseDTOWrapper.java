package com.bi.stockrecsys.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class ResponseDTOWrapper {
    List<ResponseDTO> responseDTOs;
}
