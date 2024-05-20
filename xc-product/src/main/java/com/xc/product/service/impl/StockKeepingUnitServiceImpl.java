package com.xc.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xc.api.client.media.MediaClient;
import com.xc.common.exceptions.CommonException;
import com.xc.common.utils.BeanUtils;
import com.xc.common.utils.JsonUtils;
import com.xc.product.entity.StandardProductUnit;
import com.xc.product.entity.StockKeepingUnit;
import com.xc.product.entity.vo.SkuVO;
import com.xc.product.mapper.StandardProductUnitMapper;
import com.xc.product.mapper.StockKeepingUnitMapper;
import com.xc.product.service.IStockKeepingUnitService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jirafa
 * @since 2024-05-13
 */
@Service
public class StockKeepingUnitServiceImpl extends ServiceImpl<StockKeepingUnitMapper, StockKeepingUnit> implements IStockKeepingUnitService {
    @Resource
    MediaClient mediaClient;

    @Resource
    StandardProductUnitMapper spuMapper;

    @Override
    @Transactional
    public boolean createSku(SkuVO vo) {
        if(!testifyImageId(vo.getImageId())){
            throw new CommonException("非法imageId");
        }
        if(!JsonUtils.isJson(vo.getAttributes())){
            throw new CommonException("attributes 需要为json格式");
        }
        Integer spuNum = spuMapper.selectCount(new LambdaQueryWrapper<StandardProductUnit>().eq(StandardProductUnit::getId, vo.getSpuId()));
        if(spuNum.equals(0)){
            throw new CommonException("非法spuId");
        }
        boolean save = save(BeanUtils.copyBean(vo, StockKeepingUnit.class));
        boolean update=false;
        if(save&&vo.getAvailable()){
            Integer i = spuMapper.updateNumWhenCreateSku(vo);
            update=i==1;
        }

        if(!(save&&update)){
            throw new CommonException("创建sku异常");
        }
        return true;
    }

    @Override
    @Transactional
    public boolean removeSku(Long id) {
        StockKeepingUnit sku = baseMapper.selectById(id);
        if(sku!=null){
            boolean update=true;
            if(sku.isAvailable()) {
                update = spuMapper.updateNumWhenRemoveSku(sku)==1;
            }
            boolean remove = removeById(id);
            if(!(remove&&update)){
                throw new CommonException("删除失败");
            }
            return true;
        }
        throw new CommonException("SKU Id 不存在");
    }

    boolean testifyImageId(Long id){
        List<Long> realId = mediaClient.judgeFileExist(Collections.singletonList(id));
        return realId.contains(id);
    }
}
