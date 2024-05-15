package com.xc.log.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author pengyalin
 * @since 2024年05月15日
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("log_info")
public class LogInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id	
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;

    /**
     * 操作的方法
     */
    @TableField("method")
    private String method;

    /**
     * 详细信息		
     */
    @TableField("message")
    private String message;

    /**
     * 事件发生时间
     */
    @TableField("event_time")
    private LocalDateTime eventTime;

    /**
     * 日志级别（1：info  2：error
     */
    @TableField("log_level")
    private Boolean logLevel;


}
