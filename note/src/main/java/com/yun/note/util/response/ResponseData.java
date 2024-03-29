package com.yun.note.util.response;


//响应数据类

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseData<K> {
    private Boolean success;  //是否请求成功
    private String message; //请求通知
    private String code; // 业务状态码
    private K data; //数据

    //右键 ->生成 -> 构造函数 -> 选择三个函数
    public ResponseData(Boolean success, String message, String code) {
        this.success = success;
        this.message = message;
        this.code = code;
    }

    //异常
    public static ResponseData error(String message, String code) {
        return new ResponseData(false, message, code);
    }

    //成功
    public static ResponseData success(String message, String code) {
        return new ResponseData(true, message, code);
    }

    //成功
    public static <T> ResponseData success(String message, String code, T data) {
        return new ResponseData(true, message, code, data);
    }
}
