package com.jian.untils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jian.entity.Result;
import com.jian.entity.User;
import com.jian.mapper.User_Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Calendar;
import java.util.Map;

public class JWTUtils {
    private static String SECRET = "JMaskWebUserToken";

    @Autowired
    User_Mapper user_mapper;

    /**
     * 生产token
     */
    public static String getToken(Map<String, String> map,int time) {
        JWTCreator.Builder builder = JWT.create();

        //payload
        map.forEach((k, v) -> {
            builder.withClaim(k, v);
        });

        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, time); //默认7天过期

        builder.withExpiresAt(instance.getTime());//指定令牌的过期时间
        String token = builder.sign(Algorithm.HMAC256(SECRET));//签名
        return token;
    }

    /**
     * 验证token
     */
    public static DecodedJWT verify(String token) {
        //如果有任何验证异常，此处都会抛出异常
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
        return decodedJWT;
    }

    /**
     * 通过验证token获取用户id
     * @param token 用户token
     * @return
     */
    public static String verifyUserToken(@RequestParam("token") String token) {
        if (token == null) {
            return null;
        }
        DecodedJWT verify;
        try {
            verify = JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
        } catch (Exception e) {
            return null;
        }
        return verify.getClaim("id").asString();
    }
}