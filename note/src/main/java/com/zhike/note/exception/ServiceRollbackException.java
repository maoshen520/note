package com.zhike.note.exception;



import lombok.Getter;


/*
 * 业务回滚异常
 * */
@Getter  //让外键可以获取
public class ServiceRollbackException extends ServiceException{  //继承ServiceException，点击创建业务构造器
    public ServiceRollbackException(String message, String code) {
        super(message, code);
    }  //继承ServiceException


}
