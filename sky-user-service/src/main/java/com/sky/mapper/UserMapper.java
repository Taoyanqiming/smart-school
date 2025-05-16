package com.sky.mapper;


import com.github.pagehelper.Page;
import com.sky.entity.User;
import com.sky.dto.UserPageQueryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    /**
     * 根据手机号查询员工
     * @param phoneNumber
     * @return
     */
    User getByPhone(@Param("phoneNumber") String phoneNumber);

    /**
     * 插入员工数据
     * @param user
     */

    void insert(User user);

    /**
     * 员工分页查询
     * @param userPageQueryDTO
     * @return
     */
    Page<User> pageQuery(UserPageQueryDTO userPageQueryDTO);

    /**
     * 启用禁用员工账户,编辑员工信息
     * @param user
     */

    void update(User user);



}
