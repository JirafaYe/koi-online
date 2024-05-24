package com.xc.product.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xc.api.client.media.MediaClient;
import com.xc.api.client.user.UserClient;
import com.xc.api.dto.media.FileDTO;
import com.xc.api.dto.user.res.UserInfoResVO;
import com.xc.common.domain.dto.PageDTO;
import com.xc.common.exceptions.CommonException;
import com.xc.common.utils.BeanUtils;
import com.xc.common.utils.CollUtils;
import com.xc.common.utils.JsonUtils;
import com.xc.product.entity.StandardProductUnit;
import com.xc.product.entity.StockKeepingUnit;
import com.xc.product.entity.query.SkuQuery;
import com.xc.product.entity.vo.SkuPageVO;
import com.xc.product.entity.vo.SkuVO;
import com.xc.product.entity.vo.SpuPageVO;
import com.xc.product.mapper.StandardProductUnitMapper;
import com.xc.product.mapper.StockKeepingUnitMapper;
import com.xc.product.service.IStockKeepingUnitService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

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
    UserClient userClient;

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
        StockKeepingUnit stockKeepingUnit = BeanUtils.copyBean(vo, StockKeepingUnit.class);
        stockKeepingUnit.setPrice((int) (vo.getPrice()*100));
        boolean save = save(stockKeepingUnit);
        boolean update=true;
        if(save&&vo.getAvailable()){
//            Integer i = spuMapper.updateNumWhenCreateSku(vo);
//            update=i==1;
//            update=1==spuMapper.updateMinPriceWhenUpdateSku(vo.getSpuId());
            if(!(spuMapper.updateNumWhenCreateSku(vo)==1
                    &&spuMapper.updateMinPriceWhenUpdateSku(vo.getSpuId())==1)){
                update=false;
            }
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
//                update = spuMapper.updateNumWhenRemoveSku(sku)==1;
                if(!(spuMapper.updateNumWhenRemoveSku(sku)==1)){
                    update=false;
                }
            }
            boolean remove = removeById(id);
            boolean updatePrice=true;
            if(sku.isAvailable()){
                updatePrice=spuMapper.updateMinPriceWhenUpdateSku(sku.getSpuId())==1;
            }

            if(!(remove&&update&&updatePrice)){
                throw new CommonException("删除失败");
            }
            return true;
        }
        throw new CommonException("SKU Id 不存在");
    }

    @Override
    @Transactional
    public boolean updateSku(SkuVO vo) {
        if(vo.getId()==null){
            throw new CommonException("SKuId不得为空");
        }
        if(!testifyImageId(vo.getImageId())){
            throw new CommonException("非法imageId");
        }
        StockKeepingUnit sku = baseMapper.selectById(vo.getId());
        if(sku==null){
            throw new CommonException("skuId不正确");
        }
        if(!JsonUtils.isJson(vo.getAttributes())){
            throw new CommonException("attributes 需要为json格式");
        }
        boolean updateNum=true;
        SkuVO template = BeanUtils.copyBean(vo, SkuVO.class);
        if(vo.getAvailable()){
            if(sku.isAvailable()){
                template.setNum(vo.getNum()-sku.getNum());
            }
            updateNum=spuMapper.updateNumWhenCreateSku(template)==1;
        }else{
            if(sku.isAvailable()){
                updateNum=spuMapper.updateNumWhenRemoveSku(sku)==1;
            }
        }
        StockKeepingUnit stockKeepingUnit = BeanUtils.copyBean(vo, StockKeepingUnit.class);
        stockKeepingUnit.setPrice((int) (vo.getPrice()*100));
        boolean updateSku = updateById(stockKeepingUnit);
        boolean updatePrice=spuMapper.updateMinPriceWhenUpdateSku(vo.getSpuId())==1;
        if(!(updateNum&&updateSku&&updatePrice)){
            throw new CommonException("更新sku错误");
        }
        return true;
    }

    @Override
    public PageDTO<SkuPageVO> queryPageBySpuId(SkuQuery query) {
        Page<StockKeepingUnit> page = lambdaQuery()
                .eq(StockKeepingUnit::getSpuId, query.getSpuId())
                .eq(StockKeepingUnit::isAvailable, query.isAvailable())
                .page(query.toMpPageDefaultSortByCreateTimeDesc());
        List<StockKeepingUnit> records = page.getRecords();
        PageDTO<SkuPageVO> res = new PageDTO<>();
        if(!CollUtils.isEmpty(records)){
            Set<Long> ids = records.stream().map(StockKeepingUnit::getImageId).collect(Collectors.toSet());
            Set<Long> creaters = records.stream().map(StockKeepingUnit::getCreater).collect(Collectors.toSet());
            Set<Long> spuIds = records.stream().map(StockKeepingUnit::getSpuId).collect(Collectors.toSet());
            Map<Long, String> userMap = userClient.getUserInfos(creaters).stream().collect(Collectors.toMap(
                    UserInfoResVO::getUserId,
                    UserInfoResVO::getAccount
            ));
            Map<Long, String> imageMap = mediaClient.getFileInfos(ids).stream().collect(Collectors.toMap(
                    FileDTO::getId,
                    FileDTO::getFileUrl
            ));

            Map<Long, String> spuMap=spuMapper.selectBatchIds(spuIds).stream().collect(Collectors.toMap(
               StandardProductUnit::getId,
               StandardProductUnit::getSpuName
            ));
            List<SkuPageVO> list = records.stream().map(obj -> {
                SkuPageVO pageVO = BeanUtils.copyBean(obj, SkuPageVO.class);
                pageVO.setPrice((double) obj.getPrice() /100);
                pageVO.setImage(imageMap.get(obj.getImageId()));
                pageVO.setCreaterName(userMap.get(obj.getCreater()));
                pageVO.setSpuName(spuMap.get(pageVO.getSpuId()));
                return pageVO;
            }).collect(Collectors.toList());

            res=PageDTO.of(page,list);
        }
        return res;
    }

    @Override
    public Map<String, Set<String>> getAttributes(Long spuId) {
        List<StockKeepingUnit> sku = lambdaQuery().eq(StockKeepingUnit::getSpuId, spuId)
                .eq(StockKeepingUnit::isAvailable,true).list();
        Map<String, Set<String>> attributes= new HashMap<>();
        if(!CollUtils.isEmpty(sku)){
            List<HashMap> maps = sku.stream().map(obj
                    -> JsonUtils.parseObj(obj.getAttributes()).toBean(HashMap.class)).collect(Collectors.toList());
            for (HashMap map : maps) {
                map.keySet().forEach(obj->{
                    if(!attributes.containsKey(obj)){
                        attributes.put((String) obj,new HashSet<>(Collections.singletonList((String) map.get(obj))));
                    }else {
                        Set<String> set = attributes.get(obj);
                        set.add((String) map.get(obj));
                        attributes.put((String) obj,set);
                    }
                });
            }
        }
        return attributes;
    }

    @Override
    public SkuPageVO getSkuByAttributes(String attributes, Long spuId) {
        if(!JsonUtils.isJson(attributes)){
            throw new CommonException("attributes 需要为json格式");
        }
        StockKeepingUnit match = lambdaQuery().eq(StockKeepingUnit::getSpuId, spuId)
                .eq(StockKeepingUnit::isAvailable, true).eq(StockKeepingUnit::getAttributes, attributes).one();
        SkuPageVO vo=BeanUtils.copyBean(match,SkuPageVO.class);
        if(vo!=null) {
            vo.setPrice((double) match.getPrice() / 100);
            vo.setSpuName(spuMapper.selectById(vo.getSpuId()).getSpuName());
            mediaClient.getFileInfos(Collections.singletonList(match.getImageId())).stream()
                    .findFirst().ifPresent(fileDTO -> vo.setImage(fileDTO.getFileUrl()));
        }
        return vo;
    }

    @Override
    public List<SkuPageVO> getSkuById(List<Long> skuID) {
        List<StockKeepingUnit> stockKeepingUnits = baseMapper.selectList(
                new LambdaQueryWrapper<StockKeepingUnit>()
                        .in(StockKeepingUnit::getId, skuID)
        );
        List<SkuPageVO> res=null;
        if(!CollUtils.isEmpty(stockKeepingUnits)) {
            Set<Long> images = stockKeepingUnits.stream().map(StockKeepingUnit::getImageId).collect(Collectors.toSet());
            Map<Long, String> map = mediaClient.getFileInfos(images).stream().collect(Collectors.toMap(
                    FileDTO::getId,
                    FileDTO::getFileUrl
            ));
            res = stockKeepingUnits.stream().map(obj -> {
                SkuPageVO pageVO = BeanUtils.copyBean(obj, SkuPageVO.class);
                pageVO.setPrice((double) obj.getPrice() / 100);
                pageVO.setSpuName(spuMapper.selectById(pageVO.getSpuId()).getSpuName());
                pageVO.setImage(map.get(obj.getImageId()));
                return pageVO;
            }).collect(Collectors.toList());
        }

        return res;
    }

    boolean testifyImageId(Long id){
        List<Long> realId = mediaClient.judgeFileExist(Collections.singletonList(id));
        return realId.contains(id);
    }
}
