package com.jian.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 登录用类
 * @author qi
 */

@Data
@Schema
public class Login {
    private String password;//密码
    private String createDate;//创建时间
}
