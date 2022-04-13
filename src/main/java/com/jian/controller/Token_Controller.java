package com.jian.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.jian.entity.Result;
import com.jian.untils.JWTUtils;
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

/**
 * @author Jian Qi
 * @Date 2022/3/24 5:49 下午
 * @Description Token相关控制
 * @Version 1
 */

@RestController
@RequestMapping("JMask")
@Tag(name = "Token相关")
public class Token_Controller {
    @RequestMapping(method = RequestMethod.POST, value = "/checkToken")
    @Operation(summary = "校验Token信息是否有效")
    @Parameters({
            @Parameter(name = "token", description = "用户token", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "20000", description = "请求成功"),
            @ApiResponse(responseCode = "50008", description = "请求失败", content = @Content)
    })
    public Result checkToken(@RequestParam(value = "token") String token){
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
