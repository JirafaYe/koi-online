package com.xc.product.service;

import com.xc.common.domain.dto.PageDTO;
import com.xc.product.entity.StockKeepingUnit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.product.entity.query.SkuQuery;
import com.xc.product.entity.query.SpuQuery;
import com.xc.product.entity.vo.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

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

    boolean createSku(SkuListVO vo);

    boolean removeSku(Long id);

    boolean updateSku(SkuVO vo);

    PageDTO<SkuPageVO> queryPageBySpuId(SkuQuery query);

    Map<String , Set<String>> getAttributes(Long spuID);

    SkuPageVO getSkuByAttributes(String attributes, Long spuID);

    List<SkuPageVO> getSkuById(List<Long> skuID);

    void updateSkuNum(Map<Long,Integer> numMap);

    boolean changeAvailable(Long id);
}
