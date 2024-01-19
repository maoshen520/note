package com.yun.note.advice;


import com.yun.note.exception.ServiceException;
import com.yun.note.util.response.ResponseData;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 全局异常处理

@RestControllerAdvice  //全局返回json数据
public class GlobalExceptionAdvice {

    // 只要项目抛出了 ServiceException 异常（包含其子类）， 并且未处理时，自动执行该函数
    @ExceptionHandler(value = {ServiceException.class})
    public ResponseData ServiceExceptionHandler(ServiceException e) {
        e.printStackTrace();
        return ResponseData.error(e.getMessage(), e.getCode());
    }

    //  然后把所有的  throws ServiceException  去掉
    //  try catch去掉

}
