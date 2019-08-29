package com.neuedu.service;

import com.neuedu.exception.MyException;
import com.neuedu.pojo.UserInfo;

public interface IUserService {

    //登录
    public boolean findByUsername(String username);
    public UserInfo findByUsernameAndPassword(String username,String password);

    //注册
    public boolean findByEmail(String email);
    public int register(String username,String password,String email,
                        String phone,String question,String answer);

    //忘记密码
    public String findQuestion(String username);
    //校验答案
    public String findAnswer(String username);
    //修改密码
    public int updatePassword(String username,String passwordNew);
    //校验密码
    public int checkPasswordOld(String passwordOld,String username);
    //修改密码
    public int updatePasswordNew(String passwordNew,String username);
    //登录状态修改个人信息
    public int updateUserInfo(String email,String phone,String question,String answer,String username);
}
