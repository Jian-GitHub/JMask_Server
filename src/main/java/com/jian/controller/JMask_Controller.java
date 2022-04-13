package com.jian.controller;

import com.jian.entity.CompareAppWeb;
import com.jian.entity.JMaskAppInfo;
import com.jian.entity.Result;
import com.jian.mapper.JMask_Mapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jian Qi
 * @Date 2022/4/6 5:16 下午
 * @Description
 * @Version
 */
@RestController
@RequestMapping("/JMask/Data")
@Tag(name = "JMask系统信息相关")
public class JMask_Controller {
    final
    JMask_Mapper jMask_mapper;

    public JMask_Controller(JMask_Mapper jMask_mapper) {
        this.jMask_mapper = jMask_mapper;
    }

    /**
     * 获得 JMask App 与 Web 功能对比表
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/CompareAppWeb")
    @Operation(summary = "获取App Web功能对比表")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "20000", description = "请求成功"),
            @ApiResponse(responseCode = "50008", description = "请求失败", content = @Content)
    })
    public Result getCompareAppWeb() {
        List<CompareAppWeb> compareAppWebList = jMask_mapper.selectCompareAppWebList();
        if (compareAppWebList == null) {
            return Result.getFail();
        }
        HashMap<String, List<CompareAppWeb>> resultData = new HashMap<>();
        resultData.put("compareAppWeb", compareAppWebList);
        return Result.getSuccess().setData(resultData);
    }

    /**
     * 获得 JMask App 信息
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/JMaskAppInfo")
    @Operation(summary = "获取App信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "20000", description = "请求成功"),
            @ApiResponse(responseCode = "50008", description = "请求失败", content = @Content)
    })
    public Result getJMaskAppInfo() {
        JMaskAppInfo jMaskAppInfo = jMask_mapper.selectJMaskAppInfo();
        if (jMaskAppInfo == null){
            return Result.getFail();
        }
//        jMaskAppInfo.setDate(jMaskAppInfo.getDate().split(" ")[0]);
        SimpleDateFormat resultTimeFormat = new SimpleDateFormat("yyyy年M月d日");
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date date = timeFormat.parse(jMaskAppInfo.getDate());
            String dateStr = resultTimeFormat.format(date);
            jMaskAppInfo.setDate(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Map<String, JMaskAppInfo> resultData = new HashMap<>();
        resultData.put("JMaskAppInfo", jMaskAppInfo);
        return Result.getSuccess().setData(resultData);
    }
}