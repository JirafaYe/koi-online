package com.xc.trade.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xc.api.client.product.ProductClient;
import com.xc.api.dto.product.SkuPageVO;
import com.xc.common.domain.dto.PageDTO;
import com.xc.common.exceptions.CommonException;
import com.xc.common.utils.BeanUtils;
import com.xc.common.utils.CollUtils;
import com.xc.common.utils.UserContext;
import com.xc.trade.entity.po.ShoppingChart;
import com.xc.trade.entity.dto.ShoppingChartDTO;
import com.xc.trade.entity.query.ShoppingChartQuery;
import com.xc.trade.entity.vo.ShoppingChartVO;
import com.xc.trade.mapper.ShoppingChartMapper;
import com.xc.trade.service.IShoppingChartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jirafa
 * @since 2024-05-24
 */
@Service
public class ShoppingChartServiceImpl extends ServiceImpl<ShoppingChartMapper, ShoppingChart> implements IShoppingChartService {

    @Resource
    ProductClient productClient;

    @Override
    public boolean create(ShoppingChartVO vo) {
        vo.setId(null);
        ShoppingChart one = lambdaQuery().eq(ShoppingChart::getUserId, UserContext.getUser())
                .eq(ShoppingChart::getSkuId, vo.getSkuId()).one();
        if(one!=null){
            vo.setQuantity(one.getQuantity()+vo.getQuantity());
            vo.setId(one.getId());
            return update(vo);
        }
        return save(getShoppingChart(vo));
    }

    @Override
    public boolean update(ShoppingChartVO vo) {
        if(vo.getId()==null){
            throw new CommonException("id value required");
        }

        return updateById(getShoppingChart(vo));
    }
    ShoppingChart getShoppingChart(ShoppingChartVO vo){
        List<SkuPageVO> skuById = productClient.getSkuById(List.of(vo.getSkuId()));
        if(CollUtils.isEmpty(skuById)){
           throw new CommonException("sku id invalid");
        }
        SkuPageVO sku= skuById.get(0);
        if (sku.getNum() < vo.getQuantity()) {
            throw new CommonException("quantity invalid");
        }

        return BeanUtils.copyBean(vo, ShoppingChart.class)
                .setUserId(UserContext.getUser())
                .setSpuId(sku.getSpuId())
                .setPrice((int) (sku.getPrice() * 100));
    }

    @Override
    public boolean remove(List<Long> ids) {
        List<Long> list = lambdaQuery().eq(ShoppingChart::getUserId, UserContext.getUser()).list()
                .stream().map(ShoppingChart::getId).collect(Collectors.toList());
        ids.retainAll(list);
        return removeByIds(ids);
    }

    @Override
    public PageDTO<ShoppingChartDTO> pageQuery(ShoppingChartQuery query) {
        Page<ShoppingChart> page = lambdaQuery().eq(ShoppingChart::getUserId, UserContext.getUser())
                .page(query.toMpPageDefaultSortByCreateTimeDesc());
        List<ShoppingChart> records = page.getRecords();
        List<ShoppingChartDTO> res=null;
        if(!CollUtils.isEmpty(records)){
            Set<Long> skuIds = records.stream()
                    .map(ShoppingChart::getSkuId).collect(Collectors.toSet());

            Map<Long, SkuPageVO> skuMap = productClient.getSkuById(skuIds).stream().collect(Collectors.toMap(
                    SkuPageVO::getId,
                    Function.identity()
            ));

            res=records.stream().map(obj->{
                ShoppingChartDTO dto = BeanUtils.copyBean(obj, ShoppingChartDTO.class);
                SkuPageVO vo = skuMap.get(obj.getSkuId());
                if(vo!=null) {
                    dto.setImage(vo.getImage());
                    dto.setSpuName(vo.getSpuName());
                    dto.setAttributes(vo.getAttributes());
                    dto.setAvailable(vo.getAvailable());
                }else{
                    dto.setAvailable(false);
                }
                dto.setPrice((double) obj.getPrice() /100);
                return dto;
            }).collect(Collectors.toList());
        }
        return PageDTO.of(page,res);
    }
}
