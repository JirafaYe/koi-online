package com.xc.trade.controller;

import com.xc.common.domain.dto.PageDTO;
import com.xc.trade.entity.query.AddressQuery;
import com.xc.trade.entity.vo.AddressVO;
import com.xc.trade.service.IAddressService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 收货地址
 */
@RequestMapping("/address")
@RestController
public class AddressController {

    @Resource
    IAddressService addressService;

    /**
     * 创建地址
     * @param vo
     * @return
     */
    @PostMapping("/create")
    public boolean create(@RequestBody@Valid AddressVO vo){
        return addressService.create(vo);
    }

    /**
     * 单个个更新地址
     * @param vo
     * @return
     */
    @PostMapping("/update")
    boolean update(@Valid@RequestBody AddressVO vo){
        return addressService.update(vo);
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @PostMapping("/remove")
    boolean remove(@RequestBody List<Long> ids){
        return addressService.remove(ids);
    }

    /**
     * 分页查询个人地址
     * @param query
     * @return
     */
    @GetMapping("/page")
    PageDTO<AddressVO> pageQueryAddress(AddressQuery query){
        return addressService.pageQueryAddress(query);
    }
}
