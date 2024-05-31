package com.xc.firmad.vo.res;

import lombok.Data;

/**
 * 用户端你返回的广告
 */
@Data
public class AdvertiseVO {

    private Long id;

    private String adName;

    private String adUri;

    private String imgUrl;
}
