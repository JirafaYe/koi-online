package com.xc.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.user.entity.UserAddress;
import com.xc.user.vo.req.AddDeliveryAddressReqVO;

import java.util.List;

/**
 * <p>
 * 用户收货地址信息表 服务类
 * </p>
 *
 * @author pengyalin
 * @since 2024年05月16日
 */
public interface UserAddressService extends IService<UserAddress> {

    /**
     * 新增地址
     * @param vo
     * @return
     */
    boolean addDeliveryAddress(AddDeliveryAddressReqVO vo);

    /**
     * 展示用户收货地址
     * @return
     */
    List<UserAddress> listUserAddress();
}
