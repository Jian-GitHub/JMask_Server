package com.jian.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Jian Qi
 * @Date 2022/4/6 5:10 下午
 * @Description App与Web功能对比表
 * @Version 1
 */
@Data
@Schema
public class CompareAppWeb {
    private int id;
    private String item;
    private Boolean canApp;
    private Boolean canWeb;
}
