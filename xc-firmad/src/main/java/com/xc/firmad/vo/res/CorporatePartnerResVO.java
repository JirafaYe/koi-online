package com.xc.firmad.vo.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CorporatePartnerResVO {

    /**
     * id
     */
    private Integer id;

    /**
     * 合作商名称
     */
    private String partnerName;

    /**
     * 品牌链接地址
     */
    private String uriBrand;

    /**
     * 合作商图片
     */
    private String fileId;
}
