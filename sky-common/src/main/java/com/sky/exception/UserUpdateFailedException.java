package com.sky.exception;

/**
 * 用户信息更新失败异常
 */
public class UserUpdateFailedException extends BaseException {

    public UserUpdateFailedException() {
    }

    public UserUpdateFailedException(String msg) {
        super(msg);
    }
}