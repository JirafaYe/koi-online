package com.xc.user.vo.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateUserStatusVO {
    /**
     * 用户id
     */
    @NotBlank(message = "id不能为空")
    private Long id;
    /**
     * 修改的状态 0 禁用  1 启用
     */
    @NotBlank(message = "状态不能为空")
    private Integer status;
}
