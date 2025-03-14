package com.sky.controller.pub;

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
@RequestMapping("/pub")
@Slf4j
@Api(tags = "公共注册相关接口")
public class PubController {
    @Autowired
    private UserService userService;

    /**
     * 退出
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 新增用户，注册
     * @param userDTO
     * @return
     */
    @PostMapping("/user/add")
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
