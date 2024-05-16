package com.xc.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
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
@TableName("category")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 父目录，为空则为一级目录
     */
    private Long parentId;

    private String categoryName;

    /**
     * 指定顺序
     */
    private Integer sequence;

    /**
     * 不为空则已删除
     */
    private Integer deleted;

    private Long creater;

    private Long updater;

    private LocalDateTime updatedTime;

    private LocalDateTime createTime;


}
