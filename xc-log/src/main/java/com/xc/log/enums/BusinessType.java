package com.xc.log.enums;

public enum BusinessType {

    /**
     * 新增
     */
    INSERT(1, "新增"),

    /**
     * 修改
     */
    UPDATE(2, "修改"),

    /**
     * 删除
     */
    DELETE(3, "删除"),

    QUERY(4, "查询"),
    /**
     * 其它
     */
    OTHER(5, "其他");


    private Integer value;
    private String msg;

    BusinessType() {

    }

    private BusinessType(int value, String msg) {
        this.value = value;
        this.msg = msg;
    }
//根据value获取msg
    public static String getValue(String type) {
        for (BusinessType businessType : BusinessType.values()) {
            if (businessType.value.equals(type)) {
                return businessType.msg;
            }
        }
        return null;
    }
}
