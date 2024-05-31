package com.xc.product.entity.dto;

import com.xc.common.domain.dto.PageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class SpuPageDTO<T> extends PageDTO<T> {
    private Integer total_available;

    private Integer total_unAvailable;

    public SpuPageDTO(PageDTO<T> pageDTO, Integer unAvailable,Integer available) {
        super(pageDTO.getTotal(), pageDTO.getPages(), pageDTO.getList());
        this.total_unAvailable = unAvailable;
        this.total_available=available;
    }
}
