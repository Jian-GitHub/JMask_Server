package com.jian.controller;

import com.jian.entity.User;
import com.jian.mapper.User_Mapper;
import com.jian.untils.Base64Util;
import com.jian.untils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("JMask/User")
public class User_Controller {
    @Autowired
    User_Mapper userMapper;

    @RequestMapping("select")
    public List<User> selectUsers() {
//        System.out.println(userMapper.select());
        return userMapper.select();
    }

    @RequestMapping("selectByUserID")
    public String selectByUserID(int ID) {
//        System.out.println(userMapper.select());
        User user = userMapper.selectUserByID(ID);
        String result = "";
        try {
            result = Base64Util.encode(JsonUtils.toJson(user));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
}

