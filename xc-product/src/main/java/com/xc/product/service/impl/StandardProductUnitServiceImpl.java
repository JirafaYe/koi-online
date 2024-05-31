package com.xc.product.service.impl;

import cn.hutool.core.lang.func.Func;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xc.api.client.media.MediaClient;
import com.xc.api.client.user.UserClient;
import com.xc.api.dto.media.FileDTO;
import com.xc.api.dto.media.MediaDTO;
import com.xc.api.dto.user.res.UserInfoResVO;
import com.xc.common.domain.dto.PageDTO;
import com.xc.common.exceptions.CommonException;
import com.xc.common.utils.BeanUtils;
import com.xc.common.utils.CollUtils;
import com.xc.common.utils.DateUtils;
import com.xc.common.utils.StringUtils;
import com.xc.product.controller.SpuController;
import com.xc.product.entity.Brand;
import com.xc.product.entity.Category;
import com.xc.product.entity.StandardProductUnit;
import com.xc.product.entity.StockKeepingUnit;
import com.xc.product.entity.dto.SpuPageDTO;
import com.xc.product.entity.query.SpuAdminQuery;
import com.xc.product.entity.query.SpuQuery;
import com.xc.product.entity.query.SpuUserQuery;
import com.xc.product.entity.vo.BrandPageVO;
import com.xc.product.entity.vo.SkuPageVO;
import com.xc.product.entity.vo.SpuPageVO;
import com.xc.product.entity.vo.SpuVO;
import com.xc.product.mapper.BrandMapper;
import com.xc.product.mapper.CategoryMapper;
import com.xc.product.mapper.StandardProductUnitMapper;
import com.xc.product.mapper.StockKeepingUnitMapper;
import com.xc.product.service.IStandardProductUnitService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import java.time.LocalDateTime;
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
public class StandardProductUnitServiceImpl extends ServiceImpl<StandardProductUnitMapper, StandardProductUnit> implements IStandardProductUnitService {

    @Resource
    MediaClient mediaClient;

    @Resource
    UserClient userClient;

    @Resource
    CategoryMapper categoryMapper;

    @Resource
    BrandMapper brandMapper;

    @Resource
    StockKeepingUnitMapper stockKeepingUnitMapper;
    @Autowired
    private StandardProductUnitMapper standardProductUnitMapper;

    @Override
    public Integer countByBrand(Long brandId) {
        return lambdaQuery().eq(StandardProductUnit::getBrandId,brandId).count();
    }

    @Override
    public Integer countByCategory(Long id) {
        return lambdaQuery().eq(StandardProductUnit::getCategoryId,id).count();
    }

    @Override
    public boolean createSpu(SpuVO vo) {
        vo.setId(null);
        boolean res=false;
        vo = trimSpuVO(vo);
        if(vo!=null){
            res=save(BeanUtils.copyBean(vo,StandardProductUnit.class));
        }
        return res;
    }

    @Override
    public boolean removeSpu(Long id) {
        stockKeepingUnitMapper.deleteBatchIds(
                stockKeepingUnitMapper.selectList(new LambdaQueryWrapper<StockKeepingUnit>()
                .eq(StockKeepingUnit::getSpuId, id))
                        .stream().map(StockKeepingUnit::getId).collect(Collectors.toList()));
        return removeById(id);
    }

    @Override
    public boolean updateSpu(SpuVO vo) {
        if(vo.getId()==null||vo.getId().equals(0L)){
            throw new CommonException("required value of spu");
        }
        boolean res=false;
        vo = trimSpuVO(vo);
        if(vo!=null){
            res=updateById(BeanUtils.copyBean(vo,StandardProductUnit.class));
        }
        return res;
    }

    @Override
    public List<SpuPageVO> queryByName(String name) {
        List<StandardProductUnit> spu = baseMapper.selectList(new LambdaQueryWrapper<StandardProductUnit>()
                .like(StandardProductUnit::getSpuName, name));

        return convert2voList(spu);
    }

    public List<SpuPageVO> convert2voList(List<StandardProductUnit> spu){
        List<SpuPageVO> res=CollUtils.emptyList();
        if(!CollUtils.isEmpty(spu)){
            Set<Long> users = spu.stream().map(StandardProductUnit::getCreater).collect(Collectors.toSet());
            Set<Long> video = spu.stream().map(StandardProductUnit::getMainVideoId).filter(Objects::nonNull).collect(Collectors.toSet());
            HashSet<Long> images = new HashSet<>();
            HashMap<Long, List<Long>> imagesMapMain = new HashMap<>();
            HashMap<Long, List<Long>> imagesMapContents = new HashMap<>();

            for (StandardProductUnit unit : spu) {
                List<Long> mains = splitImagesId(unit.getMainImagesId());
                imagesMapMain.put(unit.getId(), mains);
                List<Long> contents = splitImagesId(unit.getContentImagesId());
                imagesMapContents.put(unit.getId(), contents);
                images.addAll(mains);
                images.addAll(contents);
            }

            Map<Long, String> imageMap = mediaClient.getFileInfos(images).stream().collect(Collectors.toMap(
                    FileDTO::getId,
                    FileDTO::getFileUrl
            ));
            Map<Long, String> userMap = userClient.getUserInfos(users).stream().collect(Collectors.toMap(
                    UserInfoResVO::getUserId,
                    UserInfoResVO::getAccount
            ));
            Map<Long, String> realVideo;
            if(!CollUtils.isEmpty(video)){
                 realVideo= mediaClient.getMediaInfos(video).stream().collect(Collectors.toMap(
                        MediaDTO::getId,
                        MediaDTO::getMediaUrl
                ));
            } else {
                realVideo = null;
            }


            LocalDateTime daysBefore = LocalDateTime.now().minusDays(7);
            res = spu.stream().map(obj -> {
                SpuPageVO spuPageVO = BeanUtils.copyBean(obj, SpuPageVO.class);
                spuPageVO.setContentImagesUrl(getUrlList(imagesMapContents.get(obj.getId()), imageMap));
                spuPageVO.setMainImagesUrl(getUrlList(imagesMapMain.get(obj.getId()), imageMap));
                spuPageVO.setCreater(userMap.get(obj.getCreater()));
                if(!obj.getCreateTime().isBefore(daysBefore)){
                    spuPageVO.setUpToDate(true);
                }
                if(obj.getMainVideoId()!=null) {
                    spuPageVO.setMainVideoUrl(realVideo != null ? realVideo.get(obj.getMainVideoId()) : null);
                }
                return spuPageVO;
            }).collect(Collectors.toList());
        }
        return res;
    }

    @Override
    public List<SpuPageVO> queryById(List<Long> ids) {
        List<SpuPageVO> res=List.of();
        List<StandardProductUnit> units = baseMapper.selectBatchIds(ids);
        if(!CollUtils.isEmpty(units)){
            res=units.stream().map(obj->{
                SpuPageVO spuPageVO = new SpuPageVO();
                spuPageVO.setId(obj.getId());
                spuPageVO.setSpuName(obj.getSpuName());
                return spuPageVO;
            }).collect(Collectors.toList());
        }
        return res;
    }

    @Override
    public SpuPageDTO<SpuPageVO> queryByPage(SpuQuery query) {
        LambdaQueryChainWrapper<StandardProductUnit> wrapper= lambdaQuery();
        if(query.getBrandId()!=null){
            wrapper.eq(StandardProductUnit::getBrandId,query.getBrandId());
        } else if (query.getCategoryId() != null) {
            wrapper.eq(StandardProductUnit::getCategoryId,query.getCategoryId());
        }
        Page<StandardProductUnit> page = wrapper.page(query.toMpPageDefaultSortByCreateTimeDesc());
        return generateSpuPageDTO(page);
    }

    @Override
    public boolean changeAvailable(Long spuId) {
        return standardProductUnitMapper.updateAvailable(spuId)==1;
    }

    public List<Long> splitImagesId(String ids){
        String[] strings = StringUtils.splitToArray(ids, ",");
        int len= Math.min(strings.length, 8);
        LinkedList<Long> idList = new LinkedList<>();
        for (int i = 0; i < len; i++) {
            idList.add(Long.valueOf(strings[i]));
        }
        return idList;
    }

    @Override
    public SpuPageDTO<SpuPageVO> pageQuery(SpuAdminQuery query, boolean isAdmin) {
        LambdaQueryChainWrapper<StandardProductUnit> chainWrapper = lambdaQuery();
        chainWrapper
                .like(!StringUtils.isEmpty(query.getSpuName()), StandardProductUnit::getSpuName,query.getSpuName())
                .eq(StandardProductUnit::isAvailable, "1");
        if(isAdmin){
            chainWrapper
                    .eq(query.getAvailable()!=null,StandardProductUnit::isAvailable,query.getAvailable())
                    .ge(query.getStartTime()!=null,StandardProductUnit::getCreateTime,query.getStartTime())
                    .le(query.getStartTime()!=null,StandardProductUnit::getCreateTime,query.getEndTime());
        }

        Page<StandardProductUnit> page = chainWrapper.page(query.toMpPage());
        return generateSpuPageDTO(page);
    }

    @Nullable
    private SpuPageDTO<SpuPageVO> generateSpuPageDTO(Page<StandardProductUnit> page) {
        List<StandardProductUnit> records = page.getRecords();
        SpuPageDTO<SpuPageVO> res=null;
        List<SpuPageVO> spuPageVOS = convert2voList(records);
        if(!CollUtils.isEmpty(spuPageVOS)){
            long unAvailable = spuPageVOS.stream().filter(p -> !p.getAvailable()).count();
            long available = spuPageVOS.stream().filter(SpuPageVO::getAvailable).count();
            PageDTO<SpuPageVO> spuPageVOPageDTO = PageDTO.of(page, spuPageVOS);
            res=new SpuPageDTO<>(spuPageVOPageDTO, (int) unAvailable, (int) available);
        }
        return res;
    }

    @Override
    public PageDTO<SpuPageVO> adminQuery(SpuAdminQuery query) {
        return null;
    }

    public String getUrlList(List<Long> ids, Map<Long, String> urlMap){
        List<String> urlResult = new ArrayList<>();
        for(Long id:ids){
            urlResult.add(urlMap.get(id));
        }
        return String.join(",",urlResult);
    }

    public boolean ifCategoryExist(Long id) {
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id);

        return !categoryMapper.selectCount(wrapper).equals(0);
    }

    public boolean ifBrandExist(Long id) {
        QueryWrapper<Brand> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id);

        return !brandMapper.selectCount(wrapper).equals(0);
    }

    public SpuVO trimSpuVO(SpuVO vo){
        List<Long> main = splitImagesId(vo.getMainImagesId());
        List<Long> content = splitImagesId(vo.getContentImagesId());

        if(!CollUtils.isEmpty(main)&&!CollUtils.isEmpty(content)
                &&!ifCategoryExist(vo.getCategoryId())&&!ifBrandExist(vo.getBrandId())){
            return null;
        }

        HashSet<Long> ids = new HashSet<>();
        ids.addAll(main);
        ids.addAll(content);
        List<Long> realIds = mediaClient.judgeFileExist(ids);

        main.retainAll(realIds);
        content.retainAll(realIds);


        vo.setMainImagesId(main.stream().map(Objects::toString).collect(Collectors.joining(",")));
        vo.setContentImagesId(content.stream().map(Objects::toString).collect(Collectors.joining(",")));

        if(vo.getMainVideoId()!=null){
            List<Long> longs = mediaClient.judgeMediaExist(List.of(vo.getMainVideoId()));

            vo.setMainVideoId(!longs.isEmpty() ?longs.get(0):null);
        }

        return vo;
    }
}
