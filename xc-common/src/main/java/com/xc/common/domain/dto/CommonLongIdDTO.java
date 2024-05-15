package com.xc.common.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 公共Long id
 */
@Data
public class CommonLongIdDTO {

    /**
     * id
     */
    @NotNull(message = "id不能为空")
    private Long id;
}
