package com.xc.trade.entity.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ReportsByRangeReqDTO {
    private LocalDate startTime ;

    private LocalDate endTime ;
}
