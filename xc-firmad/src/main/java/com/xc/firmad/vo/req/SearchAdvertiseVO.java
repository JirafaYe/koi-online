package com.xc.firmad.vo.req;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xc.common.domain.query.PageQuery;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SearchAdvertiseVO extends PageQuery {
    /**
     * 广告商名称
     */
    private String adName;

    /**
     * 收取最高费用
     */
    private BigDecimal minExpense;

    /**
     * 收取最高费用
     */
    private BigDecimal maxExpense;

    /**
     * 广告开始时间
     */
    private LocalDateTime adStartDate;

    /**
     * 广告结束时间
     */
    private LocalDateTime adEndDate;


}
