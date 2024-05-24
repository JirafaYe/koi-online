package com.xc.trade.service;

import com.xc.common.domain.dto.PageDTO;
import com.xc.trade.entity.Address;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.trade.entity.query.AddressQuery;
import com.xc.trade.entity.vo.AddressVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jirafa
 * @since 2024-05-24
 */
public interface IAddressService extends IService<Address> {
    boolean create(AddressVO vo);
    boolean update(AddressVO vo);
    boolean remove(List<Long> ids);
    PageDTO<AddressVO> pageQueryAddress(AddressQuery query);
}
