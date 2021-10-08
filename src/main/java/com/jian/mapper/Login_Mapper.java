package com.jian.mapper;
/**
 * 登录服务
 * @author qi
 */

import com.jian.entity.Login;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface Login_Mapper {

    @Select("select password,createDate from user where userName = #{userName};")
    Login selectPasswordByUserName(String userName);
}
