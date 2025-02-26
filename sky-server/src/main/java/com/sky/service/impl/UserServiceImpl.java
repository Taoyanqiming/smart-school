package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.dto.UserDTO;
import com.sky.dto.UserLoginDTO;
import com.sky.dto.UserPageQueryDTO;
import com.sky.entity.User;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.UserMapper;
import com.sky.result.PageResult;
import com.sky.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    /**
     * 用户登录
     */
    public User login(UserLoginDTO userLoginDTO) {
        String phoneNumber = userLoginDTO.getPhoneNumber();
        String password = userLoginDTO.getPassword();
        //根据用户手机号查询数据
        User user = userMapper.getByPhone(phoneNumber);
        if (user == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        //密码对比
//        password = DigestUtils.md5DigestAsHex(password.getBytes());

        if (!password.equals(user.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }
       return user;
     }
    /**
     * 新增用户，注册
     * @param userDTO
     * @return
     */
    @Override
    public void save(UserDTO userDTO){
        User user = new User();
        //对象属性拷贝，前者复制给后者
        BeanUtils.copyProperties(userDTO,user);
        //设置密码，默认123456
        user.setPassword(PasswordConstant.DEFAULT_PASSWORD);
        //通过ThreadLocal 获取用户信息
       // Long currentId = BaseContext.getCurrentId();
        userMapper.insert(user);
    }

    /**
     * 用户分页展示
     * @param userPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(UserPageQueryDTO userPageQueryDTO) {
        PageHelper.startPage(userPageQueryDTO.getPage(),userPageQueryDTO.getPageSize());
        Page<User> page = userMapper.pageQuery(userPageQueryDTO);
        long total = page.getTotal();
        List<User> records = page.getResult();
        return new PageResult(total,records);
    }


    /**
     * 根据iD查询用户信息
     * @param userId
     * @return
     */
    @Override
    public User getById(Long userId) {
        User user = userMapper.getById(userId);
        user.setPassword("******");
        return user;
    }


    /**
     * 修改用户信息
     * @param userDTO
     * @return
     */
    @Override
    public void update(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO,user);
        userMapper.update(user);
    }
}
