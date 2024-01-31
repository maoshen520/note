package com.yun.note.controller;


import com.yun.note.annotation.UserToken;
import com.yun.note.exception.ServiceException;
import com.yun.note.service.IFileService;
import com.yun.note.service.INoteService;
import com.yun.note.service.IThingService;
import com.yun.note.util.vo.DeleteFileVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 回收站的控制层
 */
@Validated
@RestController
@RequestMapping("/file") //请求地址:http://127.0.0.1:18081/note
public class FileController {

    @Autowired
    private INoteService noteService;
    @Autowired
    private IThingService thingService;
    @Autowired
    private IFileService fileService;

    /**
     * 批量删除文件
     * @param fileVo  删除文件删除对象
     * @param request
     */
    @UserToken
    @PostMapping("/delete-batch")
    public void deleteMoreFile(@RequestBody @Validated DeleteFileVo fileVo, HttpServletRequest request) throws ServiceException {

        //从请求作用域中获取用户编号
        Integer userTokenID =(Integer) request.getAttribute("userTokenID");

        //调用批量删除文件业务
        fileService.deleteBatch(fileVo.getComplete(), fileVo.getDumpster(), userTokenID, fileVo.getFiles());


    }


}
