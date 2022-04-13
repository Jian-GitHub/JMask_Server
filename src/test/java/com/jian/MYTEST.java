package com.jian;

import com.jian.entity.CompareAppWeb;
import com.jian.entity.JMaskAppInfo;
import com.jian.entity.Result;
import com.jian.mapper.JMask_Mapper;
import com.jian.mapper.User_Mapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Jian Qi
 * @Date 2022/3/27 11:27 下午
 * @Description
 * @Version
 */
@SpringBootTest
public class MYTEST {
    @Resource
    private User_Mapper user_mapper;
    @Resource
    private JMask_Mapper compareAppWeb_mapper;

    @Test
    public void test(){
//        System.out.println(System.currentTimeMillis());
//        for (CompareAppWeb compareAppWeb : compareAppWeb_mapper.selectCompareAppWebList()) {
//            System.out.println(compareAppWeb.toString());
//        }
//        System.out.println(compareAppWeb_mapper.selectCompareAppWebList());
//        System.out.println(compareAppWeb_mapper.selectJMaskAppInfo().toString());
//        JMaskAppInfo jMaskAppInfo = compareAppWeb_mapper.selectJMaskAppInfo();
////        System.out.println(jMaskAppInfo.getDate().split(" ")[0]);
//
//        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy年M月d日");
//        SimpleDateFormat timeFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date = null;
//        try {
//            date = timeFormat1.parse(jMaskAppInfo.getDate());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        String dateStr = timeFormat.format(date);
//        System.out.println(dateStr);
//        jMaskAppInfo.setDate(jMaskAppInfo.getDate().split(" ")[0]);
//        System.out.println(user_mapper.countUserLog("1f0af08b05e04220acba0abc7d2a2872"));
    }
}
