package com.xc.trade.entity;

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
 * @since 2024-05-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("shopping_chart")
public class ShoppingChart implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * spu
     */
    private Long spuId;

    /**
     * sku
     */
    private Long skuId;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 总金额
     */
    private Integer price;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;

    private Long creater;

    private Long updater;


}
