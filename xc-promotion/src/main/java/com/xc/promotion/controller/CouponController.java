package com.xc.promotion.controller;


import com.xc.common.domain.dto.PageDTO;
import com.xc.promotion.domain.dto.CouponFormDTO;
import com.xc.promotion.domain.dto.CouponIssueFormDTO;
import com.xc.promotion.domain.query.CouponQuery;
import com.xc.promotion.domain.vo.CouponDetailVo;
import com.xc.promotion.domain.vo.CouponPageVO;
import com.xc.promotion.domain.vo.CouponVO;
import com.xc.promotion.service.ICouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 *
 * 管理端优惠券接口
 *
 * @author Koi
 * @since 2024-05-15
 */
@RestController
@RequestMapping("/coupons")
@Slf4j
@RequiredArgsConstructor
public class CouponController {

    private final ICouponService couponService;

    /**
     * 新增优惠券
     * @param dto
     */
    @PostMapping
    public void saveCoupon(@Valid @RequestBody CouponFormDTO dto){
        couponService.saveCoupon(dto);
    }

    /**
     * 分页查询优惠券
     * @param query
     * @return
     */
    @GetMapping("/page")
    public PageDTO<CouponPageVO> queryCouponByPage(CouponQuery query){
        return couponService.queryCouponByPage(query);
    }

    /**
     * 发放优惠券
     * @param dto
     */
    @PutMapping("/{id}/issue")
    public void beginIssue(@Valid @RequestBody CouponIssueFormDTO dto){
        couponService.beginIssue(dto);
    }

    /**
     * 根据id查询指定优惠券信息
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public CouponDetailVo queryCouponById(@PathVariable("id") Long id){
        return couponService.queryCouponById(id);
    }

    /**
     * 删除优惠券
     * @param id
     */
    @DeleteMapping("{id}")
    public void deleteById(@PathVariable("id") Long id){
        couponService.deleteById(id);
    }

    /**
     * 修改优惠券
     * @param id
     * @param dto
     */
    @PutMapping("{id}")
    public void updateCoupon(@PathVariable("id") Long id, @Valid @RequestBody CouponFormDTO dto){
        couponService.updateCoupon(dto);
    }

    /**
     * 暂停发放优惠券
     * @param id
     */
    @PutMapping("{id}/pause")
    public void pauseIssue(@PathVariable("id") Long id){
        couponService.pauseIssue(id);
    }

    /**
     * 查看发放中的优惠券
     */
    @GetMapping("/list")
    public List<CouponVO> queryIssuingCoupon(){
        return couponService.queryIssuingCoupon();
    }
}
