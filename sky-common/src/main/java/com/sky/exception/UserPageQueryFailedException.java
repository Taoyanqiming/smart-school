package com.sky.exception;

/**
 * 用户分页查询失败异常
 */
public class UserPageQueryFailedException extends BaseException {

    public UserPageQueryFailedException() {
    }

    public UserPageQueryFailedException(String msg) {
        super(msg);
    }
}