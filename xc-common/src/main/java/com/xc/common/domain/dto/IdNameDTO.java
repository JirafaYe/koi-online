package com.xc.common.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * id和name键值对
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdNameDTO {
    /**
     * id
     */
    private Long id;
    /**
     * name
     */
    private String name;
}
