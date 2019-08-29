package com.neuedu;

import com.neuedu.interceptor.AdminAuthroityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootConfiguration
public class MySpringBootConfig implements WebMvcConfigurer {

    @Autowired
    AdminAuthroityInterceptor adminAuthroityInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(adminAuthroityInterceptor)
                .addPathPatterns("/user/**","/order/**","/cart/**","/shipping/**")
                .excludePathPatterns("/user/register.do","/user/login.do","/user/forget_get_question.do",
                        "/user/forget_check_answer.do","/shipping/add.do","/user/forget_reset_password.do",
                        "/user/product/search.do","/user/product/list.do","/order/callback.do",
                        "/user/product/detail.do","/shipping/add.do");


    }
}
