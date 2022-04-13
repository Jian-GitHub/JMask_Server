package com.jian.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Jian Qi
 * @Date 2022/4/6 5:45 下午
 * @Description JMask信息表
 * @Version 1
 */
@Data
@Schema
public class JMaskAppInfo {
    private int id;
    private String version;
    private String date;
    private String iconURL;
    private String downloadMacURL;
    private String downloadWinURL;
}
