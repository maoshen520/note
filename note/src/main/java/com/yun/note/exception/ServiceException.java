package com.yun.note.exception;



import lombok.Getter;



/*
 * 业务异常
 * */
@Getter  //让外键可以获取
public class ServiceException extends Exception{
//优化异常处理后的
//public class ServiceException extends RuntimeException{
    private final String code; // 业务状态码


    //    构造器   --右键 -> 构造函数 -> exception(message:String)
    public ServiceException(String message, String code) {
        super(message);
        this.code = code;
    }

}
