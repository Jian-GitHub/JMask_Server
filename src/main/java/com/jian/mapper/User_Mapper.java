package com.jian.mapper;
/**
 * 用户类-接口
 * @author qi
 */

import com.jian.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface User_Mapper {

    /**
     * 查询全部用户
     * @return 用户集合 List<User>
     */
    @Select("select * from user")
    List<User> select();

    /**
     * 根据用户名查找用户数量
     * @param userName 用户名
     * @return 用户数量
     */
    @Select("select count(id) from user where BINARY userName = #{userName}")
    int countUsersByName(String userName);

    /**
     * 根据用户名查找用户
     * @param userName 用户名
     * @return 用户信息
     */
    @Select("select * from user where BINARY userName = #{userName}")
    User selectUserByName(String userName);

    /**
     * 根据用户ID查找用户
     * @param ID 用户ID
     * @return 用户信息
     */
    @Select("select id,userName,createDate,lastDate,isOnLine,isAvatar from user where BINARY id = #{ID}")
    User selectUserByID(int ID);

    /**
     * 插入用户信息
     * @param userName 用户名
     * @param password 密码
     * @param createDate 创建时间
     * @return 是否成功
     */
    @Insert("INSERT INTO `user` (`id`, `userName`, `password`, `createDate`) " +
            "VALUES (#{id}, #{userName}, #{password}, #{createDate});")
    boolean insertUser(String id, String userName,String password,String createDate);
}
