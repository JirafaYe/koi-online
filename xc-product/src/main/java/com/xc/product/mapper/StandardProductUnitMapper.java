package com.xc.product.mapper;

import com.xc.product.entity.StandardProductUnit;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xc.product.entity.StockKeepingUnit;
import com.xc.product.entity.vo.SkuVO;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jirafa
 * @since 2024-05-13
 */
public interface StandardProductUnitMapper extends BaseMapper<StandardProductUnit> {
    @Update("update standard_product_unit set num=num+#{num} where id = #{spuId}")
    Integer updateNumWhenCreateSku(SkuVO vo);

    @Update("update standard_product_unit set num=num-#{num} where id = #{spuId}")
    Integer updateNumWhenRemoveSku(StockKeepingUnit sku);
}
