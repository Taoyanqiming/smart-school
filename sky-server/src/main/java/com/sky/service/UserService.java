package com.sky.service;

import com.sky.dto.UserDTO;
import com.sky.dto.UserLoginDTO;
import com.sky.dto.UserPageQueryDTO;
import com.sky.entity.User;
import com.sky.result.PageResult;

public interface UserService {
    /**
     * 登录
     * @param userLoginDTO
     * @return
     */
    User login(UserLoginDTO userLoginDTO);
    /**
     * 新增用户，注册
     * @param userDTO
     * @return
     */
    void save(UserDTO userDTO);
    /**
     * 用户分页展示
     * @param userPageQueryDTO
     * @return
     */
    PageResult pageQuery(UserPageQueryDTO userPageQueryDTO);
    /**
     * 根据iD查询用户信息
     * @param userId
     * @return
     */
    User getById(Long userId);
    /**
     * 修改用户信息
     * @param userDTO
     * @return
     */
    void update(UserDTO userDTO);
}
