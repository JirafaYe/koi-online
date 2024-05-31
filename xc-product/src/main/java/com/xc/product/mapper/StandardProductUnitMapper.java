package com.xc.product.mapper;

import com.xc.product.entity.StandardProductUnit;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xc.product.entity.StockKeepingUnit;
import com.xc.product.entity.vo.SkuVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.HashMap;
import java.util.List;

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
    Integer updateNumWhenCreateSku(Long spuId,Long num);

    @Update("update standard_product_unit set num=num-#{num} where id = #{spuId}")
    Integer updateNumWhenRemoveSku(StockKeepingUnit sku);

    @Update("update standard_product_unit set min_price=COALESCE((select min(price) from stock_keeping_unit  where spu_id = #{spuId} and available=1 and deleted=0),0) where id = #{spuId}")
    Integer updateMinPriceWhenUpdateSku(Long spiId);

    @Update("update standard_product_unit set available = 1- available where id =#{id}")
    Integer updateAvailable(Long id);

    void updateSalesByIds(@Param("list") List<StandardProductUnit> list);
}
