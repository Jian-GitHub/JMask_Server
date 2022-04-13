package com.jian.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Jian Qi
 * @Date 2022/3/29 3:31 下午
 * @Description 用户头像
 * @Version 1
 */
@Data
@Schema
public class Avatar {
    private String userID;//用户ID
    private String avatarURL;//用户头像URL
}
