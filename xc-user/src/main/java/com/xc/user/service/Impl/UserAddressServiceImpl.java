package com.xc.user.service.Impl;
import java.util.List;


import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.common.utils.UserContext;
import com.xc.user.entity.UserAddress;
import com.xc.user.mapper.UserAddressMapper;
import com.xc.user.service.UserAddressService;
import com.xc.user.vo.req.AddDeliveryAddressReqVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 用户收货地址信息表 服务实现类
 * </p>
 *
 * @author pengyalin
 * @since 2024年05月16日
 */
@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress> implements UserAddressService {

    @Resource
    private UserAddressMapper userAddressMapper;
    @Override
    public boolean addDeliveryAddress(AddDeliveryAddressReqVO vo) {
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(UserContext.getUser());
        userAddress.setName(vo.getName());
        userAddress.setPhone(vo.getPhone());
        userAddress.setArea(vo.getArea());
        userAddress.setAddress(vo.getAdress());
        return this.save(userAddress);
    }

    @Override
    public List<UserAddress> listUserAddress() {
        Long user = UserContext.getUser();
        return userAddressMapper.selectList(new LambdaQueryWrapper<UserAddress>().eq(UserAddress::getUserId, user));
    }

    @Override
    public Boolean deleteAddress(List<Integer> addressId) {
        if(CollUtil.isEmpty(addressId)){
            return null;
        }
        return userAddressMapper.deleteBatchIds(addressId) > 0;

    }
}
