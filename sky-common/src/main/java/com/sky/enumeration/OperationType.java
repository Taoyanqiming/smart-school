package com.sky.enumeration;

/**
 * 数据库操作类型
 */
public enum OperationType {

    /**
     * 更新操作
     */
    UPDATE,

    /**
     * 插入操作
     */
    INSERT,
    /**
    * 插入操作（只填充create_time）
    */
    INSERT_CREATE_ONLY
}
