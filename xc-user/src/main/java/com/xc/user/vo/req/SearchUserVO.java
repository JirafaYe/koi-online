package com.xc.user.vo.req;

import com.xc.common.domain.query.PageQuery;
import lombok.Data;

@Data
public class SearchUserVO extends PageQuery {
   //搜索条件

    /**
     * 账户名
     */
    private String account;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 用户状态
     */
    private Integer status;

}
