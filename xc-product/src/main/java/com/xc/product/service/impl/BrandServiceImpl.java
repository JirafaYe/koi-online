package com.xc.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.api.client.media.MediaClient;
import com.xc.api.client.user.UserClient;
import com.xc.api.dto.media.FileDTO;
import com.xc.api.dto.user.res.UserInfoResVO;
import com.xc.common.domain.dto.PageDTO;
import com.xc.common.domain.query.PageQuery;
import com.xc.common.exceptions.BadRequestException;
import com.xc.common.exceptions.BizIllegalException;
import com.xc.common.exceptions.CommonException;
import com.xc.common.utils.CollUtils;
import com.xc.product.Constants.BrandConstants;
import com.xc.product.entity.Brand;
import com.xc.product.entity.query.BrandQuery;
import com.xc.product.entity.vo.BrandPageVO;
import com.xc.product.entity.vo.BrandVO;
import com.xc.product.mapper.BrandMapper;
import com.xc.product.service.IBrandService;
import com.xc.product.service.IStandardProductUnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
@RequiredArgsConstructor
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements IBrandService {

    @Resource
    IStandardProductUnitService spuService;

    @Resource
    UserClient userClient;

    private final MediaClient mediaClient;

    private final StringRedisTemplate redisTemplate;

    @Override
    @Transactional
    public Boolean updateBrand(BrandVO vo) {
        Long brandId = vo.getBrandId();
        if(brandId==null||brandId.equals(0L)){
            throw new CommonException("required brandId value");
        }
        cleanCache();
        Brand brand = new Brand();
        brand.setId(brandId);
        brand.setImageId(vo.getImageId());
        brand.setBrandName(vo.getBrandName());
        return updateById(brand);
    }

    @Override
    @Transactional
    public Boolean createBand(BrandVO vo) {
        Brand brand = new Brand();
        brand.setImageId(vo.getImageId());
        brand.setBrandName(vo.getBrandName());
        cleanCache();
        return save(brand);
    }

    @Override
    @Transactional
    public Boolean removeBrand(Long brandId) {
        if(brandId==null||brandId==0) {
            throw new CommonException("required brandId value");
        }
        if(!spuService.countByBrand(brandId).equals(0)){
            throw new BadRequestException("该品牌下包含商品");
        }
        boolean success = removeById(brandId);
        if(success){
            cleanCache();
        }
        return success;
    }

    @Override
    public PageDTO<BrandPageVO> queryBrandsByPage(BrandQuery q){
        List<BrandPageVO> voList = getBrandCache(q.getPageNo());
        Page<Brand> page = lambdaQuery().page(q.toMpPageDefaultSortByCreateTimeDesc());
        if(CollUtils.isNotEmpty(voList)){
            return PageDTO.of(page, voList);
        }
        PageDTO<BrandPageVO> result = PageDTO.empty(page);
        List<Brand> records = page.getRecords();
        if(!CollUtils.isEmpty(records)){
            Set<Long> userIds = records.stream().map(Brand::getCreater).collect(Collectors.toSet());
            Set<Long> imageIds = records.stream().map(Brand::getImageId).collect(Collectors.toSet());
            userIds.addAll(records.stream().map(Brand::getUpdater).collect(Collectors.toList()));

            HashMap<Long, String> userMap = new HashMap<>();
            for (UserInfoResVO userInfo : userClient.getUserInfos(userIds)) {
                userMap.put(userInfo.getUserId(),userInfo.getAccount());
            }
            Map<Long, String> fileMap = mediaClient.getFileInfos(imageIds).stream().collect(Collectors.toMap(FileDTO::getId, FileDTO::getFileUrl));

            if(!CollUtils.isEmpty(userMap)){
                voList = records.stream().map(obj -> new BrandPageVO(obj.getId(), obj.getBrandName(), obj.getImageId()
                        ,fileMap.get(obj.getImageId())
                        , userMap.get(obj.getCreater()), userMap.get(obj.getUpdater()))).collect(Collectors.toList());

                result=PageDTO.of(page, voList);
            }

        }
        cacheBrand(result);
        return result;
    }

    private List<BrandPageVO> getBrandCache(Integer pageNo){
        String res = redisTemplate.opsForValue().get(BrandConstants.PRODUCT_BRAND_PREFIX + pageNo);
        List<BrandPageVO> vos = JSON.parseArray(res, BrandPageVO.class);
        if(CollUtils.isEmpty(vos)){
            return CollUtils.emptyList();
        }
        return vos;
    }

    private void cacheBrand(PageDTO<BrandPageVO> result) {
        List<BrandPageVO> list = result.getList();
        Long pageNo = result.getPages();
        redisTemplate.opsForValue().set(BrandConstants.PRODUCT_BRAND_PREFIX + pageNo, JSON.toJSONString(list));
    }

    private void cleanCache() {
        int count = lambdaQuery().count();
        int size = PageQuery.DEFAULT_PAGE_SIZE;
        int cnt = count / size != 0 ? count / size + 1 : count / size;
        String key = BrandConstants.PRODUCT_BRAND_PREFIX;
        try {
            for (int i = 1; i <= cnt + 1; i++) {
                redisTemplate.delete(key + i);
            }
        } catch (Exception e) {
            throw new BizIllegalException("清理缓存出错");
        }
    }

}
