package com.xc.user.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@Data
@Slf4j
public class IdGeneratorSnowflake {
    public static synchronized long snowflakeId(){
        int workerId=RandomUtil.randomInt(1,5);
        int datacenterId= RandomUtil.randomInt(1,5);
        Snowflake snowflake = IdUtil.getSnowflake(workerId,datacenterId);
        return snowflake.nextId();
    }

    public static void main(String[] args) {
        System.out.println(snowflakeId());
    }

}
