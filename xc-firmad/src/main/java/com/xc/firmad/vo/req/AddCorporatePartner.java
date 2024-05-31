package com.xc.firmad.vo.req;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AddCorporatePartner {

    /**
     * 合作商名称
     */
    @NotNull(message = "名称不能为空")
    private String partnerName;

    /**
     * 品牌链接地址
     */
    @NotNull(message = "品牌链接不能为空")
    private String uriBrand;

    /**
     * 合作商图片
     */
    @NotNull(message = "图片不能为空")
    private Long fileId;

    /**
     * 备注
     */
    private String remark;
}
