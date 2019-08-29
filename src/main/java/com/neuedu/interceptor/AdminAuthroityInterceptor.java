package com.neuedu.interceptor;

import com.neuedu.common.ServerResponse;
import com.neuedu.constant.Constants;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IUserService;
import com.neuedu.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

@Component
public class AdminAuthroityInterceptor implements HandlerInterceptor {

    @Autowired
    IUserService userService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        System.out.println("到达拦截器");

        HttpSession session = request.getSession();
        UserInfo user = (UserInfo) session.getAttribute(Constants.CURRENT_USER);
        if (user==null){
            //重置response
            response.reset();
            //乱码
            response.setHeader("Content-Type","application/json;charset=UTF-8");
            PrintWriter printWriter = response.getWriter();
            ServerResponse serverResponse = ServerResponse.createServerResponseFail(1,"未登录");
            String json = JsonUtils.objToStringPretty(serverResponse);
            printWriter.write(json);
            printWriter.flush();
            printWriter.close();

            return false;
        }

        System.out.println("符合条件，未拦截");

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
