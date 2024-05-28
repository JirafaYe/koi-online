package com.xc.trade.service;

import com.xc.common.domain.dto.PageDTO;
import com.xc.trade.entity.po.ShoppingChart;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.trade.entity.dto.ShoppingChartDTO;
import com.xc.trade.entity.query.ShoppingChartQuery;
import com.xc.trade.entity.vo.ShoppingChartVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jirafa
 * @since 2024-05-24
 */
public interface IShoppingChartService extends IService<ShoppingChart> {
    boolean create(ShoppingChartVO vo);

    boolean update(ShoppingChartVO vo);

    boolean remove(List<Long> ids);

    PageDTO<ShoppingChartDTO> pageQuery(ShoppingChartQuery query);
}
