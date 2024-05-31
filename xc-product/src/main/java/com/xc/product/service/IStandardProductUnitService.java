package com.xc.product.service;

import com.xc.common.domain.dto.PageDTO;
import com.xc.product.entity.StandardProductUnit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.product.entity.dto.SpuPageDTO;
import com.xc.product.entity.query.SpuAdminQuery;
import com.xc.product.entity.query.SpuQuery;
import com.xc.product.entity.query.SpuUserQuery;
import com.xc.product.entity.vo.SpuDetailsVO;
import com.xc.product.entity.vo.SpuPageVO;
import com.xc.product.entity.vo.SpuVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jirafa
 * @since 2024-05-13
 */
public interface IStandardProductUnitService extends IService<StandardProductUnit> {
    Integer countByBrand(Long brandId);

    Integer countByCategory(Long id);

    boolean createSpu(SpuVO vo);

    boolean removeSpu(Long id);

    boolean updateSpu(SpuVO vo);

    SpuPageDTO<SpuPageVO> queryByPage(SpuQuery query);

    List<SpuPageVO> queryByName(String name);

    List<SpuPageVO> queryById(List<Long> ids);

    SpuDetailsVO queryById(Long spuId);

    boolean changeAvailable(Long spuId);

    SpuPageDTO<SpuPageVO> pageQuery(SpuAdminQuery query, boolean isAdmin);

    PageDTO<SpuPageVO> adminQuery(SpuAdminQuery query);
}
