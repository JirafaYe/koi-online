package com.xc.log.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 日志信息返回VO
 */
@Data
public class LogInfoResVO {

    /**
     * 操作人
     */
    private String userName;


    /**
     * 模块标题
     */
    private String title;

    /**
     * 业务类型
     */
    private Integer businessType;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 主机地址
     */
    private String operIp;

    /**
     * 操作地点
     */
    private String operLocation;


    /**
     * 操作状态
     */
    private Integer status;


    /**
     * 操作时间
     */
    private LocalDateTime operTime;

}

