package com.xc.firmad.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 合作企页表
 * </p>
 *
 * @author pengyalin
 * @since 2024年05月17日
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("corporate_partner")
public class CorporatePartner implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 合作商名称
     */
    @TableField("partner_name")
    private String partnerName;

    /**
     * 品牌链接地址
     */
    @TableField("uri_brand")
    private String uriBrand;

    /**
     * 合作商图片所关联的文件id
     */
    @TableField("file_ids")
    private String fileIds;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

}
