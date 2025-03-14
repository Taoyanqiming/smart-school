package com.sky.controller.admin;

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
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@Slf4j
@Api(tags = "管理员用户相关接口")
public class AdminController {
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
        log.info("用户登录：{}", userLoginDTO);
        try {
            User user = userService.login(userLoginDTO);
            if (user == null) {
                throw new UserNotFoundException("用户不存在，请检查用户名和密码");
            }

            // 登录成功后，生成管理员 JWT 令牌
            Map<String, Object> claims = new HashMap<>();
            claims.put(JwtClaimsConstant.EMP_ID, user.getUserId());
            String token = JwtUtil.createJWT(
                    jwtProperties.getAdminSecretKey(),
                    jwtProperties.getAdminTtl(),
                    claims);

            UserLoginVO userLoginVO = UserLoginVO.builder()
                    .phoneNumber(user.getPhoneNumber())
                    .email((user.getEmail()))
                    .userId(user.getUserId())
                    .userName(user.getUsername())
                    .token(token)
                    .build();

            return Result.success(userLoginVO);
        } catch (UserNotFoundException e) {
            return Result.error(e.getMessage());
        }
    }


    /**
     * 用户分页展示
     * @param userPageQueryDTO
     * @return
     */
    @GetMapping("/user/page")
    @ApiOperation("用户分页查询")
    public Result<PageResult> page(UserPageQueryDTO userPageQueryDTO) {
        log.info("用户分页查询，参数为：{}", userPageQueryDTO);
        try {
            PageResult pageResult = userService.pageQuery(userPageQueryDTO);
            return Result.success(pageResult);
        } catch (UserPageQueryFailedException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 修改用户信息
     * @param userDTO
     * @return
     */
    @PutMapping("/user/update")
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
}