package com.xc.common.domain;

import com.xc.common.constants.Constant;
import lombok.Data;
import org.slf4j.MDC;

import static com.xc.common.constants.ErrorInfo.Code.FAILED;
import static com.xc.common.constants.ErrorInfo.Code.SUCCESS;
import static com.xc.common.constants.ErrorInfo.Msg.OK;

@Data
/**
 * 通用响应结果
 */
public class R<T> {
    /**
     * 业务状态码，200-成功，其它-失败
     */
    //@ApiModelProperty(value = "业务状态码，200-成功，其它-失败")
    private int code;
    /**
     * value : "响应消息"
     * example: "OK"
     */
    //@ApiModelProperty(value = "响应消息", example = "OK")
    private String msg;
    /**
     * value = "响应数据"
     */
    //@ApiModelProperty(value = "响应数据")
    private T data;

    public static R<Void> ok() {
        return new R<Void>(SUCCESS, OK, null);
    }

    public static <T> R<T> ok(T data) {
        return new R<>(SUCCESS, OK, data);
    }

    public static <T> R<T> error(String msg) {
        return new R<>(FAILED, msg, null);
    }

    public static <T> R<T> error(int code, String msg) {
        return new R<>(code, msg, null);
    }

    public R() {
    }

    public R(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public boolean success(){
        return code == SUCCESS;
    }

}