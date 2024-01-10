package com.zhike.note.service;

import com.zhike.note.exception.ServiceException;
import com.zhike.note.pojo.Note;

import java.util.Date;
import java.util.List;

public interface INoteService {

    /**
     * 获取用户正常的笔记
     * @param userId  笔记列表
     * @return  笔记列表
     * @throws ServiceException
     */
    List<Note> getUserNormalNotes(int userId) throws ServiceException;

    /**
     * 置顶笔记（取消置顶笔记）
     *
     * @param isTop  是否置顶笔记
     * @param noteId  笔记编号
     * @param userId  用户编号
     * @throws ServiceException  业务异常
     */
    void topNote(boolean isTop, int noteId, int userId) throws ServiceException;

    /**
     * 删除笔记（删除或彻底删除笔记）
     *
     * @param complete  是否彻底删除
     * @param noteId  笔记编号
     * @param userId  用户编号
     * @param isRecycleBin  是否是回收站中的操作
     * @throws ServiceException  业务异常
     */
    void deleteNoteById(boolean complete, int noteId, int userId, boolean isRecycleBin) throws ServiceException;

    /**
     * 创建笔记（并且初始化）
     * @param userId
     * @return
     * @throws ServiceException
     */
    int createNoteInit(int userId) throws ServiceException;

    /**
     * 获取笔记信息
     *
     * @param noteId  笔记编号
     * @param userId  用户编号
     * @throws ServiceException  业务异常
     */
    Note getEditNote(int noteId, int userId) throws ServiceException;

    /**
     * 保存正在编辑的笔记
     *
     * @param noteId  笔记编号
     * @param userId  用户编号
     * @param title  笔记标题
     * @param body  笔记内容
     * @param content  笔记内容（完整的）
     * @throws ServiceException  业务异常
     */
    Date saveEditingNote(int noteId, int userId, String title, String body, String content) throws ServiceException;

}
