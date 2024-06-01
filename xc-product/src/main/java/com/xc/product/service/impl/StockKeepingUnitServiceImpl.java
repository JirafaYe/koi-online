package com.xc.product.service.impl;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xc.api.client.media.MediaClient;
import com.xc.api.client.user.UserClient;
import com.xc.api.dto.media.FileDTO;
import com.xc.api.dto.user.res.UserInfoResVO;
import com.xc.common.domain.dto.PageDTO;
import com.xc.common.exceptions.BizIllegalException;
import com.xc.common.exceptions.CommonException;
import com.xc.common.utils.BeanUtils;
import com.xc.common.utils.CollUtils;
import com.xc.common.utils.JsonUtils;
import com.xc.common.utils.StringUtils;
import com.xc.product.Constants.RedisConstants;
import com.xc.product.entity.StandardProductUnit;
import com.xc.product.entity.StockKeepingUnit;
import com.xc.product.entity.query.SkuQuery;
import com.xc.product.entity.vo.*;
import com.xc.product.mapper.StandardProductUnitMapper;
import com.xc.product.mapper.StockKeepingUnitMapper;
import com.xc.product.service.IStockKeepingUnitService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.*;
import java.util.function.Function;
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

    @Autowired
    StringRedisTemplate redisTemplate;

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
        stockKeepingUnit.setPrice(vo.getPrice());
        boolean save = save(stockKeepingUnit);
        boolean update=true;
        if(save&&vo.getAvailable()){
            if(!(spuMapper.updateNumWhenCreateSku(vo.getSpuId(),vo.getNum())==1
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
    public boolean createSku(SkuListVO vo) {
        vo.getVos().forEach(p->{
            if(!JsonUtils.isJson(p.getAttributes())){
                throw new CommonException("attributes 需要为json格式");
            }
        });
        Set<Long> imageList = vo.getVos().stream().map(SkuDetailsVO::getImageId).collect(Collectors.toSet());
        List<Long> imgRes = mediaClient.judgeFileExist(imageList);
        if(imgRes.size()!=imageList.size()){
            imgRes.forEach(imageList::remove);
            throw new BizIllegalException("images不合法:"+imageList);
        }
        List<StockKeepingUnit> skuList = new LinkedList<>();
        vo.getVos().forEach(p->{
            StockKeepingUnit sku = BeanUtils.copyBean(p, StockKeepingUnit.class);
            sku.setSpuId(vo.getSpuId());
            skuList.add(sku);
            if(sku.isAvailable()){
                if(spuMapper.updateNumWhenCreateSku(vo.getSpuId(),p.getNum())!=1){
                    throw new BizIllegalException("更新spu数量失败");
                }
            }
        });

        if(!CollUtils.isEmpty(skuList)){
           if(! saveBatch(skuList)){
               throw new BizIllegalException("批量插入sku失败");
           }
        }
        if(spuMapper.updateMinPriceWhenUpdateSku(vo.getSpuId())!=1){
            throw new BizIllegalException("更新spu价格失败");
        }

        redisTemplate.delete(RedisConstants.SKU_PREFIX + vo.getSpuId());
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
            redisTemplate.delete(RedisConstants.SKU_PREFIX + sku.getSpuId());
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
        return updateByVO(vo,sku);
    }

    private boolean updateByVO(SkuVO vo,StockKeepingUnit sku){
        boolean updateNum=true;
        SkuVO template = BeanUtils.copyBean(vo, SkuVO.class);
        if(vo.getAvailable()){
            if(sku.isAvailable()){
                template.setNum(vo.getNum()-sku.getNum());
            }
            updateNum=spuMapper.updateNumWhenCreateSku(template.getSpuId(), template.getNum())==1;
        }else{
            if(sku.isAvailable()){
                updateNum=spuMapper.updateNumWhenRemoveSku(sku)==1;
            }
        }
        StockKeepingUnit stockKeepingUnit = BeanUtils.copyBean(vo, StockKeepingUnit.class);
        stockKeepingUnit.setPrice(vo.getPrice());
        boolean updateSku = updateById(stockKeepingUnit);
        boolean updatePrice=spuMapper.updateMinPriceWhenUpdateSku(vo.getSpuId())==1;
        if(!(updateNum&&updateSku&&updatePrice)){
            throw new CommonException("更新sku错误");
        }
        redisTemplate.delete(RedisConstants.SKU_PREFIX + vo.getSpuId());
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
                pageVO.setPrice(obj.getPrice());
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
        Map<String, Set<String>> attributes= new HashMap<>();
        String key = redisTemplate.opsForValue().get(RedisConstants.SKU_PREFIX + spuId);
        Map resultMaps=null;
        boolean redis=false;
        if(!StringUtils.isEmpty(key)){
            redisTemplate.expire(RedisConstants.SKU_PREFIX + spuId,Duration.ofMinutes(RedisConstants.EXPIRATION_MINUTES));
            resultMaps = JsonUtils.parse(key).toBean(Map.class);
            redis=true;
        }else {
            List<StockKeepingUnit> sku = lambdaQuery().eq(StockKeepingUnit::getSpuId, spuId)
                    .eq(StockKeepingUnit::isAvailable,true).list();
            if(!CollUtils.isEmpty(sku)){
                //HashMap<Long,HashMap<String,String>>
                resultMaps = sku.stream()
                        .collect(Collectors.toMap(
                                StockKeepingUnit::getId,
                                obj -> JsonUtils.parseObj(obj.getAttributes()).toBean(HashMap.class)
                        ));
                redisTemplate.opsForValue().set(RedisConstants.SKU_PREFIX+spuId,JsonUtils.parse(resultMaps).toString(), Duration.ofMinutes(RedisConstants.EXPIRATION_MINUTES));

            }
        }
        if(CollUtils.isEmpty(resultMaps)){
            return null;
        }
        List maps = new LinkedList<>(resultMaps.values());

        for (Object mapTmp : maps) {
            Map map;
            if(redis) {
               map = JsonUtils.toBean((JSONObject) mapTmp, HashMap.class);
            }else {
                map= (Map) mapTmp;
            }
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
        return attributes;
    }

    @Override
    public SkuPageVO getSkuByAttributes(String attributes, Long spuId) {
        if(!JsonUtils.isJson(attributes)){
            throw new CommonException("attributes 需要为json格式");
        }
        HashMap attributesObj = JsonUtils.parseObj(attributes).toBean(HashMap.class);
        String maps = redisTemplate.opsForValue().get(RedisConstants.SKU_PREFIX + spuId);
        Map resultMaps=null;
        if(!StringUtils.isEmpty(maps)){
            redisTemplate.expire(RedisConstants.SKU_PREFIX + spuId,Duration.ofMinutes(RedisConstants.EXPIRATION_MINUTES));
            resultMaps = JsonUtils.parse(maps).toBean(Map.class);
        }else {
            List<StockKeepingUnit> sku = lambdaQuery().eq(StockKeepingUnit::getSpuId, spuId)
                    .eq(StockKeepingUnit::isAvailable,true).list();
            if(CollUtils.isEmpty(sku)){
                throw new BizIllegalException("spu Id 不存在");
            }
            //HashMap<Long,HashMap<String,String>>
            resultMaps = sku.stream()
                    .collect(Collectors.toMap(
                            StockKeepingUnit::getId,
                            obj -> JsonUtils.parseObj(obj.getAttributes()).toBean(HashMap.class)
                    ));
            redisTemplate.opsForValue().set(RedisConstants.SKU_PREFIX+spuId,JsonUtils.parse(resultMaps).toString(), Duration.ofMinutes(RedisConstants.EXPIRATION_MINUTES));
        }
        Map finalResultMaps = resultMaps;
        List list = (List) resultMaps.keySet().stream().filter(p -> {
                    Map<String, String> attributesMap = (Map<String, String>) finalResultMaps.get(p);
                    return attributesMap.entrySet().equals(attributesObj.entrySet());
                }
        ).collect(Collectors.toList());

        SkuPageVO result = null;
        if (!CollUtils.isEmpty(list)) {
            Long matchId;
            Object o = list.get(0);
            if(o instanceof Long){
                matchId = (Long) o;
            }else if(o instanceof String){
                matchId = Long.valueOf((String) o);
            }else {
                throw new CommonException("无法转换类型");
            }
            StockKeepingUnit match = lambdaQuery()
                    .eq(StockKeepingUnit::getId, matchId)
                    .eq(StockKeepingUnit::isAvailable, true)
                    .one();

            if (match != null) {
                result = BeanUtils.copyBean(match, SkuPageVO.class);
                result.setPrice(match.getPrice());
                result.setSpuName(spuMapper.selectById(result.getSpuId()).getSpuName());

                List<FileDTO> fileDTOs = mediaClient.getFileInfos(Collections.singletonList(match.getImageId()));
                if (!CollUtils.isEmpty(fileDTOs)) {
                    result.setImage(fileDTOs.get(0).getFileUrl());
                }
            }
        }

        return result;
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
            Set<Long> spu = stockKeepingUnits.stream().map(StockKeepingUnit::getSpuId).collect(Collectors.toSet());
            List<StandardProductUnit> units = spuMapper.selectBatchIds(spu);

            List<Long> invalidSpu = units.stream().filter(p -> !p.isAvailable()).map(StandardProductUnit::getId).collect(Collectors.toList());
            if(!CollUtils.isEmpty(invalidSpu)){
                throw new BizIllegalException("不合法spu id:"+invalidSpu);
            }
            Map<Long, StandardProductUnit> spuMap = units.stream().collect(Collectors.toMap(
                    StandardProductUnit::getId,
                    Function.identity()
            ));
            Map<Long, String> map = mediaClient.getFileInfos(images).stream().collect(Collectors.toMap(
                    FileDTO::getId,
                    FileDTO::getFileUrl
            ));
            res = stockKeepingUnits.stream().map(obj -> {
                SkuPageVO pageVO = BeanUtils.copyBean(obj, SkuPageVO.class);
                pageVO.setPrice(obj.getPrice() );
                StandardProductUnit temp = spuMap.get(obj.getSpuId());
                pageVO.setSpuName(temp.getSpuName());
                pageVO.setCategoryId(temp.getCategoryId());
                pageVO.setImage(map.get(obj.getImageId()));
                return pageVO;
            }).collect(Collectors.toList());
        }

        return res;
    }

    @Override
    public boolean changeAvailable(Long id) {
        StockKeepingUnit sku = baseMapper.selectById(id);
        SkuVO skuVO = BeanUtils.copyBean(sku, SkuVO.class);
        skuVO.setAvailable(!sku.isAvailable());
        return updateByVO(skuVO,sku);
    }

    @Override
    @Transactional
    public void updateSkuNum(Map<Long,Integer> numMap) {
        HashMap<Long, Integer> spuMap = new HashMap<>();
        numMap.keySet().forEach(
                obj->{
                    StockKeepingUnit stockKeepingUnit = baseMapper.selectById(obj);
                    if(stockKeepingUnit!=null){
                        Long spuId = stockKeepingUnit.getSpuId();
                        SkuVO skuVO = BeanUtils.copyBean(stockKeepingUnit, SkuVO.class);
                        skuVO.setNum(stockKeepingUnit.getNum()+numMap.get(obj));
                        skuVO.setPrice(stockKeepingUnit.getPrice());
                        if(stockKeepingUnit.getNum()<=0){
                            throw new BizIllegalException("不合法修改");
                        }
                        updateByVO(skuVO,stockKeepingUnit);
                        if(!spuMap.containsKey(spuId)){
                            spuMap.put(spuId, numMap.get(obj) * (-1));
                        } else{
                            spuMap.put(spuId, spuMap.get(spuId) + numMap.get(obj) * (-1));
                        }
                    }
                }
        );
        List<StandardProductUnit> list = new ArrayList<>(spuMap.size());
        spuMap.forEach( (key, value) -> {
            StandardProductUnit spu = new StandardProductUnit();
            spu.setId(key);
            spu.setSales(value);
            list.add(spu);
        });
        spuMapper.updateSalesByIds(list);

    }

    boolean testifyImageId(Long id){
        List<Long> realId = mediaClient.judgeFileExist(Collections.singletonList(id));
        return realId.contains(id);
    }
}
