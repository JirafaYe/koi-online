package com.xc.firmad.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 广告
 * </p>
 *
 * @author pengyalin
 * @since 2024年05月18日
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("advertise")
public class Advertise implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 广告商名称
     */
    @TableField("ad_name")
    private String adName;

    /**
     * 收取费用
     */
    @TableField("expense")
    private BigDecimal expense;

    /**
     * 广告开始时间
     */
    @TableField("ad_start_date")
    private LocalDateTime adStartDate;

    /**
     * 外接地址
     */
    @TableField("ad_uri")
    private String adUri;

    /**
     * 广告结束时间
     */
    @TableField("ad_end_date")
    private LocalDateTime adEndDate;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 上传图片文件id
     */
    @TableField("file_ids")
    private String fileIds;


}
