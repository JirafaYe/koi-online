package com.xc.firmad.vo.req;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AddAdvertise {


    /**
     * 广告商名称
     */
    @NotNull
    private String adName;

    /**
     * 收取费用
     */
    @NotNull
    private BigDecimal expense;

    /**
     * 广告开始时间
     */
    @NotNull
    private LocalDateTime adStartDate;

    /**
     * 外接地址
     */

    private String adUri;

    /**
     * 广告结束时间
     */
    @NotNull
    private LocalDateTime adEndDate;

    /**
     * 上传图片文件id
     */
    private List<Long> fileIds;

}
