package com.xc.trade.entity.vo;

import lombok.Data;
import org.apache.tomcat.jni.Local;
import org.springframework.context.annotation.Primary;

import java.time.LocalDateTime;

@Data
public class FlowReportsVO {
    /**
     * 日期
     */
    private LocalDateTime time;

    /**
     * 流量
     */
    private Long flow;
}
