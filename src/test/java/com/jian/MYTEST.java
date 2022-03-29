package com.jian;

import com.jian.entity.User;
import com.jian.mapper.User_Mapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

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
    @Test
    public void test(){
        System.out.println(System.currentTimeMillis());
    }
}
