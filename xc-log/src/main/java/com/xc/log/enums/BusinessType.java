package com.xc.log.enums;

public enum BusinessType {

    /**
     * 新增
     */
    INSERT(1, "增加"),

    /**
     * 修改
     */
    UPDATE(2, "修改"),

    /**
     * 删除
     */
    DELETE(3, "删除"),

    /**
     * 其它
     */
    OTHER(4, "其他"),

    /**
     * 查询
     */
    QUERY(5, "查询"),

    RESET(6, "重置"),

    /**
     * 授权
     */
    GRANT,

    /**
     * 导出
     */
    EXPORT,

    /**
     * 导入
     */
    IMPORT,

    /**
     * 强退
     */
    FORCE,

    /**
     * 生成代码
     */
    GENCODE,

    /**
     * 清空数据
     */
    CLEAN;

    private Integer value;
    private String msg;

    BusinessType() {

    }

    private BusinessType(int value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    public String getValue(BusinessType type) {

        return null;
    }
}
