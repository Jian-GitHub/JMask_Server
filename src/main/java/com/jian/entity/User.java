package com.jian.entity;


import lombok.Data;

/**
 * 用户类
 * @author qi
 */

@Data
public class User {
    private String id;
    private String userName;//用户名称
    private String password;//密码
    private String createDate;//创建时间
    private String lastDate;//最后登录时间
    private boolean onLine;//是否在线
    private boolean hasAvatar;//是否有头像
    private String role;//用户角色
}
