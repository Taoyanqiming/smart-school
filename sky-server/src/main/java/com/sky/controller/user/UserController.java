package com.sky.controller.user;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.UserDTO;
import com.sky.dto.UserLoginDTO;
import com.sky.dto.UserPageQueryDTO;
import com.sky.entity.User;
import com.sky.exception.*;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.UserService;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
@Api(tags = "用户相关接口")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     * @param userLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("用户登录： userLoginDTO:{}", userLoginDTO);
        User user = userService.login(userLoginDTO);

        // 生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getUserId());
        claims.put(JwtClaimsConstant.USER_ROLE, user.getRole()); // 添加角色信息
        String token = JwtUtil.createJWT(jwtProperties.getSecretKey(), jwtProperties.getTtl(), claims);
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .userId(user.getUserId())
                .userName(user.getUsername())
                .role(user.getRole())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .token(token)
                .build();

        return Result.success(userLoginVO);
    }


    /**
     * 修改用户信息
     * @param userDTO
     * @return
     */
    @PutMapping("/update")
    @ApiOperation("编辑用户信息")
    public Result update(@RequestBody UserDTO userDTO) {
        log.info("编辑用户信息：{}", userDTO);
        try {
            userService.update(userDTO);
            return Result.success("修改成功");
        } catch (UserUpdateFailedException e) {
            return Result.error(e.getMessage());
        }
    }
    /**
     * 退出
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success("退出成功");
    }

    /**
     * 注册
     * @param userDTO
     * @return
     */
    @PostMapping("/register")
    @ApiOperation("新增用户")
    public Result save(@RequestBody UserDTO userDTO) {
        User user = userService.getByNumber(userDTO.getPhoneNumber());
        if (user != null) {
            return Result.error("用户已存在");
        }
        log.info("新增用户：{}", userDTO);
        try {
            userService.save(userDTO);
            return Result.success("创建成功");
        } catch (UserSaveFailedException e) {
            return Result.error(e.getMessage());
        }
    }


}
