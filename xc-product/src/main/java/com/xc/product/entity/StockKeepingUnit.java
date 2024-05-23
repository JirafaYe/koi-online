package com.xc.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author jirafa
 * @since 2024-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("stock_keeping_unit")
public class StockKeepingUnit implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 所属spu id
     */
    private Long spuId;

    private Long imageId;

    /**
     * 价格
     */
    private double price;

    /**
     * 库存
     */
    private Long num;

    /**
     * json储存的特征描述
     */
    private String attributes;

    private boolean available;

    private Integer deleted;

    private Long creater;

    private Long updater;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;


}
