package com.neuedu.controller;


import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IUserService;
import com.neuedu.utils.GuavaCacheUtils;
import com.neuedu.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    IUserService userService;
    @Autowired
    ServerResponse serverResponse;

    @RequestMapping(value = "/login.do",method = RequestMethod.GET)
    public ServerResponse login(@RequestParam("username") String username,
                                @RequestParam("password") String password,
                                HttpSession session){

        if (username==null||username.equals("")){
            return ServerResponse.createServerResponseFail(100,"用户名不能为空！");
        }
        if (password==null||password.equals("")){
            return ServerResponse.createServerResponseFail(100,"密码不能为空！");
        }
        password = MD5Utils.getMD5Code(password);
        if (!userService.findByUsername(username)){
            return ServerResponse.createServerResponseFail(101,"用户名不存在！");
        }
        UserInfo user = userService.findByUsernameAndPassword(username,password);
        if (user!=null){
            if (user.getRole()==0){
                session.setAttribute("user",user);
                return ServerResponse.createServerResponseSuccess(null,user);
            }else {
                return ServerResponse.createServerResponseFail(100,"没有权限！");
            }
        }else {
        return ServerResponse.createServerResponseFail(1,"密码错误！");
        }

    }

    @RequestMapping(value = "/register.do",method = RequestMethod.GET)
    public ServerResponse register(@RequestParam("username") String username,
                                   @RequestParam("password") String password,
                                   @RequestParam("email") String email,
                                   @RequestParam("phone") String phone,
                                   @RequestParam("question") String question,
                                   @RequestParam("answer") String answer){

        if (username==null||username.equals("")){
            return ServerResponse.createServerResponseFail(100,"注册信息不能为空！");
        }
        if (password==null||password.equals("")){
            return ServerResponse.createServerResponseFail(100,"注册信息不能为空！");
        }
        if (email==null||email.equals("")){
            return ServerResponse.createServerResponseFail(100,"注册信息不能为空！");
        }
        if (phone==null||phone.equals("")){
            return ServerResponse.createServerResponseFail(100,"注册信息不能为空！");
        }
        if (question==null||question.equals("")){
            return ServerResponse.createServerResponseFail(100,"注册信息不能为空！");
        }
        if (answer==null||answer.equals("")){
            return ServerResponse.createServerResponseFail(100,"注册信息不能为空！");
        }
        if (userService.findByUsername(username)){
            return ServerResponse.createServerResponseFail(1,"用户名已存在！");
        }
        if (userService.findByEmail(email)){
            return ServerResponse.createServerResponseFail(2,"邮箱已注册！");
        }
        password = MD5Utils.getMD5Code(password);
        int count = userService.register(username, password, email, phone, question, answer);
        if (count>0){
            return ServerResponse.createServerResponseSuccess("用户注册成功!");
        }else {
            return ServerResponse.createServerResponseFail(3,"注册失败！");
        }
    }

    @RequestMapping(value = "/check_valid.do",method = RequestMethod.GET)
    public ServerResponse checkValid(@RequestParam("str") String str,
                                     @RequestParam("type") String type){

        if (type.equals("username")){
            if(userService.findByUsername(str)){
                return ServerResponse.createServerResponseFail(1,"用户名已存在！");
            }
            return ServerResponse.createServerResponseSuccess("校验成功！");
        }else {
            if(userService.findByEmail(str)){
                return ServerResponse.createServerResponseFail(1,"邮箱已注册！");
            }
            return ServerResponse.createServerResponseSuccess("校验成功！");
        }

    }

    @RequestMapping(value = "/get_user_info.do",method = RequestMethod.GET)
    public ServerResponse getUserInfo(HttpSession session){

        UserInfo user = (UserInfo) session.getAttribute("user");
        if (user!=null){
            return ServerResponse.createServerResponseSuccess(null,user);
        }else {
            return ServerResponse.createServerResponseFail(1,"用户未登录，无法获取当前用户信息!");
        }

    }

    @RequestMapping(value = "/forget_get_question.do",method = RequestMethod.GET)
    public ServerResponse forget(@RequestParam("username") String username){

        if (username==null||username.equals("")){
            return ServerResponse.createServerResponseFail(100,"用户名不能为空！");
        }
        if(!userService.findByUsername(username)){
            return ServerResponse.createServerResponseFail(101,"用户名不存在！");
        }
        String question = userService.findQuestion(username);
        if (question == null) {
            return ServerResponse.createServerResponseFail(1,"该用户未设置找回密码问题!");
        }else {
            return ServerResponse.createServerResponseSuccess(null,question);
        }
    }

    @RequestMapping(value = "/forget_check_answer.do",method = RequestMethod.GET)
    public ServerResponse checkAnswer(@RequestParam("username") String username,
                                      @RequestParam("question") String question,
                                      @RequestParam("answer") String answer){

        if (username==null||username.equals("")){
            return ServerResponse.createServerResponseFail(100,"用户名不能为空！");
        }
        if (question==null||question.equals("")){
            return ServerResponse.createServerResponseFail(100,"问题不能为空！");
        }
        if (answer==null||answer.equals("")){
            return ServerResponse.createServerResponseFail(100,"答案不能为空！");
        }
        String answerT = userService.findAnswer(username);
        if (answer.equals(answerT)){
            String uuid = UUID.randomUUID().toString();
            GuavaCacheUtils.put(username, uuid);
            return ServerResponse.createServerResponseSuccess(null,uuid);
        }else {
            return ServerResponse.createServerResponseFail(1,"问题答案错误！");
        }
    }
/**
 * 有问题，待解决
 *
 * */
    @RequestMapping(value = "/forget_reset_password.do",method = RequestMethod.GET)
    public ServerResponse resetPassword(@RequestParam("username") String username,
                                        @RequestParam("passwordNew") String passwordNew,
                                        @RequestParam("forgetToken") String forgetToken){

        if (username==null||username.equals("")){
            return ServerResponse.createServerResponseFail(1,"修改密码操作失败！");
        }
        if (passwordNew==null||passwordNew.equals("")){
            return ServerResponse.createServerResponseFail(1,"修改密码操作失败！");
        }
        if (forgetToken==null||forgetToken.equals("")){
            return ServerResponse.createServerResponseFail(1,"修改密码操作失败！");
        }
        String token = GuavaCacheUtils.get(username);
        if (!token.equals(forgetToken)){
            return ServerResponse.createServerResponseFail(104,"非法的token！");
        }
        passwordNew = MD5Utils.getMD5Code(passwordNew);
        int count = userService.updatePassword(username, passwordNew);
        if (count>0){
            return ServerResponse.createServerResponseSuccess("修改密码成功！");
        }else {
            return ServerResponse.createServerResponseFail(1,"修改密码操作失败!");
        }

    }

    @RequestMapping(value = "/reset_password.do",method = RequestMethod.GET)
    public ServerResponse loginUpdatePassword(@RequestParam("passwordOld") String passwordOld,
                                              @RequestParam("passwordNew") String passwordNew,
                                              HttpSession session){

        if (passwordOld==null||passwordOld.equals("")){
            return ServerResponse.createServerResponseFail(100,"密码不能为空！");
        }
        if (passwordNew==null||passwordNew.equals("")){
            return ServerResponse.createServerResponseFail(100,"密码不能为空！");
        }
        UserInfo user = (UserInfo) session.getAttribute("user");
        if (user==null){
            return ServerResponse.createServerResponseFail(1,"用户未登录，无法获取当前用户信息");
        }
        passwordOld = MD5Utils.getMD5Code(passwordOld);
        int count = userService.checkPasswordOld(passwordOld,user.getUsername());
        if (count>0){
            passwordNew = MD5Utils.getMD5Code(passwordNew);
            int counts = userService.updatePassword(user.getUsername(),passwordNew);
            if (counts>0){
                return ServerResponse.createServerResponseSuccess("修改密码成功！");
            }else {
                return ServerResponse.createServerResponseFail(2,"修改密码失败！");
            }
        }else {
            return ServerResponse.createServerResponseFail(1,"旧密码输入错误");
        }
    }

    @RequestMapping(value = "/update_information.do",method = RequestMethod.GET)
    public ServerResponse updateUserInfo(@RequestParam("email") String email,
                                         @RequestParam("phone") String phone,
                                         @RequestParam("question") String question,
                                         @RequestParam("answer") String answer,
                                         HttpSession session) {

        UserInfo user = (UserInfo) session.getAttribute("user");
        if (user==null){
            return ServerResponse.createServerResponseFail(1,"用户未登录！");
        }
        int count = userService.updateUserInfo(email,phone,question,answer,user.getUsername());
        if (count>0){
            return ServerResponse.createServerResponseSuccess("更新个人信息成功!");
        }else {
            return ServerResponse.createServerResponseFail(2,"更新用户信息失败！");
        }

    }

    @RequestMapping(value = "/get_inforamtion.do",method = RequestMethod.GET)
    public ServerResponse getUserInfoByLogin(HttpSession session){

        UserInfo user = (UserInfo) session.getAttribute("user");
        if (user == null){
            return ServerResponse.createServerResponseFail(10,"用户未登录，无法获取当前用户信息,status=10强制退出!");
        }else {
            return ServerResponse.createServerResponseSuccess(null,user);
        }

    }

    @RequestMapping(value = "/logout.do",method = RequestMethod.GET)
    public ServerResponse logout(HttpServletRequest request,
                                 HttpServletResponse response,
                                 HttpSession session){

        Cookie[] cookies = request.getCookies();
        session = request.getSession();
        session.invalidate();
        if (cookies!=null&&cookies.length>0){
            for (Cookie cookie:cookies){
                if (cookie.getName().equals("username")){
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
                if (cookie.getName().equals("password")){
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
            return ServerResponse.createServerResponseSuccess("退出登录成功！");
        }else {
            return ServerResponse.createServerResponseFail(1,"服务器端异常！");
        }
    }
}
