package com.xc.media.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * @author Koi
 * @since 2024-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("media")
public class Media implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 文件在云端的唯一标示，例如：aaa.mp4
     */
    @TableField("`key`")
    private String key;

    /**
     * 文件名称
     */
    private String filename;

    /**
     * 媒体播放地址
     */
    private String mediaUrl;

    /**
     * 视频时长，单位秒
     */
    private Float duration;

    /**
     * 视频大小，单位是字节
     */
    private Long size;


    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private Long creater;

    /**
     * 更新人
     */
    private Long updater;

    /**
     * 逻辑删除，默认0
     */
    private Integer deleted;


}
