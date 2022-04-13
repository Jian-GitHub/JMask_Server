package com.jian.controller;

import com.jian.entity.Result;
import com.jian.mapper.User_Mapper;
import com.jian.untils.Base64Util;
import com.jian.untils.Uuid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author qi
 * 注册
 */

@RestController
@RequestMapping("JMask/Registration")
@Tag(name = "注册相关")
public class Registration_Controller {
    @Autowired
    User_Mapper userMapper;

    @RequestMapping(method = {RequestMethod.POST}, value = "registration")
    @Operation(summary = "用户注册")
    @Parameters({
            @Parameter(name = "userName", description = "用户名", required = true),
            @Parameter(name = "password", description = "密码", required = true),
            @Parameter(name = "time", description = "注册时间", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "20000", description = "请求成功"),
            @ApiResponse(responseCode = "50008", description = "请求失败", content = @Content)
    })
    public Result registration(
            @RequestParam("userName") String userName,
            @RequestParam("password") String password,
            @RequestParam("time") String time
    ) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = timeFormat.format(new Date(Long.parseLong(Base64Util.decode(time)) * 1000)); // 时间戳转换日期
//        System.out.println(Base64Util.decode(userName));
//        System.out.println(Base64Util.decode(password));
//        System.out.println(createTime);
        if (userMapper.insertUser(
                Uuid.getUUID(),
                Base64Util.decode(userName),
                password,
                createTime
        )){
            return Result.getSuccess();
        }else {
            return Result.getFail();
        }
    }

    @RequestMapping(method = {RequestMethod.POST}, value = "canUseUserName")
    @Operation(summary = "查询用户名是否可用")
    @Parameters({
            @Parameter(name = "userName", description = "用户名", required = true)
    })
    public boolean canUseUserName(@RequestParam("userName") String userName) {
//        System.out.println(userName);
        String queryUserName = Base64Util.decode(userName);
        boolean result = "".equals(queryUserName) ? false : userMapper.countUsersByName(queryUserName) == 0 ? true : false;
        return result;
    }
}
