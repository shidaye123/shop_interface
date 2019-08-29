package com.neuedu.exception;

public class MyException extends RuntimeException {

    private String page;
    public MyException(){}
    public MyException(String msg){
        super(msg);
    }
    public MyException(String msg, String page){
        super(msg);
        this.page = page;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
