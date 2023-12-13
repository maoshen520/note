package com.zhike.note.exception;



import lombok.Getter;


/*
 * 业务异常
 * */
@Getter  //让外键可以获取
public class ValidateParamException extends ServiceException{

    public ValidateParamException(String message, String code) {
        super(message, code);
    }
}
