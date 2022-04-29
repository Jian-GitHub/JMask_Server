package com.jian.mapper;
/**
 * 用户类-接口
 * @author qi
 */

import com.jian.entity.Log;
import com.jian.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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
//    @Select("select * from user")
    List<User> selectAllUsers();

    /**
     * 根据用户名查找用户数量
     * @param userName 用户名
     * @return 用户数量
     */
//    @Select("select count(id) from user where BINARY userName = #{userName}")
    int countUsersByName(String userName);

    /**
     * 根据用户名查找用户
     * @param userName 用户名
     * @return 用户信息
     */
//    @Select("select * from user where BINARY userName = #{userName}")
    User selectUserByName(String userName);

    /**
     * 根据用户ID查找用户
     * @param ID 用户ID
     * @return 用户信息
     */
//    @Select("select id,userName,createDate,lastDate,isOnLine,isAvatar from user where BINARY id = #{ID}")
    User selectUserByID(String ID);

    /**
     * 插入用户信息
     * @param userName 用户名
     * @param password 密码
     * @param createDate 创建时间
     * @return 是否成功
     */
//    @Insert("INSERT INTO `user` (`id`, `userName`, `password`, `createDate`) " +
//            "VALUES (#{id}, #{userName}, #{password}, #{createDate});")
    boolean insertUser(String id, String userName,String password,String createDate);

    /**
     *更新用户名
     * @param id 要修改用户名的用户ID
     * @param newUserName 要修改为的用户名
     * @return 是否成功
     */
    boolean updateUserName(@Param("id") String id, @Param("newUserName")  String newUserName);

    /**
     *更新密码
     * @param id 要修改用户名的用户ID
     * @param newPassWord 要修改为的用户名
     * @return 是否成功
     */
    boolean updatePassWord(@Param("id") String id, @Param("newPassWord")  String newPassWord);

    /**
     *注销用户
     * @param id 要注销的用户ID
     * @return 是否成功
     */
    boolean removeAccount(@Param("id") String id);

    /**
     *上传头像
     * @param id 上传的用户ID
     * @param avatarURL 上传的用户头像的URL
     * @return 是否成功
     */
    boolean uploadAvatar(@Param("id") String id,@Param("avatarURL")  String avatarURL);

    /**
     * 更新是否拥有头像
     * @param id 用户ID
     * @param hasAvatar 是否存在头像
     * @return
     */
    boolean updateHasAvatar(@Param("id") String id,@Param("hasAvatar")  boolean hasAvatar);

    /**
     * 增加用户识别记录
     * @param id 用户id
     * @param imgName 识别图片的名称
     * @return 是否成功
     */
    boolean addUserLog(@Param("id") String id, @Param("imgName") String imgName);

    /**
     * 查询用户识别记录
     * @param id 用户id
     * @return 用户记录(Log) List
     */
    List<Log> selectUserLog(@Param("id") String id, @Param("startIndex") int startIndex, @Param("endIndex") int endIndex);

    /**
     * 查询用户记录数量
     * @param id 用户id
     * @return
     */
    int countUserLog(@Param("id") String id);

    /**
     * 更新用户最后登录时间
     * @param lastDate 当前时间
     */
    void updateUserLoginDate(@Param("lastDate") String lastDate, @Param("id") String id);

    /**
     * 更新用户登录状态
     * @param isOnline 用户是否登录
     * @param id 用户id
     */
    void updateUserLoginState(@Param("isOnline") boolean isOnline, @Param("id") String id);
}
