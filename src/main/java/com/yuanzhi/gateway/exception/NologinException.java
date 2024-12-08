package com.yuanzhi.gateway.exception;

/**
 * Created by zhangding on 2020-02-15.
 */
public class NologinException extends Exception {

    public NologinException(){
        this("您尚未登录,请登录。");
    }

    public NologinException(String msg){
        super(msg);
    }
}
