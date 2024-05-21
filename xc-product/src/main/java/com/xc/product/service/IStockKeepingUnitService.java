package com.xc.product.service;

import com.xc.common.domain.dto.PageDTO;
import com.xc.product.entity.StockKeepingUnit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.product.entity.query.SkuQuery;
import com.xc.product.entity.query.SpuQuery;
import com.xc.product.entity.vo.SkuPageVO;
import com.xc.product.entity.vo.SkuVO;
import com.xc.product.entity.vo.SpuPageVO;
import com.xc.product.entity.vo.SpuVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jirafa
 * @since 2024-05-13
 */
public interface IStockKeepingUnitService extends IService<StockKeepingUnit> {

    boolean createSku(SkuVO vo);

    boolean removeSku(Long id);

    boolean updateSku(SkuVO vo);

    PageDTO<SkuPageVO> queryPageBySpuId(SkuQuery query);

    Map<String , List<String>> getAttributes(Long skuId);
}
