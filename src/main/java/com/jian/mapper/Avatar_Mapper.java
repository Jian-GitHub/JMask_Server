package com.jian.mapper;

import com.jian.entity.Login;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author Jian Qi
 * @Date 2022/3/29 3:34 下午
 * @Description 用户头像Mapper
 * @Version 1
 */
@Mapper
@Repository
public interface Avatar_Mapper {
    @Select("select avatarURL from avatar where userID = #{userID};")
    String selectAvatarByUserID(String userID);
}
