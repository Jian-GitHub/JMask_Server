package com.jian.controller;

import com.jian.entity.Result;
import com.jian.entity.User;
import com.jian.mapper.User_Mapper;
import com.jian.untils.Base64Util;
import com.jian.untils.HmacSHA512_Util;
import com.jian.untils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jian Qi
 * @Date 2022/3/23 3:51 下午
 * @Description Web页面用户登录
 * @Version 1
 */

@RestController
@RequestMapping("JMask")
public class Login_Web_Controller {
    final
    User_Mapper userMapper;

    public Login_Web_Controller(User_Mapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * Web页面用户登录接口
     *
     * @param username 用户名
     * @param password 用户密码
     * @return 用户token信息
     */
    @RequestMapping("/WebLogin")
    public Result login(@RequestParam(value = "username", required = true) String username, @RequestParam(value = "password", required = true) String password) throws UnsupportedEncodingException {
//        System.out.println(username);
//        System.out.println(password);
        if (
                username == null || "".equals(username) ||
                        password == null || "".equals(password)
        ) {
            return Result.getFail();
        }
        User user = userMapper.selectUserByName(username);
        if (user == null) {
            return Result.getFail();//.setData("用户不存在");
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        try {
            date = timeFormat.parse(user.getCreateDate());
        } catch (ParseException e) {
            e.printStackTrace();
            return Result.getFail();
        }
        String createTime = String.valueOf(date.getTime() / 1000); // 时间戳转换日期

//        System.out.println(user.getPassword());
//        System.out.println(Base64Util.encode(HmacSHA512_Util.HmacSHA512(password, createTime)));

        if (user.getPassword().equals(Base64Util.encode(HmacSHA512_Util.HmacSHA512(password, createTime)))) {
            Map<String, String> payload = new HashMap<>();
            Map<String, String> data = new HashMap<>();

            payload.put("id", user.getId());
            payload.put("username", user.getUserName());
            payload.put("role", user.getRole());

            String token = JWTUtils.getToken(payload);
            data.put("token", token);
            return Result.getSuccess().setData(data);
        }
        return Result.getFail();
    }
}
