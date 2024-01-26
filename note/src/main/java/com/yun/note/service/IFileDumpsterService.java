package com.yun.note.service;

import com.yun.note.exception.ServiceException;
import com.yun.note.pojo.FileDumpster;
import com.yun.note.pojo.Note;

import java.util.Date;
import java.util.List;

public interface IFileDumpsterService {

    /**
     * 获取用户正常的回收站
     * @param userId  回收站列表
     * @return  回收站列表
     * @throws ServiceException
     */
    List<FileDumpster> getUserFiles(int userId) throws ServiceException;

}
