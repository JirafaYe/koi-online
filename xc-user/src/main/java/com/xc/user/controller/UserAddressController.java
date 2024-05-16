package com.xc.user.controller;


import com.xc.user.entity.UserAddress;
import com.xc.user.service.UserAddressService;
import com.xc.user.vo.req.AddDeliveryAddressReqVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 用户收货地址信息表 前端控制器
 * </p>
 *
 * @author pengyalin
 * @since 2024年05月16日
 */
@RestController
@RequestMapping("/userAddress")
public class UserAddressController {
    @Resource
    private UserAddressService userAddressService;

    /**
     * 添加收货地址
     */
    @PostMapping("addDeliveryAddress")
    public boolean addDeliveryAddress(@RequestBody @Valid AddDeliveryAddressReqVO vo){
        return userAddressService.addDeliveryAddress(vo);
    }

    /**
     * 地址列表查询
     */
    @PostMapping("listUserAddress")
    public List<UserAddress> listUserAddress(){
        return userAddressService.listUserAddress();
    }
}

