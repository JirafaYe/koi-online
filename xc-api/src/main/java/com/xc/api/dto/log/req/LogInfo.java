package com.xc.api.dto.log.req;
;
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
public class LogInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 方法名称
     */
    private String method;

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
     * 操作类别
     */
    private String operatoeType;

    /**
     * 请求url
     */
    private String operUrl;

    /**
     * 主机地址
     */
    private String operIp;

    /**
     * 操作地点
     */
    private String operLocation;

    /**
     * 返回参数
     */
    private String jsonResult;

    /**
     * 操作状态
     */
    private Integer status;

    /**
     * 错误消息
     */
    private String errorMsg;

    /**
     * 操作时间
     */
    private LocalDateTime operTime;

    /**
     * 请求参数
     */
    private String operParam;

    /**
     * 备注
     */
    private String remark;
}
