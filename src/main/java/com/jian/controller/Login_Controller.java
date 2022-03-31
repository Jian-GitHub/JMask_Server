package com.jian.controller;

import com.jian.entity.User;
import com.jian.mapper.Login_Mapper;
import com.jian.mapper.User_Mapper;
import com.jian.untils.Base64Util;
import com.jian.untils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("JMask/Login")
public class Login_Controller {
    @Autowired
    User_Mapper user_mapper;

    /**
     * 传入用户名，寻得并以Base64编码返回该用户信息
     * @param userName 用户名
     * @return String Base64编码后的用户信息，若发生异常则返回空字符串。
     */
    @RequestMapping(
            method = {RequestMethod.POST},
            value ="/getPassword"
    )
    public String login(@RequestParam("userName") String userName){
        String result = "";
        try {
//            Login login = login_mapper.selectPasswordByUserName(Base64Util.decode(userName));
            User user = user_mapper.selectUserByName(Base64Util.decode(userName));

            if (user == null){
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

}
