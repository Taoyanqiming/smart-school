package com.sky.constant;

/**
 * 状态常量，启用或者禁用
 */
public class StatusConstant {

    //启用
    public static final Integer ENABLE = 1;
    //禁用
    public static final Integer DISABLE = 0;

    // 反馈状态常量
    public static final Integer STATUS_PENDING = 1; // 待解决
    public static final Integer STATUS_RESOLVED = 2; // 已解决

    // 反馈类型常量
    public static final Integer TYPE_COMPLAINT = 1; // 投诉
    public static final Integer TYPE_SUGGESTION = 2; // 建议
    public static final Integer TYPE_CONSULTATION = 3; // 咨询
}
