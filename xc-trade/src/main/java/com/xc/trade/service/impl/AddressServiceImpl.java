package com.xc.trade.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xc.common.domain.dto.PageDTO;
import com.xc.common.exceptions.CommonException;
import com.xc.common.utils.BeanUtils;
import com.xc.common.utils.CollUtils;
import com.xc.common.utils.UserContext;
import com.xc.trade.entity.po.Address;
import com.xc.trade.entity.query.AddressQuery;
import com.xc.trade.entity.vo.AddressVO;
import com.xc.trade.mapper.AddressMapper;
import com.xc.trade.service.IAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements IAddressService {
    @Override
    public boolean create(AddressVO vo) {
        Long userId = UserContext.getUser();
        vo.setId(null);
        if(!isMobileNum(vo.getPhone())){
            throw new CommonException("invalid phone number");
        }
        Address address = BeanUtils.copyBean(vo, Address.class);
        address.setUserId(userId);
        return save(address);
    }

    @Override
    public boolean update(AddressVO vo) {
        if(vo.getId() == null){
            throw new CommonException("require id value");
        }
        if(!isMobileNum(vo.getPhone())){
            throw new CommonException("invalid phone number");
        }
        Long userId = UserContext.getUser();
        Address address = BeanUtils.copyBean(vo, Address.class);
        address.setUserId(userId);
        return updateById(address);
    }

    @Override
    //todo: check dependencies from order table
    public boolean remove(List<Long> ids) {
        List<Address> addresses = baseMapper.selectBatchIds(ids);
        for(Address address : addresses){
            if(address!=null&&!address.getUserId().equals(UserContext.getUser())){
                throw new CommonException(address.getId()+"非本人，无权限");
            }
        }

        return removeByIds(ids);
    }

    @Override
    public PageDTO<AddressVO> pageQueryAddress(AddressQuery query) {
        Page<Address> page = lambdaQuery().eq(Address::getUserId, UserContext.getUser())
                .page(query.toMpPageDefaultSortByCreateTimeDesc());
        PageDTO<AddressVO> res= null;
        List<Address> records = page.getRecords();
        if(CollUtils.isNotEmpty(records)){
            List<AddressVO> vos = records.stream().map(q -> BeanUtils.copyBean(q, AddressVO.class))
                    .collect(Collectors.toList());
            res=PageDTO.of(page,vos);
        }
        return res;
    }

    public static boolean isMobileNum(String telNum) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(telNum);
        return m.matches();
    }
}
