package com.yun.note.controller;


import cn.hutool.core.lang.Validator;
import com.yun.note.annotation.UserToken;
import com.yun.note.exception.ServiceException;
import com.yun.note.pojo.FileRecentlyUse;
import com.yun.note.pojo.Thing;
import com.yun.note.pojo.User;
import com.yun.note.service.IFileService;
import com.yun.note.service.INoteService;
import com.yun.note.service.IThingService;
import com.yun.note.util.code.EventCode;
import com.yun.note.util.response.ResponseData;
import com.yun.note.util.validate.TokenValidateUtil;
import com.yun.note.util.vo.DeleteFileVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
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

    @Autowired
    private StringRedisTemplate redisTemplate; // redis对象

    /**
     * 批量删除文件
     * @param fileVo  删除文件删除对象
     * @param request
     */
    @UserToken
    @PostMapping("/delete-batch")
    public ResponseData deleteMoreFile(@RequestBody @Validated DeleteFileVo fileVo, HttpServletRequest request) throws ServiceException {
        try {
            //从请求作用域中获取用户编号
            Integer userTokenID =(Integer) request.getAttribute("userTokenID");

            //调用批量删除文件业务
            fileService.deleteBatch(fileVo.getComplete(), fileVo.getDumpster(), userTokenID, fileVo.getFiles());

            return new ResponseData(true,"彻底删除成功", EventCode.UPDATE_SUCCESS);
        } catch (ServiceException e) {
            e.printStackTrace();
            return new ResponseData(false,e.getMessage(), e.getCode());
        }

    }

    @PostMapping("/restoreOne")
    public ResponseData restoreOne(int id, int type, @RequestHeader String userToken) {

        try {
            // 判断登录参数
            User user = TokenValidateUtil.validateUserToken(userToken, redisTemplate);

            //验证编辑编号参数
            if (Validator.isEmpty(id)) return new ResponseData(false, "编号参数有误", EventCode.PARAM_WRONG);

            if (Validator.isEmpty(type)) return new ResponseData(false, "类型参数有误", EventCode.PARAM_WRONG);

            fileService.restoreOne( user.getId(), id, type);
            return new ResponseData(true,"恢复成功", type == 1 ? EventCode.NOTE_RESTORE_SUCCESS : EventCode.THING_RESTORE_SUCCESS);
        } catch (ServiceException e) {
            e.printStackTrace();
            return new ResponseData(false,e.getMessage(), e.getCode());
        }
    }


    /**
     * 获取用户最近使用的文件
     * @param request
     */
    @UserToken
    @GetMapping("/recently-use")
    public ResponseData getFileRecentlyUse(HttpServletRequest request) throws ServiceException {
        try {
            //从请求作用域中获取用户编号
            Integer userTokenID =(Integer) request.getAttribute("userTokenID");

            List<FileRecentlyUse> recentlyUses = fileService.getRecentlyUseFiles(userTokenID);
            return new ResponseData(true,"获取成功", EventCode.SELECT_SUCCESS, recentlyUses);
        } catch (ServiceException e) {
            e.printStackTrace();
            return new ResponseData(false,e.getMessage(), e.getCode());
        }

    }

}
