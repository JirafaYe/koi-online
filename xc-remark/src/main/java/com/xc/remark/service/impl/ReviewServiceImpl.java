package com.xc.remark.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xc.api.client.media.MediaClient;
import com.xc.api.client.product.ProductClient;
import com.xc.api.client.trade.TradeClient;
import com.xc.api.client.user.UserClient;
import com.xc.api.dto.media.FileDTO;
import com.xc.api.dto.media.MediaDTO;
import com.xc.api.dto.product.SkuPageVO;
import com.xc.api.dto.product.SpuPageVO;
import com.xc.api.dto.user.res.UserInfoResVO;
import com.xc.common.domain.dto.PageDTO;
import com.xc.common.exceptions.BadRequestException;
import com.xc.common.utils.BeanUtils;
import com.xc.common.utils.CollUtils;
import com.xc.common.utils.StringUtils;
import com.xc.common.utils.UserContext;
import com.xc.remark.domain.dto.ReviewFormDTO;
import com.xc.remark.domain.po.Reply;
import com.xc.remark.domain.po.Review;
import com.xc.remark.domain.query.ReviewPageQuery;
import com.xc.remark.domain.vo.ReviewVO;
import com.xc.remark.mapper.ReplyMapper;
import com.xc.remark.mapper.ReviewMapper;
import com.xc.remark.service.ILikedRecordService;
import com.xc.remark.service.IReviewService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.sf.jsqlparser.expression.LongValue;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 评论表 服务实现类
 * </p>
 *
 * @author Koi
 * @since 2024-05-21
 */
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review> implements IReviewService {

    private final ProductClient productClient;

    private final UserClient userClient;

    private final MediaClient mediaClient;

    private final TradeClient tradeClient;

    private final ReplyMapper replyMapper;

    private final ILikedRecordService likedRecordService;
    @Override
    public void saveReview(ReviewFormDTO reviewDto) {
        Long userId = UserContext.getUser();
        Review review = BeanUtils.copyBean(reviewDto, Review.class);
        review.setUserId(userId);
        if(reviewDto.getHavePic()){
            String pics = reviewDto.getPics().stream().map(String::valueOf).collect(Collectors.joining(","));
            review.setPics(pics);
        }
        save(review);
    }

    @Override
    public PageDTO<ReviewVO> queryReviewPage(ReviewPageQuery query) {
        List<Long> productIds = null;
        if(StringUtils.isNotBlank(query.getProductName())){
            productIds = productClient.queryByName(query.getProductName()).stream().map(SpuPageVO::getId).collect(Collectors.toList());
            if(CollUtils.isEmpty(productIds)){
                return PageDTO.empty(0L, 0L);
            }
        }
        LocalDateTime begin = query.getBeginTime();
        LocalDateTime end = query.getEndTime();
        Page<Review> page = lambdaQuery().in(productIds != null, Review::getProductId, productIds)
                .gt(begin != null, Review::getCreateTime, begin)
                .lt(end != null, Review::getCreateTime, end)
                .page(query.toMpPageDefaultSortByCreateTimeDesc());
        List<Review> records = page.getRecords();
        if(CollUtils.isEmpty(records)){
            return PageDTO.empty(page);
        }
        Set<Long> userIds = new HashSet<>();
        Set<Long> pIds = new HashSet<>();
        Set<Long> oIds = new HashSet<>();
        Set<Long> fileIds = new HashSet<>();
        Set<Long> mediaIds = new HashSet<>();
        Set<Long> rIds = new HashSet<>();
        for (Review r : records) {
            rIds.add(r.getId());
            userIds.add(r.getUserId());
            pIds.add(r.getProductId());
            oIds.add(r.getOrderDetailId());
            if(r.getHaveVideo()){
                mediaIds.add(r.getVideo());
            }
            if(r.getHavePic()){
                fileIds.addAll(Arrays.stream(r.getPics().split(","))
                        .map(Long::parseLong).collect(Collectors.toSet()));
            }
        }
        Set<Long> bizLiked = likedRecordService.isBizLiked(rIds);
        List<UserInfoResVO> userInfos = userClient.getUserInfos(userIds);
        Map<Long, UserInfoResVO> userMap = new HashMap<>(userInfos.size());
        if(CollUtils.isNotEmpty(userInfos)){
            userMap = userInfos.stream().collect(Collectors.toMap(UserInfoResVO::getUserId, u -> u));
        }
        List<SpuPageVO> productInfos = productClient.queryById(pIds);
        Map<Long, String> productMap = new HashMap<>(productInfos.size());
        if(CollUtils.isNotEmpty(productInfos)){
            productMap = productInfos.stream().collect(Collectors.toMap(SpuPageVO::getId, SpuPageVO::getSpuName));
        }
        List<Long> skuIds = tradeClient.getSKuIds(oIds);
        Map<Long, SkuPageVO> orderMap = new HashMap<>(skuIds.size());
        List<SkuPageVO> skus = new ArrayList<>();
        if(CollUtils.isNotEmpty(skuIds)){
            skus = productClient.getSkuById(skuIds);
        }
        Iterator<Long> iterator = oIds.iterator();
        for (SkuPageVO sku : skus) {
            if(iterator.hasNext()){
                Long oId = iterator.next();
                orderMap.put(oId, sku);
            }
        }
        List<MediaDTO> mediaInfos = mediaClient.getMediaInfos(mediaIds);
        Map<Long, String> mediaMap = new HashMap<>(mediaInfos.size());
        if(CollUtils.isNotEmpty(mediaInfos)){
            mediaMap = mediaInfos.stream().collect(Collectors.toMap(MediaDTO::getId, MediaDTO::getMediaUrl));
        }
        List<FileDTO> fileInfos = mediaClient.getFileInfos(fileIds);
        Map<Long, String> fileMap = new HashMap<>(fileInfos.size());
        if(CollUtils.isNotEmpty(fileInfos)){
            fileMap = fileInfos.stream().collect(Collectors.toMap(FileDTO::getId, FileDTO::getFileUrl));
        }
        List<ReviewVO> voList = new ArrayList<>();
        for (Review review : records) {
            ReviewVO vo = BeanUtils.copyBean(review, ReviewVO.class);
            UserInfoResVO userInfo = userMap.get(review.getUserId());
            if(userInfo != null){
                vo.setUserName(userInfo.getAccount());
            }
            String spuName = productMap.get(review.getProductId());
            if(spuName != null){
                vo.setProductName(spuName);
            }
            SkuPageVO sku = orderMap.get(review.getOrderDetailId());
            if(sku != null){
                vo.setSpuName(sku.getSpuName());
                vo.setAttributes(sku.getAttributes());
                vo.setImage(sku.getImage());
            }
            if(review.getHaveVideo()){
                String mediaUrl = mediaMap.get(review.getVideo());
                if(mediaUrl != null){
                    vo.setMediaUrl(mediaUrl);
                }
            }
            if(review.getHavePic()){
                List<Long> pics = Arrays.stream(review.getPics().split(",")).map(Long::parseLong).collect(Collectors.toList());
                List<String> fileUrl = new ArrayList<>(pics.size());
                Map<Long, String> finalFileMap = fileMap;
                pics.forEach(p -> fileUrl.add(finalFileMap.get(p)));
                vo.setFileUrl(fileUrl);
            }
            vo.setLiked(bizLiked.contains(review.getId()));
            voList.add(vo);
        }
        return PageDTO.of(page, voList);
    }

    @Override
    @Transactional
    public void deleteReview(Long id) {
        Review review = getById(id);
        if(review == null){
            return;
        }
        if(!review.getUserId().equals(UserContext.getUser())){
            throw new BadRequestException("不可删除其他人的评论");
        }
        removeById(id);
        LambdaQueryWrapper<Reply> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Reply::getReviewId, id);
        replyMapper.delete(wrapper);
    }
}
