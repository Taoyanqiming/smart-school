package com.sky.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class UserDTO implements Serializable {
    // --------------------- 用户主表字段 ---------------------
    private Integer userId;          // 主键（注意：原表为 bigint(20) UNSIGNED，对应 Java 类型为 Long）
    private String phoneNumber;   // 手机号码
    private String password;      // 密码（加密存储）
    private String username;      // 昵称
    private String email;         // 邮箱
    private String role;          // 角色（admin/user）

    // --------------------- 详情表字段 ---------------------
    private LocalDate birthday;   // 出生日期（日期类型，对应数据库 date 字段）
    private Integer sex;          // 性别（0:未知, 1:男, 2:女，对应数据库 tinyint(1)）
    private String realName;      // 真实姓名
    private String idCard;        // 身份证号码
    private String address;       // 联系地址



}

