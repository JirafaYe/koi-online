package com.xc.firmad.mapper;

import com.xc.firmad.entity.CorporatePartner;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xc.firmad.vo.res.CorporatePartnerResVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 合作企页表 Mapper 接口
 * </p>
 *
 * @author pengyalin
 * @since 2024年05月17日
 */
@Mapper
public interface CorporatePartnerMapper extends BaseMapper<CorporatePartner> {

    /**
     * 分页获取合作列表
     * @return
     */
    List<CorporatePartnerResVO> getCorporatePage();
}
