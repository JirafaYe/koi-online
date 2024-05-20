package com.xc.user.vo.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateUserStatusVO {
    /**
     * 用户id
     */
    @NotNull(message = "id不能为空")
    private Long id;
    /**
     * 修改的状态 0 禁用  1 启用
     */
    @NotNull(message = "状态不能为空")
    private Integer status;
}
