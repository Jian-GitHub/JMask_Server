package com.jian.controller;

import com.jian.entity.Result;
import com.jian.mapper.User_Mapper;
import com.jian.untils.Base64Util;
import com.jian.untils.Uuid;
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
public class Registration_Controller {
    @Autowired
    User_Mapper userMapper;

    @RequestMapping(method = {RequestMethod.POST}, value = "registration")
    public Result registration(
            @RequestParam("userName") String userName,
            @RequestParam("password") String password,
            @RequestParam("time") String time
    ) throws UnsupportedEncodingException {
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
    public boolean canUseUserName(@RequestParam("userName") String userName) {
        System.out.println(userName);
        String queryUserName = Base64Util.decode(userName);
        System.out.println();
        boolean result = "".equals(queryUserName) ? false : userMapper.countUsersByName(queryUserName) == 0 ? true : false;
        return result;
    }
}
