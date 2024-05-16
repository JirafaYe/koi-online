package com.xc.user.controller;


import com.xc.api.dto.user.req.LongIdsVO;
import com.xc.api.dto.user.res.UserInfoResVO;
import com.xc.user.service.UserBaseService;
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
     * 获取单个用户信息
     */
//    @PostMapping("/getUserInfo")
//      public UserInfoResVO getUserInfo(@RequestBody @Valid CommonLongIdDTO vo){
//         return userBaseService.getUserInfo(vo);
//      }
//
    /**
     * 获取多个用户信息
     */
      @PostMapping("getUserInfos")
      public List<UserInfoResVO> getUserInfos(@RequestBody @Valid LongIdsVO vo){
         return userBaseService.getUserInfos(vo);
      }
}
