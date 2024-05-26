package com.xc.user.utils;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;


/**
 * TO DO
 *
 * @author
 * @date 2023/06/15 10:34
 */
public class CodeGeneration {

    public static void main(String[] args) {
        /**
         * 先配置数据源
         */
        MySqlQuery mySqlQuery = new MySqlQuery() {
            @Override
            public String[] fieldCustom() {
                return new String[]{"Default"};
            }
        };


        DataSourceConfig dsc = new DataSourceConfig.Builder(
                "jdbc:mysql://47.108.192.109:3306/xc_user?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai&allowMultiQueries=true",
                "root",
                "Root123321@")
                .dbQuery(mySqlQuery).build();
        //通过datasourceConfig创建AutoGenerator
        AutoGenerator generator = new AutoGenerator(dsc);

        /**
         * 全局配置
         */
        //java下的文件路径
//        String filePath = "E:/javaProjects/AI洞察/code/app/common-api" + "/src/main/java/";
        String filePath = "F:/project/koi-online/xc-user" + "/src/main/java/";
        GlobalConfig global = new GlobalConfig.Builder()
                //生成的输出路径
                .outputDir(filePath)
                //生成的作者名字
                .author("pengyalin")
                //时间策略
                .dateType(DateType.TIME_PACK)
                //需开启swaggger配置
//                .enableSwagger()
                //格式化时间格式
                .commentDate("yyyy年MM月dd日")
                //禁止打开输出目录，默认false
                .disableOpenDir()
                //覆盖生成文件
                .fileOverride()
                .build();

        /**
         * 包配置
         */
        PackageConfig packages = new PackageConfig.Builder()
                //设置模块包名
                .moduleName("generator")
                //实体类包名
                .entity("entity")
                //父包名。如果为空，将下面子包名必须写全部， 否则就只需写子包名
                .parent("com.xc")
                //控制层包名
                // .controller("controller")
                //mapper层包名
                .mapper("mapper")
                //数据访问层xml包名
                 .xml("mapper.xml")
                //service层包名
                .service("service")
                //service实现类包名
                .serviceImpl("service.impl")
                //输出自定义文件时的包名
                // .other("output")
                //路径配置信息,就是配置各个文件模板的路径信息,这里以mapper.xml为例
                // .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "E:/javaProjects/普益资配模块化项目/app/product" + "/src/main/resources/mapper"))
                .build();
        /**
         * 模板配置
         */

        TemplateConfig template = new TemplateConfig.Builder()
//            .disable()//禁用所有模板
                //.disable(TemplateType.ENTITY)禁用指定模板
//                .service(filePath + "/service.java")//service模板路径
//                .serviceImpl(filePath + "/service/impl/serviceImpl.java")//实现类模板路径
//                .mapper(filePath + "/mapper.java")//mapper模板路径
//                .mapperXml("/templates/mapper.xml")//xml文件模板路路径
//                .controller(filePath + "/controller")//controller层模板路径
                .build();


        /**
         * 策略配置开始
         */
        StrategyConfig strategyConfig = new StrategyConfig.Builder()
                //开启全局大写命名
                .enableCapitalMode()
                //.likeTable()模糊表匹配
                //添加表匹配，指定要生成的数据表名，不写默认选定数据库所有表
                .addInclude("user_base")
                //.disableSqlFilter()禁用sql过滤:默认(不使用该方法）true
                //.enableSchema()启用schema:默认false
                //实体策略配置

                .entityBuilder()
                //.disableSerialVersionUID()禁用生成SerialVersionUID：默认true
                //开启链式模型
                .enableChainModel()
                //开启lombok
                .enableLombok()
                //开启 Boolean 类型字段移除 is 前缀
                .enableRemoveIsPrefix()
                //开启生成实体时生成字段注解
                .enableTableFieldAnnotation()
                //.addTableFills()添加表字段填充
                //数据表映射实体命名策略：默认下划线转驼峰underline_to_camel
                .naming(NamingStrategy.underline_to_camel)
                //表字段映射实体属性命名规则：默认null，不指定按照naming执行
                .columnNaming(NamingStrategy.underline_to_camel)
                //添加全局主键类型
                .idType(IdType.AUTO)
                //格式化实体名称，%s取消首字母I
                .formatFileName("%s")
                .build()
                //mapper文件策略
                .mapperBuilder()
                //开启mapper注解
                .enableMapperAnnotation()
                //启用xml文件中的BaseResultMap 生成
                .enableBaseResultMap()
                //启用xml文件中的BaseColumnList
                .enableBaseColumnList()
                //.cache(缓存类.class)设置缓存实现类
                //格式化Dao类名称
                .formatMapperFileName("%sMapper")
                //格式化xml文件名称
                .formatXmlFileName("%sMapper")
                .build()
                //service文件策略
                .serviceBuilder()
                //格式化 service 接口文件名称
                .formatServiceFileName("%sService")
                //格式化 service 接口文件名称
                .formatServiceImplFileName("%sServiceImpl")
                .build()
                //控制层策略
                .controllerBuilder()
                //.enableHyphenStyle()开启驼峰转连字符，默认：false
                //开启生成@RestController
                .enableRestStyle()
                //格式化文件名称
                // .formatFileName("%sController")
                .build();
        /*至此，策略配置才算基本完成！*/

        /**
         * 将所有配置项整合到AutoGenerator中进行执行
         */

        generator.global(global)
                .template(template)
                .packageInfo(packages)
                .strategy(strategyConfig)
                .execute();
    }

}
