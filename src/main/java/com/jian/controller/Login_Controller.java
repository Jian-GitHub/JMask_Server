package com.jian.controller;

import com.jian.entity.Result;
import com.jian.entity.User;
import com.jian.mapper.User_Mapper;
import com.jian.untils.Base64Util;
import com.jian.untils.HmacSHA512_Util;
import com.jian.untils.JWTUtils;
import com.jian.untils.JsonUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("JMask")
@Tag(name = "登录相关")
public class Login_Controller {
    final
    User_Mapper userMapper;

    public Login_Controller(User_Mapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 传入用户名，寻得并以Base64编码返回该用户信息
     *
     * @param userName 用户名
     * @return String Base64编码后的用户信息，若发生异常则返回空字符串。
     */
    @RequestMapping(
            method = {RequestMethod.POST},
            value = "/Login/getPassword"
    )
    @Operation(summary = "获取用户加密后的密码")
    @Parameters({
            @Parameter(name = "userName", description = "用户名", required = true),
    })
    public String login(@RequestParam("userName") String userName) {
        String result = "";
        try {
            User user = userMapper.selectUserByName(Base64Util.decode(userName));

            if (user == null) {
                return result;
            }
            user.setCreateDate(Base64Util.encode(user.getCreateDate()));
            result = Base64Util.encode(JsonUtils.toJson(user));
            return result;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Web页面用户登录接口
     *
     * @param username 用户名
     * @param password 用户密码
     * @return 用户token信息
     */
    @RequestMapping(method = RequestMethod.POST, value = "/WebLogin")
    @Operation(summary = "获取用户加密后的密码")
    @Parameters({
            @Parameter(name = "userName", description = "用户名", required = true),
            @Parameter(name = "password", description = "密码", required = true),
            @Parameter(name = "type", description = "登录类型(是否为客户端)", required = false)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "20000", description = "请求成功"),
            @ApiResponse(responseCode = "50008", description = "请求失败", content = @Content)
    })
    public Result<Map<String, String>> login(
            @RequestParam(value = "username", required = true) String username,
            @RequestParam(value = "password", required = true) String password,
            @RequestParam(value = "type", required = false) boolean isClient
    ) {
        if (
                username == null || "".equals(username) ||
                        password == null || "".equals(password)
        ) {
            return Result.getFail();
        }
        User user = userMapper.selectUserByName(username);
        if (user == null) {
            // 用户不存在
            return Result.getFail();
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
        String encodePassWord;
        try {
            encodePassWord = Base64Util.encode(HmacSHA512_Util.HmacSHA512(password, createTime));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Result.getFail();
        }
        if (user.getPassword().equals(encodePassWord)) {
            Map<String, String> payload = new HashMap<>();
            Map<String, String> data = new HashMap<>();

            payload.put("id", user.getId());
            payload.put("username", user.getUserName());
            payload.put("role", user.getRole());

            //客户端token有效期为365天，Web端token有效期为7天
            int time = isClient ? 365 : 7;
            String token = JWTUtils.getToken(payload, time);
            data.put("token", token);

            // 更新用户最后登录时间
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formatStr =formatter.format(new Date());
            userMapper.updateUserLoginDate(formatStr, user.getId());

            // 更新用户登录状态
            userMapper.updateUserLoginState(true, user.getId());

            return Result.getSuccess().setData(data);
        }
        return Result.getFail();
    }
}
