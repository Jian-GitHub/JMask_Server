package com.jian.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Jian Qi
 * @Date 2022/4/7 9:06 上午
 * @Description 用户识别记录表
 * @Version 1
 */
@Data
@Schema
public class Log {
    private int id;
    private String userID;
    private String date;
    private String imgName;
}
