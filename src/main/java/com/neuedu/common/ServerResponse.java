package com.neuedu.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.stereotype.Component;

@Component
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> {

    private Integer status;
    private String msg;
    private T data;


    private ServerResponse(){}

    private ServerResponse(Integer status){
        this.status = status;
    }

    private ServerResponse(T data){
        this.data = data;
    }

    private ServerResponse(String msg){
        this.msg = msg;
    }

    private ServerResponse(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    private ServerResponse(Integer status,T data){
        this.status = status;
        this.data = data;
    }

    private ServerResponse(String msg,T data){
        this.msg = msg;
        this.data = data;
    }

    private ServerResponse(Integer status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

//判断接口是否调用成功
    @JsonIgnore
    public boolean isSuccess(){
        return this.status == 0;
    }

    public static <T> ServerResponse<T> createServerResponseSuccess(){
        return new ServerResponse<>(0);
    }

    public static <T> ServerResponse<T> createServerResponseSuccess(String msg){
        return new ServerResponse<>(0,msg);
    }

    public static <T> ServerResponse<T> createServerResponseSuccess(T data){
        return new ServerResponse<>(0,data);
    }

    public static <T> ServerResponse<T> createServerResponseSuccess(String msg,T data){
        return new ServerResponse<>(0,msg,data);
    }

    public static <T> ServerResponse<T> createServerResponseFail(Integer status){
        return new ServerResponse<>(status);
    }

    public static <T> ServerResponse<T> createServerResponseFail(String msg){
        return new ServerResponse<>(msg);
    }

    public static <T> ServerResponse<T> createServerResponseFail(T data){
        return new ServerResponse<>(data);
    }

    public static <T> ServerResponse<T> createServerResponseFail(Integer status, String msg){
        return new ServerResponse<>(status,msg);
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
