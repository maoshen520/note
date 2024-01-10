package com.zhike.note.controller;

import cn.hutool.core.lang.Validator;
import cn.hutool.http.server.HttpServerRequest;
import com.zhike.note.exception.ServiceException;
import com.zhike.note.service.IMailservice;
import com.zhike.note.util.code.EventCode;
import com.zhike.note.util.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * 关于富文本编辑器图像上传的控制层
 */


@RestController
@RequestMapping("/ckeditor")  //请求地址:http://127.0.0.1:18081/ckeditor
public class CKEditorController {

    /**
     * 富文本编辑器图像上传
     * 请求地址 url:http://127.0.0.1:18081/ckeditor/upload/pic
     * @param file  上传的文件
     * @return  响应数据查询验证码的关键词
     */


    @PostMapping("/upload/pic")
    public HashMap<String, Object> uploadPic(@RequestParam("upload") MultipartFile file, HttpServletRequest request) {

        HashMap<String, Object> responseData = new HashMap<>();  //响应数据的一层数据
        HashMap<String, String> responseDataInfo = new HashMap<>(); //响应数据的二层数据

        // 判断是否有上传的文件
        if(file.isEmpty()){
            responseDataInfo.put("message", "未找到图片源！");
            responseData.put("error", responseDataInfo);
            return responseData;
        }

        //上传到服务器哪个磁盘文件夹下
        String fileMkdirsPath = "D:" + File.separator + "ck-file" + File.separator + "image";

        //上传到哪个磁盘文件下的虚拟路径地址
        String urlImagePath =
                request.getScheme() +
                    "://" +
                    request.getLocalAddr() +
                    ":" +
                    request.getServerPort() +
                    request.getContextPath() +
                    "/image/";

        //获取上传文件的名称
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null) {
            responseDataInfo.put("message", "未找到图片源！");
            responseData.put("error", responseDataInfo);
            return responseData;
        }

        //获取上传文件的后缀名
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));

        //上传后的文件名称
        String fileName = System.currentTimeMillis() + extension;

        //判断上传的文件夹是否存在
        File temp = new File(fileMkdirsPath);
        //不存在则创建
        if(!temp.exists()) temp.mkdirs();

        //最终上传的文件
        File localFile = new File(fileMkdirsPath + File.separator + fileName);
        try {
            file.transferTo(localFile);
        } catch (IOException e) {
            e.printStackTrace();
            responseDataInfo.put("message", "未找到图片源！");
            responseData.put("error", responseDataInfo);
            return responseData;
        }

        responseData.put("url", urlImagePath + fileName);
        return responseData;
    }

}
