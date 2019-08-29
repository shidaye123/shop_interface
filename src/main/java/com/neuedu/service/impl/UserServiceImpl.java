package com.neuedu.service.impl;


import com.neuedu.dao.UserInfoMapper;
import com.neuedu.exception.MyException;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    UserInfoMapper userInfoMapper;

    @Override
    public boolean findByUsername(String username) {

        int count = userInfoMapper.findByUsername(username);
        if (count>0){
            return true;
        }
        return false;
    }

    @Override
    public UserInfo findByUsernameAndPassword(String username, String password) {
        UserInfo userInfo = userInfoMapper.findByUsernameAndPassword(username, password);
        return userInfo;
    }

    //注册
    @Override
    public boolean findByEmail(String email) {

        int count = userInfoMapper.findByEmail(email);
        if (count>0){
            return true;
        }
        return false;
    }

    @Override
    public int register(String username, String password, String email, String phone, String question, String answer) {

        int count = userInfoMapper.register(username, password, email, phone, question, answer);
        return count;
    }

    @Override
    public String findQuestion(String username) {

        String question = userInfoMapper.findQuestion(username);
        return question;
    }

    @Override
    public String findAnswer(String username) {

        String answer = userInfoMapper.findAnswer(username);
        return answer;
    }

    @Override
    public int updatePassword(String username, String passwordNew) {
        int count = userInfoMapper.updatePassword(username, passwordNew);
        return count;
    }

    @Override
    public int checkPasswordOld(String passwordOld,String username) {

        int count = userInfoMapper.checkPasswordOld(passwordOld,username);
        return count;
    }

    @Override
    public int updatePasswordNew(String passwordNew, String username) {
        int count = userInfoMapper.updatePassword(passwordNew, username);
        return count;
    }

    @Override
    public int updateUserInfo(String email, String phone, String question, String answer, String username) {

        int count = userInfoMapper.updateUserInfo(email, phone, question, answer, username);
        return count;
    }

}
