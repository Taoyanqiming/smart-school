package com.sky.exception;

/**
 * 用户不存在异常
 */
public class UserNotFoundException extends BaseException {

    public UserNotFoundException() {
    }

    public UserNotFoundException(String msg) {
        super(msg);
    }
}