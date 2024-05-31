package com.xc.firmad.vo.res;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AdvertisePageResVO {
    /**
     * id
     */
    private Integer id;

    /**
     * 广告商名称
     */
    private String adName;

    /**
     * 收取费用
     */
    private BigDecimal expense;

    /**
     * 广告开始时间
     */
    private LocalDateTime adStartDate;

    /**
     * 外接地址
     */
    private String adUri;

    /**
     * 广告结束时间
     */
    private LocalDateTime adEndDate;


    /**
     * 上传图片链接
     */
    private String fileUrl;
}
