package com.neuedu.dao;

import com.neuedu.pojo.UserInfo;
import org.apache.ibatis.annotations.Param;

public interface UserInfoMapper {

    //登录
    public int findByUsername(String username);

    public UserInfo findByUsernameAndPassword(@Param("username") String username,
                                              @Param("password") String password);

    //注册
    public int findByEmail(String email);
    public int register(@Param("username") String username,@Param("password") String password,
                        @Param("email") String email, @Param("phone") String phone,
                        @Param("question") String question,@Param("answer") String answer);

    //忘记密码
    public String findQuestion(String username);
    //校验答案
    public String findAnswer(String username);
    //修改密码
    public int updatePassword(@Param("username") String username,@Param("passwordNew") String passwordNew);
    //校验旧密码
    public int checkPasswordOld(@Param("passwordOld") String passwordOld,
                                @Param("username") String username);

    //登录状态更新个人信息
    public int updateUserInfo(@Param("email") String email, @Param("phone") String phone,
                              @Param("question") String question,@Param("answer") String answer,
                              @Param("username") String username);


}