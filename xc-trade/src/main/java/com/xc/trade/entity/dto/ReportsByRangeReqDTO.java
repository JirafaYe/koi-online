package com.xc.trade.entity.dto;

import lombok.Data;

import java.time.LocalDate;


@Data
public class ReportsByRangeReqDTO {
    private LocalDate startTime ;

    private LocalDate endTime ;
}
