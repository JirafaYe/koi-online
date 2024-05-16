package com.xc.api.dto.log.req;

import lombok.Data;

/**
 * 日志信息请求参数
 */
@Data
public class IogInfoReqDTO {

    /**
     * 操作的方法
     */
    private String method;

    /**
     * 详细信息
     */
    private String message;

    /**
     * 类型 1 info 2 error
     */
    private Integer type;

    public IogInfoReqDTO() {
    }

    public IogInfoReqDTO(String method,String message) {
        this.method = method;
        this.message = message;
    }

    public IogInfoReqDTO(String method, String message, Integer type) {
        this.method = method;
        this.message = message;
        this.type = type;
    }
}

