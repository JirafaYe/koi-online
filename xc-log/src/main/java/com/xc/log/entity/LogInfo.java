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
 * @since 2024年05月20日
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
    private Long id;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 方法名称
     */
    @TableField("method")
    private String method;

    /**
     * 模块标题
     */
    @TableField("title")
    private String title;

    /**
     * 业务类型
     */
    @TableField("business_type")
    private Integer businessType;

    /**
     * 请求方式
     */
    @TableField("request_method")
    private String requestMethod;

    /**
     * 操作类别
     */
    @TableField("operatoe_type")
    private String operatoeType;

    /**
     * 请求url
     */
    @TableField("oper_url")
    private String operUrl;

    /**
     * 主机地址
     */
    @TableField("oper_ip")
    private String operIp;

    /**
     * 操作地点
     */
    @TableField("oper_location")
    private String operLocation;

    /**
     * 返回参数
     */
    @TableField("json_result")
    private String jsonResult;

    /**
     * 操作状态
     */
    @TableField("status")
    private Integer status;

    /**
     * 错误消息
     */
    @TableField("error_msg")
    private String errorMsg;

    /**
     * 操作时间
     */
    @TableField("oper_time")
    private LocalDateTime operTime;

    /**
     * 请求参数
     */
    @TableField("oper_param")
    private String operParam;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;
}
