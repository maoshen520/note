package com.zhike.note.service;

import com.zhike.note.exception.ServiceException;
import com.zhike.note.pojo.Note;

import java.util.List;

public interface INoteService {

    /**
     * 获取用户正常的笔记
     * @param userId  笔记列表
     * @return  笔记列表
     * @throws ServiceException
     */
    List<Note> getUserNormalNotes(int userId) throws ServiceException;

}
