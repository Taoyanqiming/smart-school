package com.sky.exception;

/**
 * 用户保存失败异常
 */
public class UserSaveFailedException extends BaseException {

    public UserSaveFailedException() {
    }

    public UserSaveFailedException(String msg) {
        super(msg);
    }
}