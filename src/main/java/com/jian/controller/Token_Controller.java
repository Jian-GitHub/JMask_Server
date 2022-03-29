package com.jian.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.jian.entity.Result;
import com.jian.untils.JWTUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jian Qi
 * @Date 2022/3/24 5:49 下午
 * @Description Token相关控制
 * @Version 1
 */

@RestController
@RequestMapping("JMask")
public class Token_Controller {
    @RequestMapping("/checkToken")
    public Result checkToken(String token){
        DecodedJWT verify = null;
//        System.out.println(token);
        if (token == null){
            return Result.getFail();
        }
        try {
            verify = JWTUtils.verify(token);
//            System.out.println(verify);
//            System.out.println("getHeader: "+verify.getHeader());
//            System.out.println("getPayload: "+verify.getPayload());
//            System.out.println("getToken: "+verify.getToken());
//            System.out.println("getSignature: "+verify.getSignature());
//            System.out.println("getClaims: "+verify.getClaims().toString());
//            System.out.println("获取:"+verify.getClaim("role"));
            return Result.getSuccess().setData(verify);
        }catch (Exception e){
            return Result.getFail();
        }
    }
}
