package com.xc.user.controller;


import com.xc.common.domain.dto.CommonLongIdDTO;
import com.xc.common.domain.dto.PageDTO;
import com.xc.user.service.UserBaseService;
import com.xc.user.vo.res.UserInfoResVO;
import org.apache.ibatis.annotations.ResultMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class UserbackController {
    @Resource
    private UserBaseService userBaseService;

    /**
     * 展示用户信息
     */
//    PageDTO<List<UserInfoResVO>> listUserInfo

    /**
     * 获取用户信息
     */
    @PostMapping("/getUserInfo")
      public UserInfoResVO getUserInfo(@RequestBody @Valid CommonLongIdDTO vo){
         return userBaseService.getUserInfo(vo);
      }

}
