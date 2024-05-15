package com.xc.user.vo.req;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRegisterReqVO {

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("密码")
    private String password;


}
